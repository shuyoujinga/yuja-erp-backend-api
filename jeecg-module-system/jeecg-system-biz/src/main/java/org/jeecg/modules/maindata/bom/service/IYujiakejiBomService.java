package org.jeecg.modules.maindata.bom.service;

import org.jeecg.modules.maindata.bom.entity.YujiakejiBomDetail;
import org.jeecg.modules.maindata.bom.entity.YujiakejiBom;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 材料清单_主表
 * @Author: 舒有敬
 * @Date:   2025-11-27
 * @Version: V1.0
 */
public interface IYujiakejiBomService extends IService<YujiakejiBom> {

	/**
	 * 添加一对多
	 *
	 * @param yujiakejiBom
	 * @param yujiakejiBomDetailList
	 */
	public void saveMain(YujiakejiBom yujiakejiBom,List<YujiakejiBomDetail> yujiakejiBomDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param yujiakejiBom
   * @param yujiakejiBomDetailList
	 */
	public void updateMain(YujiakejiBom yujiakejiBom,List<YujiakejiBomDetail> yujiakejiBomDetailList);
	
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
