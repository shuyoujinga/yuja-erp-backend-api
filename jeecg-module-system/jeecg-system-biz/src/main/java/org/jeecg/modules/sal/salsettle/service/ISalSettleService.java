package org.jeecg.modules.sal.salsettle.service;

import org.jeecg.modules.sal.salsettle.entity.SalSettleDetail;
import org.jeecg.modules.sal.salsettle.entity.SalSettle;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售结算
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalSettleService extends IService<SalSettle> {

	/**
	 * 添加一对多
	 *
	 * @param salSettle
	 * @param salSettleDetailList
	 */
	public void saveMain(SalSettle salSettle,List<SalSettleDetail> salSettleDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salSettle
   * @param salSettleDetailList
	 */
	public void updateMain(SalSettle salSettle,List<SalSettleDetail> salSettleDetailList);
	
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
