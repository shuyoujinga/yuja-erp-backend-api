package org.jeecg.modules.pur.purpayment.service;

import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import org.jeecg.modules.pur.purpayment.entity.PurPayment;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购付款
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface IPurPaymentService extends IService<PurPayment> {

	/**
	 * 添加一对多
	 *
	 * @param purPayment
	 * @param purPaymentDetailList
	 */
	public void saveMain(PurPayment purPayment,List<PurPaymentDetail> purPaymentDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purPayment
   * @param purPaymentDetailList
	 */
	public void updateMain(PurPayment purPayment,List<PurPaymentDetail> purPaymentDetailList);
	
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
