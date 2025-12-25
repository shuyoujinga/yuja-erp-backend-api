package org.jeecg.modules.pur.purreturn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import org.jeecg.modules.pur.purorder.service.IPurOrderDetailService;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnDetailMapper;
import org.jeecg.modules.pur.purreturn.service.IPurReturnDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 采购退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurReturnDetailServiceImpl extends ServiceImpl<PurReturnDetailMapper, PurReturnDetail> implements IPurReturnDetailService {
	
	@Resource
	private PurReturnDetailMapper purReturnDetailMapper;
	@Autowired
	private IPurOrderDetailService purOrderDetailService;
	
	@Override
	public List<PurReturnDetail> selectByMainId(String mainId) {
		return purReturnDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<PurReturnDetail> selectByTargetId(String ids) {
		List<PurReturnDetail> result=new ArrayList<>();
		List<String> list = Arrays.asList(ids.split(","));
		if (CollectionUtil.isEmpty(list)) {
			return result;
		}
		List<PurOrderDetail> purOrderDetails = purOrderDetailService.listByIds(list);

		for (PurOrderDetail orderDetail : purOrderDetails) {
			PurReturnDetail  entity=new PurReturnDetail();

			entity.setMaterialCode(orderDetail.getMaterialCode());
			entity.setOrderDetailId(orderDetail.getId());
			entity.setOrderQty(orderDetail.getOrderQty());
			entity.setOrderUnitPrice(orderDetail.getUnitPrice());
			entity.setOrderAmount(orderDetail.getAmount());

			entity.setQty(orderDetail.getOrderQty());
			entity.setUnitPrice(orderDetail.getUnitPrice());
			entity.setAmount(orderDetail.getAmount());

			entity.setRemark(orderDetail.getRemark());
			result.add(entity);


		}



		return result;
    }
}
