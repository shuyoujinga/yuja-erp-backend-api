package org.jeecg.modules.pur.pursettle.service.impl;

import org.jeecg.modules.pur.pursettle.entity.PurSettle;
import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import org.jeecg.modules.pur.pursettle.mapper.PurSettleDetailMapper;
import org.jeecg.modules.pur.pursettle.mapper.PurSettleMapper;
import org.jeecg.modules.pur.pursettle.service.IPurSettleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购结算
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Service
public class PurSettleServiceImpl extends ServiceImpl<PurSettleMapper, PurSettle> implements IPurSettleService {

	@Autowired
	private PurSettleMapper purSettleMapper;
	@Autowired
	private PurSettleDetailMapper purSettleDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurSettle purSettle, List<PurSettleDetail> purSettleDetailList) {
		purSettleMapper.insert(purSettle);
		if(purSettleDetailList!=null && purSettleDetailList.size()>0) {
			for(PurSettleDetail entity:purSettleDetailList) {
				//外键设置
				entity.setPid(purSettle.getId());
				purSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurSettle purSettle,List<PurSettleDetail> purSettleDetailList) {
		purSettleMapper.updateById(purSettle);
		
		//1.先删除子表数据
		purSettleDetailMapper.deleteByMainId(purSettle.getId());
		
		//2.子表数据重新插入
		if(purSettleDetailList!=null && purSettleDetailList.size()>0) {
			for(PurSettleDetail entity:purSettleDetailList) {
				//外键设置
				entity.setPid(purSettle.getId());
				purSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purSettleDetailMapper.deleteByMainId(id);
		purSettleMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purSettleDetailMapper.deleteByMainId(id.toString());
			purSettleMapper.deleteById(id);
		}
	}
	
}
