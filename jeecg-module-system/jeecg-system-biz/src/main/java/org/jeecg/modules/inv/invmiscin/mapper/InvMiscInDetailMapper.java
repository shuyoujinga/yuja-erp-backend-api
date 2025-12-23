package org.jeecg.modules.inv.invmiscin.mapper;

import java.util.List;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 其他入库_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface InvMiscInDetailMapper extends BaseMapper<InvMiscInDetail> {

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
   * @return List<InvMiscInDetail>
   */
	public List<InvMiscInDetail> selectByMainId(@Param("mainId") String mainId);
}
