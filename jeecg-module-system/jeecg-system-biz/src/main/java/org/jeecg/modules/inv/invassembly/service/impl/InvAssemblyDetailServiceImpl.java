package org.jeecg.modules.inv.invassembly.service.impl;

import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import org.jeecg.modules.inv.invassembly.mapper.InvAssemblyDetailMapper;
import org.jeecg.modules.inv.invassembly.service.IInvAssemblyDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 组装单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-16
 * @Version: V1.0
 */
@Service
public class InvAssemblyDetailServiceImpl extends ServiceImpl<InvAssemblyDetailMapper, InvAssemblyDetail> implements IInvAssemblyDetailService {
	
	@Autowired
	private InvAssemblyDetailMapper invAssemblyDetailMapper;
	
	@Override
	public List<InvAssemblyDetail> selectByMainId(String mainId) {
		return invAssemblyDetailMapper.selectByMainId(mainId);
	}
}
