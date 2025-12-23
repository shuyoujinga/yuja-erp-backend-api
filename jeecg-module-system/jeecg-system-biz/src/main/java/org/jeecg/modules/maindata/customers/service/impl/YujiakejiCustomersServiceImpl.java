package org.jeecg.modules.maindata.customers.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.constant.Constants;
import org.constant.TipsMessage;
import org.jeecg.modules.maindata.customers.entity.YujiakejiCustomers;
import org.jeecg.modules.maindata.customers.mapper.YujiakejiCustomersMapper;
import org.jeecg.modules.maindata.customers.service.IYujiakejiCustomersService;
import org.jeecg.modules.maindata.suppliers.entity.YujiakejiSuppliers;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.utils.Assert;
import org.utils.CommonMethodUtils;

/**
 * @Description: 客户管理
 * @Author: 舒有敬
 * @Date:   2024-06-08
 * @Version: V1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class YujiakejiCustomersServiceImpl extends ServiceImpl<YujiakejiCustomersMapper, YujiakejiCustomers> implements IYujiakejiCustomersService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(YujiakejiCustomers yujiakejiCustomers) {
        /**
         * 数据校验
         */
        String category = yujiakejiCustomers.getCategory();
        Assert.isTrue(StringUtils.isEmpty(category), TipsMessage.CUSTOMERS_TYPE_ERROR.getMessage());
        YujiakejiCustomers entity = baseMapper.selectOne(new LambdaQueryWrapper<YujiakejiCustomers>().eq(YujiakejiCustomers::getCategory, category).orderByDesc(YujiakejiCustomers::getCode).last(Constants.CONST_SQL.LIMIT_ONE).select(YujiakejiCustomers::getCode));
        yujiakejiCustomers.setCode(ObjectUtils.isEmpty(entity) ?   "A0001" : CommonMethodUtils.incrementCode(entity.getCode()));
        baseMapper.insert(yujiakejiCustomers);
    }
}
