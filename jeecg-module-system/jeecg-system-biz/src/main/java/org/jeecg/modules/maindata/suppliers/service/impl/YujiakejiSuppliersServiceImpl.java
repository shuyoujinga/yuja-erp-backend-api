package org.jeecg.modules.maindata.suppliers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.constant.Constants;
import org.jeecg.modules.maindata.suppliers.entity.YujiakejiSuppliers;
import org.jeecg.modules.maindata.suppliers.mapper.YujiakejiSuppliersMapper;
import org.jeecg.modules.maindata.suppliers.service.IYujiakejiSuppliersService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.utils.Assert;
import org.utils.CommonMethodUtils;

import static org.constant.TipsMessage.SUPPLIERS_TYPE_ERROR;

/**
 * @Description: 供应商管理
 * @Author: 舒有敬
 * @Date: 2024-06-02
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class YujiakejiSuppliersServiceImpl extends ServiceImpl<YujiakejiSuppliersMapper, YujiakejiSuppliers> implements IYujiakejiSuppliersService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(YujiakejiSuppliers yujiakejiSuppliers) {
        /**
         * 数据校验
         */
        String category = yujiakejiSuppliers.getCategory();
        Assert.isTrue(StringUtils.isEmpty(category), SUPPLIERS_TYPE_ERROR.getMessage());
        YujiakejiSuppliers entity = baseMapper.selectOne(new LambdaQueryWrapper<YujiakejiSuppliers>().eq(YujiakejiSuppliers::getCategory, category).orderByDesc(YujiakejiSuppliers::getCode).last(Constants.CONST_SQL.LIMIT_ONE).select(YujiakejiSuppliers::getCode));
        yujiakejiSuppliers.setCode(ObjectUtils.isEmpty(entity) ? category + "0001" : CommonMethodUtils.incrementCode(entity.getCode()));
        baseMapper.insert(yujiakejiSuppliers);
    }
}
