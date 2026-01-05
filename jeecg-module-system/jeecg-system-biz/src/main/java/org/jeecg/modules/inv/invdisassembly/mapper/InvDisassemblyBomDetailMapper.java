package org.jeecg.modules.inv.invdisassembly.mapper;

import java.util.List;
import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyBomDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 拆卸单_材料清单
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
public interface InvDisassemblyBomDetailMapper extends BaseMapper<InvDisassemblyBomDetail> {

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
   * @return List<InvDisassemblyBomDetail>
   */
	public List<InvDisassemblyBomDetail> selectByMainId(@Param("mainId") String mainId);
}
