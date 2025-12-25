package org.jeecg.modules.prd.prdissue.service;

import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import org.jeecg.modules.prd.prdissue.entity.PrdIssue;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产领料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdIssueService extends IService<PrdIssue> {

	/**
	 * 添加一对多
	 *
	 * @param prdIssue
	 * @param prdIssueDetailList
	 */
	public void saveMain(PrdIssue prdIssue,List<PrdIssueDetail> prdIssueDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param prdIssue
   * @param prdIssueDetailList
	 */
	public void updateMain(PrdIssue prdIssue,List<PrdIssueDetail> prdIssueDetailList);
	
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
