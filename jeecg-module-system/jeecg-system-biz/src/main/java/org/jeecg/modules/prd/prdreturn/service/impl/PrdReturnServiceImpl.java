package org.jeecg.modules.prd.prdreturn.service.impl;

import org.jeecg.modules.prd.prdreturn.entity.PrdReturn;
import org.jeecg.modules.prd.prdreturn.entity.PrdReturnDetail;
import org.jeecg.modules.prd.prdreturn.mapper.PrdReturnDetailMapper;
import org.jeecg.modules.prd.prdreturn.mapper.PrdReturnMapper;
import org.jeecg.modules.prd.prdreturn.service.IPrdReturnService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产退料
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdReturnServiceImpl extends ServiceImpl<PrdReturnMapper, PrdReturn> implements IPrdReturnService {

	@Autowired
	private PrdReturnMapper prdReturnMapper;
	@Autowired
	private PrdReturnDetailMapper prdReturnDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdReturn prdReturn, List<PrdReturnDetail> prdReturnDetailList) {
		prdReturnMapper.insert(prdReturn);
		if(prdReturnDetailList!=null && prdReturnDetailList.size()>0) {
			for(PrdReturnDetail entity:prdReturnDetailList) {
				//外键设置
				entity.setPid(prdReturn.getId());
				prdReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdReturn prdReturn,List<PrdReturnDetail> prdReturnDetailList) {
		prdReturnMapper.updateById(prdReturn);
		
		//1.先删除子表数据
		prdReturnDetailMapper.deleteByMainId(prdReturn.getId());
		
		//2.子表数据重新插入
		if(prdReturnDetailList!=null && prdReturnDetailList.size()>0) {
			for(PrdReturnDetail entity:prdReturnDetailList) {
				//外键设置
				entity.setPid(prdReturn.getId());
				prdReturnDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdReturnDetailMapper.deleteByMainId(id);
		prdReturnMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdReturnDetailMapper.deleteByMainId(id.toString());
			prdReturnMapper.deleteById(id);
		}
	}
	
}
