package org.jeecg.modules.prd.prdmps.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsDetailMapper;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsDetailService;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcess;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessDetailService;
import org.jeecg.modules.prd.prdprocess.service.IPrdProcessService;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanBomDetailService;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanDetailService;
import org.jeecg.modules.sal.salbizplan.service.ISalBizPlanService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 生产计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Service
public class PrdMpsDetailServiceImpl extends ServiceImpl<PrdMpsDetailMapper, PrdMpsDetail> implements IPrdMpsDetailService {
	
	@Resource
	private PrdMpsDetailMapper prdMpsDetailMapper;

	@Autowired
	private ISalBizPlanService salBizPlanService;

	@Autowired
	private ISalBizPlanDetailService salBizPlanDetailService;


	@Autowired
	private IPrdProcessService prdProcessService;

	@Autowired
	private IPrdProcessDetailService prdProcessDetailService;



	
	@Override
	public List<PrdMpsDetail> selectByMainId(String mainId) {
		return prdMpsDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<PrdMpsDetail> selectByTargetId(String ids) {
		List<PrdMpsDetail> result=new ArrayList<>();

		List<String> list = Arrays.asList(ids.split(","));
		Assert.isTrue(CollectionUtil.isEmpty(list),"操作失败!传入ID为空!");

		List<SalBizPlanDetail> salBizPlanDetailList = salBizPlanDetailService.listByIds(list);
		Assert.isTrue(CollectionUtil.isEmpty(salBizPlanDetailList),"操作失败! 明细列表为为空!");
		List<PrdProcess> prdProcessesList = prdProcessService.list();
		Assert.isTrue(CollectionUtil.isEmpty(salBizPlanDetailList),"操作失败! 工序表表为为空!");
		Map<String, String> processMap = prdProcessesList.stream().collect(Collectors.toMap(PrdProcess::getMaterialCode, PrdProcess::getId));
		Map<String,List<PrdProcessDetail>> processDetailMap=new HashMap<>();
		for (SalBizPlanDetail planDetail : salBizPlanDetailList) {

			Assert.isTrue(!processMap.containsKey(planDetail.getMaterialCode()),String.format("操作失败!物料[%s]不存在生产工序,请联系相关人员维护!",planDetail.getMaterialCode()));

			List<PrdProcessDetail> processDetailList = prdProcessDetailService.selectByMainId(processMap.get(planDetail.getMaterialCode()));
			processDetailMap.put(planDetail.getMaterialCode(),processDetailList);
		}
		// 开始合并-拆分前中后工序
		for (SalBizPlanDetail bizPlanDetail : salBizPlanDetailList) {

			List<PrdProcessDetail> processDetailList = processDetailMap.get(bizPlanDetail.getMaterialCode());
			for (PrdProcessDetail prdProcessDetail : processDetailList) {
				PrdMpsDetail entity=new PrdMpsDetail();
				entity.setBizDetailId(bizPlanDetail.getId());
				entity.setMaterialCode(prdProcessDetail.getMaterialCode());
				entity.setUnit(prdProcessDetail.getUnit());
				entity.setSpecifications(prdProcessDetail.getSpecifications());
				entity.setProcessType(prdProcessDetail.getProcessType());
				entity.setProcessCode(prdProcessDetail.getProcessCode());
				entity.setBizQty(bizPlanDetail.getQty());
				entity.setQty(bizPlanDetail.getQty());
				entity.setRemark(bizPlanDetail.getRemark());
				result.add(entity);
			}
		}


		return result;
    }
}
