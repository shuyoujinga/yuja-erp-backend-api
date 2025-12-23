package org.jeecg.modules.pur.purpayment.service;

import org.jeecg.modules.pur.purpayment.entity.PurPaymentDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 采购付款_明细
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface IPurPaymentDetailService extends IService<PurPaymentDetail> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<PurPaymentDetail>
	 */
	public List<PurPaymentDetail> selectByMainId(String mainId);
}
