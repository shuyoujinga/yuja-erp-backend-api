package org.jeecg.modules.pur.purorder.service;

import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import org.jeecg.modules.pur.purorder.entity.PurOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购订单
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
public interface IPurOrderService extends IService<PurOrder> {

	/**
	 * 添加一对多
	 *
	 * @param purOrder
	 * @param purOrderDetailList
	 */
	public void saveMain(PurOrder purOrder,List<PurOrderDetail> purOrderDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purOrder
   * @param purOrderDetailList
	 */
	public void updateMain(PurOrder purOrder,List<PurOrderDetail> purOrderDetailList);
	
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

	int audit(List<String> ids);

	int unAudit(List<String> ids);
}
