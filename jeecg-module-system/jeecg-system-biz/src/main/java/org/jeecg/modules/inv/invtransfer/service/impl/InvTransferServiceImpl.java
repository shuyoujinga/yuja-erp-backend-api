package org.jeecg.modules.inv.invtransfer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.jeecg.modules.inv.invtransfer.entity.InvTransfer;
import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import org.jeecg.modules.inv.invtransfer.mapper.InvTransferDetailMapper;
import org.jeecg.modules.inv.invtransfer.mapper.InvTransferMapper;
import org.jeecg.modules.inv.invtransfer.service.IInvTransferService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 物料调拨
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class InvTransferServiceImpl extends ServiceImpl<InvTransferMapper, InvTransfer> implements IInvTransferService {

	@Resource
	private InvTransferMapper invTransferMapper;
	@Resource
	private InvTransferDetailMapper invTransferDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvStockService iInvStockService;
	@Autowired
	private IInvMaterialVoucherCustomService invMaterialVoucherCustomService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvTransfer invTransfer, List<InvTransferDetail> invTransferDetailList) {
		invTransfer.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.WZDB));
		invTransferMapper.insert(invTransfer);
		if(invTransferDetailList!=null && invTransferDetailList.size()>0) {
			for(InvTransferDetail entity:invTransferDetailList) {
				//外键设置
				entity.setPid(invTransfer.getId());
				invTransferDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvTransfer invTransfer,List<InvTransferDetail> invTransferDetailList) {
		invTransferMapper.updateById(invTransfer);

		//1.先删除子表数据
		invTransferDetailMapper.deleteByMainId(invTransfer.getId());

		//2.子表数据重新插入
		if(invTransferDetailList!=null && invTransferDetailList.size()>0) {
			for(InvTransferDetail entity:invTransferDetailList) {
				//外键设置
				entity.setPid(invTransfer.getId());
				invTransferDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invTransferDetailMapper.deleteByMainId(id);
		invTransferMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invTransferDetailMapper.deleteByMainId(id.toString());
			invTransferMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		log.info("【物料调拨审核】开始，ids={}", ids);
		List<InvTransfer> invTransfers = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(invTransfers), "待审核的物料调拨单据为空,请注意检查!");

		for (InvTransfer invTransfer : invTransfers) {

			// 0. 幂等校验
			Assert.isTrue(Constants.DICT_AUDIT_STATUS.YES.equals(invTransfer.getAudit()),
					"调拨单已审核，禁止重复操作");

			// 1. 查询明细
			List<InvTransferDetail> details =
					invTransferDetailMapper.selectByMainId(invTransfer.getId());
			Assert.isTrue(CollectionUtil.isEmpty(details),
					"调拨单明细为空，禁止审核");

			// 2. 按物料汇总调拨数量
			Map<String, Double> transferQtyMap = details.stream()
					.collect(Collectors.groupingBy(
							InvTransferDetail::getMaterialCode,
							Collectors.summingDouble(InvTransferDetail::getQty)
					));

			List<String> materialCodes = new ArrayList<>(transferQtyMap.keySet());

			// 3. 查询调出仓库存
			List<InvStock> outStockList = iInvStockService.list(
					new LambdaQueryWrapper<InvStock>()
							.eq(InvStock::getWarehouseCode, invTransfer.getFromWarehouseCode())
							.in(InvStock::getMaterialCode, materialCodes)
							.eq(InvStock::getDelFlag, Constants.YN.Y)
			);

			Map<String, InvStock> outStockMap = outStockList.stream()
					.collect(Collectors.toMap(InvStock::getMaterialCode, s -> s));

			// 4. 库存校验
			for (Map.Entry<String, Double> entry : transferQtyMap.entrySet()) {
				String materialCode = entry.getKey();
				Double needQty = entry.getValue();

				InvStock stock = outStockMap.get(materialCode);
				Assert.isTrue(ObjectUtils.isEmpty(stock),
						"物料【" + materialCode + "】在调出仓无库存");

				Assert.isTrue(stock.getStockQty() < needQty,
						"物料【" + materialCode + "】库存不足，当前库存："
								+ stock.getStockQty() + "，需调拨：" + needQty);
			}
			// 5. 生成物料凭证（调出 + 调入）

			// 调出物料凭证
			InvMaterialVoucher ibvOut = new InvMaterialVoucher(Constants.BIZ_TYPE.WZDB,Constants.DICT_SOURCE_DOC_TYPE.CC,invTransfer.getDocCode(),invTransfer.getId(),Constants.DICT_MOVE_TYPE.DBCK,"",Constants.YN.N,"");
			List<InvMaterialVoucherDetail> detailOutList=new ArrayList<>();
			for (InvTransferDetail entity : details) {
				InvMaterialVoucherDetail detailEntity = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(entity, detailEntity);
				detailEntity.setId(null);
				detailEntity.setPid(null);
				detailEntity.setSourceDocDetailId(entity.getId());
				detailEntity.setQty(entity.getQty());
				detailEntity.setPrice(entity.getUnitPrice());
				detailEntity.setAmount(entity.getAmount());
				detailEntity.setMoveType(ibvOut.getMoveType());
				detailEntity.setStockType(Constants.DICT_STOCK_TYPE.OUT);
				detailEntity.setWarehouseCode(invTransfer.getFromWarehouseCode());
				detailOutList.add(detailEntity);
			}
			invMaterialVoucherCustomService.createVoucher(ibvOut,detailOutList);


			// 调入物料凭证
			InvMaterialVoucher ibvIn = new InvMaterialVoucher(Constants.BIZ_TYPE.WZDB,Constants.DICT_SOURCE_DOC_TYPE.CC,invTransfer.getDocCode(),invTransfer.getId(),Constants.DICT_MOVE_TYPE.DBRK,"",Constants.YN.N,"");
			List<InvMaterialVoucherDetail> detailInList=new ArrayList<>();
			for (InvTransferDetail entity : details) {
				InvMaterialVoucherDetail detailEntity = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(entity, detailEntity);
				detailEntity.setId(null);
				detailEntity.setPid(null);
				detailEntity.setSourceDocDetailId(entity.getId());
				detailEntity.setQty(entity.getQty());
				detailEntity.setPrice(entity.getUnitPrice());
				detailEntity.setAmount(entity.getAmount());
				detailEntity.setMoveType(ibvIn.getMoveType());
				detailEntity.setStockType(Constants.DICT_STOCK_TYPE.IN);
				detailEntity.setWarehouseCode(invTransfer.getToWarehouseCode());
				detailInList.add(detailEntity);
			}
			invMaterialVoucherCustomService.createVoucher(ibvIn,detailInList);

			log.info("调拨入库凭证写完了!"+ibvIn.getId());




			invTransfer.setFromMaterialVoucherId(ibvIn.getId());
			invTransfer.setToMaterialVoucherId(ibvOut.getId());



			updateById(invTransfer);
		}



		// 8. 更新审核状态
		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {

		List<InvTransfer> invTransfers = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(invTransfers),
				"待审核的物料调拨单据为空,请注意检查!");

		// 冲销物料凭证
		for (InvTransfer invTransfer : invTransfers) {
			// 冲销调出凭证
			log.info("调出凭证冲销好了!");
			invMaterialVoucherCustomService.reversalVoucher(invTransfer.getFromMaterialVoucherId());
			// 冲销调入凭证
			log.info("调入凭证冲销好了!");
			invMaterialVoucherCustomService.reversalVoucher(invTransfer.getToMaterialVoucherId());
		}


		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.NO);
	}

	/**
	 * 批量更新审核状态
	 *
	 * @param ids 待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<InvTransfer> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (InvTransfer record : records) {
			if (!status.equals(record.getAudit())) {
				record.setAudit(status);
				record.setAuditBy(sysUser.getUsername());
				record.setAuditTime(new Date());
				count++;
			}
		}

		if (count > 0) {
			updateBatchById(records);
		}
		return count;
	}

}
