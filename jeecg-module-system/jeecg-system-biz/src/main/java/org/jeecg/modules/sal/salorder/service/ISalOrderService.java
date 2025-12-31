package org.jeecg.modules.sal.salorder.service;

import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import org.jeecg.modules.sal.salorder.entity.SalOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售订单
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface ISalOrderService extends IService<SalOrder> {

	/**
	 * 添加一对多
	 *
	 * @param salOrder
	 * @param salOrderDetailList
	 */
	public void saveMain(SalOrder salOrder,List<SalOrderDetail> salOrderDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salOrder
   * @param salOrderDetailList
	 */
	public void updateMain(SalOrder salOrder,List<SalOrderDetail> salOrderDetailList);
	
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
