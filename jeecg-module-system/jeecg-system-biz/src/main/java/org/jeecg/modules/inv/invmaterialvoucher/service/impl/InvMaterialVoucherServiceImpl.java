package org.jeecg.modules.inv.invmaterialvoucher.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.constant.Constants;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.mapper.InvMaterialVoucherDetailMapper;
import org.jeecg.modules.inv.invmaterialvoucher.mapper.InvMaterialVoucherMapper;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherService;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 物料凭证
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Service
public class InvMaterialVoucherServiceImpl extends ServiceImpl<InvMaterialVoucherMapper, InvMaterialVoucher> implements IInvMaterialVoucherService {

	@Resource
	private InvMaterialVoucherMapper invMaterialVoucherMapper;
	@Resource
	private InvMaterialVoucherDetailMapper invMaterialVoucherDetailMapper;
	@Resource
	private SerialNumberService serialNumberService;
	@Autowired
	private IYujiakejiMaterialsService yujiakejiMaterialsService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(InvMaterialVoucher invMaterialVoucher, List<InvMaterialVoucherDetail> invMaterialVoucherDetailList) {

		invMaterialVoucher.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.WLPZ));
		if (ObjectUtils.isEmpty(invMaterialVoucher.getDocTime())) {
			invMaterialVoucher.setDocTime(new Date());
		}

		List<String> materialCodes = invMaterialVoucherDetailList.stream().map(InvMaterialVoucherDetail::getMaterialCode).collect(Collectors.toList());

		List<YujiakejiMaterials> list = yujiakejiMaterialsService.list(new LambdaQueryWrapper<YujiakejiMaterials>().in(YujiakejiMaterials::getMaterialCode, materialCodes));

		Map<String, String> materialMap = list.stream().collect(Collectors.toMap(YujiakejiMaterials::getMaterialCode, YujiakejiMaterials::getMaterialName));


		invMaterialVoucherMapper.insert(invMaterialVoucher);
		if(invMaterialVoucherDetailList!=null && invMaterialVoucherDetailList.size()>0) {
			for(InvMaterialVoucherDetail entity:invMaterialVoucherDetailList) {
				//外键设置
				entity.setPid(invMaterialVoucher.getId());
				entity.setMaterialName(materialMap.get(entity.getMaterialCode()));
				invMaterialVoucherDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(InvMaterialVoucher invMaterialVoucher,List<InvMaterialVoucherDetail> invMaterialVoucherDetailList) {
		invMaterialVoucherMapper.updateById(invMaterialVoucher);
		
		//1.先删除子表数据
		invMaterialVoucherDetailMapper.deleteByMainId(invMaterialVoucher.getId());
		
		//2.子表数据重新插入
		if(invMaterialVoucherDetailList!=null && invMaterialVoucherDetailList.size()>0) {
			for(InvMaterialVoucherDetail entity:invMaterialVoucherDetailList) {
				//外键设置
				entity.setPid(invMaterialVoucher.getId());
				invMaterialVoucherDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		invMaterialVoucherDetailMapper.deleteByMainId(id);
		invMaterialVoucherMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			invMaterialVoucherDetailMapper.deleteByMainId(id.toString());
			invMaterialVoucherMapper.deleteById(id);
		}
	}
	
}
