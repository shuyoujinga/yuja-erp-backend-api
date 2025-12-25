package org.jeecg.modules.pur.purreturn.service;

import org.jeecg.modules.pur.purreturn.entity.PurReturnDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购退货_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface IPurReturnDetailService extends IService<PurReturnDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurReturnDetail>
	 */
	public List<PurReturnDetail> selectByMainId(String mainId);

	List<PurReturnDetail> selectByTargetId(String ids);
}
