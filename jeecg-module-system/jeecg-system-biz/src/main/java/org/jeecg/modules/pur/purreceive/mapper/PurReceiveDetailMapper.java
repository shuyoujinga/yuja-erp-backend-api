package org.jeecg.modules.pur.purreceive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;

import java.util.List;

/**
 * @Description: 采购收货_明细
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
public interface PurReceiveDetailMapper extends BaseMapper<PurReceiveDetail> {

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
	 * @return List<PurReceiveDetail>
	 */
	public List<PurReceiveDetail> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 根据采购订单明细ID集合，统计已审核收货数量
	 */
	Double selectReceiveQtyByOrderIds(@Param("list") List<String> list);
}
