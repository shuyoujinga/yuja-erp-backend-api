package org.jeecg.modules.inv.invtransfer.service;

import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import org.jeecg.modules.inv.invtransfer.entity.InvTransfer;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物料调拨
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface IInvTransferService extends IService<InvTransfer> {

	/**
	 * 添加一对多
	 *
	 * @param invTransfer
	 * @param invTransferDetailList
	 */
	public void saveMain(InvTransfer invTransfer,List<InvTransferDetail> invTransferDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invTransfer
   * @param invTransferDetailList
	 */
	public void updateMain(InvTransfer invTransfer,List<InvTransferDetail> invTransferDetailList);
	
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

	int audit(List<String> ids) throws Exception;

	int unAudit(List<String> ids) throws Exception;
}
