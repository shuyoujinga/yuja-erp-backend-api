package org.jeecg.modules.inv.invmiscout.service;

import org.jeecg.modules.inv.invmiscout.entity.InvMiscOutDetail;
import org.jeecg.modules.inv.invmiscout.entity.InvMiscOut;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 其他出库
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface IInvMiscOutService extends IService<InvMiscOut> {

	/**
	 * 添加一对多
	 *
	 * @param invMiscOut
	 * @param invMiscOutDetailList
	 */
	public void saveMain(InvMiscOut invMiscOut,List<InvMiscOutDetail> invMiscOutDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invMiscOut
   * @param invMiscOutDetailList
	 */
	public void updateMain(InvMiscOut invMiscOut,List<InvMiscOutDetail> invMiscOutDetailList);
	
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
