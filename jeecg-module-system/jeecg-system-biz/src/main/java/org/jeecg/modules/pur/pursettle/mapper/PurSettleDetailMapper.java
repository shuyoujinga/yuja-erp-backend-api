package org.jeecg.modules.pur.pursettle.mapper;

import java.util.List;
import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
public interface PurSettleDetailMapper extends BaseMapper<PurSettleDetail> {

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
   * @return List<PurSettleDetail>
   */
	public List<PurSettleDetail> selectByMainId(@Param("mainId") String mainId);
}
