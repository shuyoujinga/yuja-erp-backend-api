package org.jeecg.modules.prd.prdworkorder.service;

import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产工单
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdWorkOrderService extends IService<PrdWorkOrder> {

	/**
	 * 添加一对多
	 *
	 * @param prdWorkOrder
	 * @param prdWorkOrderDetailList
	 */
	public void saveMain(PrdWorkOrder prdWorkOrder,List<PrdWorkOrderDetail> prdWorkOrderDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param prdWorkOrder
   * @param prdWorkOrderDetailList
	 */
	public void updateMain(PrdWorkOrder prdWorkOrder,List<PrdWorkOrderDetail> prdWorkOrderDetailList);
	
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
