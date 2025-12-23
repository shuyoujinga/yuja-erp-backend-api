package org.jeecg.modules.inv.invmaterialvoucher.service;

import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 物料凭证_明细
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
public interface IInvMaterialVoucherDetailService extends IService<InvMaterialVoucherDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvMaterialVoucherDetail>
	 */
	public List<InvMaterialVoucherDetail> selectByMainId(String mainId);
}
