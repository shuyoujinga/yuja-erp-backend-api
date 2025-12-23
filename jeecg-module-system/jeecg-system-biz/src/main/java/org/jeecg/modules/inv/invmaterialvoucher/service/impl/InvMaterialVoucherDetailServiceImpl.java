package org.jeecg.modules.inv.invmaterialvoucher.service.impl;

import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.mapper.InvMaterialVoucherDetailMapper;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 物料凭证_明细
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Service
public class InvMaterialVoucherDetailServiceImpl extends ServiceImpl<InvMaterialVoucherDetailMapper, InvMaterialVoucherDetail> implements IInvMaterialVoucherDetailService {
	
	@Autowired
	private InvMaterialVoucherDetailMapper invMaterialVoucherDetailMapper;
	
	@Override
	public List<InvMaterialVoucherDetail> selectByMainId(String mainId) {
		return invMaterialVoucherDetailMapper.selectByMainId(mainId);
	}
}
