package org.jeecg.modules.inv.invassembly.mapper;

import java.util.List;
import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 组装单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-16
 * @Version: V1.0
 */
public interface InvAssemblyDetailMapper extends BaseMapper<InvAssemblyDetail> {

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
   * @return List<InvAssemblyDetail>
   */
	public List<InvAssemblyDetail> selectByMainId(@Param("mainId") String mainId);
}
