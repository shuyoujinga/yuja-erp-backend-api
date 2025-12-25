package org.jeecg.modules.pur.purreturn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveDetailService;
import org.jeecg.modules.pur.purreturn.entity.PurReturn;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnDetailMapper;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnMapper;
import org.jeecg.modules.pur.purreturn.service.IPurReturnService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utils.AmountUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @Description: 采购退货
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurReturnServiceImpl extends ServiceImpl<PurReturnMapper, PurReturn> implements IPurReturnService {

	@Resource
	private PurReturnMapper purReturnMapper;
	@Resource
	private PurReturnDetailMapper purReturnDetailMapper;
	@Autowired
	private IPurReceiveDetailService purReceiveDetailService;
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurReturn purReturn, List<PurReturnDetail> purReturnDetailList) {

		purReturn.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGTH));
		double totalAmount = AmountUtils.sumTotalAmount(purReturnDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d));
		purReturn.setAmount(totalAmount);
		purReturnMapper.insert(purReturn);
		if(purReturnDetailList!=null && purReturnDetailList.size()>0) {
			for(PurReturnDetail entity:purReturnDetailList) {
				//外键设置
				entity.setPid(purReturn.getId());
				entity.setId(null);
				purReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurReturn purReturn,List<PurReturnDetail> purReturnDetailList) {
		purReturnMapper.updateById(purReturn);
		
		//1.先删除子表数据
		purReturnDetailMapper.deleteByMainId(purReturn.getId());
		
		//2.子表数据重新插入
		if(purReturnDetailList!=null && purReturnDetailList.size()>0) {
			for(PurReturnDetail entity:purReturnDetailList) {
				//外键设置
				entity.setPid(purReturn.getId());
				purReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purReturnDetailMapper.deleteByMainId(id);
		purReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purReturnDetailMapper.deleteByMainId(id.toString());
			purReturnMapper.deleteById(id);
		}
	}

	@Override
	public String smarkRemark(String orderDetailId, String supplierCode) {
		List<String> list = Arrays.asList(orderDetailId.split(","));
		Double receiveQty = purReceiveDetailService.selectReceiveQtyByOrderIds(list);

		// 兜底，防止 null
		double qty = receiveQty == null ? 0D : receiveQty;

		return String.format(
				"智能备注提示您，您选择的单据共计收货 %.2f，请注意退货数量，如果为“已收货退货”，请关注库存是否足够扣除，如库存不足将无法审核！",
				qty
		);
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

		List<PurReturn> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PurReturn record : records) {

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
