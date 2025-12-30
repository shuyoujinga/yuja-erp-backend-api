package org.jeecg.modules.sal.salquote.service;

import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import org.jeecg.modules.sal.salquote.entity.SalQuote;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售报价
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
public interface ISalQuoteService extends IService<SalQuote> {

	/**
	 * 添加一对多
	 *
	 * @param salQuote
	 * @param salQuoteDetailList
	 */
	public void saveMain(SalQuote salQuote,List<SalQuoteDetail> salQuoteDetailList) ;
	
	/**
	 * 修改一对多
	 *
   * @param salQuote
   * @param salQuoteDetailList
	 */
	public void updateMain(SalQuote salQuote,List<SalQuoteDetail> salQuoteDetailList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	int audit(List<String> ids);

	int unAudit(List<String> ids);
}
