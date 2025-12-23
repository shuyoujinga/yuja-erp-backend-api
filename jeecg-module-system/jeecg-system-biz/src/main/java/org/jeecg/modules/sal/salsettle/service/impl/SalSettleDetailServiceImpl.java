package org.jeecg.modules.sal.salsettle.service.impl;

import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import org.jeecg.modules.sal.salsettle.mapper.SalSettleDetailMapper;
import org.jeecg.modules.sal.salsettle.service.ISalSettleDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalSettleDetailServiceImpl extends ServiceImpl<SalSettleDetailMapper, SalSettleDetail> implements ISalSettleDetailService {
	
	@Autowired
	private SalSettleDetailMapper salSettleDetailMapper;
	
	@Override
	public List<SalSettleDetail> selectByMainId(String mainId) {
		return salSettleDetailMapper.selectByMainId(mainId);
	}
}
