package org.jeecg.modules.sal.saldelivery.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.sal.saldelivery.entity.SalDelivery;
import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.mapper.SalDeliveryDetailMapper;
import org.jeecg.modules.sal.saldelivery.mapper.SalDeliveryMapper;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @Description: 销售出货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalDeliveryServiceImpl extends ServiceImpl<SalDeliveryMapper, SalDelivery> implements ISalDeliveryService {

	@Resource
	private SalDeliveryMapper salDeliveryMapper;
	@Resource
	private SalDeliveryDetailMapper salDeliveryDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService invMaterialVoucherCustomService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalDelivery salDelivery, List<SalDeliveryDetail> salDeliveryDetailList) {
		salDelivery.setDocCode((serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSFH)));
		double totalAmount = AmountUtils.sumTotalAmount(
				salDeliveryDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		salDelivery.setAmount(totalAmount);
		salDeliveryMapper.insert(salDelivery);
		if(salDeliveryDetailList!=null && salDeliveryDetailList.size()>0) {
			for(SalDeliveryDetail entity:salDeliveryDetailList) {
				//外键设置
				entity.setPid(salDelivery.getId());
				salDeliveryDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalDelivery salDelivery,List<SalDeliveryDetail> salDeliveryDetailList) {
		double totalAmount = AmountUtils.sumTotalAmount(
				salDeliveryDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		salDelivery.setAmount(totalAmount);
		salDeliveryMapper.updateById(salDelivery);

		//1.先删除子表数据
		salDeliveryDetailMapper.deleteByMainId(salDelivery.getId());

		//2.子表数据重新插入
		if(salDeliveryDetailList!=null && salDeliveryDetailList.size()>0) {
			for(SalDeliveryDetail entity:salDeliveryDetailList) {
				//外键设置
				entity.setPid(salDelivery.getId());
				salDeliveryDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salDeliveryDetailMapper.deleteByMainId(id);
		salDeliveryMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salDeliveryDetailMapper.deleteByMainId(id.toString());
			salDeliveryMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		List<SalDelivery> deliveryList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(deliveryList),"操作失败!选择的数据不存在!");

		for (SalDelivery sd : deliveryList) {
			// 过滤已经审核的数据
			if (Constants.DICT_AUDIT_STATUS.YES.equals(sd.getAudit())) {
				continue;
			}

			List<SalDeliveryDetail> salDeliveryDetailList = salDeliveryDetailMapper.selectByMainId(sd.getId());
			Assert.isTrue(CollectionUtil.isEmpty(salDeliveryDetailList),String.format("操作失败!选择的[%s]明细数据不存在!",sd.getDocCode()));

			//开始构建物料凭证
			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(Constants.DICT_MOVE_TYPE.XSCH, sd.getDocCode(), sd.getId(), sd.getRemark());
			List<InvMaterialVoucherDetail> detailList=new ArrayList<>();
			for (SalDeliveryDetail entity : salDeliveryDetailList) {
				InvMaterialVoucherDetail detail = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(entity, detail);
				detail.setId(null);
				detail.setPid(null);
				detail.setSourceDocDetailId(entity.getId());
				detail.setMoveType(Constants.DICT_MOVE_TYPE.XSCH);
				detail.setPrice(entity.getUnitPrice());
				detail.setWarehouseCode(Constants.WAREHOUSE.PRODUCTION);
				detailList.add(detail);
			}
			invMaterialVoucherCustomService.createVoucher(invMaterialVoucher,detailList);

		}
		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		List<SalDelivery> deliveryList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(deliveryList),"操作失败!选择的数据不存在!");
		for (SalDelivery sd : deliveryList) {
			// 过滤未审核的数据
			if (Constants.DICT_AUDIT_STATUS.NO.equals(sd.getAudit())) {
				continue;
			}
			String voucherIdBySourceDocId = invMaterialVoucherCustomService.getVoucherIdBySourceDocId(sd.getId());
			if (StringUtils.isEmpty(voucherIdBySourceDocId)){
				continue;
			}
			invMaterialVoucherCustomService.reversalVoucher(voucherIdBySourceDocId);


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

		List<SalDelivery> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (SalDelivery record : records) {
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
