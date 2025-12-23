package org.jeecg.modules.pur.purapply.service;

import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import org.jeecg.modules.pur.purapply.entity.PurApply;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购申请
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
public interface IPurApplyService extends IService<PurApply> {

	/**
	 * 添加一对多
	 *
	 * @param purApply
	 * @param purApplyDetailList
	 */
	public void saveMain(PurApply purApply,List<PurApplyDetail> purApplyDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purApply
   * @param purApplyDetailList
	 */
	public void updateMain(PurApply purApply,List<PurApplyDetail> purApplyDetailList);
	
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

    int audit(List<String> ids);

	int unAudit(List<String> ids);
}
