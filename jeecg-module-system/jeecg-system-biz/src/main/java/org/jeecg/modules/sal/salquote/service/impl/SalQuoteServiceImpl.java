package org.jeecg.modules.sal.salquote.service.impl;

import org.jeecg.modules.sal.salquote.entity.SalQuote;
import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import org.jeecg.modules.sal.salquote.mapper.SalQuoteDetailMapper;
import org.jeecg.modules.sal.salquote.mapper.SalQuoteMapper;
import org.jeecg.modules.sal.salquote.service.ISalQuoteService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售报价
 * @Author: 舒有敬
 * @Date:   2025-12-08
 * @Version: V1.0
 */
@Service
public class SalQuoteServiceImpl extends ServiceImpl<SalQuoteMapper, SalQuote> implements ISalQuoteService {

	@Autowired
	private SalQuoteMapper salQuoteMapper;
	@Autowired
	private SalQuoteDetailMapper salQuoteDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(SalQuote salQuote, List<SalQuoteDetail> salQuoteDetailList) {
		salQuoteMapper.insert(salQuote);
		if(salQuoteDetailList!=null && salQuoteDetailList.size()>0) {
			for(SalQuoteDetail entity:salQuoteDetailList) {
				//外键设置
				entity.setPid(salQuote.getId());
				salQuoteDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(SalQuote salQuote,List<SalQuoteDetail> salQuoteDetailList) {
		salQuoteMapper.updateById(salQuote);
		
		//1.先删除子表数据
		salQuoteDetailMapper.deleteByMainId(salQuote.getId());
		
		//2.子表数据重新插入
		if(salQuoteDetailList!=null && salQuoteDetailList.size()>0) {
			for(SalQuoteDetail entity:salQuoteDetailList) {
				//外键设置
				entity.setPid(salQuote.getId());
				salQuoteDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		salQuoteDetailMapper.deleteByMainId(id);
		salQuoteMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			salQuoteDetailMapper.deleteByMainId(id.toString());
			salQuoteMapper.deleteById(id);
		}
	}
	
}
