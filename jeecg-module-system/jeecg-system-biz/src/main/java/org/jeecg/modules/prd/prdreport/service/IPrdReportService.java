package org.jeecg.modules.prd.prdreport.service;

import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import org.jeecg.modules.prd.prdreport.entity.PrdReport;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产报工
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdReportService extends IService<PrdReport> {

	/**
	 * 添加一对多
	 *
	 * @param prdReport
	 * @param prdReportDetailList
	 */
	public void saveMain(PrdReport prdReport,List<PrdReportDetail> prdReportDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param prdReport
   * @param prdReportDetailList
	 */
	public void updateMain(PrdReport prdReport,List<PrdReportDetail> prdReportDetailList);
	
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
