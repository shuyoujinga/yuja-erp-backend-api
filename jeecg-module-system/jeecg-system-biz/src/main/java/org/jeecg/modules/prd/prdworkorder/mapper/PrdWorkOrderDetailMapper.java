package org.jeecg.modules.prd.prdworkorder.mapper;

import java.util.List;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产工单_物料明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface PrdWorkOrderDetailMapper extends BaseMapper<PrdWorkOrderDetail> {

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
   * @return List<PrdWorkOrderDetail>
   */
	public List<PrdWorkOrderDetail> selectByMainId(@Param("mainId") String mainId);
}
