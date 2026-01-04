package org.jeecg.modules.sal.saldelivery.service;

import org.jeecg.modules.sal.saldelivery.entity.SalDeliveryDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售发货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalDeliveryDetailService extends IService<SalDeliveryDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalDeliveryDetail>
	 */
	public List<SalDeliveryDetail> selectByMainId(String mainId);

	List<SalDeliveryDetail> selectByTargetId(String ids);
}
