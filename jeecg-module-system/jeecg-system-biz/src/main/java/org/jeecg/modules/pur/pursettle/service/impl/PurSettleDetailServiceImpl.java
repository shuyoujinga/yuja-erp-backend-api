package org.jeecg.modules.pur.pursettle.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.util.StringUtils;
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveDetailService;
import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import org.jeecg.modules.pur.pursettle.mapper.PurSettleDetailMapper;
import org.jeecg.modules.pur.pursettle.service.IPurSettleDetailService;
import org.jeecg.modules.pur.pursettle.service.IPurSettleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Description: 采购结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Service
public class PurSettleDetailServiceImpl extends ServiceImpl<PurSettleDetailMapper, PurSettleDetail> implements IPurSettleDetailService {
	
	@Resource
	private PurSettleDetailMapper purSettleDetailMapper;

	@Autowired
	private IPurReceiveDetailService purReceiveDetailService;
	
	@Override
	public List<PurSettleDetail> selectByMainId(String mainId) {
		return purSettleDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<PurSettleDetail> selectByTargetId(String id) {
		List<PurSettleDetail> result= new ArrayList<PurSettleDetail>();
		if (StringUtils.isEmpty(id)) {
			return result;
		}
		List<String> list = Arrays.asList(id.split(","));
		List<PurReceiveDetail> purReceiveDetails = purReceiveDetailService.listByIds(list);
		if (!CollectionUtil.isEmpty(purReceiveDetails)) {
			for (PurReceiveDetail purReceiveDetail : purReceiveDetails) {
				PurSettleDetail entity = new PurSettleDetail();
				entity.setReceiveDetailId(purReceiveDetail.getId());
				entity.setMaterialCode(purReceiveDetail.getMaterialCode());
				entity.setReceiveUnitPrice(purReceiveDetail.getUnitPrice());
				entity.setReceiveNum(purReceiveDetail.getReceiveQty());
				entity.setReceiveAmount(purReceiveDetail.getAmount());
				entity.setSettleNum(purReceiveDetail.getReceiveQty());
				entity.setSettleUnitPrice(purReceiveDetail.getUnitPrice());
				entity.setSettleAmount(purReceiveDetail.getAmount());
				entity.setSettleDifferAmount(0d);
				result.add(entity);
			}
		}

		return result;
    }
}
