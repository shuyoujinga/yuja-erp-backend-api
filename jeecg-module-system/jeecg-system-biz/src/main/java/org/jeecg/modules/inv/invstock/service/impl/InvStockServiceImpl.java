package org.jeecg.modules.inv.invstock.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.mapper.InvStockMapper;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 实时库存
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
@Service
public class InvStockServiceImpl extends ServiceImpl<InvStockMapper, InvStock> implements IInvStockService {

    @Override
    public IPage<InvStock> queryPageWithMaterial(Page<InvStock> page,
                                                 InvStock invStock,
                                                 Map<String, String[]> params) {


        // 调用 Mapper 的自定义分页查询
        return baseMapper.queryPageWithMaterial(page, params);
    }

    @Override
    public List<InvStock> queryListWithMaterial(Map<String, String[]> paramMap) {
        return baseMapper.queryListWithMaterial(paramMap);
    }


}
