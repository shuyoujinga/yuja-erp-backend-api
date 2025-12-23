package org.jeecg.modules.inv.invmaterialvoucher.service;

import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物料凭证
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
public interface IInvMaterialVoucherService extends IService<InvMaterialVoucher> {

	/**
	 * 添加一对多
	 *
	 * @param invMaterialVoucher
	 * @param invMaterialVoucherDetailList
	 */
	public void saveMain(InvMaterialVoucher invMaterialVoucher,List<InvMaterialVoucherDetail> invMaterialVoucherDetailList)  throws Exception ;
	
	/**
	 * 修改一对多
	 *
   * @param invMaterialVoucher
   * @param invMaterialVoucherDetailList
	 */
	public void updateMain(InvMaterialVoucher invMaterialVoucher,List<InvMaterialVoucherDetail> invMaterialVoucherDetailList) throws Exception ;
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
