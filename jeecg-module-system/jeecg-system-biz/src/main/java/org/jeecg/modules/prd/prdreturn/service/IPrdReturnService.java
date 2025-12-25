package org.jeecg.modules.prd.prdreturn.service;

import org.jeecg.modules.prd.prdreturn.entity.PrdReturnDetail;
import org.jeecg.modules.prd.prdreturn.entity.PrdReturn;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产退料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdReturnService extends IService<PrdReturn> {

	/**
	 * 添加一对多
	 *
	 * @param prdReturn
	 * @param prdReturnDetailList
	 */
	public void saveMain(PrdReturn prdReturn,List<PrdReturnDetail> prdReturnDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param prdReturn
   * @param prdReturnDetailList
	 */
	public void updateMain(PrdReturn prdReturn,List<PrdReturnDetail> prdReturnDetailList);
	
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
