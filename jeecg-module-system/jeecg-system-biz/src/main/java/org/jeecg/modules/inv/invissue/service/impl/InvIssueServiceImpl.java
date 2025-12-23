package org.jeecg.modules.inv.invissue.service.impl;

import org.jeecg.modules.inv.invissue.entity.InvIssue;
import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.mapper.InvIssueDetailMapper;
import org.jeecg.modules.inv.invissue.mapper.InvIssueMapper;
import org.jeecg.modules.inv.invissue.service.IInvIssueService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 物料领用
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvIssueServiceImpl extends ServiceImpl<InvIssueMapper, InvIssue> implements IInvIssueService {

	@Autowired
	private InvIssueMapper invIssueMapper;
	@Autowired
	private InvIssueDetailMapper invIssueDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvIssue invIssue, List<InvIssueDetail> invIssueDetailList) {
		invIssueMapper.insert(invIssue);
		if(invIssueDetailList!=null && invIssueDetailList.size()>0) {
			for(InvIssueDetail entity:invIssueDetailList) {
				//外键设置
				entity.setPid(invIssue.getId());
				invIssueDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvIssue invIssue,List<InvIssueDetail> invIssueDetailList) {
		invIssueMapper.updateById(invIssue);
		
		//1.先删除子表数据
		invIssueDetailMapper.deleteByMainId(invIssue.getId());
		
		//2.子表数据重新插入
		if(invIssueDetailList!=null && invIssueDetailList.size()>0) {
			for(InvIssueDetail entity:invIssueDetailList) {
				//外键设置
				entity.setPid(invIssue.getId());
				invIssueDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invIssueDetailMapper.deleteByMainId(id);
		invIssueMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invIssueDetailMapper.deleteByMainId(id.toString());
			invIssueMapper.deleteById(id);
		}
	}
	
}
