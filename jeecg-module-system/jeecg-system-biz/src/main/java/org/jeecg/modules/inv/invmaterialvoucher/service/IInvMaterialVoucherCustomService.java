package org.jeecg.modules.inv.invmaterialvoucher.service;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface IInvMaterialVoucherCustomService {

    // 创建物料凭证
    Result<T> createVoucher(InvMaterialVoucher voucher, List<InvMaterialVoucherDetail> details) throws Exception;

    // 冲销物料凭证
    Result<T> reversalVoucher(String voucherId) throws Exception;
}
