package org.jeecg.modules.inv.invassembly.service.impl;

import org.jeecg.modules.inv.invassembly.entity.InvAssemblyBomDetail;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyBomDetailMapper;
import org.jeecg.modules.inv.invassembly.service.IInvAssemblyBomDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 组装单_材料明细
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
@Service
public class InvAssemblyBomDetailServiceImpl extends ServiceImpl<InvAssemblyBomDetailMapper, InvAssemblyBomDetail> implements IInvAssemblyBomDetailService {
	
	@Autowired
	private InvAssemblyBomDetailMapper invAssemblyBomDetailMapper;
	
	@Override
	public List<InvAssemblyBomDetail> selectByMainId(String mainId) {
		return invAssemblyBomDetailMapper.selectByMainId(mainId);
	}
}
