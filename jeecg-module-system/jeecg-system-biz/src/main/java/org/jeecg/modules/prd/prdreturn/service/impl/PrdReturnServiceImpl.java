package org.jeecg.modules.prd.prdreturn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invissue.entity.InvIssue;
import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.service.IInvIssueService;
import org.jeecg.modules.prd.prdreturn.entity.PrdReturn;
import org.jeecg.modules.prd.prdreturn.entity.PrdReturnDetail;
import org.jeecg.modules.prd.prdreturn.mapper.PrdReturnDetailMapper;
import org.jeecg.modules.prd.prdreturn.mapper.PrdReturnMapper;
import org.jeecg.modules.prd.prdreturn.service.IPrdReturnService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 生产退料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PrdReturnServiceImpl extends ServiceImpl<PrdReturnMapper, PrdReturn> implements IPrdReturnService {

	@Resource
	private PrdReturnMapper prdReturnMapper;
	@Resource
	private PrdReturnDetailMapper prdReturnDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvIssueService invIssueService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdReturn prdReturn, List<PrdReturnDetail> prdReturnDetailList) {
		prdReturn.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.SCTL));

		prdReturnMapper.insert(prdReturn);
		if(prdReturnDetailList!=null && prdReturnDetailList.size()>0) {
			for(PrdReturnDetail entity:prdReturnDetailList) {
				//外键设置
				entity.setPid(prdReturn.getId());
				prdReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdReturn prdReturn,List<PrdReturnDetail> prdReturnDetailList) {
		prdReturnMapper.updateById(prdReturn);
		
		//1.先删除子表数据
		prdReturnDetailMapper.deleteByMainId(prdReturn.getId());
		
		//2.子表数据重新插入
		if(prdReturnDetailList!=null && prdReturnDetailList.size()>0) {
			for(PrdReturnDetail entity:prdReturnDetailList) {
				//外键设置
				entity.setPid(prdReturn.getId());
				prdReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdReturnDetailMapper.deleteByMainId(id);
		prdReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdReturnDetailMapper.deleteByMainId(id.toString());
			prdReturnMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		// 审核
		List<PrdReturn> prdIssueList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(prdIssueList),"操作失败!领料单不存在!");
		for (PrdReturn prdIssue : prdIssueList) {
			if (Constants.DICT_AUDIT_STATUS.YES.equals(prdIssue.getAudit())) {
				continue;
			}
			List<PrdReturnDetail> prdIssueDetailList = prdReturnDetailMapper.selectByMainId(prdIssue.getId());
			Map<String,List<PrdReturnDetail>> prdIssueDetailMap = new HashMap<String,List<PrdReturnDetail>>();
			for (PrdReturnDetail detail : prdIssueDetailList) {
				prdIssueDetailMap
						.computeIfAbsent(detail.getWarehouseCode(), k -> new ArrayList<>())
						.add(detail);
			}
			for (String key : prdIssueDetailMap.keySet()) {
				InvIssue invIssue = new InvIssue();
				invIssue.setWarehouseCode(key);
				invIssue.setDocTime(prdIssue.getDocTime());
				invIssue.setOrgCode(prdIssue.getPrdLine());
				invIssue.setPurpose(Constants.ISSUE_PURPOSE.RETURN);
				List<PrdReturnDetail> issueDetailList = prdIssueDetailMap.get(key);
				List<InvIssueDetail> invIssueDetailList=new ArrayList<>();
				for (PrdReturnDetail prdIssueDetail : issueDetailList) {
					InvIssueDetail invIssueDetail = new InvIssueDetail();
					BeanUtils.copyProperties(prdIssueDetail,invIssueDetail);
					invIssueDetail.setId(null);
					invIssueDetailList.add(invIssueDetail);
				}
				invIssue.setPrdIssueId(prdIssue.getId());
				invIssueService.saveMain(invIssue,invIssueDetailList);
				invIssueService.audit(Collections.singletonList(invIssue.getId()));


			}
		}


		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		// 审核
		List<PrdReturn> prdIssueList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(prdIssueList),"操作失败!领料单不存在!");
		for (PrdReturn prdIssue : prdIssueList) {
			if (Constants.DICT_AUDIT_STATUS.NO.equals(prdIssue.getAudit())) {
				continue;
			}
			List<InvIssue> invIssueList=invIssueService.list(new LambdaQueryWrapper<InvIssue>().eq(InvIssue::getPrdIssueId, prdIssue.getId()));
			Assert.isTrue(CollectionUtil.isEmpty(invIssueList),"操作失败!物料领料单不存在!");

			List<String> invIds = invIssueList.stream().map(InvIssue::getId).collect(Collectors.toList());
			invIssueService.unAudit(invIds);
			invIssueService.delBatchMain(invIds);

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

		List<PrdReturn> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PrdReturn record : records) {
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
