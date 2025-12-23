package org.jeecg.modules.inv.invassembly.service;

import org.jeecg.modules.inv.invassembly.entity.InvAssemblyDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 组装单_明细
 * @Author: 舒有敬
 * @Date:   2025-12-16
 * @Version: V1.0
 */
public interface IInvAssemblyDetailService extends IService<InvAssemblyDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<InvAssemblyDetail>
	 */
	public List<InvAssemblyDetail> selectByMainId(String mainId);
}
