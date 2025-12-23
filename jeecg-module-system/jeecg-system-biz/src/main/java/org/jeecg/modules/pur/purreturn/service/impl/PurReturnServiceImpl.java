package org.jeecg.modules.pur.purreturn.service.impl;

import org.jeecg.modules.pur.purreturn.entity.PurReturn;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnDetailMapper;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnMapper;
import org.jeecg.modules.pur.purreturn.service.IPurReturnService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购退货
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurReturnServiceImpl extends ServiceImpl<PurReturnMapper, PurReturn> implements IPurReturnService {

	@Autowired
	private PurReturnMapper purReturnMapper;
	@Autowired
	private PurReturnDetailMapper purReturnDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurReturn purReturn, List<PurReturnDetail> purReturnDetailList) {
		purReturnMapper.insert(purReturn);
		if(purReturnDetailList!=null && purReturnDetailList.size()>0) {
			for(PurReturnDetail entity:purReturnDetailList) {
				//外键设置
				entity.setPid(purReturn.getId());
				purReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurReturn purReturn,List<PurReturnDetail> purReturnDetailList) {
		purReturnMapper.updateById(purReturn);
		
		//1.先删除子表数据
		purReturnDetailMapper.deleteByMainId(purReturn.getId());
		
		//2.子表数据重新插入
		if(purReturnDetailList!=null && purReturnDetailList.size()>0) {
			for(PurReturnDetail entity:purReturnDetailList) {
				//外键设置
				entity.setPid(purReturn.getId());
				purReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purReturnDetailMapper.deleteByMainId(id);
		purReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purReturnDetailMapper.deleteByMainId(id.toString());
			purReturnMapper.deleteById(id);
		}
	}
	
}
