package org.jeecg.modules.pur.supplierquotation.service;

import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotation;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购报价
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
public interface IPurSupplierQuotationService extends IService<PurSupplierQuotation> {

	/**
	 * 添加一对多
	 *
	 * @param purSupplierQuotation
	 * @param purSupplierQuotationDetailList
	 */
	public void saveMain(PurSupplierQuotation purSupplierQuotation,List<PurSupplierQuotationDetail> purSupplierQuotationDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purSupplierQuotation
   * @param purSupplierQuotationDetailList
	 */
	public void updateMain(PurSupplierQuotation purSupplierQuotation,List<PurSupplierQuotationDetail> purSupplierQuotationDetailList);
	
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
