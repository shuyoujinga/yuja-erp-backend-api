package org.jeecg.modules.sal.saldelivery.service.impl;

import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.mapper.SalDeliveryDetailMapper;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售发货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalDeliveryDetailServiceImpl extends ServiceImpl<SalDeliveryDetailMapper, SalDeliveryDetail> implements ISalDeliveryDetailService {
	
	@Autowired
	private SalDeliveryDetailMapper salDeliveryDetailMapper;
	
	@Override
	public List<SalDeliveryDetail> selectByMainId(String mainId) {
		return salDeliveryDetailMapper.selectByMainId(mainId);
	}
}
