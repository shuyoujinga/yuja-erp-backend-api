package org.jeecg.modules.inv.invmiscin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucher;
import org.jeecg.modules.inv.invmaterialvoucher.entity.InvMaterialVoucherDetail;
import org.jeecg.modules.inv.invmaterialvoucher.service.IInvMaterialVoucherCustomService;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscIn;
import org.jeecg.modules.inv.invmiscin.entity.InvMiscInDetail;
import org.jeecg.modules.inv.invmiscin.mapper.InvMiscInDetailMapper;
import org.jeecg.modules.inv.invmiscin.mapper.InvMiscInMapper;
import org.jeecg.modules.inv.invmiscin.service.IInvMiscInService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.utils.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 其他入库
 * @Author: 舒有敬
 * @Date: 2025-12-10
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvMiscInServiceImpl extends ServiceImpl<InvMiscInMapper, InvMiscIn> implements IInvMiscInService {

    @Resource
    private InvMiscInMapper invMiscInMapper;
    @Resource
    private InvMiscInDetailMapper invMiscInDetailMapper;
    @Autowired
    private SerialNumberService serialNumberService;
    @Autowired
    private IInvMaterialVoucherCustomService iInvMaterialVoucherCustomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(InvMiscIn invMiscIn, List<InvMiscInDetail> invMiscInDetailList) {
        invMiscIn.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.QTRK));
        invMiscInMapper.insert(invMiscIn);
        if (invMiscInDetailList != null && invMiscInDetailList.size() > 0) {
            for (InvMiscInDetail entity : invMiscInDetailList) {
                //外键设置
                entity.setPid(invMiscIn.getId());
                invMiscInDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(InvMiscIn invMiscIn, List<InvMiscInDetail> invMiscInDetailList) {
        invMiscInMapper.updateById(invMiscIn);

        //1.先删除子表数据
        invMiscInDetailMapper.deleteByMainId(invMiscIn.getId());

        //2.子表数据重新插入
        if (invMiscInDetailList != null && invMiscInDetailList.size() > 0) {
            for (InvMiscInDetail entity : invMiscInDetailList) {
                //外键设置
                entity.setPid(invMiscIn.getId());
                invMiscInDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        invMiscInDetailMapper.deleteByMainId(id);
        invMiscInMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            invMiscInDetailMapper.deleteByMainId(id.toString());
            invMiscInMapper.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(List<String> ids) throws Exception {

        // 生成物料凭证
        List<InvMiscIn> invMiscIns = listByIds(ids);

        Assert.isTrue(CollectionUtil.isEmpty(invMiscIns), "其他入库单审核失败,选择数据不存在!");

        for (InvMiscIn invMiscIn : invMiscIns) {
            // 审核的数据就不需要生成物料凭证
            if (Constants.DICT_AUDIT_STATUS.YES.equals(invMiscIn.getAudit())) {
                continue;
            }

            List<InvMiscInDetail> invMiscInDetails = invMiscInDetailMapper.selectByMainId(invMiscIn.getId());
            Assert.isTrue(CollectionUtil.isEmpty(invMiscInDetails), String.format("其他入库单审核失败,[%s]不存在明细!", invMiscIn.getDocCode()));

            InvMaterialVoucher invMaterialVoucher = new InvMaterialVoucher(invMiscIn.getInType(), invMiscIn.getDocCode(), invMiscIn.getId(), invMiscIn.getRemark());

            List<InvMaterialVoucherDetail> detailList = new ArrayList<>();
            for (InvMiscInDetail invMiscInDetail : invMiscInDetails) {
                InvMaterialVoucherDetail entity = new InvMaterialVoucherDetail();

                entity.setSourceDocDetailId(invMiscInDetail.getId());
                entity.setMaterialCode(invMiscInDetail.getMaterialCode());
                entity.setUnit(invMiscInDetail.getUnit());
                entity.setSpecifications(invMiscInDetail.getSpecifications());
                entity.setQty(invMiscInDetail.getQty());

                entity.setMoveType(invMiscIn.getInType());
                entity.setWarehouseCode(invMiscIn.getWarehouseCode());

                detailList.add(entity);


            }
            iInvMaterialVoucherCustomService.createVoucher(invMaterialVoucher, detailList);


        }


        return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int unAudit(List<String> ids) throws Exception {
        // 生成物料凭证
        List<InvMiscIn> invMiscIns = listByIds(ids);
        Assert.isTrue(CollectionUtil.isEmpty(invMiscIns), "其他入库单反审核失败,选择数据不存在!");

        for (InvMiscIn invMiscIn : invMiscIns) {
            // 反审核的数据就不需要生成物料凭证
            if (Constants.DICT_AUDIT_STATUS.NO.equals(invMiscIn.getAudit())) {
                continue;
            }
            String voucherIdBySourceDocId = iInvMaterialVoucherCustomService.getVoucherIdBySourceDocId(invMiscIn.getId());
            if (StringUtils.isEmpty(voucherIdBySourceDocId)) {
                continue;
            }
            iInvMaterialVoucherCustomService.reversalVoucher(voucherIdBySourceDocId);

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

        List<InvMiscIn> records = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(records)) {
            return 0;
        }
        int count = 0;
        for (InvMiscIn record : records) {
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
