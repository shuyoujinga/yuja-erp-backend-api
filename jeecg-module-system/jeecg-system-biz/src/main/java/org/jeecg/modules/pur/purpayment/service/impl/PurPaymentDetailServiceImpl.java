package org.jeecg.modules.pur.purpayment.service.impl;

import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import org.jeecg.modules.pur.purpayment.mapper.PurPaymentDetailMapper;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购付款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurPaymentDetailServiceImpl extends ServiceImpl<PurPaymentDetailMapper, PurPaymentDetail> implements IPurPaymentDetailService {
	
	@Autowired
	private PurPaymentDetailMapper purPaymentDetailMapper;
	
	@Override
	public List<PurPaymentDetail> selectByMainId(String mainId) {
		return purPaymentDetailMapper.selectByMainId(mainId);
	}
}
