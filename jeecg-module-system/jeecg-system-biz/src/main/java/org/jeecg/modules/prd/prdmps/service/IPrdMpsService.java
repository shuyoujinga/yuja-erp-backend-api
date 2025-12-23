package org.jeecg.modules.prd.prdmps.service;

import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMps;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产计划
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
public interface IPrdMpsService extends IService<PrdMps> {

	/**
	 * 添加一对多
	 *
	 * @param prdMps
	 * @param prdMpsDetailList
	 * @param prdMpsBomDetailList
	 */
	public void saveMain(PrdMps prdMps,List<PrdMpsDetail> prdMpsDetailList,List<PrdMpsBomDetail> prdMpsBomDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param prdMps
   * @param prdMpsDetailList
   * @param prdMpsBomDetailList
	 */
	public void updateMain(PrdMps prdMps,List<PrdMpsDetail> prdMpsDetailList,List<PrdMpsBomDetail> prdMpsBomDetailList);
	
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
