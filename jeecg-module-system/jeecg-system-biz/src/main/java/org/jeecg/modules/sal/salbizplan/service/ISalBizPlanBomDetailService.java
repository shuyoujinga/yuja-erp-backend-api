package org.jeecg.modules.sal.salbizplan.service;

import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanBomDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 业务计划_材料明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalBizPlanBomDetailService extends IService<SalBizPlanBomDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalBizPlanBomDetail>
	 */
	public List<SalBizPlanBomDetail> selectByMainId(String mainId);
}
