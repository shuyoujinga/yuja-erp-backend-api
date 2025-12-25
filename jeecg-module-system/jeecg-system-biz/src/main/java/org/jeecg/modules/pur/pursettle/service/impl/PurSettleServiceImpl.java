package org.jeecg.modules.pur.pursettle.service.impl;

import org.constant.Constants;
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
import org.utils.AmountUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

/**
 * @Description: 采购结算
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PurSettleServiceImpl extends ServiceImpl<PurSettleMapper, PurSettle> implements IPurSettleService {

	@Resource
	private PurSettleMapper purSettleMapper;
	@Resource
	private PurSettleDetailMapper purSettleDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;
	
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
	
}
