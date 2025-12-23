package org.jeecg.modules.inv.invdisassembly.service;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassembly;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 拆卸单
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface IInvDisassemblyService extends IService<InvDisassembly> {

	/**
	 * 添加一对多
	 *
	 * @param invDisassembly
	 * @param invDisassemblyDetailList
	 */
	public void saveMain(InvDisassembly invDisassembly,List<InvDisassemblyDetail> invDisassemblyDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invDisassembly
   * @param invDisassemblyDetailList
	 */
	public void updateMain(InvDisassembly invDisassembly,List<InvDisassemblyDetail> invDisassemblyDetailList);
	
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
