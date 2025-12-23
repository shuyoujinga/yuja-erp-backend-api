package org.jeecg.modules.inv.invdisassembly.service.impl;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassembly;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyDetailMapper;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyMapper;
import org.jeecg.modules.inv.invdisassembly.service.IInvDisassemblyService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 拆卸单
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Service
public class InvDisassemblyServiceImpl extends ServiceImpl<InvDisassemblyMapper, InvDisassembly> implements IInvDisassemblyService {

	@Autowired
	private InvDisassemblyMapper invDisassemblyMapper;
	@Autowired
	private InvDisassemblyDetailMapper invDisassemblyDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvDisassembly invDisassembly, List<InvDisassemblyDetail> invDisassemblyDetailList) {
		invDisassemblyMapper.insert(invDisassembly);
		if(invDisassemblyDetailList!=null && invDisassemblyDetailList.size()>0) {
			for(InvDisassemblyDetail entity:invDisassemblyDetailList) {
				//外键设置
				entity.setPid(invDisassembly.getId());
				invDisassemblyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvDisassembly invDisassembly,List<InvDisassemblyDetail> invDisassemblyDetailList) {
		invDisassemblyMapper.updateById(invDisassembly);
		
		//1.先删除子表数据
		invDisassemblyDetailMapper.deleteByMainId(invDisassembly.getId());
		
		//2.子表数据重新插入
		if(invDisassemblyDetailList!=null && invDisassemblyDetailList.size()>0) {
			for(InvDisassemblyDetail entity:invDisassemblyDetailList) {
				//外键设置
				entity.setPid(invDisassembly.getId());
				invDisassemblyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invDisassemblyDetailMapper.deleteByMainId(id);
		invDisassemblyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invDisassemblyDetailMapper.deleteByMainId(id.toString());
			invDisassemblyMapper.deleteById(id);
		}
	}
	
}
