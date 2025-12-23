package org.jeecg.modules.inv.invmiscout.service.impl;

import org.jeecg.modules.inv.invmiscout.entity.InvMiscOutDetail;
import org.jeecg.modules.inv.invmiscout.mapper.InvMiscOutDetailMapper;
import org.jeecg.modules.inv.invmiscout.service.IInvMiscOutDetailService;
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
public class InvMiscOutDetailServiceImpl extends ServiceImpl<InvMiscOutDetailMapper, InvMiscOutDetail> implements IInvMiscOutDetailService {
	
	@Autowired
	private InvMiscOutDetailMapper invMiscOutDetailMapper;
	
	@Override
	public List<InvMiscOutDetail> selectByMainId(String mainId) {
		return invMiscOutDetailMapper.selectByMainId(mainId);
	}
}
