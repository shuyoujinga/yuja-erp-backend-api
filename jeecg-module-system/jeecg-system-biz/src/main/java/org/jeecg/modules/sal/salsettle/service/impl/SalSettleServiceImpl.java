package org.jeecg.modules.sal.salsettle.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.sal.salsettle.entity.SalSettle;
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import org.jeecg.modules.sal.salsettle.mapper.SalSettleDetailMapper;
import org.jeecg.modules.sal.salsettle.mapper.SalSettleMapper;
import org.jeecg.modules.sal.salsettle.service.ISalSettleService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售结算
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalSettleServiceImpl extends ServiceImpl<SalSettleMapper, SalSettle> implements ISalSettleService {

	@Resource
	private SalSettleMapper salSettleMapper;
	@Resource
	private SalSettleDetailMapper salSettleDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalSettle salSettle, List<SalSettleDetail> salSettleDetailList) {
		salSettle.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSJS));
		Assert.isTrue(AmountUtils.isNegative(salSettle.getAmount()),"结算合计金额是负数,不可结算!");
		salSettleMapper.insert(salSettle);
		if(salSettleDetailList!=null && salSettleDetailList.size()>0) {
			for(SalSettleDetail entity:salSettleDetailList) {
				//外键设置
				entity.setPid(salSettle.getId());
				salSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalSettle salSettle,List<SalSettleDetail> salSettleDetailList) {
		Assert.isTrue(AmountUtils.isNegative(salSettle.getAmount()),"结算合计金额是负数,不可结算!");
		salSettleMapper.updateById(salSettle);
		
		//1.先删除子表数据
		salSettleDetailMapper.deleteByMainId(salSettle.getId());
		
		//2.子表数据重新插入
		if(salSettleDetailList!=null && salSettleDetailList.size()>0) {
			for(SalSettleDetail entity:salSettleDetailList) {
				//外键设置
				entity.setPid(salSettle.getId());
				salSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salSettleDetailMapper.deleteByMainId(id);
		salSettleMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salSettleDetailMapper.deleteByMainId(id.toString());
			salSettleMapper.deleteById(id);
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

		List<SalSettle> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (SalSettle record : records) {
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
