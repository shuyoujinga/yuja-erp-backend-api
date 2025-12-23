package org.jeecg.modules.sal.salprepay.service.impl;

import org.jeecg.modules.sal.salprepay.entity.SalPrepay;
import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import org.jeecg.modules.sal.salprepay.mapper.SalPrepayDetailMapper;
import org.jeecg.modules.sal.salprepay.mapper.SalPrepayMapper;
import org.jeecg.modules.sal.salprepay.service.ISalPrepayService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售预收
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalPrepayServiceImpl extends ServiceImpl<SalPrepayMapper, SalPrepay> implements ISalPrepayService {

	@Autowired
	private SalPrepayMapper salPrepayMapper;
	@Autowired
	private SalPrepayDetailMapper salPrepayDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalPrepay salPrepay, List<SalPrepayDetail> salPrepayDetailList) {
		salPrepayMapper.insert(salPrepay);
		if(salPrepayDetailList!=null && salPrepayDetailList.size()>0) {
			for(SalPrepayDetail entity:salPrepayDetailList) {
				//外键设置
				entity.setPid(salPrepay.getId());
				salPrepayDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalPrepay salPrepay,List<SalPrepayDetail> salPrepayDetailList) {
		salPrepayMapper.updateById(salPrepay);
		
		//1.先删除子表数据
		salPrepayDetailMapper.deleteByMainId(salPrepay.getId());
		
		//2.子表数据重新插入
		if(salPrepayDetailList!=null && salPrepayDetailList.size()>0) {
			for(SalPrepayDetail entity:salPrepayDetailList) {
				//外键设置
				entity.setPid(salPrepay.getId());
				salPrepayDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salPrepayDetailMapper.deleteByMainId(id);
		salPrepayMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salPrepayDetailMapper.deleteByMainId(id.toString());
			salPrepayMapper.deleteById(id);
		}
	}
	
}
