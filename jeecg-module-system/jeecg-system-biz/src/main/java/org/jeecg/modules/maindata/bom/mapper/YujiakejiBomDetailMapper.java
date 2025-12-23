package org.jeecg.modules.maindata.bom.mapper;

import java.util.List;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 材料清单_明细表
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
public interface YujiakejiBomDetailMapper extends BaseMapper<YujiakejiBomDetail> {

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
   * @return List<YujiakejiBomDetail>
   */
	public List<YujiakejiBomDetail> selectByMainId(@Param("mainId") String mainId);
}
