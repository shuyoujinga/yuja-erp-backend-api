package org.jeecg.modules.pur.purreceive.service;

import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购收货_明细
 * @Author: 舒有敬
 * @Date:   2025-11-29
 * @Version: V1.0
 */
public interface IPurReceiveDetailService extends IService<PurReceiveDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurReceiveDetail>
	 */
	public List<PurReceiveDetail> selectByMainId(String mainId);

    List<PurReceiveDetail> selectByTargetId(String id);

	Double selectReceiveQtyByOrderIds(List<String> list);
}
