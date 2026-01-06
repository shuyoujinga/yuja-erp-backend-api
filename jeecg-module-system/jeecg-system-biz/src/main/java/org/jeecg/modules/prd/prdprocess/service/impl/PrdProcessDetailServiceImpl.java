package org.jeecg.modules.prd.prdprocess.service.impl;

import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import org.jeecg.modules.prd.prdprocess.mapper.PrdProcessDetailMapper;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产工序_明细
 * @Author: 舒有敬
 * @Date:   2026-01-06
 * @Version: V1.0
 */
@Service
public class PrdProcessDetailServiceImpl extends ServiceImpl<PrdProcessDetailMapper, PrdProcessDetail> implements IPrdProcessDetailService {
	
	@Autowired
	private PrdProcessDetailMapper prdProcessDetailMapper;
	
	@Override
	public List<PrdProcessDetail> selectByMainId(String mainId) {
		return prdProcessDetailMapper.selectByMainId(mainId);
	}
}
