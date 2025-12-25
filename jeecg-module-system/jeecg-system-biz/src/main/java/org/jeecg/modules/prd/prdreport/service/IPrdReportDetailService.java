package org.jeecg.modules.prd.prdreport.service;

import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产报工_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdReportDetailService extends IService<PrdReportDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdReportDetail>
	 */
	public List<PrdReportDetail> selectByMainId(String mainId);
}
