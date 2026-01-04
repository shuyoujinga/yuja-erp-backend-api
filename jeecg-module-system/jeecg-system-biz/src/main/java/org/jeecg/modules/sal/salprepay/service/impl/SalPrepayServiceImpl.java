package org.jeecg.modules.sal.salprepay.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.sal.salprepay.entity.SalPrepay;
import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import org.jeecg.modules.sal.salprepay.mapper.SalPrepayDetailMapper;
import org.jeecg.modules.sal.salprepay.mapper.SalPrepayMapper;
import org.jeecg.modules.sal.salprepay.service.ISalPrepayService;
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
 * @Description: 销售预收
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalPrepayServiceImpl extends ServiceImpl<SalPrepayMapper, SalPrepay> implements ISalPrepayService {

	@Resource
	private SalPrepayMapper salPrepayMapper;
	@Resource
	private SalPrepayDetailMapper salPrepayDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalPrepay salPrepay, List<SalPrepayDetail> salPrepayDetailList) {
		salPrepay.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSYS));
		salPrepayMapper.insert(salPrepay);
		if(salPrepayDetailList!=null && salPrepayDetailList.size()>0) {
			for(SalPrepayDetail entity:salPrepayDetailList) {
				//外键设置
				entity.setPid(salPrepay.getId());
				salPrepayDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalPrepay salPrepay,List<SalPrepayDetail> salPrepayDetailList) {
		salPrepayMapper.updateById(salPrepay);
		
		//1.先删除子表数据
		salPrepayDetailMapper.deleteByMainId(salPrepay.getId());
		
		//2.子表数据重新插入
		if(salPrepayDetailList!=null && salPrepayDetailList.size()>0) {
			for(SalPrepayDetail entity:salPrepayDetailList) {
				//外键设置
				entity.setPid(salPrepay.getId());
				salPrepayDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salPrepayDetailMapper.deleteByMainId(id);
		salPrepayMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salPrepayDetailMapper.deleteByMainId(id.toString());
			salPrepayMapper.deleteById(id);
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

		List<SalPrepay> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (SalPrepay record : records) {

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
