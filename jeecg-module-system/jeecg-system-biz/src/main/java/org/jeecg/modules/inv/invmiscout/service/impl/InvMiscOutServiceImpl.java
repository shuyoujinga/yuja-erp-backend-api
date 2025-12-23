package org.jeecg.modules.inv.invmiscout.service.impl;

import org.jeecg.modules.inv.invmiscout.entity.InvMiscOut;
import org.jeecg.modules.inv.invmiscout.entity.InvMiscOutDetail;
import org.jeecg.modules.inv.invmiscout.mapper.InvMiscOutDetailMapper;
import org.jeecg.modules.inv.invmiscout.mapper.InvMiscOutMapper;
import org.jeecg.modules.inv.invmiscout.service.IInvMiscOutService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 其他出库
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Service
public class InvMiscOutServiceImpl extends ServiceImpl<InvMiscOutMapper, InvMiscOut> implements IInvMiscOutService {

	@Autowired
	private InvMiscOutMapper invMiscOutMapper;
	@Autowired
	private InvMiscOutDetailMapper invMiscOutDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvMiscOut invMiscOut, List<InvMiscOutDetail> invMiscOutDetailList) {
		invMiscOutMapper.insert(invMiscOut);
		if(invMiscOutDetailList!=null && invMiscOutDetailList.size()>0) {
			for(InvMiscOutDetail entity:invMiscOutDetailList) {
				//外键设置
				entity.setPid(invMiscOut.getId());
				invMiscOutDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvMiscOut invMiscOut,List<InvMiscOutDetail> invMiscOutDetailList) {
		invMiscOutMapper.updateById(invMiscOut);
		
		//1.先删除子表数据
		invMiscOutDetailMapper.deleteByMainId(invMiscOut.getId());
		
		//2.子表数据重新插入
		if(invMiscOutDetailList!=null && invMiscOutDetailList.size()>0) {
			for(InvMiscOutDetail entity:invMiscOutDetailList) {
				//外键设置
				entity.setPid(invMiscOut.getId());
				invMiscOutDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invMiscOutDetailMapper.deleteByMainId(id);
		invMiscOutMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invMiscOutDetailMapper.deleteByMainId(id.toString());
			invMiscOutMapper.deleteById(id);
		}
	}
	
}
