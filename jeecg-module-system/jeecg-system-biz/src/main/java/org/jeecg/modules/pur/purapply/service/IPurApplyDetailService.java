package org.jeecg.modules.pur.purapply.service;

import org.jeecg.modules.pur.purapply.entity.PurApplyDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购申请明细
 * @Author: 舒有敬
 * @Date:   2025-11-28
 * @Version: V1.0
 */
public interface IPurApplyDetailService extends IService<PurApplyDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurApplyDetail>
	 */
	public List<PurApplyDetail> selectByMainId(String mainId);
}
