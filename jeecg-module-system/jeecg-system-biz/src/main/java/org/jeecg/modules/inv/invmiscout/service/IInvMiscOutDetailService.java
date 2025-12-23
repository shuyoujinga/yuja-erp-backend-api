package org.jeecg.modules.inv.invmiscout.service;

import org.jeecg.modules.inv.invmiscout.entity.InvMiscOutDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 其他入库_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface IInvMiscOutDetailService extends IService<InvMiscOutDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvMiscOutDetail>
	 */
	public List<InvMiscOutDetail> selectByMainId(String mainId);
}
