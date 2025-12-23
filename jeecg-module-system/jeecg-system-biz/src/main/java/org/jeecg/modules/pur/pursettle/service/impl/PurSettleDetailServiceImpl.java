package org.jeecg.modules.pur.pursettle.service.impl;

import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import org.jeecg.modules.pur.pursettle.mapper.PurSettleDetailMapper;
import org.jeecg.modules.pur.pursettle.service.IPurSettleDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
@Service
public class PurSettleDetailServiceImpl extends ServiceImpl<PurSettleDetailMapper, PurSettleDetail> implements IPurSettleDetailService {
	
	@Autowired
	private PurSettleDetailMapper purSettleDetailMapper;
	
	@Override
	public List<PurSettleDetail> selectByMainId(String mainId) {
		return purSettleDetailMapper.selectByMainId(mainId);
	}
}
