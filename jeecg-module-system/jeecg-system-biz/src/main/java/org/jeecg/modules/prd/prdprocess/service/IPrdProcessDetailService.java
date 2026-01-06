package org.jeecg.modules.prd.prdprocess.service;

import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产工序_明细
 * @Author: 舒有敬
 * @Date:   2026-01-06
 * @Version: V1.0
 */
public interface IPrdProcessDetailService extends IService<PrdProcessDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdProcessDetail>
	 */
	public List<PrdProcessDetail> selectByMainId(String mainId);
}
