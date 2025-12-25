package org.jeecg.modules.prd.prdreturn.service.impl;

import org.jeecg.modules.prd.prdreturn.entity.PrdReturnDetail;
import org.jeecg.modules.prd.prdreturn.mapper.PrdReturnDetailMapper;
import org.jeecg.modules.prd.prdreturn.service.IPrdReturnDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产退料_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdReturnDetailServiceImpl extends ServiceImpl<PrdReturnDetailMapper, PrdReturnDetail> implements IPrdReturnDetailService {
	
	@Autowired
	private PrdReturnDetailMapper prdReturnDetailMapper;
	
	@Override
	public List<PrdReturnDetail> selectByMainId(String mainId) {
		return prdReturnDetailMapper.selectByMainId(mainId);
	}
}
