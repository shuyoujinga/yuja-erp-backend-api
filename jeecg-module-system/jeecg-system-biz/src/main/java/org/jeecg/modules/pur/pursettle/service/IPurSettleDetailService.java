package org.jeecg.modules.pur.pursettle.service;

import org.jeecg.modules.pur.pursettle.entity.PurSettleDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购结算_明细
 * @Author: 舒有敬
 * @Date:   2025-12-06
 * @Version: V1.0
 */
public interface IPurSettleDetailService extends IService<PurSettleDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurSettleDetail>
	 */
	public List<PurSettleDetail> selectByMainId(String mainId);

	List<PurSettleDetail> selectByTargetId(String id);
}
