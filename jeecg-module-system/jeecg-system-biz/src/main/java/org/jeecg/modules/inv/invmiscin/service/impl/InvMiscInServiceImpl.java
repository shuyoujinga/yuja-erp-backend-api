package org.jeecg.modules.inv.invmiscin.service.impl;

import org.jeecg.modules.inv.invmiscin.entity.InvMiscIn;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import org.jeecg.modules.inv.invmiscin.mapper.InvMiscInDetailMapper;
import org.jeecg.modules.inv.invmiscin.mapper.InvMiscInMapper;
import org.jeecg.modules.inv.invmiscin.service.IInvMiscInService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 其他入库
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Service
public class InvMiscInServiceImpl extends ServiceImpl<InvMiscInMapper, InvMiscIn> implements IInvMiscInService {

	@Autowired
	private InvMiscInMapper invMiscInMapper;
	@Autowired
	private InvMiscInDetailMapper invMiscInDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvMiscIn invMiscIn, List<InvMiscInDetail> invMiscInDetailList) {
		invMiscInMapper.insert(invMiscIn);
		if(invMiscInDetailList!=null && invMiscInDetailList.size()>0) {
			for(InvMiscInDetail entity:invMiscInDetailList) {
				//外键设置
				entity.setPid(invMiscIn.getId());
				invMiscInDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvMiscIn invMiscIn,List<InvMiscInDetail> invMiscInDetailList) {
		invMiscInMapper.updateById(invMiscIn);
		
		//1.先删除子表数据
		invMiscInDetailMapper.deleteByMainId(invMiscIn.getId());
		
		//2.子表数据重新插入
		if(invMiscInDetailList!=null && invMiscInDetailList.size()>0) {
			for(InvMiscInDetail entity:invMiscInDetailList) {
				//外键设置
				entity.setPid(invMiscIn.getId());
				invMiscInDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invMiscInDetailMapper.deleteByMainId(id);
		invMiscInMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invMiscInDetailMapper.deleteByMainId(id.toString());
			invMiscInMapper.deleteById(id);
		}
	}
	
}
