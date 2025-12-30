package org.jeecg.modules.sal.salorder.service;

import org.jeecg.modules.sal.salorder.entity.SalOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售订单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface ISalOrderDetailService extends IService<SalOrderDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalOrderDetail>
	 */
	public List<SalOrderDetail> selectByMainId(String mainId);

	List<SalOrderDetail> selectByTargetId(String ids);
}
