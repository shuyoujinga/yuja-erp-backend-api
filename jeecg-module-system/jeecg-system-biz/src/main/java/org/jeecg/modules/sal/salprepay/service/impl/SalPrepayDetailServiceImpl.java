package org.jeecg.modules.sal.salprepay.service.impl;

import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import org.jeecg.modules.sal.salprepay.mapper.SalPrepayDetailMapper;
import org.jeecg.modules.sal.salprepay.service.ISalPrepayDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 预收使用_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalPrepayDetailServiceImpl extends ServiceImpl<SalPrepayDetailMapper, SalPrepayDetail> implements ISalPrepayDetailService {
	
	@Autowired
	private SalPrepayDetailMapper salPrepayDetailMapper;
	
	@Override
	public List<SalPrepayDetail> selectByMainId(String mainId) {
		return salPrepayDetailMapper.selectByMainId(mainId);
	}
}
