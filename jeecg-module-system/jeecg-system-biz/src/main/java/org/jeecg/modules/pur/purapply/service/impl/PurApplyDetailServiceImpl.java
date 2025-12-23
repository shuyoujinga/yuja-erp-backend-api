package org.jeecg.modules.pur.purapply.service.impl;

import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import org.jeecg.modules.pur.purapply.mapper.PurApplyDetailMapper;
import org.jeecg.modules.pur.purapply.service.IPurApplyDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购申请明细
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
@Service
public class PurApplyDetailServiceImpl extends ServiceImpl<PurApplyDetailMapper, PurApplyDetail> implements IPurApplyDetailService {
	
	@Autowired
	private PurApplyDetailMapper purApplyDetailMapper;
	
	@Override
	public List<PurApplyDetail> selectByMainId(String mainId) {
		return purApplyDetailMapper.selectByMainId(mainId);
	}
}
