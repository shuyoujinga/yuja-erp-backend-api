package org.jeecg.modules.pur.purpayment.service.impl;

import org.jeecg.modules.pur.purpayment.entity.PurPayment;
import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import org.jeecg.modules.pur.purpayment.mapper.PurPaymentDetailMapper;
import org.jeecg.modules.pur.purpayment.mapper.PurPaymentMapper;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购付款
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurPaymentServiceImpl extends ServiceImpl<PurPaymentMapper, PurPayment> implements IPurPaymentService {

	@Autowired
	private PurPaymentMapper purPaymentMapper;
	@Autowired
	private PurPaymentDetailMapper purPaymentDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurPayment purPayment, List<PurPaymentDetail> purPaymentDetailList) {
		purPaymentMapper.insert(purPayment);
		if(purPaymentDetailList!=null && purPaymentDetailList.size()>0) {
			for(PurPaymentDetail entity:purPaymentDetailList) {
				//外键设置
				entity.setPid(purPayment.getId());
				purPaymentDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurPayment purPayment,List<PurPaymentDetail> purPaymentDetailList) {
		purPaymentMapper.updateById(purPayment);
		
		//1.先删除子表数据
		purPaymentDetailMapper.deleteByMainId(purPayment.getId());
		
		//2.子表数据重新插入
		if(purPaymentDetailList!=null && purPaymentDetailList.size()>0) {
			for(PurPaymentDetail entity:purPaymentDetailList) {
				//外键设置
				entity.setPid(purPayment.getId());
				purPaymentDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purPaymentDetailMapper.deleteByMainId(id);
		purPaymentMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purPaymentDetailMapper.deleteByMainId(id.toString());
			purPaymentMapper.deleteById(id);
		}
	}
	
}
