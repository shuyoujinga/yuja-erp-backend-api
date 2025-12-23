package org.jeecg.modules.inv.invdisassembly.service;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 拆卸单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-10
 * @Version: V1.0
 */
public interface IInvDisassemblyDetailService extends IService<InvDisassemblyDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvDisassemblyDetail>
	 */
	public List<InvDisassemblyDetail> selectByMainId(String mainId);
}
