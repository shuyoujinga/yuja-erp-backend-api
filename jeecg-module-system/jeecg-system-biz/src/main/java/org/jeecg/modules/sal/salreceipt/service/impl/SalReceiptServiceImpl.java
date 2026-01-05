package org.jeecg.modules.sal.salreceipt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.sal.salreceipt.entity.SalReceipt;
import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import org.jeecg.modules.sal.salreceipt.mapper.SalReceiptDetailMapper;
import org.jeecg.modules.sal.salreceipt.mapper.SalReceiptMapper;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptDetailService;
import org.jeecg.modules.sal.salreceipt.service.ISalReceiptService;
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
 * @Description: 销售收款
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalReceiptServiceImpl extends ServiceImpl<SalReceiptMapper, SalReceipt> implements ISalReceiptService {

	@Resource
	private SalReceiptMapper salReceiptMapper;
	@Resource
	private SalReceiptDetailMapper salReceiptDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalReceipt salReceipt, List<SalReceiptDetail> salReceiptDetailList) {
		salReceipt.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSSK));
		salReceiptMapper.insert(salReceipt);
		if(salReceiptDetailList!=null && salReceiptDetailList.size()>0) {
			for(SalReceiptDetail entity:salReceiptDetailList) {
				//外键设置
				entity.setPid(salReceipt.getId());
				salReceiptDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalReceipt salReceipt,List<SalReceiptDetail> salReceiptDetailList) {
		salReceiptMapper.updateById(salReceipt);
		
		//1.先删除子表数据
		salReceiptDetailMapper.deleteByMainId(salReceipt.getId());
		
		//2.子表数据重新插入
		if(salReceiptDetailList!=null && salReceiptDetailList.size()>0) {
			for(SalReceiptDetail entity:salReceiptDetailList) {
				//外键设置
				entity.setPid(salReceipt.getId());
				salReceiptDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salReceiptDetailMapper.deleteByMainId(id);
		salReceiptMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salReceiptDetailMapper.deleteByMainId(id.toString());
			salReceiptMapper.deleteById(id);
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

		List<SalReceipt> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (SalReceipt record : records) {
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
