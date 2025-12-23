package org.jeecg.modules.inv.invstocktake.service;

import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import org.jeecg.modules.inv.invstocktake.entity.InvStockTake;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物料盘点
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface IInvStockTakeService extends IService<InvStockTake> {

	/**
	 * 添加一对多
	 *
	 * @param invStockTake
	 * @param invStockTakeDetailList
	 */
	public void saveMain(InvStockTake invStockTake,List<InvStockTakeDetail> invStockTakeDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invStockTake
   * @param invStockTakeDetailList
	 */
	public void updateMain(InvStockTake invStockTake,List<InvStockTakeDetail> invStockTakeDetailList);
	
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
