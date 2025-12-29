package org.jeecg.modules.inv.invissue.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invissue.entity.InvIssue;
import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.mapper.InvIssueDetailMapper;
import org.jeecg.modules.inv.invissue.mapper.InvIssueMapper;
import org.jeecg.modules.inv.invissue.service.IInvIssueService;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Description: 物料领用
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvIssueServiceImpl extends ServiceImpl<InvIssueMapper, InvIssue> implements IInvIssueService {

	@Autowired
	private InvIssueMapper invIssueMapper;
	@Autowired
	private InvIssueDetailMapper invIssueDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherService invMaterialVoucherService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvIssue invIssue, List<InvIssueDetail> invIssueDetailList) {
		invIssue.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.WLLY));
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

    @Override
	@Transactional(rollbackFor = Exception.class)
    public int audit(List<String> ids) {


		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) {

		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.NO);
	}
	/**
	 * 批量更新审核状态
	 *
	 * @param ids 待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<InvIssue> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (InvIssue record : records) {
			if (!status.equals(record.getAudit())) {
				record.setAudit(status);
				record.setAuditBy(sysUser.getUsername());
				record.setAuditTime(new Date());
				count++;
			}
		}

		if (count > 0) {
			updateBatchById(records);
		}
		return count;
	}
}
