package org.jeecg.modules.pur.purreceive.service;

import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购收货
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
public interface IPurReceiveService extends IService<PurReceive> {

	/**
	 * 添加一对多
	 *
	 * @param purReceive
	 * @param purReceiveDetailList
	 */
	public void saveMain(PurReceive purReceive,List<PurReceiveDetail> purReceiveDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param purReceive
   * @param purReceiveDetailList
	 */
	public void updateMain(PurReceive purReceive,List<PurReceiveDetail> purReceiveDetailList);
	
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

	int audit(List<String> ids) throws Exception;

	int unAudit(List<String> ids) throws Exception;
}
