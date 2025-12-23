package org.jeecg.modules.pur.purorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import org.jeecg.modules.pur.purapply.entity.PurApply;
import org.jeecg.modules.pur.purapply.service.IPurApplyService;
import org.jeecg.modules.pur.purorder.entity.PurOrder;
import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import org.jeecg.modules.pur.purorder.mapper.PurOrderDetailMapper;
import org.jeecg.modules.pur.purorder.mapper.PurOrderMapper;
import org.jeecg.modules.pur.purorder.service.IPurOrderService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

/**
 * @Description: 采购订单
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
@Service
public class PurOrderServiceImpl extends ServiceImpl<PurOrderMapper, PurOrder> implements IPurOrderService {

	@Resource
	private PurOrderMapper purOrderMapper;
	@Resource
	private PurOrderDetailMapper purOrderDetailMapper;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private IPurApplyService purApplyService;

	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurOrder purOrder, List<PurOrderDetail> purOrderDetailList) {
		purOrder.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGDD));
		double totalAmount = AmountUtils.sumTotalAmount(purOrderDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d));
		purOrder.setAmount(totalAmount);
		purOrderMapper.insert(purOrder);
		if(purOrderDetailList!=null && purOrderDetailList.size()>0) {
			for(PurOrderDetail entity:purOrderDetailList) {
				//外键设置
				entity.setId(null);
				entity.setPid(purOrder.getId());
				purOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurOrder purOrder,List<PurOrderDetail> purOrderDetailList) {
		double totalAmount = AmountUtils.sumTotalAmount(purOrderDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d));
		purOrder.setAmount(totalAmount);
		purOrderMapper.updateById(purOrder);
		
		//1.先删除子表数据
		purOrderDetailMapper.deleteByMainId(purOrder.getId());
		
		//2.子表数据重新插入
		if(purOrderDetailList!=null && purOrderDetailList.size()>0) {
			for(PurOrderDetail entity:purOrderDetailList) {
				//外键设置
				entity.setPid(purOrder.getId());
				purOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purOrderDetailMapper.deleteByMainId(id);
		purOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purOrderDetailMapper.deleteByMainId(id.toString());
			purOrderMapper.deleteById(id);
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
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
	 * @param ids 待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<PurOrder> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PurOrder record : records) {

			PurApply entity = purApplyService.getById(record.getApplyId());
			Assert.isTrue(ObjectUtils.isEmpty(entity),String.format("操作失败，单据%s不存在！", record.getDocCode()+"("+record.getApplyId()+")"));
			// 当前状态未审核 且 申请单已经引用的，则不允许审核！
			boolean flag = Constants.DICT_AUDIT_STATUS.YES.equals(entity.getStatus())&&Constants.DICT_AUDIT_STATUS.NO.equals(record.getAudit());
			Assert.isTrue(flag,String.format("操作失败，单据%s已引用！", record.getDocCode()+"("+record.getApplyId()+")"));



			// 引用状态位与审核的一样
			entity.setStatus(status);
			purApplyService.updateById(entity);



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
