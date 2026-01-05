package org.jeecg.modules.sal.salreturn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.sal.salreturn.entity.SalReturn;
import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import org.jeecg.modules.sal.salreturn.mapper.SalReturnDetailMapper;
import org.jeecg.modules.sal.salreturn.mapper.SalReturnMapper;
import org.jeecg.modules.sal.salreturn.service.ISalReturnService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @Description: 销售退货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class SalReturnServiceImpl extends ServiceImpl<SalReturnMapper, SalReturn> implements ISalReturnService {

	@Resource
	private SalReturnMapper salReturnMapper;
	@Resource
	private SalReturnDetailMapper salReturnDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService iInvMaterialVoucherCustomService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalReturn salReturn, List<SalReturnDetail> salReturnDetailList) {
		salReturn.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSTH));
		double totalAmount = AmountUtils.sumTotalAmount(
				salReturnDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		salReturn.setAmount(totalAmount);
		salReturnMapper.insert(salReturn);
		if(salReturnDetailList!=null && salReturnDetailList.size()>0) {
			for(SalReturnDetail entity:salReturnDetailList) {
				//外键设置
				entity.setPid(salReturn.getId());
				salReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalReturn salReturn,List<SalReturnDetail> salReturnDetailList) {
		double totalAmount = AmountUtils.sumTotalAmount(
				salReturnDetailList,
				d -> Optional.ofNullable(d.getAmount()).orElse(0d)
		);
		salReturn.setAmount(totalAmount);
		salReturnMapper.updateById(salReturn);
		
		//1.先删除子表数据
		salReturnDetailMapper.deleteByMainId(salReturn.getId());
		
		//2.子表数据重新插入
		if(salReturnDetailList!=null && salReturnDetailList.size()>0) {
			for(SalReturnDetail entity:salReturnDetailList) {
				//外键设置
				entity.setPid(salReturn.getId());
				salReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salReturnDetailMapper.deleteByMainId(id);
		salReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salReturnDetailMapper.deleteByMainId(id.toString());
			salReturnMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		// 产生物料凭证
		List<SalReturn> salReturnList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(salReturnList),"销售退货数据不存在!请检查!");


		for (SalReturn salReturn : salReturnList) {
			// 审核的数据排除在外
			if (Constants.DICT_AUDIT_STATUS.YES.equals(salReturn.getAudit())) {
				continue;
			}

			List<SalReturnDetail> salReturnDetailList = salReturnDetailMapper.selectByMainId(salReturn.getId());
			Assert.isTrue(CollectionUtil.isEmpty(salReturnDetailList),"销售退货明细数据不存在!请检查!");

			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(Constants.DICT_MOVE_TYPE.XSTH, salReturn.getDocCode(), salReturn.getId(), salReturn.getRemark());

			List<InvMaterialVoucherDetail> detailList= new ArrayList<>();
			for (SalReturnDetail salReturnDetail : salReturnDetailList) {

				InvMaterialVoucherDetail entity = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(salReturnDetail, entity);
				entity.setId(null);
				entity.setSourceDocDetailId(salReturnDetail.getId());
				entity.setPrice(salReturnDetail.getUnitPrice());
				entity.setMoveType(Constants.DICT_MOVE_TYPE.XSTH);
				entity.setWarehouseCode(salReturn.getStockCode());
				detailList.add(entity);
			}
			iInvMaterialVoucherCustomService.createVoucher(invMaterialVoucher,detailList);

		}

		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		// 产生物料凭证
		List<SalReturn> salReturnList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(salReturnList),"销售退货数据不存在!请检查!");


		for (SalReturn salReturn : salReturnList) {
			if (Constants.DICT_AUDIT_STATUS.NO.equals(salReturn.getAudit())) {
				continue;
			}
			String voucherIdBySourceDocId = iInvMaterialVoucherCustomService.getVoucherIdBySourceDocId(salReturn.getId());
			if (StringUtils.isEmpty(voucherIdBySourceDocId)) {
				continue;
			}
			iInvMaterialVoucherCustomService.reversalVoucher(voucherIdBySourceDocId);


		}
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

		List<SalReturn> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		// 判断是否存在已审核或状态为1的单据
		boolean hasInvalid = records.stream().anyMatch(p -> p.getStatus() == 1);

		Assert.isTrue(hasInvalid, "操作失败！存在引用的单据，请注意下游单据！");
		int count = 0;
		for (SalReturn record : records) {
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
