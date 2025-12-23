package org.jeecg.modules.inv.invstocktake.service.impl;

import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import org.jeecg.modules.inv.invstocktake.mapper.InvStockTakeDetailMapper;
import org.jeecg.modules.inv.invstocktake.service.IInvStockTakeDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 库存盘点_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvStockTakeDetailServiceImpl extends ServiceImpl<InvStockTakeDetailMapper, InvStockTakeDetail> implements IInvStockTakeDetailService {
	
	@Autowired
	private InvStockTakeDetailMapper invStockTakeDetailMapper;
	
	@Override
	public List<InvStockTakeDetail> selectByMainId(String mainId) {
		return invStockTakeDetailMapper.selectByMainId(mainId);
	}
}
