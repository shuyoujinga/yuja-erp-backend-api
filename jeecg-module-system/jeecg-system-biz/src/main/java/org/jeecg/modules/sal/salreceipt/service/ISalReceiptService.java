package org.jeecg.modules.sal.salreceipt.service;

import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import org.jeecg.modules.sal.salreceipt.entity.SalReceipt;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售收款
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalReceiptService extends IService<SalReceipt> {

	/**
	 * 添加一对多
	 *
	 * @param salReceipt
	 * @param salReceiptDetailList
	 */
	public void saveMain(SalReceipt salReceipt,List<SalReceiptDetail> salReceiptDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salReceipt
   * @param salReceiptDetailList
	 */
	public void updateMain(SalReceipt salReceipt,List<SalReceiptDetail> salReceiptDetailList);
	
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
