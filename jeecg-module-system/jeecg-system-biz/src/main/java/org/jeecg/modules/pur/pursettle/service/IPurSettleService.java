package org.jeecg.modules.pur.pursettle.service;

import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import org.jeecg.modules.pur.pursettle.entity.PurSettle;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购结算
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
public interface IPurSettleService extends IService<PurSettle> {

	/**
	 * 添加一对多
	 *
	 * @param purSettle
	 * @param purSettleDetailList
	 */
	public void saveMain(PurSettle purSettle,List<PurSettleDetail> purSettleDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purSettle
   * @param purSettleDetailList
	 */
	public void updateMain(PurSettle purSettle,List<PurSettleDetail> purSettleDetailList);
	
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
