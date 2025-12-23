package org.jeecg.modules.prd.prdmps.service;

import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产计划_材料清单
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
public interface IPrdMpsBomDetailService extends IService<PrdMpsBomDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdMpsBomDetail>
	 */
	public List<PrdMpsBomDetail> selectByMainId(String mainId);
}
