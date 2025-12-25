package org.jeecg.modules.prd.prdreturn.service;

import org.jeecg.modules.prd.prdreturn.entity.PrdReturnDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 生产退料_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
public interface IPrdReturnDetailService extends IService<PrdReturnDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PrdReturnDetail>
	 */
	public List<PrdReturnDetail> selectByMainId(String mainId);
}
