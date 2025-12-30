package org.jeecg.modules.inv.invmiscout.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscIn;
import org.jeecg.modules.inv.invmiscout.entity.InvMiscOut;
import org.jeecg.modules.inv.invmiscout.entity.InvMiscOutDetail;
import org.jeecg.modules.inv.invmiscout.mapper.InvMiscOutDetailMapper;
import org.jeecg.modules.inv.invmiscout.mapper.InvMiscOutMapper;
import org.jeecg.modules.inv.invmiscout.service.IInvMiscOutService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 其他出库
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvMiscOutServiceImpl extends ServiceImpl<InvMiscOutMapper, InvMiscOut> implements IInvMiscOutService {

	@Resource
	private InvMiscOutMapper invMiscOutMapper;
	@Resource
	private InvMiscOutDetailMapper invMiscOutDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService iInvMaterialVoucherCustomService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvMiscOut invMiscOut, List<InvMiscOutDetail> invMiscOutDetailList) {
		invMiscOut.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.QTCK));
		invMiscOutMapper.insert(invMiscOut);
		if(invMiscOutDetailList!=null && invMiscOutDetailList.size()>0) {
			for(InvMiscOutDetail entity:invMiscOutDetailList) {
				//外键设置
				entity.setPid(invMiscOut.getId());
				invMiscOutDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvMiscOut invMiscOut,List<InvMiscOutDetail> invMiscOutDetailList) {
		invMiscOutMapper.updateById(invMiscOut);
		
		//1.先删除子表数据
		invMiscOutDetailMapper.deleteByMainId(invMiscOut.getId());
		
		//2.子表数据重新插入
		if(invMiscOutDetailList!=null && invMiscOutDetailList.size()>0) {
			for(InvMiscOutDetail entity:invMiscOutDetailList) {
				//外键设置
				entity.setPid(invMiscOut.getId());
				invMiscOutDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invMiscOutDetailMapper.deleteByMainId(id.toString());
		invMiscOutDetailMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for (Serializable id : idList) {
			invMiscOutDetailMapper.deleteByMainId(id.toString());
			invMiscOutDetailMapper.deleteById(id);
		}
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {

		// 生成物料凭证
		List<InvMiscOut> invMiscIns = listByIds(ids);

		Assert.isTrue(CollectionUtil.isEmpty(invMiscIns), "其他入库单审核失败,选择数据不存在!");

		for (InvMiscOut invMiscIn : invMiscIns) {
			// 审核的数据就不需要生成物料凭证
			if (Constants.DICT_AUDIT_STATUS.YES.equals(invMiscIn.getAudit())) {
				continue;
			}

			List<InvMiscOutDetail> invMiscInDetails = invMiscOutDetailMapper.selectByMainId(invMiscIn.getId());
			Assert.isTrue(CollectionUtil.isEmpty(invMiscInDetails), String.format("其他入库单审核失败,[%s]不存在明细!", invMiscIn.getDocCode()));

			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(invMiscIn.getOutType(), invMiscIn.getDocCode(), invMiscIn.getId(), invMiscIn.getRemark());

			List<InvMaterialVoucherDetail> detailList = new ArrayList<>();
			for (InvMiscOutDetail invMiscInDetail : invMiscInDetails) {
				InvMaterialVoucherDetail entity = new InvMaterialVoucherDetail();

				entity.setSourceDocDetailId(invMiscInDetail.getId());
				entity.setMaterialCode(invMiscInDetail.getMaterialCode());
				entity.setUnit(invMiscInDetail.getUnit());
				entity.setSpecifications(invMiscInDetail.getSpecifications());
				entity.setQty(invMiscInDetail.getQty());

				entity.setMoveType(invMiscIn.getOutType());
				entity.setWarehouseCode(invMiscIn.getWarehouseCode());

				detailList.add(entity);


			}
			iInvMaterialVoucherCustomService.createVoucher(invMaterialVoucher, detailList);


		}


		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		// 生成物料凭证
		List<InvMiscOut> invMiscIns = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(invMiscIns), "其他入库单反审核失败,选择数据不存在!");

		for (InvMiscOut invMiscIn : invMiscIns) {
			// 反审核的数据就不需要生成物料凭证
			if (Constants.DICT_AUDIT_STATUS.NO.equals(invMiscIn.getAudit())) {
				continue;
			}
			String voucherIdBySourceDocId = iInvMaterialVoucherCustomService.getVoucherIdBySourceDocId(invMiscIn.getId());
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
	 * @param ids    待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<InvMiscOut> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (InvMiscOut record : records) {
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
