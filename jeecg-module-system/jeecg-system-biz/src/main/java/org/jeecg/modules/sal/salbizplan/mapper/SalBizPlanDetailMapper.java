package org.jeecg.modules.sal.salbizplan.mapper;

import java.util.List;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 业务计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface SalBizPlanDetailMapper extends BaseMapper<SalBizPlanDetail> {

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
   * @return List<SalBizPlanDetail>
   */
	public List<SalBizPlanDetail> selectByMainId(@Param("mainId") String mainId);
}
