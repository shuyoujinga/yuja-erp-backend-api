package org.jeecg.modules.sal.salreturn.service.impl;

import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import org.jeecg.modules.sal.salreturn.mapper.SalReturnDetailMapper;
import org.jeecg.modules.sal.salreturn.service.ISalReturnDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalReturnDetailServiceImpl extends ServiceImpl<SalReturnDetailMapper, SalReturnDetail> implements ISalReturnDetailService {
	
	@Autowired
	private SalReturnDetailMapper salReturnDetailMapper;
	
	@Override
	public List<SalReturnDetail> selectByMainId(String mainId) {
		return salReturnDetailMapper.selectByMainId(mainId);
	}
}
