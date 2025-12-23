package org.jeecg.modules.inv.invassembly.service;

import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import org.jeecg.modules.inv.invassembly.entity.InvAssembly;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 组装单
 * @Author: 舒有敬
 * @Date:   2025-12-16
 * @Version: V1.0
 */
public interface IInvAssemblyService extends IService<InvAssembly> {

	/**
	 * 添加一对多
	 *
	 * @param invAssembly
	 * @param invAssemblyDetailList
	 */
	public void saveMain(InvAssembly invAssembly,List<InvAssemblyDetail> invAssemblyDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invAssembly
   * @param invAssemblyDetailList
	 */
	public void updateMain(InvAssembly invAssembly,List<InvAssemblyDetail> invAssemblyDetailList);
	
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
