package org.jeecg.modules.inv.invissue.service;

import org.jeecg.modules.inv.invissue.entity.InvIssueDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 物料领用_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface IInvIssueDetailService extends IService<InvIssueDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvIssueDetail>
	 */
	public List<InvIssueDetail> selectByMainId(String mainId);
}
