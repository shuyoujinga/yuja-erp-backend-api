package org.jeecg.modules.prd.prdmps.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.prd.prdmps.entity.PrdMps;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsDetailMapper;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsBomDetailMapper;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsMapper;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsService;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderService;
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

/**
 * @Description: 生产计划
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PrdMpsServiceImpl extends ServiceImpl<PrdMpsMapper, PrdMps> implements IPrdMpsService {

	@Resource
	private PrdMpsMapper prdMpsMapper;
	@Resource
	private PrdMpsDetailMapper prdMpsDetailMapper;
	@Resource
	private PrdMpsBomDetailMapper prdMpsBomDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IPrdWorkOrderService 	prdWorkOrderService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdMps prdMps, List<PrdMpsDetail> prdMpsDetailList,List<PrdMpsBomDetail> prdMpsBomDetailList) {
		prdMps.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.SCJH));
		prdMpsMapper.insert(prdMps);
		if(prdMpsDetailList!=null && prdMpsDetailList.size()>0) {
			for(PrdMpsDetail entity:prdMpsDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsDetailMapper.insert(entity);
			}
		}
		if(prdMpsBomDetailList!=null && prdMpsBomDetailList.size()>0) {
			for(PrdMpsBomDetail entity:prdMpsBomDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdMps prdMps,List<PrdMpsDetail> prdMpsDetailList,List<PrdMpsBomDetail> prdMpsBomDetailList) {
		prdMpsMapper.updateById(prdMps);
		
		//1.先删除子表数据
		prdMpsDetailMapper.deleteByMainId(prdMps.getId());
		prdMpsBomDetailMapper.deleteByMainId(prdMps.getId());
		
		//2.子表数据重新插入
		if(prdMpsDetailList!=null && prdMpsDetailList.size()>0) {
			for(PrdMpsDetail entity:prdMpsDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsDetailMapper.insert(entity);
			}
		}
		if(prdMpsBomDetailList!=null && prdMpsBomDetailList.size()>0) {
			for(PrdMpsBomDetail entity:prdMpsBomDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdMpsDetailMapper.deleteByMainId(id);
		prdMpsBomDetailMapper.deleteByMainId(id);
		prdMpsMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdMpsDetailMapper.deleteByMainId(id.toString());
			prdMpsBomDetailMapper.deleteByMainId(id.toString());
			prdMpsMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
		// 审核生成生产工单
		List<PrdMps> prdMpsList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(prdMpsList),"操作失败!生产计划不存在!");

		for (PrdMps pm : prdMpsList) {
			if (Constants.DICT_AUDIT_STATUS.YES.equals(pm.getAudit())) {
				continue;
			}

			// 生成生产工单
			List<PrdMpsDetail> prdMpsDetailList = prdMpsDetailMapper.selectByMainId(pm.getId());
			List<PrdMpsBomDetail> prdMpsBomDetailList = prdMpsBomDetailMapper.selectByMainId(pm.getId());

			Assert.isTrue(CollectionUtil.isEmpty(prdMpsBomDetailList)||CollectionUtil.isEmpty(prdMpsDetailList),"操作失败!生产计划明细不存在!");
			Map<String,List<PrdMpsBomDetail>> bomDetailMap=new HashMap<String,List<PrdMpsBomDetail>>();


			for (PrdMpsBomDetail prdMpsBomDetail : prdMpsBomDetailList) {
				if (bomDetailMap.containsKey(prdMpsBomDetail.getProductionMaterialCode())) {
					List<PrdMpsBomDetail> prdMpsBomDetails = bomDetailMap.get(prdMpsBomDetail.getProductionMaterialCode());
					prdMpsBomDetails.add(prdMpsBomDetail);
					bomDetailMap.put(prdMpsBomDetail.getProductionMaterialCode(),prdMpsBomDetails);
				}else{
					List<PrdMpsBomDetail> prdMpsBomDetails =new ArrayList<>();
					prdMpsBomDetails.add(prdMpsBomDetail);
					bomDetailMap.put(prdMpsBomDetail.getProductionMaterialCode(),prdMpsBomDetails);
				}

			}

			for (PrdMpsDetail pmd : prdMpsDetailList) {
				PrdWorkOrder prdWorkOrder = new PrdWorkOrder();
				BeanUtils.copyProperties(pmd,prdWorkOrder);
				prdWorkOrder.setId(null);
				prdWorkOrder.setMpsDocCodes(pm.getDocCode());
				prdWorkOrder.setDocTime(pm.getDocTime());
				prdWorkOrder.setMpsIds(pm.getId());
				prdWorkOrder.setPlanQty(pmd.getQty());
				List<PrdWorkOrderDetail> detailList=new ArrayList<>();
				List<PrdMpsBomDetail> prdMpsBomDetails = bomDetailMap.get(pmd.getMaterialCode());
				for (PrdMpsBomDetail prdMpsBomDetail : prdMpsBomDetails) {
					PrdWorkOrderDetail prdWorkOrderDetail = new PrdWorkOrderDetail();
					BeanUtils.copyProperties(prdMpsBomDetail,prdWorkOrderDetail);
					prdWorkOrderDetail.setId(null);
					prdWorkOrderDetail.setPid(null);
					detailList.add(prdWorkOrderDetail);
				}
				prdWorkOrderService.saveMain(prdWorkOrder,detailList);


			}

		}


		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) {
		// 审核生成生产工单
		List<PrdMps> prdMpsList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(prdMpsList),"操作失败!生产计划不存在!");

		for (PrdMps pm : prdMpsList) {
			if (Constants.DICT_AUDIT_STATUS.NO.equals(pm.getAudit())) {
				continue;
			}
			//TODO 校验

			List<PrdWorkOrder> list = prdWorkOrderService.list(new LambdaQueryWrapper<PrdWorkOrder>().in(PrdWorkOrder::getMpsIds, pm.getId()));
			if (!CollectionUtil.isEmpty(list)) {
				for (PrdWorkOrder prdWorkOrder : list) {
					prdWorkOrderService.delMain(prdWorkOrder.getId());
				}

			}

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

		List<PrdMps> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PrdMps record : records) {
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
