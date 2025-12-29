package org.jeecg.modules.inv.invstocktake.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.inv.invstocktake.entity.InvStockTake;
import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import org.jeecg.modules.inv.invstocktake.mapper.InvStockTakeDetailMapper;
import org.jeecg.modules.inv.invstocktake.mapper.InvStockTakeMapper;
import org.jeecg.modules.inv.invstocktake.service.IInvStockTakeService;
import org.jeecg.modules.inv.invtransfer.entity.InvTransfer;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 物料盘点
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvStockTakeServiceImpl extends ServiceImpl<InvStockTakeMapper, InvStockTake> implements IInvStockTakeService {

	@Resource
	private InvStockTakeMapper invStockTakeMapper;
	@Resource
	private InvStockTakeDetailMapper invStockTakeDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService invMaterialVoucherCustomService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvStockTake invStockTake, List<InvStockTakeDetail> invStockTakeDetailList) {
		invStockTake.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.WLPD));
		invStockTakeMapper.insert(invStockTake);
		if(invStockTakeDetailList!=null && invStockTakeDetailList.size()>0) {
			for(InvStockTakeDetail entity:invStockTakeDetailList) {
				//外键设置
				entity.setPid(invStockTake.getId());
				invStockTakeDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvStockTake invStockTake,List<InvStockTakeDetail> invStockTakeDetailList) {
		invStockTakeMapper.updateById(invStockTake);
		
		//1.先删除子表数据
		invStockTakeDetailMapper.deleteByMainId(invStockTake.getId());
		
		//2.子表数据重新插入
		if(invStockTakeDetailList!=null && invStockTakeDetailList.size()>0) {
			for(InvStockTakeDetail entity:invStockTakeDetailList) {
				//外键设置
				entity.setPid(invStockTake.getId());
				invStockTakeDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invStockTakeDetailMapper.deleteByMainId(id);
		invStockTakeMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invStockTakeDetailMapper.deleteByMainId(id.toString());
			invStockTakeMapper.deleteById(id);
		}
	}

	@Override
	public int audit(List<String> ids) throws Exception {
		// 审核生成物料凭证

		List<InvStockTake> invStockTakes = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(invStockTakes),"盘点单不存在,请检查!");



		for (InvStockTake invStockTake : invStockTakes) {
			List<InvStockTakeDetail> invStockTakeDetails = invStockTakeDetailMapper.selectByMainId(invStockTake.getId());
			List<InvStockTakeDetail> diffDetails = invStockTakeDetails.stream()
					.filter(d -> d.getTakeType() != null && d.getTakeType() != 0)
					.collect(Collectors.toList());
			// 账实一致,不需要生成凭证
			if (CollectionUtil.isEmpty(diffDetails)) {
				break;
			}
			long profitCount = diffDetails.stream()
					.filter(d -> d.getTakeType() == 1)
					.count();

			long lossCount = diffDetails.stream()
					.filter(d -> d.getTakeType() == -1)
					.count();
			String moveType;

			if (profitCount > lossCount) {
				moveType = Constants.DICT_MOVE_TYPE.PYRK;
			} else {
				moveType = Constants.DICT_MOVE_TYPE.PKCK;
			}

			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(moveType,invStockTake.getDocCode(),invStockTake.getId(),invStockTake.getRemark());

			List<InvMaterialVoucherDetail> detailList=new ArrayList<>();

			for (InvStockTakeDetail diffDetail : diffDetails) {
				InvMaterialVoucherDetail entity = new InvMaterialVoucherDetail();

				entity.setMaterialCode(diffDetail.getMaterialCode());
				entity.setUnit(diffDetail.getUnit());
				entity.setSpecifications(diffDetail.getSpecifications());
				entity.setWarehouseCode(invStockTake.getWarehouseCode());
				entity.setQty(Math.abs(diffDetail.getDiffQty()));
				entity.setStockType(diffDetail.getTakeType());
				entity.setMoveType(diffDetail.getTakeType()==1?Constants.DICT_MOVE_TYPE.PYRK: Constants.DICT_MOVE_TYPE.PKCK);
				detailList.add(entity);

			}
			invMaterialVoucherCustomService.createVoucher(invMaterialVoucher,detailList);
		}

		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	public int unAudit(List<String> ids) throws Exception {
		List<InvStockTake> invStockTakes = listByIds(ids);
		for (InvStockTake invStockTake : invStockTakes) {
			String voucherIdBySourceDocId = invMaterialVoucherCustomService.getVoucherIdBySourceDocId(invStockTake.getId());
			if (StringUtils.isEmpty(voucherIdBySourceDocId)) {
				continue;
			}
			invMaterialVoucherCustomService.reversalVoucher(voucherIdBySourceDocId);
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

		List<InvStockTake> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (InvStockTake record : records) {
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
