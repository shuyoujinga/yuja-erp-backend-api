package org.jeecg.modules.pur.purpayment.mapper;

import java.util.List;
import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购付款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface PurPaymentDetailMapper extends BaseMapper<PurPaymentDetail> {

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
   * @return List<PurPaymentDetail>
   */
	public List<PurPaymentDetail> selectByMainId(@Param("mainId") String mainId);
}
