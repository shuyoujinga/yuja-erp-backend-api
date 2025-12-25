package org.jeecg.modules.prd.prdreport.service.impl;

import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import org.jeecg.modules.prd.prdreport.mapper.PrdReportDetailMapper;
import org.jeecg.modules.prd.prdreport.service.IPrdReportDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产报工_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdReportDetailServiceImpl extends ServiceImpl<PrdReportDetailMapper, PrdReportDetail> implements IPrdReportDetailService {
	
	@Autowired
	private PrdReportDetailMapper prdReportDetailMapper;
	
	@Override
	public List<PrdReportDetail> selectByMainId(String mainId) {
		return prdReportDetailMapper.selectByMainId(mainId);
	}
}
