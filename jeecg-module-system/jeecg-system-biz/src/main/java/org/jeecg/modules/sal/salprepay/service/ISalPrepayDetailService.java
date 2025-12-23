package org.jeecg.modules.sal.salprepay.service;

import org.jeecg.modules.sal.salprepay.entity.SalPrepayDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 预收使用_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalPrepayDetailService extends IService<SalPrepayDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalPrepayDetail>
	 */
	public List<SalPrepayDetail> selectByMainId(String mainId);
}
