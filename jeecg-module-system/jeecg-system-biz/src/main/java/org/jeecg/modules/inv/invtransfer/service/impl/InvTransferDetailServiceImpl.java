package org.jeecg.modules.inv.invtransfer.service.impl;

import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import org.jeecg.modules.inv.invtransfer.mapper.InvTransferDetailMapper;
import org.jeecg.modules.inv.invtransfer.service.IInvTransferDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 物料调拨_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
@Service
public class InvTransferDetailServiceImpl extends ServiceImpl<InvTransferDetailMapper, InvTransferDetail> implements IInvTransferDetailService {
	
	@Autowired
	private InvTransferDetailMapper invTransferDetailMapper;
	
	@Override
	public List<InvTransferDetail> selectByMainId(String mainId) {
		return invTransferDetailMapper.selectByMainId(mainId);
	}
}
