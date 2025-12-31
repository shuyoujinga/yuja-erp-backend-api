package org.jeecg.modules.sal.salorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.sal.salorder.entity.SalOrder;
import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.mapper.SalOrderDetailMapper;
import org.jeecg.modules.sal.salorder.mapper.SalOrderMapper;
import org.jeecg.modules.sal.salorder.service.ISalOrderService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.utils.AmountUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

/**
 * @Description: 销售订单
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalOrderServiceImpl extends ServiceImpl<SalOrderMapper, SalOrder> implements ISalOrderService {

	@Resource
	private SalOrderMapper salOrderMapper;
	@Resource
	private SalOrderDetailMapper salOrderDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalOrder salOrder, List<SalOrderDetail> salOrderDetailList) {
		salOrder.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSDD));
		double totalAmount = AmountUtils.sumTotalAmount(salOrderDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d));
		salOrder.setAmount(totalAmount);
		salOrderMapper.insert(salOrder);
		if(salOrderDetailList!=null && salOrderDetailList.size()>0) {
			for(SalOrderDetail entity:salOrderDetailList) {
				//外键设置
				entity.setPid(salOrder.getId());
				salOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalOrder salOrder,List<SalOrderDetail> salOrderDetailList) {
		double totalAmount = AmountUtils.sumTotalAmount(salOrderDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d));
		salOrder.setAmount(totalAmount);
		salOrderMapper.updateById(salOrder);
		
		//1.先删除子表数据
		salOrderDetailMapper.deleteByMainId(salOrder.getId());
		
		//2.子表数据重新插入
		if(salOrderDetailList!=null && salOrderDetailList.size()>0) {
			for(SalOrderDetail entity:salOrderDetailList) {
				//外键设置
				entity.setPid(salOrder.getId());
				salOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salOrderDetailMapper.deleteByMainId(id);
		salOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salOrderDetailMapper.deleteByMainId(id.toString());
			salOrderMapper.deleteById(id);
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

		List<SalOrder> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (SalOrder record : records) {
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
