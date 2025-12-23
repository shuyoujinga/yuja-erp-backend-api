package org.jeecg.modules.sal.salreceipt.service.impl;

import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import org.jeecg.modules.sal.salreceipt.mapper.SalReceiptDetailMapper;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售收款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalReceiptDetailServiceImpl extends ServiceImpl<SalReceiptDetailMapper, SalReceiptDetail> implements ISalReceiptDetailService {
	
	@Autowired
	private SalReceiptDetailMapper salReceiptDetailMapper;
	
	@Override
	public List<SalReceiptDetail> selectByMainId(String mainId) {
		return salReceiptDetailMapper.selectByMainId(mainId);
	}
}
