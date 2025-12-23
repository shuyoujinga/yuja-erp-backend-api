package org.jeecg.modules.pur.supplierquotation.service.impl;

import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
import org.jeecg.modules.pur.supplierquotation.mapper.PurSupplierQuotationDetailMapper;
import org.jeecg.modules.pur.supplierquotation.service.IPurSupplierQuotationDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购报价_明细
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
@Service
public class PurSupplierQuotationDetailServiceImpl extends ServiceImpl<PurSupplierQuotationDetailMapper, PurSupplierQuotationDetail> implements IPurSupplierQuotationDetailService {
	
	@Autowired
	private PurSupplierQuotationDetailMapper purSupplierQuotationDetailMapper;
	
	@Override
	public List<PurSupplierQuotationDetail> selectByMainId(String mainId) {
		return purSupplierQuotationDetailMapper.selectByMainId(mainId);
	}
}
