package org.jeecg.modules.sal.salsettle.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryDetailService;
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import org.jeecg.modules.sal.salsettle.mapper.SalSettleDetailMapper;
import org.jeecg.modules.sal.salsettle.service.ISalSettleDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 销售结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalSettleDetailServiceImpl extends ServiceImpl<SalSettleDetailMapper, SalSettleDetail> implements ISalSettleDetailService {
	
	@Resource
	private SalSettleDetailMapper salSettleDetailMapper;

	@Autowired
	private ISalDeliveryDetailService salDeliveryDetailService;
	
	@Override
	public List<SalSettleDetail> selectByMainId(String mainId) {
		return salSettleDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<SalSettleDetail> selectByTargetId(String ids) {
		List<SalSettleDetail> result=new ArrayList<SalSettleDetail>();
		Assert.isTrue(StringUtils.isEmpty(ids),"传入ids数据为空!");
		List<String> list = Arrays.asList(ids.split(","));
		List<SalDeliveryDetail> salDeliveryDetailList = salDeliveryDetailService.listByIds(list);
		Assert.isTrue(CollectionUtil.isEmpty(salDeliveryDetailList),"发货明细不存在!");
		for (SalDeliveryDetail sdd : salDeliveryDetailList) {
			SalSettleDetail detail = new SalSettleDetail();
			BeanUtils.copyProperties(sdd,detail);
			detail.setId(null);
			detail.setDeliveryDetailId(sdd.getId());
			detail.setDeliveryQty(sdd.getQty());
			detail.setDeliveryAmount(sdd.getAmount());
			result.add(detail);
		}


		return result;
    }
}
