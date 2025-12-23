package org.jeecg.modules.inv.invissue.service.impl;

import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.mapper.InvIssueDetailMapper;
import org.jeecg.modules.inv.invissue.service.IInvIssueDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 物料领用_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvIssueDetailServiceImpl extends ServiceImpl<InvIssueDetailMapper, InvIssueDetail> implements IInvIssueDetailService {
	
	@Autowired
	private InvIssueDetailMapper invIssueDetailMapper;
	
	@Override
	public List<InvIssueDetail> selectByMainId(String mainId) {
		return invIssueDetailMapper.selectByMainId(mainId);
	}
}
