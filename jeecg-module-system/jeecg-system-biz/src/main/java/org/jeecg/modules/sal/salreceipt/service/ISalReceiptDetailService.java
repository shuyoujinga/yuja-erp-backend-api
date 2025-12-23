package org.jeecg.modules.sal.salreceipt.service;

import org.jeecg.modules.sal.salreceipt.entity.SalReceiptDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售收款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-09
 * @Version: V1.0
 */
public interface ISalReceiptDetailService extends IService<SalReceiptDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<SalReceiptDetail>
	 */
	public List<SalReceiptDetail> selectByMainId(String mainId);
}
