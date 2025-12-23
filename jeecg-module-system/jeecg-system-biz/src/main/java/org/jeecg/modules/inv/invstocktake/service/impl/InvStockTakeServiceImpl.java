package org.jeecg.modules.inv.invstocktake.service.impl;

import org.jeecg.modules.inv.invstocktake.entity.InvStockTake;
import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import org.jeecg.modules.inv.invstocktake.mapper.InvStockTakeDetailMapper;
import org.jeecg.modules.inv.invstocktake.mapper.InvStockTakeMapper;
import org.jeecg.modules.inv.invstocktake.service.IInvStockTakeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 物料盘点
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvStockTakeServiceImpl extends ServiceImpl<InvStockTakeMapper, InvStockTake> implements IInvStockTakeService {

	@Autowired
	private InvStockTakeMapper invStockTakeMapper;
	@Autowired
	private InvStockTakeDetailMapper invStockTakeDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvStockTake invStockTake, List<InvStockTakeDetail> invStockTakeDetailList) {
		invStockTakeMapper.insert(invStockTake);
		if(invStockTakeDetailList!=null && invStockTakeDetailList.size()>0) {
			for(InvStockTakeDetail entity:invStockTakeDetailList) {
				//外键设置
				entity.setPid(invStockTake.getId());
				invStockTakeDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvStockTake invStockTake,List<InvStockTakeDetail> invStockTakeDetailList) {
		invStockTakeMapper.updateById(invStockTake);
		
		//1.先删除子表数据
		invStockTakeDetailMapper.deleteByMainId(invStockTake.getId());
		
		//2.子表数据重新插入
		if(invStockTakeDetailList!=null && invStockTakeDetailList.size()>0) {
			for(InvStockTakeDetail entity:invStockTakeDetailList) {
				//外键设置
				entity.setPid(invStockTake.getId());
				invStockTakeDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invStockTakeDetailMapper.deleteByMainId(id);
		invStockTakeMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invStockTakeDetailMapper.deleteByMainId(id.toString());
			invStockTakeMapper.deleteById(id);
		}
	}
	
}
