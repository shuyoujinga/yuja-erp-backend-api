package org.jeecg.modules.inv.invissue.service;

import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import org.jeecg.modules.inv.invissue.entity.InvIssue;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 物料领用
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface IInvIssueService extends IService<InvIssue> {

	/**
	 * 添加一对多
	 *
	 * @param invIssue
	 * @param invIssueDetailList
	 */
	public void saveMain(InvIssue invIssue,List<InvIssueDetail> invIssueDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param invIssue
   * @param invIssueDetailList
	 */
	public void updateMain(InvIssue invIssue,List<InvIssueDetail> invIssueDetailList);
	
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
