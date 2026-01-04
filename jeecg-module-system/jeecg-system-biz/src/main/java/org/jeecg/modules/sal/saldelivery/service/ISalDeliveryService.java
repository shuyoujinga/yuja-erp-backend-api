package org.jeecg.modules.sal.saldelivery.service;

import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import org.jeecg.modules.sal.saldelivery.entity.SalDelivery;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售出货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalDeliveryService extends IService<SalDelivery> {

	/**
	 * 添加一对多
	 *
	 * @param salDelivery
	 * @param salDeliveryDetailList
	 */
	public void saveMain(SalDelivery salDelivery,List<SalDeliveryDetail> salDeliveryDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salDelivery
   * @param salDeliveryDetailList
	 */
	public void updateMain(SalDelivery salDelivery,List<SalDeliveryDetail> salDeliveryDetailList);
	
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
