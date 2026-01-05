package org.jeecg.modules.inv.invdisassembly.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassembly;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyBomDetail;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyDetailMapper;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyBomDetailMapper;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyMapper;
import org.jeecg.modules.inv.invdisassembly.service.IInvDisassemblyService;
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
import java.util.stream.Collectors;

/**
 * @Description: 拆卸单
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvDisassemblyServiceImpl extends ServiceImpl<InvDisassemblyMapper, InvDisassembly> implements IInvDisassemblyService {

	@Resource
	private InvDisassemblyMapper invDisassemblyMapper;
	@Resource
	private InvDisassemblyDetailMapper invDisassemblyDetailMapper;
	@Resource
	private InvDisassemblyBomDetailMapper invDisassemblyBomDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IInvMaterialVoucherCustomService 	iInvMaterialVoucherCustomService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvDisassembly invDisassembly, List<InvDisassemblyDetail> invDisassemblyDetailList,List<InvDisassemblyBomDetail> invDisassemblyBomDetailList) {
		invDisassembly.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CXD));
		invDisassemblyMapper.insert(invDisassembly);
		List<String> baseList = invDisassemblyDetailList.stream().map(InvDisassemblyDetail::getBomCode).collect(Collectors.toList());
		List<InvDisassemblyBomDetail> detailList =new ArrayList<>();
		for (InvDisassemblyBomDetail disassemblyDetail : invDisassemblyBomDetailList) {
			if (baseList.contains(disassemblyDetail.getBomCode())) {
				detailList.add(disassemblyDetail);
			}
		}
		if(invDisassemblyDetailList!=null && invDisassemblyDetailList.size()>0) {
			for(InvDisassemblyDetail entity:invDisassemblyDetailList) {
				//外键设置
				entity.setPid(invDisassembly.getId());
				invDisassemblyDetailMapper.insert(entity);
			}
		}
		invDisassemblyBomDetailList=new ArrayList<>(detailList) ;
		if(invDisassemblyBomDetailList!=null && invDisassemblyBomDetailList.size()>0) {
			for(InvDisassemblyBomDetail entity:invDisassemblyBomDetailList) {
				//外键设置
				entity.setPid(invDisassembly.getId());
				invDisassemblyBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvDisassembly invDisassembly,List<InvDisassemblyDetail> invDisassemblyDetailList,List<InvDisassemblyBomDetail> invDisassemblyBomDetailList) {
		invDisassemblyMapper.updateById(invDisassembly);
		List<String> baseList = invDisassemblyDetailList.stream().map(InvDisassemblyDetail::getBomCode).collect(Collectors.toList());
		List<InvDisassemblyBomDetail> detailList =new ArrayList<>();
		for (InvDisassemblyBomDetail disassemblyDetail : invDisassemblyBomDetailList) {
			if (baseList.contains(disassemblyDetail.getBomCode())) {
				detailList.add(disassemblyDetail);
			}
		}

		//1.先删除子表数据
		invDisassemblyDetailMapper.deleteByMainId(invDisassembly.getId());
		invDisassemblyBomDetailMapper.deleteByMainId(invDisassembly.getId());
		
		//2.子表数据重新插入
		if(invDisassemblyDetailList!=null && invDisassemblyDetailList.size()>0) {
			for(InvDisassemblyDetail entity:invDisassemblyDetailList) {
				//外键设置
				entity.setPid(invDisassembly.getId());
				invDisassemblyDetailMapper.insert(entity);
			}
		}
		invDisassemblyBomDetailList=new ArrayList<>(detailList) ;
		if(invDisassemblyBomDetailList!=null && invDisassemblyBomDetailList.size()>0) {
			for(InvDisassemblyBomDetail entity:invDisassemblyBomDetailList) {
				//外键设置
				entity.setPid(invDisassembly.getId());
				invDisassemblyBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invDisassemblyDetailMapper.deleteByMainId(id);
		invDisassemblyBomDetailMapper.deleteByMainId(id);
		invDisassemblyMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invDisassemblyDetailMapper.deleteByMainId(id.toString());
			invDisassemblyBomDetailMapper.deleteByMainId(id.toString());
			invDisassemblyMapper.deleteById(id);
		}
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) throws Exception {
		// 生成物料凭证
		Assert.isTrue(CollectionUtil.isEmpty(ids),"拆卸单不存在,不可审核!");
		List<InvDisassembly> invDisassemblyList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(invDisassemblyList),"拆卸单不存在,不可审核!");

		for (InvDisassembly invDisassembly : invDisassemblyList) {
			if (Constants.DICT_AUDIT_STATUS.YES.equals(invDisassembly.getAudit())) {
				continue;
			}

			List<InvDisassemblyDetail> invDisassemblyDetailList = invDisassemblyDetailMapper.selectByMainId(invDisassembly.getId());
			List<InvDisassemblyBomDetail> invDisassemblyBomDetailList = invDisassemblyBomDetailMapper.selectByMainId(invDisassembly.getId());
			Assert.isTrue(CollectionUtil.isEmpty(invDisassemblyDetailList)||CollectionUtil.isEmpty(invDisassemblyBomDetailList),"拆卸单不存在明细,不可审核!");
			//=========== 入库凭证 =================
			InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(Constants.DICT_MOVE_TYPE.CXRK, invDisassembly.getDocCode(), invDisassembly.getId(), invDisassembly.getRemark());
			List<InvMaterialVoucherDetail> inDetailList=new ArrayList<>();
			for (InvDisassemblyBomDetail inEntity : invDisassemblyBomDetailList) {
				InvMaterialVoucherDetail inEntityVoucher = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(inEntity, inEntityVoucher);
				inEntityVoucher.setId(null);
				inEntityVoucher.setSourceDocDetailId(inEntity.getId());
				inEntityVoucher.setPrice(inEntity.getUnitPrice());
				inEntityVoucher.setWarehouseCode(invDisassembly.getInWarehouseCode());
				inEntityVoucher.setMoveType(invMaterialVoucher.getMoveType());
				inDetailList.add(inEntityVoucher);
			}
			//=========== 出库凭证 =================
			InvMaterialVoucher outMaterialVoucher = new InvMaterialVoucher(Constants.DICT_MOVE_TYPE.CXCK, invDisassembly.getDocCode(), invDisassembly.getId(), invDisassembly.getRemark());
			List<InvMaterialVoucherDetail> outDetailList=new ArrayList<>();
			for (InvDisassemblyDetail outEntity : invDisassemblyDetailList) {
				InvMaterialVoucherDetail outEntityVoucher = new InvMaterialVoucherDetail();
				BeanUtils.copyProperties(outEntity, outEntityVoucher);
				outEntityVoucher.setId(null);
				outEntityVoucher.setSourceDocDetailId(outEntity.getId());
				outEntityVoucher.setPrice(outEntity.getUnitPrice());
				outEntityVoucher.setWarehouseCode(invDisassembly.getOutWarehouseCode());
				outEntityVoucher.setMoveType(outMaterialVoucher.getMoveType());
				outDetailList.add(outEntityVoucher);
			}
			iInvMaterialVoucherCustomService.createVoucher(invMaterialVoucher,inDetailList);
			iInvMaterialVoucherCustomService.createVoucher(outMaterialVoucher,outDetailList);
			invDisassembly.setInVoucherId(invMaterialVoucher.getId());
			invDisassembly.setOutVoucherId(outMaterialVoucher.getId());
			updateById(invDisassembly);

		}

		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) throws Exception {
		// 生成物料凭证
		Assert.isTrue(CollectionUtil.isEmpty(ids),"拆卸单不存在,不可审核!");
		List<InvDisassembly> invDisassemblyList = listByIds(ids);
		Assert.isTrue(CollectionUtil.isEmpty(invDisassemblyList),"拆卸单不存在,不可审核!");

		for (InvDisassembly invDisassembly : invDisassemblyList) {
			if (Constants.DICT_AUDIT_STATUS.NO.equals(invDisassembly.getAudit())) {
				continue;
			}
			iInvMaterialVoucherCustomService.reversalVoucher(invDisassembly.getInVoucherId());
			iInvMaterialVoucherCustomService.reversalVoucher(invDisassembly.getOutVoucherId());


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

		List<InvDisassembly> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}
		int count = 0;
		for (InvDisassembly record : records) {
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
