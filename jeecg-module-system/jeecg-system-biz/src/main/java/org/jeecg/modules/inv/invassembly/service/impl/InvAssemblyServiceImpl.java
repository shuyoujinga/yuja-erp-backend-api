package org.jeecg.modules.inv.invassembly.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invassembly.entity.InvAssembly;
import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import org.jeecg.modules.inv.invassembly.entity.InvAssemblyBomDetail;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyDetailMapper;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyBomDetailMapper;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyMapper;
import org.jeecg.modules.inv.invassembly.service.IInvAssemblyService;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 组装单
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvAssemblyServiceImpl extends ServiceImpl<InvAssemblyMapper, InvAssembly> implements IInvAssemblyService {

	@Resource
	private InvAssemblyMapper invAssemblyMapper;
	@Resource
	private InvAssemblyDetailMapper invAssemblyDetailMapper;
	@Resource
	private InvAssemblyBomDetailMapper invAssemblyBomDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService iInvMaterialVoucherCustomService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvAssembly invAssembly, List<InvAssemblyDetail> invAssemblyDetailList,List<InvAssemblyBomDetail> invAssemblyBomDetailList) {
		invAssembly.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.ZZD));
		invAssemblyMapper.insert(invAssembly);
		if(invAssemblyDetailList!=null && invAssemblyDetailList.size()>0) {
			for(InvAssemblyDetail entity:invAssemblyDetailList) {
				//外键设置
				entity.setPid(invAssembly.getId());
				invAssemblyDetailMapper.insert(entity);
			}
		}
		if(invAssemblyBomDetailList!=null && invAssemblyBomDetailList.size()>0) {
			for(InvAssemblyBomDetail entity:invAssemblyBomDetailList) {
				//外键设置
				entity.setPid(invAssembly.getId());
				invAssemblyBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvAssembly invAssembly,List<InvAssemblyDetail> invAssemblyDetailList,List<InvAssemblyBomDetail> invAssemblyBomDetailList) {
		invAssemblyMapper.updateById(invAssembly);
		
		//1.先删除子表数据
		invAssemblyDetailMapper.deleteByMainId(invAssembly.getId());
		invAssemblyBomDetailMapper.deleteByMainId(invAssembly.getId());
		
		//2.子表数据重新插入
		if(invAssemblyDetailList!=null && invAssemblyDetailList.size()>0) {
			for(InvAssemblyDetail entity:invAssemblyDetailList) {
				//外键设置
				entity.setPid(invAssembly.getId());
				invAssemblyDetailMapper.insert(entity);
			}
		}
		if(invAssemblyBomDetailList!=null && invAssemblyBomDetailList.size()>0) {
			for(InvAssemblyBomDetail entity:invAssemblyBomDetailList) {
				//外键设置
				entity.setPid(invAssembly.getId());
				invAssemblyBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invAssemblyDetailMapper.deleteByMainId(id);
		invAssemblyBomDetailMapper.deleteByMainId(id);
		invAssemblyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invAssemblyDetailMapper.deleteByMainId(id.toString());
			invAssemblyBomDetailMapper.deleteByMainId(id.toString());
			invAssemblyMapper.deleteById(id);
		}
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		// 生成物料凭证
		Assert.isTrue(CollectionUtil.isEmpty(ids),"拆卸单不存在,不可审核!");
		List<InvAssembly> InvAssemblyList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(InvAssemblyList),"拆卸单不存在,不可审核!");

		for (InvAssembly InvAssembly : InvAssemblyList) {
			if (Constants.DICT_AUDIT_STATUS.YES.equals(InvAssembly.getAudit())) {
				continue;
			}

			List<InvAssemblyDetail> InvAssemblyDetailList = invAssemblyDetailMapper.selectByMainId(InvAssembly.getId());
			List<InvAssemblyBomDetail> InvAssemblyBomDetailList = invAssemblyBomDetailMapper.selectByMainId(InvAssembly.getId());
			Assert.isTrue(CollectionUtil.isEmpty(InvAssemblyDetailList)||CollectionUtil.isEmpty(InvAssemblyBomDetailList),"拆卸单不存在明细,不可审核!");
			//=========== 入库凭证 =================
			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(Constants.DICT_MOVE_TYPE.ZZRK, InvAssembly.getDocCode(), InvAssembly.getId(), InvAssembly.getRemark());
			List<InvMaterialVoucherDetail> inDetailList=new ArrayList<>();
			for (InvAssemblyDetail inEntity : InvAssemblyDetailList) {
				InvMaterialVoucherDetail inEntityVoucher = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(inEntity, inEntityVoucher);
				inEntityVoucher.setId(null);
				inEntityVoucher.setSourceDocDetailId(inEntity.getId());
				inEntityVoucher.setPrice(inEntity.getUnitPrice());
				inEntityVoucher.setWarehouseCode(InvAssembly.getInWarehouseCode());
				inEntityVoucher.setMoveType(invMaterialVoucher.getMoveType());
				inDetailList.add(inEntityVoucher);
			}
			//=========== 出库凭证 =================
			InvMaterialVoucher outMaterialVoucher = new InvMaterialVoucher(Constants.DICT_MOVE_TYPE.ZZCK, InvAssembly.getDocCode(), InvAssembly.getId(), InvAssembly.getRemark());
			List<InvMaterialVoucherDetail> outDetailList=new ArrayList<>();
			for (InvAssemblyBomDetail outEntity : InvAssemblyBomDetailList) {
				InvMaterialVoucherDetail outEntityVoucher = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(outEntity, outEntityVoucher);
				outEntityVoucher.setId(null);
				outEntityVoucher.setSourceDocDetailId(outEntity.getId());
				outEntityVoucher.setPrice(outEntity.getUnitPrice());
				outEntityVoucher.setWarehouseCode(InvAssembly.getOutWarehouseCode());
				outEntityVoucher.setMoveType(outMaterialVoucher.getMoveType());
				outDetailList.add(outEntityVoucher);
			}
			iInvMaterialVoucherCustomService.createVoucher(invMaterialVoucher,inDetailList);
			iInvMaterialVoucherCustomService.createVoucher(outMaterialVoucher,outDetailList);
			InvAssembly.setInVoucherId(invMaterialVoucher.getId());
			InvAssembly.setOutVoucherId(outMaterialVoucher.getId());
			updateById(InvAssembly);

		}

		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		// 生成物料凭证
		Assert.isTrue(CollectionUtil.isEmpty(ids),"拆卸单不存在,不可审核!");
		List<InvAssembly> InvAssemblyList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(InvAssemblyList),"拆卸单不存在,不可审核!");

		for (InvAssembly InvAssembly : InvAssemblyList) {
			if (Constants.DICT_AUDIT_STATUS.NO.equals(InvAssembly.getAudit())) {
				continue;
			}
			iInvMaterialVoucherCustomService.reversalVoucher(InvAssembly.getInVoucherId());
			iInvMaterialVoucherCustomService.reversalVoucher(InvAssembly.getOutVoucherId());


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

		List<InvAssembly> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (InvAssembly record : records) {
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
