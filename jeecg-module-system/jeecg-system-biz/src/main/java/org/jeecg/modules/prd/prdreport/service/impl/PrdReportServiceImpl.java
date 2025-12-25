package org.jeecg.modules.prd.prdreport.service.impl;

import org.jeecg.modules.prd.prdreport.entity.PrdReport;
import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import org.jeecg.modules.prd.prdreport.mapper.PrdReportDetailMapper;
import org.jeecg.modules.prd.prdreport.mapper.PrdReportMapper;
import org.jeecg.modules.prd.prdreport.service.IPrdReportService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产报工
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdReportServiceImpl extends ServiceImpl<PrdReportMapper, PrdReport> implements IPrdReportService {

	@Autowired
	private PrdReportMapper prdReportMapper;
	@Autowired
	private PrdReportDetailMapper prdReportDetailMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdReport prdReport, List<PrdReportDetail> prdReportDetailList) {
		prdReportMapper.insert(prdReport);
		if(prdReportDetailList!=null && prdReportDetailList.size()>0) {
			for(PrdReportDetail entity:prdReportDetailList) {
				//外键设置
				entity.setPid(prdReport.getId());
				prdReportDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(PrdReport prdReport,List<PrdReportDetail> prdReportDetailList) {
		prdReportMapper.updateById(prdReport);
		
		//1.先删除子表数据
		prdReportDetailMapper.deleteByMainId(prdReport.getId());
		
		//2.子表数据重新插入
		if(prdReportDetailList!=null && prdReportDetailList.size()>0) {
			for(PrdReportDetail entity:prdReportDetailList) {
				//外键设置
				entity.setPid(prdReport.getId());
				prdReportDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		prdReportDetailMapper.deleteByMainId(id);
		prdReportMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			prdReportDetailMapper.deleteByMainId(id.toString());
			prdReportMapper.deleteById(id);
		}
	}
	
}
