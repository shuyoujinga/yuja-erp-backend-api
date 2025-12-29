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
import org.jeecg.modules.inv.invissue.util.IssuePurposeMoveTypeMapping;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
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

	@Resource
	private InvIssueMapper invIssueMapper;
	@Resource
	private InvIssueDetailMapper invIssueDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService invMaterialVoucherService;
	
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
    public int audit(List<String> ids) throws Exception {

		// 物料领用\审核逻辑
		List<InvIssue> invIssues = listByIds(ids);
		Assert.isTrue(CollectionUtils.isEmpty(invIssues),"物料领用单已经失效,请检查!");

		for (InvIssue invIssue : invIssues) {
			// 已经审核的单据不需要构建物料凭证
			if (Constants.DICT_AUDIT_STATUS.YES.equals(invIssue.getAudit())) {
				continue;
			}

			List<InvIssueDetail> invIssueDetailList = invIssueDetailMapper.selectByMainId(invIssue.getId());
			String moveType = IssuePurposeMoveTypeMapping.getMoveType(invIssue.getPurpose());
			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(moveType,invIssue.getDocCode(),invIssue.getId(),invIssue.getRemark());
			invMaterialVoucher.setOrgCode(invIssue.getOrgCode());
			List<InvMaterialVoucherDetail> detailList =new ArrayList<InvMaterialVoucherDetail>();
			for (InvIssueDetail invIssueDetail : invIssueDetailList) {
				InvMaterialVoucherDetail entity = new InvMaterialVoucherDetail();
				entity.setSourceDocDetailId(invIssueDetail.getId());
				entity.setMaterialCode(invIssueDetail.getMaterialCode());
				entity.setUnit(invIssueDetail.getUnit());
				entity.setSpecifications(invIssueDetail.getSpecifications());
				entity.setQty(invIssueDetail.getQty());
				entity.setWarehouseCode(invIssue.getWarehouseCode());
				entity.setMoveType(moveType);
				detailList.add(entity);

			}

			invMaterialVoucherService.createVoucher(invMaterialVoucher,detailList);

		}
		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		// 物料领用\审核逻辑
		List<InvIssue> invIssues = listByIds(ids);
		Assert.isTrue(CollectionUtils.isEmpty(invIssues),"物料领用单已经失效,请检查!");

		for (InvIssue invIssue : invIssues) {
			// 未审核的单据不需要构建物料凭证
			if (Constants.DICT_AUDIT_STATUS.NO.equals(invIssue.getAudit())) {
				continue;
			}
			String reversalId = invMaterialVoucherService.getVoucherIdBySourceDocId(invIssue.getId());

			invMaterialVoucherService.reversalVoucher(reversalId);


		}


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
