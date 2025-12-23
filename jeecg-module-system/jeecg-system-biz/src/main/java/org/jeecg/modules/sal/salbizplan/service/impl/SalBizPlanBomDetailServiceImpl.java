package org.jeecg.modules.sal.salbizplan.service.impl;

import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanBomDetail;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanBomDetailMapper;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanBomDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 业务计划_材料明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalBizPlanBomDetailServiceImpl extends ServiceImpl<SalBizPlanBomDetailMapper, SalBizPlanBomDetail> implements ISalBizPlanBomDetailService {
	
	@Autowired
	private SalBizPlanBomDetailMapper salBizPlanBomDetailMapper;
	
	@Override
	public List<SalBizPlanBomDetail> selectByMainId(String mainId) {
		return salBizPlanBomDetailMapper.selectByMainId(mainId);
	}
}
