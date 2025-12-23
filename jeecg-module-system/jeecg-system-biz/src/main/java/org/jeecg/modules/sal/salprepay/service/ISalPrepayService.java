package org.jeecg.modules.sal.salprepay.service;

import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import org.jeecg.modules.sal.salprepay.entity.SalPrepay;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售预收
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalPrepayService extends IService<SalPrepay> {

	/**
	 * 添加一对多
	 *
	 * @param salPrepay
	 * @param salPrepayDetailList
	 */
	public void saveMain(SalPrepay salPrepay,List<SalPrepayDetail> salPrepayDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salPrepay
   * @param salPrepayDetailList
	 */
	public void updateMain(SalPrepay salPrepay,List<SalPrepayDetail> salPrepayDetailList);
	
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
