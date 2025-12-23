package org.jeecg.modules.prd.prdmps.mapper;

import java.util.List;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产计划_材料清单
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
public interface PrdMpsBomDetailMapper extends BaseMapper<PrdMpsBomDetail> {

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
   * @return List<PrdMpsBomDetail>
   */
	public List<PrdMpsBomDetail> selectByMainId(@Param("mainId") String mainId);
}
