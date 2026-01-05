package org.jeecg.modules.inv.invdisassembly.service;

import org.jeecg.modules.inv.invdisassembly.entity.InvDisassemblyBomDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 拆卸单_材料清单
 * @Author: 舒有敬
 * @Date:   2026-01-05
 * @Version: V1.0
 */
public interface IInvDisassemblyBomDetailService extends IService<InvDisassemblyBomDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvDisassemblyBomDetail>
	 */
	public List<InvDisassemblyBomDetail> selectByMainId(String mainId);
}
