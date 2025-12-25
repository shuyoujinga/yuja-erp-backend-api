package org.jeecg.modules.prd.prdworkorder.service.impl;

import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.mapper.PrdWorkOrderDetailMapper;
import org.jeecg.modules.prd.prdworkorder.mapper.PrdWorkOrderMapper;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产工单
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdWorkOrderServiceImpl extends ServiceImpl<PrdWorkOrderMapper, PrdWorkOrder> implements IPrdWorkOrderService {

	@Autowired
	private PrdWorkOrderMapper prdWorkOrderMapper;
	@Autowired
	private PrdWorkOrderDetailMapper prdWorkOrderDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdWorkOrder prdWorkOrder, List<PrdWorkOrderDetail> prdWorkOrderDetailList) {
		prdWorkOrderMapper.insert(prdWorkOrder);
		if(prdWorkOrderDetailList!=null && prdWorkOrderDetailList.size()>0) {
			for(PrdWorkOrderDetail entity:prdWorkOrderDetailList) {
				//外键设置
				entity.setPid(prdWorkOrder.getId());
				prdWorkOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdWorkOrder prdWorkOrder,List<PrdWorkOrderDetail> prdWorkOrderDetailList) {
		prdWorkOrderMapper.updateById(prdWorkOrder);
		
		//1.先删除子表数据
		prdWorkOrderDetailMapper.deleteByMainId(prdWorkOrder.getId());
		
		//2.子表数据重新插入
		if(prdWorkOrderDetailList!=null && prdWorkOrderDetailList.size()>0) {
			for(PrdWorkOrderDetail entity:prdWorkOrderDetailList) {
				//外键设置
				entity.setPid(prdWorkOrder.getId());
				prdWorkOrderDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdWorkOrderDetailMapper.deleteByMainId(id);
		prdWorkOrderMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdWorkOrderDetailMapper.deleteByMainId(id.toString());
			prdWorkOrderMapper.deleteById(id);
		}
	}
	
}
