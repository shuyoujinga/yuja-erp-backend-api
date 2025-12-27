package org.jeecg.modules.inv.invstock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.constant.Constants;
import org.jeecg.modules.inv.invstock.entity.InvStock;
import org.jeecg.modules.inv.invstock.mapper.InvStockMapper;
import org.jeecg.modules.inv.invstock.service.IInvStockService;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.utils.AmountUtils;
import org.utils.Assert;

import java.util.List;
import java.util.Map;

/**
 * @Description: 实时库存
 * @Author: 舒有敬
 * @Date: 2025-12-02
 * @Version: V1.0
 */
@Service
public class InvStockServiceImpl extends ServiceImpl<InvStockMapper, InvStock> implements IInvStockService {

    @Autowired
    private IYujiakejiMaterialsService yujiakejiMaterialsService;

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

    @Override
    public void addStock(String warehouseCode, String materialCode, Double qty) {
        InvStock stock = baseMapper.selectByMaterialCode(warehouseCode, materialCode);
        YujiakejiMaterials entity = yujiakejiMaterialsService.getOne(new LambdaQueryWrapper<YujiakejiMaterials>().eq(YujiakejiMaterials::getMaterialCode, materialCode).eq(YujiakejiMaterials::getDelFlag, Constants.YN.Y).last(Constants.CONST_SQL.LIMIT_ONE));
        Assert.isTrue(ObjectUtils.isEmpty(entity),String.format("操作失败!物料[%s]不存在",materialCode));
            // 新增库存
        if (ObjectUtils.isEmpty(stock)) {
            stock= new InvStock();
            stock.setWarehouseCode(warehouseCode);
            // 库区 库位
            stock.setMaterialCode(materialCode);
            stock.setUnit(entity.getUnit());
            stock.setSpecifications(entity.getSpecifications());
            stock.setStockQty(qty);
            stock.setTotalQty(qty);
            stock.setDelFlag(Constants.YN.Y);
            baseMapper.insert(stock);
        }else{
            stock.setStockQty(AmountUtils.add(stock.getStockQty(), qty));
            stock.setTotalQty(AmountUtils.add(stock.getStockQty(), qty));
            baseMapper.updateById(stock);
        }


    }
    @Override
    public void subStock(String warehouseCode, String materialCode, Double qty) {
        InvStock stock = baseMapper.selectByMaterialCode(warehouseCode, materialCode);
        Assert.isTrue(ObjectUtils.isEmpty(stock),String.format("库存扣减失败,物料号[%s]不存在库存!",materialCode));
        Assert.isTrue(AmountUtils.subtract(stock.getStockQty(),qty)<0d,String.format("库存扣减失败,物料号[%s]库存不够扣除!",materialCode));
        stock.setStockQty(AmountUtils.subtract(stock.getStockQty(),qty) );
    }


}
