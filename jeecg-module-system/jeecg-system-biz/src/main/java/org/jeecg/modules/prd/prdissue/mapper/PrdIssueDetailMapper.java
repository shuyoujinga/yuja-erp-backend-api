package org.jeecg.modules.prd.prdissue.mapper;

import java.util.List;
import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 生产领料_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface PrdIssueDetailMapper extends BaseMapper<PrdIssueDetail> {

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
   * @return List<PrdIssueDetail>
   */
	public List<PrdIssueDetail> selectByMainId(@Param("mainId") String mainId);
}
