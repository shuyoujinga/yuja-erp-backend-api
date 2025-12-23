package org.jeecg.modules.sal.salreceipt.service.impl;

import org.jeecg.modules.sal.salreceipt.entity.SalReceipt;
import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import org.jeecg.modules.sal.salreceipt.mapper.SalReceiptDetailMapper;
import org.jeecg.modules.sal.salreceipt.mapper.SalReceiptMapper;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售收款
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalReceiptServiceImpl extends ServiceImpl<SalReceiptMapper, SalReceipt> implements ISalReceiptService {

	@Autowired
	private SalReceiptMapper salReceiptMapper;
	@Autowired
	private SalReceiptDetailMapper salReceiptDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalReceipt salReceipt, List<SalReceiptDetail> salReceiptDetailList) {
		salReceiptMapper.insert(salReceipt);
		if(salReceiptDetailList!=null && salReceiptDetailList.size()>0) {
			for(SalReceiptDetail entity:salReceiptDetailList) {
				//外键设置
				entity.setPid(salReceipt.getId());
				salReceiptDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalReceipt salReceipt,List<SalReceiptDetail> salReceiptDetailList) {
		salReceiptMapper.updateById(salReceipt);
		
		//1.先删除子表数据
		salReceiptDetailMapper.deleteByMainId(salReceipt.getId());
		
		//2.子表数据重新插入
		if(salReceiptDetailList!=null && salReceiptDetailList.size()>0) {
			for(SalReceiptDetail entity:salReceiptDetailList) {
				//外键设置
				entity.setPid(salReceipt.getId());
				salReceiptDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salReceiptDetailMapper.deleteByMainId(id);
		salReceiptMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salReceiptDetailMapper.deleteByMainId(id.toString());
			salReceiptMapper.deleteById(id);
		}
	}
	
}
