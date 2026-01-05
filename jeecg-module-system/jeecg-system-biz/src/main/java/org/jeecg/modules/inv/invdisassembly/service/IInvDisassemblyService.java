package org.jeecg.modules.inv.invdisassembly.service;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyBomDetail;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassembly;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 拆卸单
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
public interface IInvDisassemblyService extends IService<InvDisassembly> {

	/**
	 * 添加一对多
	 *
	 * @param invDisassembly
	 * @param invDisassemblyDetailList
	 * @param invDisassemblyBomDetailList
	 */
	public void saveMain(InvDisassembly invDisassembly,List<InvDisassemblyDetail> invDisassemblyDetailList,List<InvDisassemblyBomDetail> invDisassemblyBomDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invDisassembly
   * @param invDisassemblyDetailList
   * @param invDisassemblyBomDetailList
	 */
	public void updateMain(InvDisassembly invDisassembly,List<InvDisassemblyDetail> invDisassemblyDetailList,List<InvDisassemblyBomDetail> invDisassemblyBomDetailList);
	
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
