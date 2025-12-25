package org.jeecg.modules.pur.pursettle.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.pur.purpayment.entity.PurPayment;
import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentDetailService;
import org.jeecg.modules.pur.purpayment.service.IPurPaymentService;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
import org.jeecg.modules.pur.pursettle.entity.PurSettle;
import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import org.jeecg.modules.pur.pursettle.mapper.PurSettleDetailMapper;
import org.jeecg.modules.pur.pursettle.mapper.PurSettleMapper;
import org.jeecg.modules.pur.pursettle.service.IPurSettleService;
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
import java.util.*;

/**
 * @Description: 采购结算
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PurSettleServiceImpl extends ServiceImpl<PurSettleMapper, PurSettle> implements IPurSettleService {

	@Resource
	private PurSettleMapper purSettleMapper;
	@Resource
	private PurSettleDetailMapper purSettleDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private IPurPaymentService purPaymentService;
	@Autowired
	private IPurPaymentDetailService purPaymentDetailService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PurSettle purSettle, List<PurSettleDetail> purSettleDetailList) {

		purSettle.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGJS));
		double totalSettleAmount = AmountUtils.sumTotalAmount(purSettleDetailList,
				d -> Optional.ofNullable(d.getSettleAmount()).orElse(0d));
		purSettle.setAmount(totalSettleAmount);

		double totalSettleDiffAmount = AmountUtils.sumTotalAmount(purSettleDetailList,
				d -> Optional.ofNullable(d.getSettleDifferAmount()).orElse(0d));
		purSettle.setDifferAmount(totalSettleDiffAmount);

		purSettleMapper.insert(purSettle);
		if(purSettleDetailList!=null && purSettleDetailList.size()>0) {
			for(PurSettleDetail entity:purSettleDetailList) {
				//外键设置
				entity.setPid(purSettle.getId());
				entity.setId(null);
				purSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PurSettle purSettle,List<PurSettleDetail> purSettleDetailList) {
		purSettleMapper.updateById(purSettle);
		
		//1.先删除子表数据
		purSettleDetailMapper.deleteByMainId(purSettle.getId());
		
		//2.子表数据重新插入
		if(purSettleDetailList!=null && purSettleDetailList.size()>0) {
			for(PurSettleDetail entity:purSettleDetailList) {
				//外键设置
				entity.setPid(purSettle.getId());
				purSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		purSettleDetailMapper.deleteByMainId(id);
		purSettleMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			purSettleDetailMapper.deleteByMainId(id.toString());
			purSettleMapper.deleteById(id);
		}
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
		List<String> list = Arrays.asList(ids.toArray(new String[ids.size()]));

		List<PurSettle> purSettles = baseMapper.selectBatchIds(list);


		// 产生采购付款单
		for (PurSettle purSettle : purSettles) {

			if (Constants.DICT_AUDIT_STATUS.YES.equals(purSettle.getAudit())) {
				continue;
			}

			PurPayment purPayment = new PurPayment();

			purPayment.setSupplierCode(purSettle.getSupplierCode());
			purPayment.setDocTime(new Date());
			purPayment.setSettleIds(purSettle.getId());
			purPayment.setSettleCodes(purSettle.getDocCode());
			purPayment.setAmount(purSettle.getAmount());
			purPayment.setRemark(purSettle.getRemark());
			List<PurSettleDetail> settleList = purSettleDetailMapper.selectByMainId(purSettle.getId());

			List<PurPaymentDetail>  detailList=new ArrayList<>();
			for (PurSettleDetail purSettleDetail : settleList) {
				PurPaymentDetail purPaymentDetail = new PurPaymentDetail();
				purPaymentDetail.setMaterialCode(purSettleDetail.getMaterialCode());
				purPaymentDetail.setSettleNum(purSettleDetail.getSettleNum());
				purPaymentDetail.setSettleUnitPrice(purSettleDetail.getSettleUnitPrice());
				purPaymentDetail.setSettleAmount(purSettleDetail.getSettleAmount());
				purPaymentDetail.setAmount(purSettle.getAmount());
				purPaymentDetail.setRemark(purSettle.getRemark());
				detailList.add(purPaymentDetail);
			}

			purPaymentService.saveMain(purPayment,detailList);
		}
		log.info("采购付款-单据添加完毕！"+ids);

		// todo 采购结算-差异处理-做物料凭证

		// todo 采购结算-生成财务凭证

		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
	}



	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) {
		List<String> list = Arrays.asList(ids.toArray(new String[ids.size()]));

		List<PurSettle> purSettles = baseMapper.selectBatchIds(list);


		List<PurPayment> purPayments = new ArrayList<>();
		for (PurSettle purSettle : purSettles) {

			if (Constants.DICT_AUDIT_STATUS.NO.equals(purSettle.getAudit())) {
				continue;
			}
			PurPayment payment = purPaymentService.getOne(new LambdaQueryWrapper<PurPayment>().eq(PurPayment::getAudit, Constants.DICT_AUDIT_STATUS.NO).eq(PurPayment::getSettleIds, purSettle.getId()).eq(PurPayment::getDelFlag, Constants.DICT_YN.YES).last(Constants.CONST_SQL.LIMIT_ONE));

			Assert.isTrue(ObjectUtils.isEmpty(payment),"存在已经付款的付款单据，因此采购结算单不可反审核！请联系财务处理！");
			purPayments.add(payment);

		}
		for (PurPayment purPayment : purPayments) {
			purPaymentService.delMain(purPayment.getId());
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

		List<PurSettle> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PurSettle record : records) {

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
