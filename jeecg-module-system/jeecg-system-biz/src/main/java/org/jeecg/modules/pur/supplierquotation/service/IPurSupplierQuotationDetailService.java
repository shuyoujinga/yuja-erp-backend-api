package org.jeecg.modules.pur.supplierquotation.service;

import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购报价_明细
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
public interface IPurSupplierQuotationDetailService extends IService<PurSupplierQuotationDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurSupplierQuotationDetail>
	 */
	public List<PurSupplierQuotationDetail> selectByMainId(String mainId);
}
