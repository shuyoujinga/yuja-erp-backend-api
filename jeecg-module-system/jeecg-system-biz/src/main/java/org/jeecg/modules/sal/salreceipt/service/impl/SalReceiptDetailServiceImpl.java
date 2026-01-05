package org.jeecg.modules.sal.salreceipt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import org.jeecg.modules.sal.salreceipt.mapper.SalReceiptDetailMapper;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptDetailService;
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
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
 * @Description: 销售收款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalReceiptDetailServiceImpl extends ServiceImpl<SalReceiptDetailMapper, SalReceiptDetail> implements ISalReceiptDetailService {
	
	@Resource
	private SalReceiptDetailMapper salReceiptDetailMapper;
	@Autowired
	private ISalSettleDetailService isalSettleDetailService;
	
	@Override
	public List<SalReceiptDetail> selectByMainId(String mainId) {
		return salReceiptDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<SalReceiptDetail> selectByTargetId(String ids) {
		List<SalReceiptDetail> result=new ArrayList<SalReceiptDetail>();
		Assert.isTrue(StringUtils.isEmpty(ids),"查询失败,销售结算明细为空!");
		List<String> list = Arrays.asList(ids.split(","));
		List<SalSettleDetail> salSettleDetailList = isalSettleDetailService.listByIds(list);
		Assert.isTrue(CollectionUtil.isEmpty(salSettleDetailList),"查询失败,销售结算明细为空!");
		for (SalSettleDetail detail : salSettleDetailList) {
			SalReceiptDetail entity = new SalReceiptDetail();
			BeanUtils.copyProperties(detail, entity);
			entity.setId(null);
			entity.setSettleDetailId(detail.getId());
			result.add(entity);
		}




		return result;
    }
}
