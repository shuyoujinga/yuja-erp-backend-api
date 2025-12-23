package org.jeecg.modules.pur.purreceive.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import org.jeecg.modules.pur.purorder.service.IPurOrderDetailService;
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import org.jeecg.modules.pur.purreceive.mapper.PurReceiveDetailMapper;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Description: 采购收货_明细
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
@Service
public class PurReceiveDetailServiceImpl extends ServiceImpl<PurReceiveDetailMapper, PurReceiveDetail> implements IPurReceiveDetailService {
	
	@Resource
	private PurReceiveDetailMapper purReceiveDetailMapper;
	@Autowired
	private IPurOrderDetailService purOrderDetailService;
	
	@Override
	public List<PurReceiveDetail> selectByMainId(String mainId) {
		return purReceiveDetailMapper.selectByMainId(mainId);
	}

	@Override
	public List<PurReceiveDetail> selectByTargetId(String ids) {
		List<PurReceiveDetail> result = Lists.newArrayList();
		if (StringUtils.isEmpty(ids)) {
			return result;
		}

		List<String> list = Arrays.asList(ids.split(","));
		List<PurOrderDetail> purOrderDetails = purOrderDetailService.listByIds(list);

		for (PurOrderDetail purOrderDetail : purOrderDetails) {
			PurReceiveDetail entity = new PurReceiveDetail();

			BeanUtils.copyProperties(purOrderDetail, entity);
			entity.setId(null);
			entity.setOrderDetailId(purOrderDetail.getId());


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
			entity.setReceiveQty(purOrderDetail.getOrderQty());


			result.add(entity);
		}

		return result;
	}

}
