package org.jeecg.modules.prd.prdreport.mapper;

import java.util.List;
import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产报工_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface PrdReportDetailMapper extends BaseMapper<PrdReportDetail> {

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
   * @return List<PrdReportDetail>
   */
	public List<PrdReportDetail> selectByMainId(@Param("mainId") String mainId);
}
