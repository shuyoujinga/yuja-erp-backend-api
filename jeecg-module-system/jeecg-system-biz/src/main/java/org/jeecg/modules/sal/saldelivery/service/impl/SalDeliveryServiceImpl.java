package org.jeecg.modules.sal.saldelivery.service.impl;

import org.jeecg.modules.sal.saldelivery.entity.SalDelivery;
import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.mapper.SalDeliveryDetailMapper;
import org.jeecg.modules.sal.saldelivery.mapper.SalDeliveryMapper;
import org.jeecg.modules.sal.saldelivery.service.ISalDeliveryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售出货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalDeliveryServiceImpl extends ServiceImpl<SalDeliveryMapper, SalDelivery> implements ISalDeliveryService {

	@Autowired
	private SalDeliveryMapper salDeliveryMapper;
	@Autowired
	private SalDeliveryDetailMapper salDeliveryDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalDelivery salDelivery, List<SalDeliveryDetail> salDeliveryDetailList) {
		salDeliveryMapper.insert(salDelivery);
		if(salDeliveryDetailList!=null && salDeliveryDetailList.size()>0) {
			for(SalDeliveryDetail entity:salDeliveryDetailList) {
				//外键设置
				entity.setPid(salDelivery.getId());
				salDeliveryDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalDelivery salDelivery,List<SalDeliveryDetail> salDeliveryDetailList) {
		salDeliveryMapper.updateById(salDelivery);
		
		//1.先删除子表数据
		salDeliveryDetailMapper.deleteByMainId(salDelivery.getId());
		
		//2.子表数据重新插入
		if(salDeliveryDetailList!=null && salDeliveryDetailList.size()>0) {
			for(SalDeliveryDetail entity:salDeliveryDetailList) {
				//外键设置
				entity.setPid(salDelivery.getId());
				salDeliveryDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salDeliveryDetailMapper.deleteByMainId(id);
		salDeliveryMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salDeliveryDetailMapper.deleteByMainId(id.toString());
			salDeliveryMapper.deleteById(id);
		}
	}
	
}
