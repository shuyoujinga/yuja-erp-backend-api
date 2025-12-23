package org.jeecg.modules.sal.salreturn.service;

import org.jeecg.modules.sal.salreturn.entity.SalReturnDetail;
import org.jeecg.modules.sal.salreturn.entity.SalReturn;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售退货
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalReturnService extends IService<SalReturn> {

	/**
	 * 添加一对多
	 *
	 * @param salReturn
	 * @param salReturnDetailList
	 */
	public void saveMain(SalReturn salReturn,List<SalReturnDetail> salReturnDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salReturn
   * @param salReturnDetailList
	 */
	public void updateMain(SalReturn salReturn,List<SalReturnDetail> salReturnDetailList);
	
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
