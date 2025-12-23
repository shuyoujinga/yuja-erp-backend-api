package org.jeecg.modules.pur.purorder.mapper;

import java.util.List;
import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购订单_明细
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
public interface PurOrderDetailMapper extends BaseMapper<PurOrderDetail> {

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
   * @return List<PurOrderDetail>
   */
	public List<PurOrderDetail> selectByMainId(@Param("mainId") String mainId);

}
