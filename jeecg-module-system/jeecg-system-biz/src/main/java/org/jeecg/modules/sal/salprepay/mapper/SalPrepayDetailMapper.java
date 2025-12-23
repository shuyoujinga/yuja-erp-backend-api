package org.jeecg.modules.sal.salprepay.mapper;

import java.util.List;
import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 预收使用_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface SalPrepayDetailMapper extends BaseMapper<SalPrepayDetail> {

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
   * @return List<SalPrepayDetail>
   */
	public List<SalPrepayDetail> selectByMainId(@Param("mainId") String mainId);
}
