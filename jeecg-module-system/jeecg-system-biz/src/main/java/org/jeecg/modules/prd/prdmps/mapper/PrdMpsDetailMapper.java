package org.jeecg.modules.prd.prdmps.mapper;

import java.util.List;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
public interface PrdMpsDetailMapper extends BaseMapper<PrdMpsDetail> {

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
   * @return List<PrdMpsDetail>
   */
	public List<PrdMpsDetail> selectByMainId(@Param("mainId") String mainId);
}
