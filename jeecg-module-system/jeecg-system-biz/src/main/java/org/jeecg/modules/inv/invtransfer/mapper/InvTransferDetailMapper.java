package org.jeecg.modules.inv.invtransfer.mapper;

import java.util.List;
import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 物料调拨_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface InvTransferDetailMapper extends BaseMapper<InvTransferDetail> {

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
   * @return List<InvTransferDetail>
   */
	public List<InvTransferDetail> selectByMainId(@Param("mainId") String mainId);
}
