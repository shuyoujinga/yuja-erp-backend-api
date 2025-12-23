package org.jeecg.modules.pur.purreturn.service;

import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import org.jeecg.modules.pur.purreturn.entity.PurReturn;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购退货
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface IPurReturnService extends IService<PurReturn> {

	/**
	 * 添加一对多
	 *
	 * @param purReturn
	 * @param purReturnDetailList
	 */
	public void saveMain(PurReturn purReturn,List<PurReturnDetail> purReturnDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purReturn
   * @param purReturnDetailList
	 */
	public void updateMain(PurReturn purReturn,List<PurReturnDetail> purReturnDetailList);
	
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
