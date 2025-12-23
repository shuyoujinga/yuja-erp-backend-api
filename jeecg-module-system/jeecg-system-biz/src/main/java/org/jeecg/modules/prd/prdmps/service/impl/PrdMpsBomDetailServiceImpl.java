package org.jeecg.modules.prd.prdmps.service.impl;

import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsBomDetailMapper;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsBomDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产计划_材料清单
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Service
public class PrdMpsBomDetailServiceImpl extends ServiceImpl<PrdMpsBomDetailMapper, PrdMpsBomDetail> implements IPrdMpsBomDetailService {
	
	@Autowired
	private PrdMpsBomDetailMapper prdMpsBomDetailMapper;
	
	@Override
	public List<PrdMpsBomDetail> selectByMainId(String mainId) {
		return prdMpsBomDetailMapper.selectByMainId(mainId);
	}
}
