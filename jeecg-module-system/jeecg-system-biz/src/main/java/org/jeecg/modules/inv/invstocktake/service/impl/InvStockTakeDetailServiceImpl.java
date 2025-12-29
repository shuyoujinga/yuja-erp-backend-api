package org.jeecg.modules.inv.invstocktake.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.constant.Constants;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import org.jeecg.modules.inv.invstocktake.mapper.InvStockTakeDetailMapper;
import org.jeecg.modules.inv.invstocktake.service.IInvStockTakeDetailService;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 库存盘点_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvStockTakeDetailServiceImpl extends ServiceImpl<InvStockTakeDetailMapper, InvStockTakeDetail> implements IInvStockTakeDetailService {
	
	@Resource
	private InvStockTakeDetailMapper invStockTakeDetailMapper;
	@Autowired
	private IInvStockService iInvStockService;
	@Autowired
	private IYujiakejiMaterialsService yujiakejiMaterialsService;
	
	@Override
	public List<InvStockTakeDetail> selectByMainId(String mainId) {
		return invStockTakeDetailMapper.selectByMainId(mainId);
	}

	@Override
	public List<InvStockTakeDetail> selectByLocation(String warehouseAndlocation) {
		List<InvStockTakeDetail> result =new ArrayList<InvStockTakeDetail>();

		String[] splitArr = warehouseAndlocation.split("_");
		List<InvStock> invStockList = iInvStockService.list(new LambdaQueryWrapper<InvStock>().eq(InvStock::getWarehouseCode, splitArr[0]).eq(InvStock::getLocationCode, splitArr[1]).eq(InvStock::getDelFlag, Constants.YN.Y));

		if (CollectionUtils.isEmpty(invStockList)) {
			return result;
		}
		List<String> materialCodeList = invStockList.stream().map(InvStock::getMaterialCode).collect(Collectors.toList());
		List<YujiakejiMaterials> list = yujiakejiMaterialsService.list(new LambdaQueryWrapper<YujiakejiMaterials>().in(YujiakejiMaterials::getMaterialCode, materialCodeList));
		Map<String, YujiakejiMaterials> materialsMap = list.stream().collect(Collectors.toMap(YujiakejiMaterials::getMaterialCode, v -> v));
		for (InvStock invStock : invStockList) {
			InvStockTakeDetail entity = new InvStockTakeDetail();
			YujiakejiMaterials yujiakejiMaterials = materialsMap.get(invStock.getMaterialCode());

			entity.setMaterialCode(invStock.getMaterialCode());
			entity.setUnit(yujiakejiMaterials.getUnit());
			entity.setSpecifications(yujiakejiMaterials.getSpecifications());
			entity.setBookQty(invStock.getTotalQty());
			result.add(entity);
		}

		return result;
	}
}
