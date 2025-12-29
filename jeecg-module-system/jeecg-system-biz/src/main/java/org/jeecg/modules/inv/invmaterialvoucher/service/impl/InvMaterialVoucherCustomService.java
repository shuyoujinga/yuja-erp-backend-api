package org.jeecg.modules.inv.invmaterialvoucher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherDetailService;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherService;
import org.jeecg.modules.inv.invmovetype.entity.InvMoveType;
import org.jeecg.modules.inv.invmovetype.service.IInvMoveTypeService;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.utils.Assert;
import org.utils.MathUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class InvMaterialVoucherCustomService implements IInvMaterialVoucherCustomService {

    @Autowired
    private IInvMaterialVoucherService invMaterialVoucherService;
    @Autowired
    private IInvMaterialVoucherDetailService invMaterialVoucherDetailService;
    @Autowired
    private IInvStockService invStockService;
    @Autowired
    private IInvMoveTypeService iInvMoveTypeService;

    // =============================================
    // 新增物料凭证
    // =============================================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<T> createVoucher(InvMaterialVoucher voucher, List<InvMaterialVoucherDetail> details) throws Exception {

        log.info("开始创建物料凭证，sourceDocCode={}，detailCount={}",
                voucher.getSourceDocCode(), details == null ? 0 : details.size());
        List<InvMoveType> moveTypeList = iInvMoveTypeService.list();
        Assert.isTrue(CollectionUtils.isEmpty(moveTypeList),"操作失败!移动类型配置表为空!");
        Map<String, InvMoveType> moveTypeMap = moveTypeList.stream().collect(Collectors.toMap(InvMoveType::getMoveType, v -> v));
        preCheckInfo(moveTypeMap,voucher,details);

        validateVoucher(voucher, details);
        preprocessDetails(details);

        // 提前获取所有物料编码
        List<String> materialCodes = details.stream()
                .map(InvMaterialVoucherDetail::getMaterialCode)
                .distinct()
                .collect(Collectors.toList());

        List<String> wareHouseCodes = details.stream()
                .map(InvMaterialVoucherDetail::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());

        List<InvStock> stockList = invStockService.list(
                new LambdaQueryWrapper<InvStock>().in(InvStock::getWarehouseCode,wareHouseCodes).in(InvStock::getMaterialCode, materialCodes)
        );

        // 执行库存操作
        applyStockChange(details, stockList);

        // 保存凭证
        invMaterialVoucherService.saveMain(voucher, details);
        return Result.OK("新增成功");
    }

    private void preCheckInfo(Map<String, InvMoveType> moveTypeMap, InvMaterialVoucher voucher, List<InvMaterialVoucherDetail> details) {
        String moveType = voucher.getMoveType();
        InvMoveType invMoveType = moveTypeMap.get(moveType);
        Assert.isTrue(ObjectUtils.isEmpty(invMoveType),String.format("移动类型%s不存在!",moveType));
        if (StringUtils.isEmpty(voucher.getBizType())) {
            voucher.setBizType(invMoveType.getBizType());
        }
        if (StringUtils.isEmpty(voucher.getSourceDocType())) {
            voucher.setSourceDocType(invMoveType.getSourceDocType());
        }
        if (StringUtils.isEmpty(voucher.getOrgCode())) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            voucher.setOrgCode(sysUser.getOrgCode());
        }
        for (InvMaterialVoucherDetail detail : details) {
            if (StringUtils.isEmpty(detail.getStockType())) {
                detail.setStockType(invMoveType.getDirection());
            }
        }
    }

    // =============================================
    // 冲销物料凭证
    // =============================================
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<T> reversalVoucher(String voucherId) throws Exception {

        log.info("开始冲销物料凭证 voucherId={}", voucherId);

        InvMaterialVoucher original = invMaterialVoucherService.getById(voucherId);
        Assert.notNull(original, "原凭证不存在");

        List<InvMaterialVoucherDetail> oldDetails =
                invMaterialVoucherDetailService.selectByMainId(voucherId);

        List<InvMaterialVoucherDetail> reversalDetails = buildReversalDetails(oldDetails);

        InvMaterialVoucher reversal = buildReversalVoucher(original);
        reversal.setReversalDocId(original.getId());
        reversal.setIsReversal(Constants.YN.Y);

        // 取库存
        List<String> materialCodes = reversalDetails.stream()
                .map(InvMaterialVoucherDetail::getMaterialCode)
                .distinct()
                .collect(Collectors.toList());
        List<String> wareHouseCodes = reversalDetails.stream()
                .map(InvMaterialVoucherDetail::getWarehouseCode)
                .distinct()
                .collect(Collectors.toList());

        List<InvStock> stockList = invStockService.list(
                new LambdaQueryWrapper<InvStock>().in(InvStock::getWarehouseCode,wareHouseCodes).in(InvStock::getMaterialCode, materialCodes)
        );

        // 库存操作（负逻辑）
        applyStockChange(reversalDetails, stockList);

        // 保存冲销凭证
        invMaterialVoucherService.saveMain(reversal, reversalDetails);

        // 更新原凭证
        original.setIsReversal(Constants.YN.Y);
        invMaterialVoucherService.updateById(original);

        return Result.OK("冲销成功");
    }

    @Override
    public String getVoucherIdBySourceDocId(String sourceDocId) {
        InvMaterialVoucher entity = invMaterialVoucherService.getOne(new LambdaQueryWrapper<InvMaterialVoucher>().eq(InvMaterialVoucher::getSourceDocId, sourceDocId).eq(InvMaterialVoucher::getIsReversal, Constants.YN.N).eq(InvMaterialVoucher::getDelFlag, Constants.YN.Y).last(Constants.CONST_SQL.LIMIT_ONE));
        if (ObjectUtils.isEmpty(entity)) {
            return "";
        }
        return entity.getId();
    }

    // =============================================
    // 校验与预处理
    // =============================================
    private void validateVoucher(InvMaterialVoucher voucher, List<InvMaterialVoucherDetail> details) {
        Assert.notNull(voucher.getBizType(), "业务类型不能为空");
        Assert.notNull(voucher.getSourceDocType(), "来源单据类型不能为空");
        Assert.hasText(voucher.getSourceDocCode(), "来源单据号不能为空");
        Assert.hasText(voucher.getSourceDocId(), "来源单据ID不能为空");
        Assert.hasText(voucher.getMoveType(), "主移动类型不能为空");
        Assert.notEmpty(details, "明细不能为空");
    }

    private void preprocessDetails(List<InvMaterialVoucherDetail> details) {
        int line = 1;
        for (InvMaterialVoucherDetail d : details) {
            Assert.hasText(d.getMaterialCode(), "物料编码不能为空");
            Assert.hasText(d.getWarehouseCode(), "仓库不能为空");
            Assert.notNull(d.getQty(), "数量不能为空");
            Assert.hasText(d.getMoveType(), "移动类型不能为空");
            Assert.notNull(d.getStockType(), "出入库类型不能为空");

            d.setLineNo(line++);

            if (d.getPrice() != null) {
                d.setAmount(
                        MathUtils.multiply(d.getQty(), d.getPrice()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue()
                );
            }
        }
    }

    // =============================================
    // 库存新增 / 扣减
    // =============================================
    private void applyStockChange(List<InvMaterialVoucherDetail> details, List<InvStock> stockList) {

        Map<String, InvStock> stockMap = stockList.stream()
                .collect(Collectors.toMap(InvStock::getMaterialCode, s -> s));

        for (InvMaterialVoucherDetail d : details) {

            String material = d.getMaterialCode();
            Integer stockType = d.getStockType();   // 1 = 入库，-1 = 出库
            BigDecimal qty = MathUtils.toBD(d.getQty());

            InvStock stock = stockMap.get(material);

            // =============================
            // 情况1：库存不存在 => 只能做入库
            // =============================
            if (stock == null) {
                if (stockType == -1) {
                    throw new RuntimeException("物料【" + material + "】无库存，无法出库");
                }

                stock = new InvStock();
                stock.setMaterialCode(material);
                stock.setWarehouseCode(d.getWarehouseCode());
                stock.setStockQty(qty.doubleValue());
                stock.setTotalQty(qty.doubleValue());

                log.info("新增库存 material={} qty={}", material, qty);
                invStockService.save(stock);
                continue;
            }

            // ============================
            // 情况2：库存存在 => 更新库存
            // ============================
            BigDecimal newQty = MathUtils.add(stock.getStockQty(), MathUtils.multiply(qty, stockType));

            if (MathUtils.lt(newQty, 0)) {
                throw new RuntimeException("物料【" + material + "】库存不足，无法扣减");
            }

            stock.setStockQty(newQty.doubleValue());
            stock.setTotalQty(newQty.doubleValue());

            log.info("更新库存 material={} oldQty={} change={} newQty={}",
                    material, stock.getStockQty(), qty.multiply(BigDecimal.valueOf(stockType)), newQty);

            invStockService.updateById(stock);
        }
    }

    // =============================================
    // 构建冲销明细
    // =============================================
    private List<InvMaterialVoucherDetail> buildReversalDetails(List<InvMaterialVoucherDetail> source) {

        List<InvMaterialVoucherDetail> list = new ArrayList<>();
        int line = 1;

        for (InvMaterialVoucherDetail d : source) {

            InvMaterialVoucherDetail n = new InvMaterialVoucherDetail();
            n.setLineNo(line++);
            n.setMaterialCode(d.getMaterialCode());
            n.setMaterialName(d.getMaterialName());
            n.setUnit(d.getUnit());
            n.setSpecifications(d.getSpecifications());
            n.setWarehouseCode(d.getWarehouseCode());
            n.setLocationCode(d.getLocationCode());
            n.setQty(d.getQty());
            n.setPrice(d.getPrice());

            if (d.getPrice() != null) {
                n.setAmount(
                        MathUtils.multiply(d.getQty(), d.getPrice()).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue()
                );
            }

            n.setMoveType(swapMoveType(d.getMoveType()));
            n.setStockType(d.getStockType() == 1 ? -1 : 1);

            list.add(n);
        }
        return list;
    }

    // =============================================
    // 构建冲销主表
    // =============================================
    private InvMaterialVoucher buildReversalVoucher(InvMaterialVoucher o) {
        InvMaterialVoucher r = new InvMaterialVoucher();
        r.setBizType(o.getBizType());
        r.setSourceDocType(o.getSourceDocType());
        r.setSourceDocCode(o.getSourceDocCode());
        r.setSourceDocId(o.getSourceDocId());
        r.setMoveType(swapMoveType(o.getMoveType()));
        return r;
    }

    // 类型尾数互换：1 ↔ 2
    private String swapMoveType(String moveType) {
        if (moveType == null || moveType.isEmpty()) return moveType;
        char c = moveType.charAt(moveType.length() - 1);
        return (c == '1')
                ? moveType.substring(0, moveType.length() - 1) + "2"
                : (c == '2')
                ? moveType.substring(0, moveType.length() - 1) + "1"
                : moveType;
    }
}
