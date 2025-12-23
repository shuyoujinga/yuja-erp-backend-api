package org.jeecg.modules.sal.salquote.mapper;

import java.util.List;
import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售报价_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface SalQuoteDetailMapper extends BaseMapper<SalQuoteDetail> {

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
   * @return List<SalQuoteDetail>
   */
	public List<SalQuoteDetail> selectByMainId(@Param("mainId") String mainId);
}
