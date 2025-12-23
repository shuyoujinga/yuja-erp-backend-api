package org.jeecg.modules.pur.purreceive.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherService;
import org.jeecg.modules.pur.purapply.entity.PurApply;
import org.jeecg.modules.pur.purapply.service.IPurApplyService;
import org.jeecg.modules.pur.purorder.entity.PurOrder;
import org.jeecg.modules.pur.purorder.service.IPurOrderService;
import org.jeecg.modules.pur.purreceive.entity.PurReceive;
import org.jeecg.modules.pur.purreceive.entity.PurReceiveDetail;
import org.jeecg.modules.pur.purreceive.mapper.PurReceiveDetailMapper;
import org.jeecg.modules.pur.purreceive.mapper.PurReceiveMapper;
import org.jeecg.modules.pur.purreceive.service.IPurReceiveService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @Description: 采购收货
 * @Author: 舒有敬
 * @Date: 2025-11-29
 * @Version: V1.0
 */
@Service
public class PurReceiveServiceImpl extends ServiceImpl<PurReceiveMapper, PurReceive> implements IPurReceiveService {

    @Resource
    private PurReceiveMapper purReceiveMapper;
    @Resource
    private PurReceiveDetailMapper purReceiveDetailMapper;
    @Resource
    private IPurOrderService purOrderService;
    @Autowired
    private SerialNumberService serialNumberService;
    @Resource
    private IInvMaterialVoucherCustomService iInvMaterialVoucherCustomService;
    @Autowired
    private IInvMaterialVoucherService iInvMaterialVoucherService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(PurReceive purReceive, List<PurReceiveDetail> purReceiveDetailList) {
        purReceive.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.CGSH));
        double totalAmount = AmountUtils.sumTotalAmount(purReceiveDetailList,
                d -> Optional.ofNullable(d.getAmount()).orElse(0d));
        purReceive.setAmount(totalAmount);
        purReceiveMapper.insert(purReceive);
        if (purReceiveDetailList != null && purReceiveDetailList.size() > 0) {
            for (PurReceiveDetail entity : purReceiveDetailList) {
                //外键设置
                entity.setPid(purReceive.getId());
                purReceiveDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(PurReceive purReceive, List<PurReceiveDetail> purReceiveDetailList) {
        double totalAmount = AmountUtils.sumTotalAmount(purReceiveDetailList,
                d -> Optional.ofNullable(d.getAmount()).orElse(0d));
        purReceive.setAmount(totalAmount);
        purReceiveMapper.updateById(purReceive);

        //1.先删除子表数据
        purReceiveDetailMapper.deleteByMainId(purReceive.getId());

        //2.子表数据重新插入
        if (purReceiveDetailList != null && purReceiveDetailList.size() > 0) {
            for (PurReceiveDetail entity : purReceiveDetailList) {
                //外键设置
                entity.setPid(purReceive.getId());
                purReceiveDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        purReceiveDetailMapper.deleteByMainId(id);
        purReceiveMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            purReceiveDetailMapper.deleteByMainId(id.toString());
            purReceiveMapper.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(List<String> ids) throws Exception {
        List<PurReceive> purReceives = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(purReceives)) {
            return 0;
        }

        for (PurReceive purReceive : purReceives) {
            // 已经审核。不需要物料凭证！
            if (purReceive.getAudit().equals(Constants.DICT_AUDIT_STATUS.YES)) {
                continue;
            }
            List<PurReceiveDetail> purReceiveDetails = purReceiveDetailMapper.selectByMainId(purReceive.getId());
            Assert.isTrue(CollectionUtil.isEmpty(purReceiveDetails),"审核失败，没有明细行！");

            InvMaterialVoucher entity = new InvMaterialVoucher(Constants.BIZ_TYPE.CGSH, Constants.DICT_SOURCE_DOC_TYPE.CG, purReceive.getDocCode(), purReceive.getId(), Constants.DICT_MOVE_TYPE.CGRK, "", Constants.YN.N, "");

            List<InvMaterialVoucherDetail> detailList=new ArrayList<>();
            for (PurReceiveDetail purReceiveDetail : purReceiveDetails) {
                InvMaterialVoucherDetail detailEntity = new InvMaterialVoucherDetail();
                BeanUtils.copyProperties(purReceiveDetail, detailEntity);
                detailEntity.setId(null);
                detailEntity.setPid(null);
                detailEntity.setSourceDocDetailId(purReceiveDetail.getId());
                detailEntity.setQty(purReceiveDetail.getReceiveQty());
                detailEntity.setPrice(purReceiveDetail.getUnitPrice());
                detailEntity.setAmount(purReceiveDetail.getAmount());
                detailEntity.setMoveType(entity.getMoveType());
                detailEntity.setStockType(Constants.DICT_STOCK_TYPE.IN);
                detailList.add(detailEntity);
            }

            iInvMaterialVoucherCustomService.createVoucher(entity, detailList);


        }

        return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unAudit(List<String> ids) throws Exception {
        List<PurReceive> purReceives = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(purReceives)) {
            return 0;
        }

        for (PurReceive purReceive : purReceives) {
            // 未审核。不需要！
            if (purReceive.getAudit().equals(Constants.DICT_AUDIT_STATUS.NO)) {
                continue;
            }
            List<PurReceiveDetail> purReceiveDetails = purReceiveDetailMapper.selectByMainId(purReceive.getId());
            Assert.isTrue(CollectionUtil.isEmpty(purReceiveDetails),"审核失败，没有明细行！");


            InvMaterialVoucher entity = iInvMaterialVoucherService.getOne(new LambdaQueryWrapper<InvMaterialVoucher>().eq(InvMaterialVoucher::getSourceDocId, purReceive.getId()).eq(InvMaterialVoucher::getIsReversal, Constants.YN.N).eq(InvMaterialVoucher::getDelFlag, Constants.YN.Y).last(Constants.CONST_SQL.LIMIT_ONE));

            Assert.isTrue(ObjectUtils.isEmpty(entity),"反审核失败！没有找到对应物料凭证！");

            iInvMaterialVoucherCustomService.reversalVoucher(entity.getId());


        }



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

        List<PurReceive> records = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(records)) {
            return 0;
        }

        int count = 0;
        for (PurReceive record : records) {

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
