package org.jeecg.modules.inv.invdisassembly.service.impl;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyBomDetail;
import org.jeecg.modules.inv.invdisassembly.mapper.InvDisassemblyBomDetailMapper;
import org.jeecg.modules.inv.invdisassembly.service.IInvDisassemblyBomDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 拆卸单_材料清单
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
@Service
public class InvDisassemblyBomDetailServiceImpl extends ServiceImpl<InvDisassemblyBomDetailMapper, InvDisassemblyBomDetail> implements IInvDisassemblyBomDetailService {
	
	@Autowired
	private InvDisassemblyBomDetailMapper invDisassemblyBomDetailMapper;
	
	@Override
	public List<InvDisassemblyBomDetail> selectByMainId(String mainId) {
		return invDisassemblyBomDetailMapper.selectByMainId(mainId);
	}
}
