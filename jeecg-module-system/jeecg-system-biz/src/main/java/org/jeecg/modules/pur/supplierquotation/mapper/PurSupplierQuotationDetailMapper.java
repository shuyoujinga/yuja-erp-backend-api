package org.jeecg.modules.pur.supplierquotation.mapper;

import java.util.List;
import org.jeecg.modules.pur.supplierquotation.entity.PurSupplierQuotationDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购报价_明细
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
public interface PurSupplierQuotationDetailMapper extends BaseMapper<PurSupplierQuotationDetail> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<PurSupplierQuotationDetail>
   */
	public List<PurSupplierQuotationDetail> selectByMainId(@Param("mainId") String mainId);
}
