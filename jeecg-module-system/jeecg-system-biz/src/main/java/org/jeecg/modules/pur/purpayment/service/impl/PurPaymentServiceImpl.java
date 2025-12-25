package org.jeecg.modules.pur.purpayment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pur.purpayment.entity.PurPayment;
import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import org.jeecg.modules.pur.purpayment.mapper.PurPaymentDetailMapper;
import org.jeecg.modules.pur.purpayment.mapper.PurPaymentMapper;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentService;
import org.jeecg.modules.pur.pursettle.entity.PurSettle;
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
 * @Description: 采购付款
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurPaymentServiceImpl extends ServiceImpl<PurPaymentMapper, PurPayment> implements IPurPaymentService {

	@Resource
	private PurPaymentMapper purPaymentMapper;
	@Resource
	private PurPaymentDetailMapper purPaymentDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurPayment purPayment, List<PurPaymentDetail> purPaymentDetailList) {
		purPayment.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGFK));
		purPaymentMapper.insert(purPayment);
		if(purPaymentDetailList!=null && purPaymentDetailList.size()>0) {
			for(PurPaymentDetail entity:purPaymentDetailList) {
				//外键设置
				entity.setPid(purPayment.getId());
				purPaymentDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurPayment purPayment,List<PurPaymentDetail> purPaymentDetailList) {
		purPaymentMapper.updateById(purPayment);
		
		//1.先删除子表数据
		purPaymentDetailMapper.deleteByMainId(purPayment.getId());
		
		//2.子表数据重新插入
		if(purPaymentDetailList!=null && purPaymentDetailList.size()>0) {
			for(PurPaymentDetail entity:purPaymentDetailList) {
				//外键设置
				entity.setPid(purPayment.getId());
				purPaymentDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purPaymentDetailMapper.deleteByMainId(id);
		purPaymentMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purPaymentDetailMapper.deleteByMainId(id.toString());
			purPaymentMapper.deleteById(id);
		}
	}

	@Override
	public int audit(List<String> ids) {

		return updateAuditStatus(ids,Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
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

		List<PurPayment> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PurPayment record : records) {

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
