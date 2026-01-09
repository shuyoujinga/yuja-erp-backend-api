package org.jeecg.modules.prd.prdreport.service.impl;

import org.constant.Constants;
import org.jeecg.modules.prd.prdreport.entity.PrdReportDetail;
import org.jeecg.modules.prd.prdreport.mapper.PrdReportDetailMapper;
import org.jeecg.modules.prd.prdreport.service.IPrdReportDetailService;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrder;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.utils.AmountUtils;
import org.utils.Assert;
import org.utils.MathUtils;

import javax.annotation.Resource;

/**
 * @Description: 生产报工_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdReportDetailServiceImpl extends ServiceImpl<PrdReportDetailMapper, PrdReportDetail> implements IPrdReportDetailService {
	
	@Resource
	private PrdReportDetailMapper prdReportDetailMapper;
	@Autowired
	private IPrdWorkOrderService prdWorkOrderService;
	@Autowired
	private ISysDepartService sysDepartService;
	
	@Override
	public List<PrdReportDetail> selectByMainId(String mainId) {
		return prdReportDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<PrdReportDetail> selectByTargetId(String ids) {
		List<PrdReportDetail> result=new ArrayList<PrdReportDetail>();

		Assert.isTrue(StringUtils.isEmpty(ids),"操作失败!工单数据异常!");
		PrdWorkOrder prdWorkOrder = prdWorkOrderService.getById(ids);
		List<SysUser>  empList=  sysDepartService.getEmpByOrgCode(prdWorkOrder.getPrdLine());
		double avgUnitPrice=0d;
		if (Constants.DICT_WORK_TYPE.JT.equals(prdWorkOrder.getWorkType())) {

			avgUnitPrice = AmountUtils.div(prdWorkOrder.getWorkUnitPrice(), Double.valueOf(empList.size()), 6);

		}else{

			avgUnitPrice=prdWorkOrder.getWorkUnitPrice();
		}
		for (SysUser sysUser : empList) {
			if (Constants.DICT_WORK_TYPE.JT.equals(prdWorkOrder.getWorkType())){
				PrdReportDetail entity = new PrdReportDetail();
				entity.setEmployeeId(sysUser.getUsername());
				entity.setScore(10d);
				entity.setAvgUnitPrice(avgUnitPrice);

				entity.setAmount(AmountUtils.mul(2,avgUnitPrice,entity.getScore(),prdWorkOrder.getQty()));
				result.add(entity);
			}else{
				PrdReportDetail entity = new PrdReportDetail();
				entity.setAvgUnitPrice(avgUnitPrice);
				entity.setScore(10d);
				entity.setAmount(AmountUtils.mul(2,avgUnitPrice,entity.getScore(),prdWorkOrder.getQty()));
				result.add(entity);
			}


		}





        return result;
    }
}
