package org.jeecg.modules.pur.purapply.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import org.jeecg.modules.pur.purapply.entity.PurApply;
import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import org.jeecg.modules.pur.purapply.mapper.PurApplyDetailMapper;
import org.jeecg.modules.pur.purapply.mapper.PurApplyMapper;
import org.jeecg.modules.pur.purapply.service.IPurApplyService;
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
import java.util.Optional;

/**
 * @Description: 采购申请
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
@Service
public class PurApplyServiceImpl extends ServiceImpl<PurApplyMapper, PurApply> implements IPurApplyService {

	@Resource
	private PurApplyMapper purApplyMapper;
	@Resource
	private PurApplyDetailMapper purApplyDetailMapper;
	@Resource
	private SerialNumberService serialNumberService;;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurApply purApply, List<PurApplyDetail> purApplyDetailList) {

		purApply.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGSQ));
		double totalAmount = AmountUtils.sumTotalAmount(
				purApplyDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		purApply.setAmount(totalAmount);
		purApplyMapper.insert(purApply);
		if(purApplyDetailList!=null && purApplyDetailList.size()>0) {
			for(PurApplyDetail entity:purApplyDetailList) {
				//外键设置
				entity.setPid(purApply.getId());
				purApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurApply purApply,List<PurApplyDetail> purApplyDetailList) {
		double totalAmount = AmountUtils.sumTotalAmount(
				purApplyDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		purApply.setAmount(totalAmount);
		purApplyMapper.updateById(purApply);
		
		//1.先删除子表数据
		purApplyDetailMapper.deleteByMainId(purApply.getId());
		
		//2.子表数据重新插入
		if(purApplyDetailList!=null && purApplyDetailList.size()>0) {
			for(PurApplyDetail entity:purApplyDetailList) {
				//外键设置
				entity.setPid(purApply.getId());
				purApplyDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purApplyDetailMapper.deleteByMainId(id);
		purApplyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purApplyDetailMapper.deleteByMainId(id.toString());
			purApplyMapper.deleteById(id);
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

		List<PurApply> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		// 判断是否存在已审核或状态为1的单据
		boolean hasInvalid = records.stream().anyMatch(p -> p.getStatus() == 1);

		Assert.isTrue(hasInvalid, "操作失败！存在引用的单据，请注意下游单据！");
		int count = 0;
		for (PurApply record : records) {
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
