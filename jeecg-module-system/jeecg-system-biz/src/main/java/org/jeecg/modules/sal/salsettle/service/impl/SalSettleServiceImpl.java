package org.jeecg.modules.sal.salsettle.service.impl;

import org.jeecg.modules.sal.salsettle.entity.SalSettle;
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import org.jeecg.modules.sal.salsettle.mapper.SalSettleDetailMapper;
import org.jeecg.modules.sal.salsettle.mapper.SalSettleMapper;
import org.jeecg.modules.sal.salsettle.service.ISalSettleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售结算
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalSettleServiceImpl extends ServiceImpl<SalSettleMapper, SalSettle> implements ISalSettleService {

	@Autowired
	private SalSettleMapper salSettleMapper;
	@Autowired
	private SalSettleDetailMapper salSettleDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalSettle salSettle, List<SalSettleDetail> salSettleDetailList) {
		salSettleMapper.insert(salSettle);
		if(salSettleDetailList!=null && salSettleDetailList.size()>0) {
			for(SalSettleDetail entity:salSettleDetailList) {
				//外键设置
				entity.setPid(salSettle.getId());
				salSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalSettle salSettle,List<SalSettleDetail> salSettleDetailList) {
		salSettleMapper.updateById(salSettle);
		
		//1.先删除子表数据
		salSettleDetailMapper.deleteByMainId(salSettle.getId());
		
		//2.子表数据重新插入
		if(salSettleDetailList!=null && salSettleDetailList.size()>0) {
			for(SalSettleDetail entity:salSettleDetailList) {
				//外键设置
				entity.setPid(salSettle.getId());
				salSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salSettleDetailMapper.deleteByMainId(id);
		salSettleMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salSettleDetailMapper.deleteByMainId(id.toString());
			salSettleMapper.deleteById(id);
		}
	}
	
}
