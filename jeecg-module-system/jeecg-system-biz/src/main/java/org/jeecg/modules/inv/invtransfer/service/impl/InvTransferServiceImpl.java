package org.jeecg.modules.inv.invtransfer.service.impl;

import org.jeecg.modules.inv.invtransfer.entity.InvTransfer;
import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import org.jeecg.modules.inv.invtransfer.mapper.InvTransferDetailMapper;
import org.jeecg.modules.inv.invtransfer.mapper.InvTransferMapper;
import org.jeecg.modules.inv.invtransfer.service.IInvTransferService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 物料调拨
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvTransferServiceImpl extends ServiceImpl<InvTransferMapper, InvTransfer> implements IInvTransferService {

	@Autowired
	private InvTransferMapper invTransferMapper;
	@Autowired
	private InvTransferDetailMapper invTransferDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvTransfer invTransfer, List<InvTransferDetail> invTransferDetailList) {
		invTransferMapper.insert(invTransfer);
		if(invTransferDetailList!=null && invTransferDetailList.size()>0) {
			for(InvTransferDetail entity:invTransferDetailList) {
				//外键设置
				entity.setPid(invTransfer.getId());
				invTransferDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvTransfer invTransfer,List<InvTransferDetail> invTransferDetailList) {
		invTransferMapper.updateById(invTransfer);
		
		//1.先删除子表数据
		invTransferDetailMapper.deleteByMainId(invTransfer.getId());
		
		//2.子表数据重新插入
		if(invTransferDetailList!=null && invTransferDetailList.size()>0) {
			for(InvTransferDetail entity:invTransferDetailList) {
				//外键设置
				entity.setPid(invTransfer.getId());
				invTransferDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invTransferDetailMapper.deleteByMainId(id);
		invTransferMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invTransferDetailMapper.deleteByMainId(id.toString());
			invTransferMapper.deleteById(id);
		}
	}
	
}
