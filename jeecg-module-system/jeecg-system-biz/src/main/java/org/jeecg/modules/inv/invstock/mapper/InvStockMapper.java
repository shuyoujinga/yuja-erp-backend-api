package org.jeecg.modules.inv.invstock.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 实时库存
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
public interface InvStockMapper extends BaseMapper<InvStock> {

    IPage<InvStock> queryPageWithMaterial(Page<?> page, @Param("paramMap") Map<String, String[]> paramMap);

    List<InvStock> queryListWithMaterial(@Param("paramMap") Map<String, String[]> paramMap);
}
