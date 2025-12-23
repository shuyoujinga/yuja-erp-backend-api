package org.jeecg.modules.prd.prdmps.service;

import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产计划_明细
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
public interface IPrdMpsDetailService extends IService<PrdMpsDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdMpsDetail>
	 */
	public List<PrdMpsDetail> selectByMainId(String mainId);
}
