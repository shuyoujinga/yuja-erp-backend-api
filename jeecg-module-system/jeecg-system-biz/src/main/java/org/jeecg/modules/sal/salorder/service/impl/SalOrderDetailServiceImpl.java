package org.jeecg.modules.sal.salorder.service.impl;

import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.mapper.SalOrderDetailMapper;
import org.jeecg.modules.sal.salorder.service.ISalOrderDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售订单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class SalOrderDetailServiceImpl extends ServiceImpl<SalOrderDetailMapper, SalOrderDetail> implements ISalOrderDetailService {
	
	@Autowired
	private SalOrderDetailMapper salOrderDetailMapper;
	
	@Override
	public List<SalOrderDetail> selectByMainId(String mainId) {
		return salOrderDetailMapper.selectByMainId(mainId);
	}
}
