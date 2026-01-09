package org.jeecg.modules.prd.prdreport.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.prd.prdreport.entity.PrdReport;
import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import org.jeecg.modules.prd.prdreport.mapper.PrdReportDetailMapper;
import org.jeecg.modules.prd.prdreport.mapper.PrdReportMapper;
import org.jeecg.modules.prd.prdreport.service.IPrdReportService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产报工
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PrdReportServiceImpl extends ServiceImpl<PrdReportMapper, PrdReport> implements IPrdReportService {

	@Resource
	private PrdReportMapper prdReportMapper;
	@Resource
	private PrdReportDetailMapper prdReportDetailMapper;
	@Autowired
	private SerialNumberService serialNumberService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(PrdReport prdReport, List<PrdReportDetail> prdReportDetailList) {
		prdReport.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.SCBG));
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int audit(List<String> ids) {
		return updateAuditStatus(ids,Constants.DICT_AUDIT_STATUS.YES);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int unAudit(List<String> ids) {
		return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.NO);
	}
	/**
	 * 批量更新审核状态
	 *
	 * @param ids    待更新记录ID列表
	 * @param status 审核状态（YES/NO）
	 * @return 更新数量
	 */
	private int updateAuditStatus(List<String> ids, Integer status) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if (CollectionUtil.isEmpty(ids)) {
			return 0;
		}

		List<PrdReport> records = baseMapper.selectBatchIds(ids);
		if (CollectionUtil.isEmpty(records)) {
			return 0;
		}

		int count = 0;
		for (PrdReport record : records) {
			if (!status.equals(record.getAudit())) {
				record.setAudit(status);
				record.setAuditBy(sysUser.getUsername());
				record.setAuditTime(new Date());
				count++;
			}
		}

		if (count > 0) {
			updateBatchById(records);
		}
		return count;
	}

}
