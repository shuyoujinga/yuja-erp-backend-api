package org.jeecg.modules.pur.purorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import org.jeecg.modules.pur.purapply.service.IPurApplyDetailService;
import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import org.jeecg.modules.pur.purorder.mapper.PurOrderDetailMapper;
import org.jeecg.modules.pur.purorder.service.IPurOrderDetailService;
import org.jeecg.modules.pur.purorder.service.IPurOrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Description: 采购订单_明细
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
@Service
public class PurOrderDetailServiceImpl extends ServiceImpl<PurOrderDetailMapper, PurOrderDetail> implements IPurOrderDetailService {
	
	@Resource
	private PurOrderDetailMapper purOrderDetailMapper;

	@Autowired
	private IPurApplyDetailService purApplyDetailService;
	
	@Override
	public List<PurOrderDetail> selectByMainId(String mainId) {
		List<PurOrderDetail> list =new ArrayList<PurOrderDetail>();
		List<PurApplyDetail> purApplyDetails = purApplyDetailService.selectByMainId(mainId);


		if (CollectionUtil.isEmpty(purApplyDetails)) {
			return purOrderDetailMapper.selectByMainId(mainId);
		}


		purApplyDetails.forEach(purApplyDetail -> {
			PurOrderDetail purOrderDetail = new PurOrderDetail();
			purOrderDetail.setApplyDetailId(purApplyDetail.getId());
			purOrderDetail.setOrderQty(purApplyDetail.getApplyQty());
			purOrderDetail.setMaterialCode(purApplyDetail.getMaterialCode());
			purOrderDetail.setSpecifications(purApplyDetail.getSpecifications());
			purOrderDetail.setUnit(purApplyDetail.getUnit());
			purOrderDetail.setUnitPrice(purApplyDetail.getUnitPrice());
			purOrderDetail.setAmount(purApplyDetail.getAmount());
			purOrderDetail.setRemark(purApplyDetail.getRemark());
			list.add(purOrderDetail);
		});


		return list;

	}

}
