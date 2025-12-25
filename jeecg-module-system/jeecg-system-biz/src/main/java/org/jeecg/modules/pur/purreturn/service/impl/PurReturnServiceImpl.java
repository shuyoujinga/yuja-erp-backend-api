package org.jeecg.modules.pur.purreturn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveDetailService;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveService;
import org.jeecg.modules.pur.purreturn.entity.PurReturn;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnDetailMapper;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnMapper;
import org.jeecg.modules.pur.purreturn.service.IPurReturnService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utils.AmountUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 采购退货
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurReturnServiceImpl extends ServiceImpl<PurReturnMapper, PurReturn> implements IPurReturnService {

	@Resource
	private PurReturnMapper purReturnMapper;
	@Resource
	private PurReturnDetailMapper purReturnDetailMapper;
	@Autowired
	private IPurReceiveDetailService purReceiveDetailService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IPurReceiveService purReceiveService;
	@Autowired
	private IYujiakejiMaterialsService yujiakejiMaterialsService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurReturn purReturn, List<PurReturnDetail> purReturnDetailList) {

		purReturn.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGTH));
		double totalAmount = AmountUtils.sumTotalAmount(purReturnDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d));
		purReturn.setAmount(totalAmount);
		purReturnMapper.insert(purReturn);
		if(purReturnDetailList!=null && purReturnDetailList.size()>0) {
			for(PurReturnDetail entity:purReturnDetailList) {
				//外键设置
				entity.setPid(purReturn.getId());
				entity.setId(null);
				purReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurReturn purReturn,List<PurReturnDetail> purReturnDetailList) {
		purReturnMapper.updateById(purReturn);
		
		//1.先删除子表数据
		purReturnDetailMapper.deleteByMainId(purReturn.getId());
		
		//2.子表数据重新插入
		if(purReturnDetailList!=null && purReturnDetailList.size()>0) {
			for(PurReturnDetail entity:purReturnDetailList) {
				//外键设置
				entity.setPid(purReturn.getId());
				purReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purReturnDetailMapper.deleteByMainId(id);
		purReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purReturnDetailMapper.deleteByMainId(id.toString());
			purReturnMapper.deleteById(id);
		}
	}

	@Override
	public String smarkRemark(String orderDetailId, String supplierCode) {
		List<String> list = Arrays.asList(orderDetailId.split(","));
		Double receiveQty = purReceiveDetailService.selectReceiveQtyByOrderIds(list);

		// 兜底，防止 null
		double qty = receiveQty == null ? 0D : receiveQty;

		return String.format(
				"智能备注提示您，您选择的单据共计收货 %.2f，请注意退货数量，如果为“已收货退货”，请关注库存是否足够扣除，如库存不足将无法审核！",
				qty
		);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		// 如果是未收货退货就直接改状态 否则需要产生采购收货单

		List<PurReturn> purReturns = this.listByIds(ids);
		if (CollectionUtil.isEmpty(purReturns)) {
			return 0;
		}

		for (PurReturn purReturn : purReturns) {
			if (Constants.DICT_RETURN_TYPE.NO_RECEIVE.equals(purReturn.getReturnType())) {
				continue;
			}
			PurReceive purReceive = new PurReceive();
			purReceive.setIsReturn(Constants.YN.Y);
			purReceive.setSupplierCode(purReturn.getSupplierCode());
			purReceive.setExternalOrderNo(purReturn.getDocCode());
			purReceive.setRemark(purReturn.getRemark());
			purReceive.setDocTime(purReturn.getDocTime());
			purReceive.setOrderCode(purReturn.getOrderCodes());
			purReceive.setOrderId(purReturn.getOrderIds());

			List<PurReceiveDetail> detailList=new ArrayList<>();

			List<PurReturnDetail> purReturnDetails = purReturnDetailMapper.selectByMainId(purReturn.getId());
			List<String> collect = purReturnDetails.stream().map(PurReturnDetail::getMaterialCode).collect(Collectors.toList());
			List<YujiakejiMaterials> list = yujiakejiMaterialsService.list(new LambdaQueryWrapper<YujiakejiMaterials>().in(YujiakejiMaterials::getMaterialCode, collect));
			Map<String, YujiakejiMaterials> materialsMap = list.stream().collect(Collectors.toMap(YujiakejiMaterials::getMaterialCode, v -> v));

			for (PurReturnDetail purReturnDetail : purReturnDetails) {
				PurReceiveDetail entity = new PurReceiveDetail();
				entity.setRemark(purReturnDetail.getRemark());
				entity.setMaterialCode(purReturnDetail.getMaterialCode());
				entity.setUnit(materialsMap.get(purReturnDetail.getMaterialCode()).getUnit());
				entity.setSpecifications(materialsMap.get(purReturnDetail.getMaterialCode()).getSpecifications());
				entity.setReceiveQty(AmountUtils.negate( purReturnDetail.getQty()) );
				entity.setUnitPrice(purReturnDetail.getUnitPrice());
				entity.setAmount(AmountUtils.negate( purReturnDetail.getAmount()) );

				// 仓库规则映射
				Map<Character, String> warehouseMap = Map.of(
						'A', "A01A03A04A01", // 原材料仓
						'B', "A01A03A04A02", // 半成品仓
						'C', "A01A03A04A03"  // 制成品仓
				);

				// 设置仓库
				String materialCode = entity.getMaterialCode();
				if (materialCode != null && !materialCode.isEmpty()) {
					entity.setWarehouseCode(warehouseMap.getOrDefault(materialCode.charAt(0), "")); // 默认空
				}
				detailList.add(entity);
			}
			purReceiveService.saveMain(purReceive,detailList);

			purReceiveService.audit(Collections.singletonList(purReceive.getId()));
			purReturn.setReceiveId(purReceive.getId());
			purReturnMapper.updateById(purReturn);

		}



		return updateAuditStatus(ids,Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		List<PurReturn> purReturns = this.listByIds(ids);
		if (CollectionUtil.isEmpty(purReturns)) {
			return 0;
		}

		for (PurReturn purReturn : purReturns) {
			if (Constants.DICT_RETURN_TYPE.NO_RECEIVE.equals(purReturn.getReturnType())) {
				continue;
			}

			purReceiveService.unAudit(Collections.singletonList(purReturn.getReceiveId()));
			purReceiveService.delMain(purReturn.getReceiveId());
			purReturn.setReceiveId("");
			purReturnMapper.updateById(purReturn);

		}

		return updateAuditStatus(ids,Constants.DICT_AUDIT_STATUS.NO);
	}

	/**
	 * 批量更新审核状态
	 *
	 * @param ids    待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<PurReturn> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PurReturn record : records) {

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
