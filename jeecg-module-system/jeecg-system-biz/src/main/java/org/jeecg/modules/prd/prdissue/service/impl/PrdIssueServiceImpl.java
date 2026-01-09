package org.jeecg.modules.prd.prdissue.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invissue.entity.InvIssue;
import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.service.IInvIssueService;
import org.jeecg.modules.prd.prdissue.entity.PrdIssue;
import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import org.jeecg.modules.prd.prdissue.mapper.PrdIssueDetailMapper;
import org.jeecg.modules.prd.prdissue.mapper.PrdIssueMapper;
import org.jeecg.modules.prd.prdissue.service.IPrdIssueService;
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
 * @Description: 生产领料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PrdIssueServiceImpl extends ServiceImpl<PrdIssueMapper, PrdIssue> implements IPrdIssueService {

	@Resource
	private PrdIssueMapper prdIssueMapper;
	@Resource
	private PrdIssueDetailMapper prdIssueDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvIssueService invIssueService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdIssue prdIssue, List<PrdIssueDetail> prdIssueDetailList) {
		prdIssue.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.SCLL));
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		// 审核
		List<PrdIssue> prdIssueList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(prdIssueList),"操作失败!领料单不存在!");
		for (PrdIssue prdIssue : prdIssueList) {
			if (Constants.DICT_AUDIT_STATUS.YES.equals(prdIssue.getAudit())) {
				continue;
			}
			List<PrdIssueDetail> prdIssueDetailList = prdIssueDetailMapper.selectByMainId(prdIssue.getId());
			Map<String,List<PrdIssueDetail>>  prdIssueDetailMap = new HashMap<String,List<PrdIssueDetail>>();
			for (PrdIssueDetail detail : prdIssueDetailList) {
				prdIssueDetailMap
						.computeIfAbsent(detail.getWarehouseCode(), k -> new ArrayList<>())
						.add(detail);
			}
			for (String key : prdIssueDetailMap.keySet()) {
				InvIssue invIssue = new InvIssue();
				invIssue.setWarehouseCode(key);
				invIssue.setDocTime(prdIssue.getDocTime());
				invIssue.setOrgCode(prdIssue.getPrdLine());
				invIssue.setPurpose(Constants.ISSUE_PURPOSE.PROD);
				List<PrdIssueDetail> issueDetailList = prdIssueDetailMap.get(key);
				List<InvIssueDetail> invIssueDetailList=new ArrayList<>();
				for (PrdIssueDetail prdIssueDetail : issueDetailList) {
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
		List<PrdIssue> prdIssueList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(prdIssueList),"操作失败!领料单不存在!");
		for (PrdIssue prdIssue : prdIssueList) {
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

		List<PrdIssue> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PrdIssue record : records) {
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
