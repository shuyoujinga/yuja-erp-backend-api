package org.jeecg.modules.sal.salbizplan.service.impl;

import org.jeecg.modules.sal.salbizplan.entity.SalBizPlan;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanBomDetail;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanDetailMapper;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanBomDetailMapper;
import org.jeecg.modules.sal.salbizplan.mapper.SalBizPlanMapper;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 业务计划
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalBizPlanServiceImpl extends ServiceImpl<SalBizPlanMapper, SalBizPlan> implements ISalBizPlanService {

	@Autowired
	private SalBizPlanMapper salBizPlanMapper;
	@Autowired
	private SalBizPlanDetailMapper salBizPlanDetailMapper;
	@Autowired
	private SalBizPlanBomDetailMapper salBizPlanBomDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalBizPlan salBizPlan, List<SalBizPlanDetail> salBizPlanDetailList,List<SalBizPlanBomDetail> salBizPlanBomDetailList) {
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
	
}
