package org.jeecg.modules.prd.prdprocess.service;

import org.jeecg.modules.prd.prdprocess.entity.PrdProcessDetail;
import org.jeecg.modules.prd.prdprocess.entity.PrdProcess;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产工序
 * @Author: 舒有敬
 * @Date:   2026-01-06
 * @Version: V1.0
 */
public interface IPrdProcessService extends IService<PrdProcess> {

	/**
	 * 添加一对多
	 *
	 * @param prdProcess
	 * @param prdProcessDetailList
	 */
	public void saveMain(PrdProcess prdProcess,List<PrdProcessDetail> prdProcessDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param prdProcess
   * @param prdProcessDetailList
	 */
	public void updateMain(PrdProcess prdProcess,List<PrdProcessDetail> prdProcessDetailList);
	
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
