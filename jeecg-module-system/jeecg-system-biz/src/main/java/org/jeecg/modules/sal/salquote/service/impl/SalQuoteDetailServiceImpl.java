package org.jeecg.modules.sal.salquote.service.impl;

import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import org.jeecg.modules.sal.salquote.mapper.SalQuoteDetailMapper;
import org.jeecg.modules.sal.salquote.service.ISalQuoteDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售报价_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class SalQuoteDetailServiceImpl extends ServiceImpl<SalQuoteDetailMapper, SalQuoteDetail> implements ISalQuoteDetailService {
	
	@Autowired
	private SalQuoteDetailMapper salQuoteDetailMapper;
	
	@Override
	public List<SalQuoteDetail> selectByMainId(String mainId) {
		return salQuoteDetailMapper.selectByMainId(mainId);
	}
}
