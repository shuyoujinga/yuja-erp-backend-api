package org.jeecg.modules.prd.prdworkorder.service;

import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产工单_物料明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdWorkOrderDetailService extends IService<PrdWorkOrderDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdWorkOrderDetail>
	 */
	public List<PrdWorkOrderDetail> selectByMainId(String mainId);
}
