package org.jeecg.modules.sal.salsettle.mapper;

import java.util.List;
import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface SalSettleDetailMapper extends BaseMapper<SalSettleDetail> {

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
   * @return List<SalSettleDetail>
   */
	public List<SalSettleDetail> selectByMainId(@Param("mainId") String mainId);
}
