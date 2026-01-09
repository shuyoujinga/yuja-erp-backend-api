package org.jeecg.modules.prd.prdissue.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.constant.Constants;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.jeecg.modules.prd.prdissue.entity.PrdIssueDetail;
import org.jeecg.modules.prd.prdissue.mapper.PrdIssueDetailMapper;
import org.jeecg.modules.prd.prdissue.service.IPrdIssueDetailService;
import org.jeecg.modules.prd.prdworkorder.entity.PrdWorkOrderDetail;
import org.jeecg.modules.prd.prdworkorder.service.IPrdWorkOrderDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.utils.Assert;

import javax.annotation.Resource;

/**
 * @Description: 生产领料_明细
 * @Author: 舒有敬
 * @Date: 2025-12-25
 * @Version: V1.0
 */
@Service
public class PrdIssueDetailServiceImpl extends ServiceImpl<PrdIssueDetailMapper, PrdIssueDetail> implements IPrdIssueDetailService {

    @Resource
    private PrdIssueDetailMapper prdIssueDetailMapper;
    @Autowired
    private IPrdWorkOrderDetailService prdWorkOrderDetailService;
    @Autowired
    private IInvStockService invStockService;

    @Override
    public List<PrdIssueDetail> selectByMainId(String mainId) {
        return prdIssueDetailMapper.selectByMainId(mainId);
    }

    @Override
    public List<PrdIssueDetail> selectByTargetId(String ids) {
        List<PrdIssueDetail> result = new ArrayList<PrdIssueDetail>();

        Assert.isTrue(StringUtils.isEmpty(ids),"操作失败!传入工单明细不存在!");
        List<String> list = Arrays.asList(ids.split(","));
        Assert.isTrue(CollectionUtil.isEmpty(list),"操作失败!传入工单明细不存在!");
        List<PrdWorkOrderDetail> detailList = prdWorkOrderDetailService.listByIds(list);
        List<String> materialList = detailList.stream().map(PrdWorkOrderDetail::getMaterialCode).collect(Collectors.toList());


        List<InvStock> invStockList = invStockService.list(new LambdaQueryWrapper<InvStock>().in(InvStock::getMaterialCode, materialList).in(InvStock::getWarehouseCode, Constants.WAREHOUSE_LIST.WAREHOUSE_LIST).eq(InvStock::getDelFlag, Constants.YN.Y));
        Assert.isTrue(CollectionUtil.isEmpty(invStockList),String.format("操作失败!物料[%s]不存在库存!",materialList.toString()));
        Map<String, InvStock> stockMap = invStockList.stream().collect(Collectors.toMap(InvStock::getMaterialCode, v -> v));
        for (PrdWorkOrderDetail prdWorkOrderDetail : detailList) {
            PrdIssueDetail prdIssueDetail = new PrdIssueDetail();
            BeanUtils.copyProperties(prdWorkOrderDetail, prdIssueDetail);
            prdIssueDetail.setWordOrderDetailId(prdWorkOrderDetail.getId());
            InvStock invStock = stockMap.get(prdWorkOrderDetail.getMaterialCode());
            if (!ObjectUtils.isEmpty(invStock)) {
                prdIssueDetail.setWarehouseCode(invStock.getWarehouseCode());
                prdIssueDetail.setStockQty(invStock.getStockQty());
            }
            prdIssueDetail.setId(null);
            result.add(prdIssueDetail);
        }

        return result;
    }
}
