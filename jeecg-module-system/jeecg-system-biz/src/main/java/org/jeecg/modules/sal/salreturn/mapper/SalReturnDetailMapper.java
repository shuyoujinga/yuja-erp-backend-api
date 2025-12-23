package org.jeecg.modules.sal.salreturn.mapper;

import java.util.List;
import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface SalReturnDetailMapper extends BaseMapper<SalReturnDetail> {

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
   * @return List<SalReturnDetail>
   */
	public List<SalReturnDetail> selectByMainId(@Param("mainId") String mainId);
}
