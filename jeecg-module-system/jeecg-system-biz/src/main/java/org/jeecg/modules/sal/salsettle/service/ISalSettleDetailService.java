package org.jeecg.modules.sal.salsettle.service;

import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalSettleDetailService extends IService<SalSettleDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalSettleDetail>
	 */
	public List<SalSettleDetail> selectByMainId(String mainId);
}
