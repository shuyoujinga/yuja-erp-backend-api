package org.jeecg.modules.pur.purreturn.mapper;

import java.util.List;
import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface PurReturnDetailMapper extends BaseMapper<PurReturnDetail> {

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
   * @return List<PurReturnDetail>
   */
	public List<PurReturnDetail> selectByMainId(@Param("mainId") String mainId);
}
