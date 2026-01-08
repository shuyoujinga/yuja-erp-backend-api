package org.jeecg.modules.prd.prdworkorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.mapper.PrdWorkOrderDetailMapper;
import org.jeecg.modules.prd.prdworkorder.mapper.PrdWorkOrderMapper;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderService;
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
 * @Description: 生产工单
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PrdWorkOrderServiceImpl extends ServiceImpl<PrdWorkOrderMapper, PrdWorkOrder> implements IPrdWorkOrderService {

	@Resource
	private PrdWorkOrderMapper prdWorkOrderMapper;
	@Resource
	private PrdWorkOrderDetailMapper prdWorkOrderDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdWorkOrder prdWorkOrder, List<PrdWorkOrderDetail> prdWorkOrderDetailList) {
		prdWorkOrder.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.SCGD));
		prdWorkOrderMapper.insert(prdWorkOrder);
		if(prdWorkOrderDetailList!=null && prdWorkOrderDetailList.size()>0) {
			for(PrdWorkOrderDetail entity:prdWorkOrderDetailList) {
				//外键设置
				entity.setPid(prdWorkOrder.getId());
				prdWorkOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdWorkOrder prdWorkOrder,List<PrdWorkOrderDetail> prdWorkOrderDetailList) {
		prdWorkOrderMapper.updateById(prdWorkOrder);
		
		//1.先删除子表数据
		prdWorkOrderDetailMapper.deleteByMainId(prdWorkOrder.getId());
		
		//2.子表数据重新插入
		if(prdWorkOrderDetailList!=null && prdWorkOrderDetailList.size()>0) {
			for(PrdWorkOrderDetail entity:prdWorkOrderDetailList) {
				//外键设置
				entity.setPid(prdWorkOrder.getId());
				prdWorkOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdWorkOrderDetailMapper.deleteByMainId(id);
		prdWorkOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdWorkOrderDetailMapper.deleteByMainId(id.toString());
			prdWorkOrderMapper.deleteById(id);
		}
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
		// 审核方法

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
	 * @param ids    待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<PrdWorkOrder> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (PrdWorkOrder record : records) {
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
