package org.jeecg.modules.inv.invmaterialvoucher.mapper;

import java.util.List;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 物料凭证_明细
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
public interface InvMaterialVoucherDetailMapper extends BaseMapper<InvMaterialVoucherDetail> {

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
   * @return List<InvMaterialVoucherDetail>
   */
	public List<InvMaterialVoucherDetail> selectByMainId(@Param("mainId") String mainId);
}
