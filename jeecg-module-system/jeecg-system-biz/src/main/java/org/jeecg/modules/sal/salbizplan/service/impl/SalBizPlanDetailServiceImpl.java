package org.jeecg.modules.sal.salbizplan.service.impl;

import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanDetailMapper;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 业务计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalBizPlanDetailServiceImpl extends ServiceImpl<SalBizPlanDetailMapper, SalBizPlanDetail> implements ISalBizPlanDetailService {
	
	@Autowired
	private SalBizPlanDetailMapper salBizPlanDetailMapper;
	
	@Override
	public List<SalBizPlanDetail> selectByMainId(String mainId) {
		return salBizPlanDetailMapper.selectByMainId(mainId);
	}
}
