package org.jeecg.modules.inv.invstock.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 实时库存
 * @Author: 舒有敬
 * @Date:   2025-12-02
 * @Version: V1.0
 */
public interface IInvStockService extends IService<InvStock> {

    IPage<InvStock> queryPageWithMaterial(Page<InvStock> page, InvStock invStock, Map<String, String[]> parameterMap);

    List<InvStock> queryListWithMaterial(Map<String, String[]> parameterMap);


    void addStock(String warehouseCode, String materialCode, Double qty);

    void subStock(String warehouseCode, String materialCode, Double qty);
}
