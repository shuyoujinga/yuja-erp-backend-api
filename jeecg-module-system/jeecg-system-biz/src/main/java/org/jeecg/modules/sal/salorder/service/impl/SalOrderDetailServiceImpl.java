package org.jeecg.modules.sal.salorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.constant.Constants;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.mapper.SalOrderDetailMapper;
import org.jeecg.modules.sal.salorder.service.ISalOrderDetailService;
import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import org.jeecg.modules.sal.salquote.service.ISalQuoteDetailService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 销售订单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class SalOrderDetailServiceImpl extends ServiceImpl<SalOrderDetailMapper, SalOrderDetail> implements ISalOrderDetailService {
	
	@Resource
	private SalOrderDetailMapper salOrderDetailMapper;
	@Autowired
	private ISalQuoteDetailService salQuoteDetailService;
	@Autowired
	private IInvStockService iInvStockService;
	
	@Override
	public List<SalOrderDetail> selectByMainId(String mainId) {
		return salOrderDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<SalOrderDetail> selectByTargetId(String ids) {
		List<SalOrderDetail> result=new ArrayList<SalOrderDetail>();

		// 把is_tax拆分出来
		String[] idsArray = ids.split("#");
		Assert.isTrue(idsArray.length==1,"查询失败!选择是否含税!");

		String detailIds = idsArray[0];

		Integer isTax=Integer.parseInt(idsArray[1]);

		List<String> detailIdList = Arrays.asList(detailIds.split(","));

		List<SalQuoteDetail> salQuoteDetails = salQuoteDetailService.listByIds(detailIdList);
		Assert.isTrue(CollectionUtil.isEmpty(salQuoteDetails),"查询失败,销售报价数据不存在!");
		List<String> materialCodeList = salQuoteDetails.stream().map(SalQuoteDetail::getMaterialCode).collect(Collectors.toList());

		List<InvStock> invStockList = iInvStockService.list(new LambdaQueryWrapper<InvStock>().gt(InvStock::getTotalQty,0d).eq(InvStock::getDelFlag, Constants.YN.Y).likeRight(InvStock::getMaterialCode, "C").eq(InvStock::getWarehouseCode, "A01A03A04A03").in(InvStock::getMaterialCode,materialCodeList));
		Assert.isTrue(CollectionUtil.isEmpty(invStockList),"查询失败!库存不存在!");
		Map<String, InvStock> stockMap = invStockList.stream()
				.collect(Collectors.toMap(
						InvStock::getMaterialCode,
						v -> v,
						(a, b) -> a   // 保留第一个
				));



		for (SalQuoteDetail salQuoteDetail : salQuoteDetails) {
			SalOrderDetail entity=new SalOrderDetail();

			entity.setQuoteDetailId(salQuoteDetail.getId());
			entity.setMaterialCode(salQuoteDetail.getMaterialCode());
			entity.setUnit(salQuoteDetail.getUnit());
			entity.setSpecifications(salQuoteDetail.getSpecifications());
			entity.setQty(salQuoteDetail.getQty());
			entity.setDiscountRate(salQuoteDetail.getDiscountRate());

			// 含税单价操作
			if (Constants.YN.Y.equals(isTax)) {
				entity.setUnitPrice(salQuoteDetail.getTaxUnitPrice());
				entity.setAmount(salQuoteDetail.getTaxAmount());
			}else{
				entity.setUnitPrice(salQuoteDetail.getUnitPrice());
				entity.setAmount(salQuoteDetail.getAmount());
			}
			InvStock invStock = stockMap.get(salQuoteDetail.getMaterialCode());
			entity.setStockQty(ObjectUtils.isEmpty(invStock)?0d: AmountUtils.isZero(invStock.getStockQty())?0d:invStock.getStockQty() );
			result.add(entity);

		}



		return result;
    }
}
