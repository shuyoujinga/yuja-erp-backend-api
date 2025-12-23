package org.jeecg.modules.pur.purreturn.service.impl;

import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.mapper.PurReturnDetailMapper;
import org.jeecg.modules.pur.purreturn.service.IPurReturnDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class PurReturnDetailServiceImpl extends ServiceImpl<PurReturnDetailMapper, PurReturnDetail> implements IPurReturnDetailService {
	
	@Autowired
	private PurReturnDetailMapper purReturnDetailMapper;
	
	@Override
	public List<PurReturnDetail> selectByMainId(String mainId) {
		return purReturnDetailMapper.selectByMainId(mainId);
	}
}
