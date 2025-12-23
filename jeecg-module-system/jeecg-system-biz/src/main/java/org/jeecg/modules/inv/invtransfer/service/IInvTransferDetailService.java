package org.jeecg.modules.inv.invtransfer.service;

import org.jeecg.modules.inv.invtransfer.entity.InvTransferDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 物料调拨_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface IInvTransferDetailService extends IService<InvTransferDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvTransferDetail>
	 */
	public List<InvTransferDetail> selectByMainId(String mainId);
}
