package org.jeecg.modules.pur.supplierquotation.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotation;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
import org.jeecg.modules.pur.supplierquotation.mapper.PurSupplierQuotationDetailMapper;
import org.jeecg.modules.pur.supplierquotation.mapper.PurSupplierQuotationMapper;
import org.jeecg.modules.pur.supplierquotation.service.IPurSupplierQuotationService;
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
 * @Description: 采购报价
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Service
public class PurSupplierQuotationServiceImpl extends ServiceImpl<PurSupplierQuotationMapper, PurSupplierQuotation> implements IPurSupplierQuotationService {

	@Resource
	private PurSupplierQuotationMapper purSupplierQuotationMapper;
	@Resource
	private PurSupplierQuotationDetailMapper purSupplierQuotationDetailMapper;
	@Resource
	private SerialNumberService serialNumberService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurSupplierQuotation purSupplierQuotation, List<PurSupplierQuotationDetail> purSupplierQuotationDetailList) {
		// 设置报价单号
		purSupplierQuotation.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGBJ));
		double totalAmount = AmountUtils.sumTotalAmount(
				purSupplierQuotationDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		purSupplierQuotation.setAmount(totalAmount);

		purSupplierQuotationMapper.insert(purSupplierQuotation);
		if(purSupplierQuotationDetailList!=null && purSupplierQuotationDetailList.size()>0) {
			for(PurSupplierQuotationDetail entity:purSupplierQuotationDetailList) {
				//外键设置
				entity.setPid(purSupplierQuotation.getId());
				purSupplierQuotationDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurSupplierQuotation purSupplierQuotation,List<PurSupplierQuotationDetail> purSupplierQuotationDetailList) {
		double totalAmount = AmountUtils.sumTotalAmount(
				purSupplierQuotationDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		purSupplierQuotation.setAmount(totalAmount);
		purSupplierQuotationMapper.updateById(purSupplierQuotation);

		//1.先删除子表数据
		purSupplierQuotationDetailMapper.deleteByMainId(purSupplierQuotation.getId());
		
		//2.子表数据重新插入
		if(purSupplierQuotationDetailList!=null && purSupplierQuotationDetailList.size()>0) {
			for(PurSupplierQuotationDetail entity:purSupplierQuotationDetailList) {
				//外键设置
				entity.setPid(purSupplierQuotation.getId());
				purSupplierQuotationDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purSupplierQuotationDetailMapper.deleteByMainId(id);
		purSupplierQuotationMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purSupplierQuotationDetailMapper.deleteByMainId(id.toString());
			purSupplierQuotationMapper.deleteById(id);
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

		List<PurSupplierQuotation> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PurSupplierQuotation record : records) {
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
