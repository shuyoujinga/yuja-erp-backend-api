package org.jeecg.modules.inv.invassembly.service.impl;

import org.jeecg.modules.inv.invassembly.entity.InvAssembly;
import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyDetailMapper;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyMapper;
import org.jeecg.modules.inv.invassembly.service.IInvAssemblyService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 组装单
 * @Author: 舒有敬
 * @Date:   2025-12-16
 * @Version: V1.0
 */
@Service
public class InvAssemblyServiceImpl extends ServiceImpl<InvAssemblyMapper, InvAssembly> implements IInvAssemblyService {

	@Autowired
	private InvAssemblyMapper invAssemblyMapper;
	@Autowired
	private InvAssemblyDetailMapper invAssemblyDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvAssembly invAssembly, List<InvAssemblyDetail> invAssemblyDetailList) {
		invAssemblyMapper.insert(invAssembly);
		if(invAssemblyDetailList!=null && invAssemblyDetailList.size()>0) {
			for(InvAssemblyDetail entity:invAssemblyDetailList) {
				//外键设置
				entity.setPid(invAssembly.getId());
				invAssemblyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvAssembly invAssembly,List<InvAssemblyDetail> invAssemblyDetailList) {
		invAssemblyMapper.updateById(invAssembly);
		
		//1.先删除子表数据
		invAssemblyDetailMapper.deleteByMainId(invAssembly.getId());
		
		//2.子表数据重新插入
		if(invAssemblyDetailList!=null && invAssemblyDetailList.size()>0) {
			for(InvAssemblyDetail entity:invAssemblyDetailList) {
				//外键设置
				entity.setPid(invAssembly.getId());
				invAssemblyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invAssemblyDetailMapper.deleteByMainId(id);
		invAssemblyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invAssemblyDetailMapper.deleteByMainId(id.toString());
			invAssemblyMapper.deleteById(id);
		}
	}
	
}
