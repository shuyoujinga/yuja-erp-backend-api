package org.jeecg.modules.inv.invmiscin.service;

import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscIn;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 其他入库
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface IInvMiscInService extends IService<InvMiscIn> {

	/**
	 * 添加一对多
	 *
	 * @param invMiscIn
	 * @param invMiscInDetailList
	 */
	public void saveMain(InvMiscIn invMiscIn,List<InvMiscInDetail> invMiscInDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invMiscIn
   * @param invMiscInDetailList
	 */
	public void updateMain(InvMiscIn invMiscIn,List<InvMiscInDetail> invMiscInDetailList);
	
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
