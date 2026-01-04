package org.jeecg.modules.sal.salbizplan.service;

import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import reactor.core.Exceptions;

import java.util.List;

/**
 * @Description: 业务计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalBizPlanDetailService extends IService<SalBizPlanDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalBizPlanDetail>
	 */
	public List<SalBizPlanDetail> selectByMainId(String mainId);

	List<SalBizPlanDetail> selectByTargetId(String ids);
}
