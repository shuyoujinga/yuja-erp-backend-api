package org.jeecg.modules.inv.invmiscin.service;

import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 其他入库_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface IInvMiscInDetailService extends IService<InvMiscInDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvMiscInDetail>
	 */
	public List<InvMiscInDetail> selectByMainId(String mainId);
}
