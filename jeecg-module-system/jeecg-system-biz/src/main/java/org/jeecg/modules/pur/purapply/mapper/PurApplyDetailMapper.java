package org.jeecg.modules.pur.purapply.mapper;

import java.util.List;
import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购申请明细
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
public interface PurApplyDetailMapper extends BaseMapper<PurApplyDetail> {

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
   * @return List<PurApplyDetail>
   */
	public List<PurApplyDetail> selectByMainId(@Param("mainId") String mainId);
}
