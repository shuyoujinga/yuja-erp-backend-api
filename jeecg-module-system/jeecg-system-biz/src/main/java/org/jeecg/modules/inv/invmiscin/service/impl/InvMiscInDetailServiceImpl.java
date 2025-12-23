package org.jeecg.modules.inv.invmiscin.service.impl;

import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import org.jeecg.modules.inv.invmiscin.mapper.InvMiscInDetailMapper;
import org.jeecg.modules.inv.invmiscin.service.IInvMiscInDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 其他入库_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
@Service
public class InvMiscInDetailServiceImpl extends ServiceImpl<InvMiscInDetailMapper, InvMiscInDetail> implements IInvMiscInDetailService {
	
	@Autowired
	private InvMiscInDetailMapper invMiscInDetailMapper;
	
	@Override
	public List<InvMiscInDetail> selectByMainId(String mainId) {
		return invMiscInDetailMapper.selectByMainId(mainId);
	}
}
