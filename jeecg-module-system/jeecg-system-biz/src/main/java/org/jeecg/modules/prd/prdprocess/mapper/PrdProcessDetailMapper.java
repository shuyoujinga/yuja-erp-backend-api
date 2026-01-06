package org.jeecg.modules.prd.prdprocess.mapper;

import java.util.List;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产工序_明细
 * @Author: 舒有敬
 * @Date:   2026-01-06
 * @Version: V1.0
 */
public interface PrdProcessDetailMapper extends BaseMapper<PrdProcessDetail> {

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
   * @return List<PrdProcessDetail>
   */
	public List<PrdProcessDetail> selectByMainId(@Param("mainId") String mainId);
}
