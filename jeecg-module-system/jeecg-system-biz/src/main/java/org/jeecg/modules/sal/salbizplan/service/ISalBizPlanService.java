package org.jeecg.modules.sal.salbizplan.service;

import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanDetail;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlanBomDetail;
import org.jeecg.modules.sal.salbizplan.entity.SalBizPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 业务计划
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalBizPlanService extends IService<SalBizPlan> {

	/**
	 * 添加一对多
	 *
	 * @param salBizPlan
	 * @param salBizPlanDetailList
	 * @param salBizPlanBomDetailList
	 */
	public void saveMain(SalBizPlan salBizPlan,List<SalBizPlanDetail> salBizPlanDetailList,List<SalBizPlanBomDetail> salBizPlanBomDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salBizPlan
   * @param salBizPlanDetailList
   * @param salBizPlanBomDetailList
	 */
	public void updateMain(SalBizPlan salBizPlan,List<SalBizPlanDetail> salBizPlanDetailList,List<SalBizPlanBomDetail> salBizPlanBomDetailList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
