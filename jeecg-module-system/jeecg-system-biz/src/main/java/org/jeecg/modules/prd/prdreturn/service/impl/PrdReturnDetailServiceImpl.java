package org.jeecg.modules.prd.prdreturn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.jeecg.modules.prd.prdreturn.entity.PrdReturnDetail;
import org.jeecg.modules.prd.prdreturn.mapper.PrdReturnDetailMapper;
import org.jeecg.modules.prd.prdreturn.service.IPrdReturnDetailService;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 生产退料_明细
 * @Author: 舒有敬
 * @Date:   2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdReturnDetailServiceImpl extends ServiceImpl<PrdReturnDetailMapper, PrdReturnDetail> implements IPrdReturnDetailService {
	
	@Resource
	private PrdReturnDetailMapper prdReturnDetailMapper;
	@Autowired
	private IPrdWorkOrderDetailService prdWorkOrderDetailService;
	
	@Override
	public List<PrdReturnDetail> selectByMainId(String mainId) {
		return prdReturnDetailMapper.selectByMainId(mainId);
	}

    @Override
    public List<PrdReturnDetail> selectByTargetId(String ids) {
		List<PrdReturnDetail> result = new ArrayList<PrdReturnDetail>();

		// 获取退料信息
		Assert.isTrue(StringUtils.isEmpty(ids),"操作失败!传入工单明细不存在!");
		List<String> list = Arrays.asList(ids.split(","));
		Assert.isTrue(CollectionUtil.isEmpty(list),"操作失败!传入工单明细不存在!");
		List<PrdWorkOrderDetail> detailList = prdWorkOrderDetailService.listByIds(list);
		for (PrdWorkOrderDetail prdWorkOrderDetail : detailList) {
			PrdReturnDetail prdReturnDetail = new PrdReturnDetail();
			BeanUtils.copyProperties(prdWorkOrderDetail, prdReturnDetail);
			prdReturnDetail.setId(null);
			prdReturnDetail.setIssueQty(prdWorkOrderDetail.getQty());
			prdReturnDetail.setQty(null);
			result.add(prdReturnDetail);
		}

        return result;
    }
}
