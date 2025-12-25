package org.jeecg.modules.prd.prdworkorder.service.impl;

import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.mapper.PrdWorkOrderDetailMapper;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产工单_物料明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdWorkOrderDetailServiceImpl extends ServiceImpl<PrdWorkOrderDetailMapper, PrdWorkOrderDetail> implements IPrdWorkOrderDetailService {
	
	@Autowired
	private PrdWorkOrderDetailMapper prdWorkOrderDetailMapper;
	
	@Override
	public List<PrdWorkOrderDetail> selectByMainId(String mainId) {
		return prdWorkOrderDetailMapper.selectByMainId(mainId);
	}
}
