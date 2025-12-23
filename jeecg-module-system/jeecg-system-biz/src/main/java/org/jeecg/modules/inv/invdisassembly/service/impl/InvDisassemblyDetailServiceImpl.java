package org.jeecg.modules.inv.invdisassembly.service.impl;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyDetailMapper;
import org.jeecg.modules.inv.invdisassembly.service.IInvDisassemblyDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 拆卸单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Service
public class InvDisassemblyDetailServiceImpl extends ServiceImpl<InvDisassemblyDetailMapper, InvDisassemblyDetail> implements IInvDisassemblyDetailService {
	
	@Autowired
	private InvDisassemblyDetailMapper invDisassemblyDetailMapper;
	
	@Override
	public List<InvDisassemblyDetail> selectByMainId(String mainId) {
		return invDisassemblyDetailMapper.selectByMainId(mainId);
	}
}
