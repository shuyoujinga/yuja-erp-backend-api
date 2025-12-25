package org.jeecg.modules.prd.prdissue.service.impl;

import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import org.jeecg.modules.prd.prdissue.mapper.PrdIssueDetailMapper;
import org.jeecg.modules.prd.prdissue.service.IPrdIssueDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产领料_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdIssueDetailServiceImpl extends ServiceImpl<PrdIssueDetailMapper, PrdIssueDetail> implements IPrdIssueDetailService {
	
	@Autowired
	private PrdIssueDetailMapper prdIssueDetailMapper;
	
	@Override
	public List<PrdIssueDetail> selectByMainId(String mainId) {
		return prdIssueDetailMapper.selectByMainId(mainId);
	}
}
