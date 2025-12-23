package org.jeecg.modules.sal.salreturn.service;

import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalReturnDetailService extends IService<SalReturnDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalReturnDetail>
	 */
	public List<SalReturnDetail> selectByMainId(String mainId);
}
