package org.jeecg.modules.prd.prdissue.service;

import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产领料_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdIssueDetailService extends IService<PrdIssueDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdIssueDetail>
	 */
	public List<PrdIssueDetail> selectByMainId(String mainId);
}
