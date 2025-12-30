package org.jeecg.modules.sal.salquote.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.constant.Constants;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.sal.salquote.entity.SalQuote;
import org.jeecg.modules.sal.salquote.entity.SalQuoteDetail;
import org.jeecg.modules.sal.salquote.mapper.SalQuoteDetailMapper;
import org.jeecg.modules.sal.salquote.mapper.SalQuoteMapper;
import org.jeecg.modules.sal.salquote.service.ISalQuoteService;
import org.jeecg.modules.system.service.impl.SerialNumberService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.utils.AmountUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

/**
 * @Description: 销售报价
 * @Author: 舒有敬
 * @Date: 2025-12-08
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SalQuoteServiceImpl extends ServiceImpl<SalQuoteMapper, SalQuote> implements ISalQuoteService {

    @Resource
    private SalQuoteMapper salQuoteMapper;
    @Resource
    private SalQuoteDetailMapper salQuoteDetailMapper;
    @Autowired
    private SerialNumberService serialNumberService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(SalQuote salQuote, List<SalQuoteDetail> salQuoteDetailList) {

        salQuote.setDocCode(serialNumberService.generateRuleCode(Constants.DICT_SERIAL_NUM.XSBJ));
        double totalAmount = AmountUtils.sumTotalAmount(
                salQuoteDetailList,
                d -> Optional.ofNullable(d.getAmount()).orElse(0d)
        );
        double totalTaxAmount = AmountUtils.sumTotalAmount(
                salQuoteDetailList,
                d -> Optional.ofNullable(d.getTaxAmount()).orElse(0d)
        );
        salQuote.setAmount(totalAmount);
        salQuote.setTaxAmount(totalTaxAmount);
        salQuoteMapper.insert(salQuote);
        if (salQuoteDetailList != null && salQuoteDetailList.size() > 0) {
            for (SalQuoteDetail entity : salQuoteDetailList) {
                //外键设置
                entity.setPid(salQuote.getId());
                salQuoteDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(SalQuote salQuote, List<SalQuoteDetail> salQuoteDetailList) {
        double totalAmount = AmountUtils.sumTotalAmount(
                salQuoteDetailList,
                d -> Optional.ofNullable(d.getAmount()).orElse(0d)
        );
        double totalTaxAmount = AmountUtils.sumTotalAmount(
                salQuoteDetailList,
                d -> Optional.ofNullable(d.getTaxAmount()).orElse(0d)
        );
        salQuote.setAmount(totalAmount);
        salQuote.setTaxAmount(totalTaxAmount);
        salQuoteMapper.insert(salQuote);
        salQuoteMapper.updateById(salQuote);

        //1.先删除子表数据
        salQuoteDetailMapper.deleteByMainId(salQuote.getId());

        //2.子表数据重新插入
        if (salQuoteDetailList != null && salQuoteDetailList.size() > 0) {
            for (SalQuoteDetail entity : salQuoteDetailList) {
                //外键设置
                entity.setPid(salQuote.getId());
                salQuoteDetailMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        salQuoteDetailMapper.deleteByMainId(id);
        salQuoteMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            salQuoteDetailMapper.deleteByMainId(id.toString());
            salQuoteMapper.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int audit(List<String> ids) {
        return updateAuditStatus(ids, Constants.DICT_AUDIT_STATUS.YES);
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

        List<SalQuote> records = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(records)) {
            return 0;
        }
        int count = 0;
        for (SalQuote record : records) {
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
