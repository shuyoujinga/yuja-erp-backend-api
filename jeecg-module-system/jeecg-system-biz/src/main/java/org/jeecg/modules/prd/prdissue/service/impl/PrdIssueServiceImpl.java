package org.jeecg.modules.prd.prdissue.service.impl;

import org.jeecg.modules.prd.prdissue.entity.PrdIssue;
import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import org.jeecg.modules.prd.prdissue.mapper.PrdIssueDetailMapper;
import org.jeecg.modules.prd.prdissue.mapper.PrdIssueMapper;
import org.jeecg.modules.prd.prdissue.service.IPrdIssueService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产领料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdIssueServiceImpl extends ServiceImpl<PrdIssueMapper, PrdIssue> implements IPrdIssueService {

	@Autowired
	private PrdIssueMapper prdIssueMapper;
	@Autowired
	private PrdIssueDetailMapper prdIssueDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdIssue prdIssue, List<PrdIssueDetail> prdIssueDetailList) {
		prdIssueMapper.insert(prdIssue);
		if(prdIssueDetailList!=null && prdIssueDetailList.size()>0) {
			for(PrdIssueDetail entity:prdIssueDetailList) {
				//外键设置
				entity.setPid(prdIssue.getId());
				prdIssueDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdIssue prdIssue,List<PrdIssueDetail> prdIssueDetailList) {
		prdIssueMapper.updateById(prdIssue);
		
		//1.先删除子表数据
		prdIssueDetailMapper.deleteByMainId(prdIssue.getId());
		
		//2.子表数据重新插入
		if(prdIssueDetailList!=null && prdIssueDetailList.size()>0) {
			for(PrdIssueDetail entity:prdIssueDetailList) {
				//外键设置
				entity.setPid(prdIssue.getId());
				prdIssueDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdIssueDetailMapper.deleteByMainId(id);
		prdIssueMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdIssueDetailMapper.deleteByMainId(id.toString());
			prdIssueMapper.deleteById(id);
		}
	}
	
}
