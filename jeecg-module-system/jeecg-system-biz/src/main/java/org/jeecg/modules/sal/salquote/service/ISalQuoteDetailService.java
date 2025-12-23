package org.jeecg.modules.sal.salquote.service;

import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售报价_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface ISalQuoteDetailService extends IService<SalQuoteDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalQuoteDetail>
	 */
	public List<SalQuoteDetail> selectByMainId(String mainId);
}
