package org.jeecg.modules.sal.salbizplan.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanDetailMapper;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanDetailService;
import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.service.ISalOrderDetailService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.utils.Assert;
import reactor.core.Exceptions;

import javax.annotation.Resource;

/**
 * @Description: 业务计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalBizPlanDetailServiceImpl extends ServiceImpl<SalBizPlanDetailMapper, SalBizPlanDetail> implements ISalBizPlanDetailService {
	
	@Resource
	private SalBizPlanDetailMapper salBizPlanDetailMapper;
	@Autowired
	private ISalOrderDetailService	salOrderDetailService;
	
	@Override
	public List<SalBizPlanDetail> selectByMainId(String mainId) {
		return salBizPlanDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<SalBizPlanDetail> selectByTargetId(String ids)  {
		List<SalBizPlanDetail> result=new ArrayList<>();

		List<String> list = Arrays.asList(ids.split(","));
		Assert.isTrue(CollectionUtil.isEmpty(list),"操作失败!销售订单明细不存在!");

		List<SalOrderDetail> salOrderDetailList = salOrderDetailService.listByIds(list);
		Assert.isTrue(CollectionUtil.isEmpty(salOrderDetailList),"操作失败!销售订单明细不存在!");

		for (SalOrderDetail entity : salOrderDetailList) {
			SalBizPlanDetail bpd = new SalBizPlanDetail();
			bpd.setOrderDetailId(entity.getId());
			bpd.setMaterialCode(entity.getMaterialCode());
			bpd.setUnit(entity.getUnit());
			bpd.setSpecifications(entity.getSpecifications());
			bpd.setOrderQty(entity.getQty());
			bpd.setRemark(entity.getRemark());
			result.add(bpd);

		}




		return result;
    }
}
