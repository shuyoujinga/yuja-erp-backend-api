package org.jeecg.modules.inv.invmiscout.mapper;

import java.util.List;
import org.jeecg.modules.inv.invmiscout.entity.InvMiscOutDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 其他入库_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface InvMiscOutDetailMapper extends BaseMapper<InvMiscOutDetail> {

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
   * @return List<InvMiscOutDetail>
   */
	public List<InvMiscOutDetail> selectByMainId(@Param("mainId") String mainId);
}
