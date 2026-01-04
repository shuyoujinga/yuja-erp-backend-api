package org.jeecg.modules.sal.salbizplan.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlan;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanBomDetail;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanDetailMapper;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanBomDetailMapper;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanMapper;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 业务计划
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalBizPlanServiceImpl extends ServiceImpl<SalBizPlanMapper, SalBizPlan> implements ISalBizPlanService {

	@Resource
	private SalBizPlanMapper salBizPlanMapper;
	@Resource
	private SalBizPlanDetailMapper salBizPlanDetailMapper;
	@Resource
	private SalBizPlanBomDetailMapper salBizPlanBomDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalBizPlan salBizPlan, List<SalBizPlanDetail> salBizPlanDetailList,List<SalBizPlanBomDetail> salBizPlanBomDetailList) {
		salBizPlan.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.YWJH));
		salBizPlanMapper.insert(salBizPlan);
		if(salBizPlanDetailList!=null && salBizPlanDetailList.size()>0) {
			for(SalBizPlanDetail entity:salBizPlanDetailList) {
				//外键设置
				entity.setPid(salBizPlan.getId());
				salBizPlanDetailMapper.insert(entity);
			}
		}
		if(salBizPlanBomDetailList!=null && salBizPlanBomDetailList.size()>0) {
			for(SalBizPlanBomDetail entity:salBizPlanBomDetailList) {
				//外键设置
				entity.setPid(salBizPlan.getId());
				salBizPlanBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalBizPlan salBizPlan,List<SalBizPlanDetail> salBizPlanDetailList,List<SalBizPlanBomDetail> salBizPlanBomDetailList) {
		salBizPlanMapper.updateById(salBizPlan);
		
		//1.先删除子表数据
		salBizPlanDetailMapper.deleteByMainId(salBizPlan.getId());
		salBizPlanBomDetailMapper.deleteByMainId(salBizPlan.getId());
		
		//2.子表数据重新插入
		if(salBizPlanDetailList!=null && salBizPlanDetailList.size()>0) {
			for(SalBizPlanDetail entity:salBizPlanDetailList) {
				//外键设置
				entity.setPid(salBizPlan.getId());
				salBizPlanDetailMapper.insert(entity);
			}
		}

		if(salBizPlanBomDetailList!=null && salBizPlanBomDetailList.size()>0) {
			for(SalBizPlanBomDetail entity:salBizPlanBomDetailList) {
				//外键设置
				entity.setPid(salBizPlan.getId());
				salBizPlanBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salBizPlanDetailMapper.deleteByMainId(id);
		salBizPlanBomDetailMapper.deleteByMainId(id);
		salBizPlanMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salBizPlanDetailMapper.deleteByMainId(id.toString());
			salBizPlanBomDetailMapper.deleteByMainId(id.toString());
			salBizPlanMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
		return updateAuditStatus(ids,Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) {
		return updateAuditStatus(ids,Constants.DICT_AUDIT_STATUS.NO);
	}
	/**
	 * 批量更新审核状态
	 *
	 * @param ids    待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<SalBizPlan> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (SalBizPlan record : records) {
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
