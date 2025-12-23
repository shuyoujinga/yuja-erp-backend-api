package org.jeecg.modules.prd.prdmps.service.impl;

import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsDetailMapper;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Service
public class PrdMpsDetailServiceImpl extends ServiceImpl<PrdMpsDetailMapper, PrdMpsDetail> implements IPrdMpsDetailService {
	
	@Autowired
	private PrdMpsDetailMapper prdMpsDetailMapper;
	
	@Override
	public List<PrdMpsDetail> selectByMainId(String mainId) {
		return prdMpsDetailMapper.selectByMainId(mainId);
	}
}
