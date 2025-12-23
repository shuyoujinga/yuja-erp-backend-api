package org.jeecg.modules.prd.prdmps.service.impl;

import org.jeecg.modules.prd.prdmps.entity.PrdMps;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsDetail;
import org.jeecg.modules.prd.prdmps.entity.PrdMpsBomDetail;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsDetailMapper;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsBomDetailMapper;
import org.jeecg.modules.prd.prdmps.mapper.PrdMpsMapper;
import org.jeecg.modules.prd.prdmps.service.IPrdMpsService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产计划
 * @Author: 舒有敬
 * @Date:   2025-12-19
 * @Version: V1.0
 */
@Service
public class PrdMpsServiceImpl extends ServiceImpl<PrdMpsMapper, PrdMps> implements IPrdMpsService {

	@Autowired
	private PrdMpsMapper prdMpsMapper;
	@Autowired
	private PrdMpsDetailMapper prdMpsDetailMapper;
	@Autowired
	private PrdMpsBomDetailMapper prdMpsBomDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdMps prdMps, List<PrdMpsDetail> prdMpsDetailList,List<PrdMpsBomDetail> prdMpsBomDetailList) {
		prdMpsMapper.insert(prdMps);
		if(prdMpsDetailList!=null && prdMpsDetailList.size()>0) {
			for(PrdMpsDetail entity:prdMpsDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsDetailMapper.insert(entity);
			}
		}
		if(prdMpsBomDetailList!=null && prdMpsBomDetailList.size()>0) {
			for(PrdMpsBomDetail entity:prdMpsBomDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdMps prdMps,List<PrdMpsDetail> prdMpsDetailList,List<PrdMpsBomDetail> prdMpsBomDetailList) {
		prdMpsMapper.updateById(prdMps);
		
		//1.先删除子表数据
		prdMpsDetailMapper.deleteByMainId(prdMps.getId());
		prdMpsBomDetailMapper.deleteByMainId(prdMps.getId());
		
		//2.子表数据重新插入
		if(prdMpsDetailList!=null && prdMpsDetailList.size()>0) {
			for(PrdMpsDetail entity:prdMpsDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsDetailMapper.insert(entity);
			}
		}
		if(prdMpsBomDetailList!=null && prdMpsBomDetailList.size()>0) {
			for(PrdMpsBomDetail entity:prdMpsBomDetailList) {
				//外键设置
				entity.setPid(prdMps.getId());
				prdMpsBomDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdMpsDetailMapper.deleteByMainId(id);
		prdMpsBomDetailMapper.deleteByMainId(id);
		prdMpsMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdMpsDetailMapper.deleteByMainId(id.toString());
			prdMpsBomDetailMapper.deleteByMainId(id.toString());
			prdMpsMapper.deleteById(id);
		}
	}
	
}
