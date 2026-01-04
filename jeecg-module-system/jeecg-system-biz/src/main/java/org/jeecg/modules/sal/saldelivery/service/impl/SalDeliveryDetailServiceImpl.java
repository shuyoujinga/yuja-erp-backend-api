package org.jeecg.modules.sal.saldelivery.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.constant.Constants;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.mapper.SalDeliveryDetailMapper;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryDetailService;
import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.service.ISalOrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 销售发货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalDeliveryDetailServiceImpl extends ServiceImpl<SalDeliveryDetailMapper, SalDeliveryDetail> implements ISalDeliveryDetailService {
	
	@Resource
	private SalDeliveryDetailMapper salDeliveryDetailMapper;

	@Autowired
	private ISalOrderDetailService salOrderDetailService;
	@Autowired
	private IInvStockService iInvStockService;

	@Override
	public List<SalDeliveryDetail> selectByMainId(String mainId) {
		return salDeliveryDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<SalDeliveryDetail> selectByTargetId(String ids) {
		List<SalDeliveryDetail> result = new ArrayList<>();
		List<String> list = Arrays.asList(ids.split(","));
		Assert.isTrue(CollectionUtil.isEmpty(list),"操作失败,销售订单为空!");
		List<SalOrderDetail> salDeliveryDetails = salOrderDetailService.listByIds(list);
		Assert.isTrue(CollectionUtil.isEmpty(salDeliveryDetails),"操作失败,销售订单为空!");
		List<String> materialCodeList  = salDeliveryDetails.stream().map(SalOrderDetail::getMaterialCode).collect(Collectors.toList());
		List<InvStock> invStockList = iInvStockService.list(new LambdaQueryWrapper<InvStock>().gt(InvStock::getTotalQty,0d).eq(InvStock::getDelFlag, Constants.YN.Y).likeRight(InvStock::getMaterialCode, "C").eq(InvStock::getWarehouseCode, "A01A03A04A03").in(InvStock::getMaterialCode,materialCodeList));
		Map<String, InvStock> stockMap = invStockList.stream()
				.collect(Collectors.toMap(
						InvStock::getMaterialCode,
						v -> v,
						(a, b) -> a   // 保留第一个
				));

		for (SalOrderDetail entity : salDeliveryDetails) {
			SalDeliveryDetail sdd = new SalDeliveryDetail();
			BeanUtils.copyProperties(entity, sdd);
			sdd.setId(null);
			sdd.setQty(entity.getQty());
			sdd.setOrderQty(entity.getQty());
			InvStock invStock = stockMap.get(sdd.getMaterialCode());
			sdd.setStockQty(ObjectUtils.isEmpty(invStock)?0d:invStock.getStockQty());
			sdd.setOrderDetailId(entity.getId());
			result.add(sdd);
		}

		return result;
    }
}
