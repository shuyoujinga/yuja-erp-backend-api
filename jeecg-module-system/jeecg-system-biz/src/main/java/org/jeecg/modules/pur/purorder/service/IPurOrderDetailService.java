package org.jeecg.modules.pur.purorder.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.pur.purorder.entity.PurOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购订单_明细
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
public interface IPurOrderDetailService extends IService<PurOrderDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurOrderDetail>
	 */
	public List<PurOrderDetail> selectByMainId(String mainId);



}
