package org.jeecg.modules.inv.invstocktake.service;

import org.jeecg.modules.inv.invstocktake.entity.InvStockTakeDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 库存盘点_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface IInvStockTakeDetailService extends IService<InvStockTakeDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvStockTakeDetail>
	 */
	public List<InvStockTakeDetail> selectByMainId(String mainId);

	List<InvStockTakeDetail> selectByLocation(String warehouseAndlocation);
}
