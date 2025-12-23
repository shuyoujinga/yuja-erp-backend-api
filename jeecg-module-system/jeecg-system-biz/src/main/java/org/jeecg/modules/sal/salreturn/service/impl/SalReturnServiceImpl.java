package org.jeecg.modules.sal.salreturn.service.impl;

import org.jeecg.modules.sal.salreturn.entity.SalReturn;
import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import org.jeecg.modules.sal.salreturn.mapper.SalReturnDetailMapper;
import org.jeecg.modules.sal.salreturn.mapper.SalReturnMapper;
import org.jeecg.modules.sal.salreturn.service.ISalReturnService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售退货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalReturnServiceImpl extends ServiceImpl<SalReturnMapper, SalReturn> implements ISalReturnService {

	@Autowired
	private SalReturnMapper salReturnMapper;
	@Autowired
	private SalReturnDetailMapper salReturnDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalReturn salReturn, List<SalReturnDetail> salReturnDetailList) {
		salReturnMapper.insert(salReturn);
		if(salReturnDetailList!=null && salReturnDetailList.size()>0) {
			for(SalReturnDetail entity:salReturnDetailList) {
				//外键设置
				entity.setPid(salReturn.getId());
				salReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalReturn salReturn,List<SalReturnDetail> salReturnDetailList) {
		salReturnMapper.updateById(salReturn);
		
		//1.先删除子表数据
		salReturnDetailMapper.deleteByMainId(salReturn.getId());
		
		//2.子表数据重新插入
		if(salReturnDetailList!=null && salReturnDetailList.size()>0) {
			for(SalReturnDetail entity:salReturnDetailList) {
				//外键设置
				entity.setPid(salReturn.getId());
				salReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salReturnDetailMapper.deleteByMainId(id);
		salReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salReturnDetailMapper.deleteByMainId(id.toString());
			salReturnMapper.deleteById(id);
		}
	}
	
}
