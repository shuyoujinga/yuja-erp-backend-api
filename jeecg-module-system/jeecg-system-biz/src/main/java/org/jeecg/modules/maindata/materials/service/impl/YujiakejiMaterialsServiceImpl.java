package org.jeecg.modules.maindata.materials.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.constant.TipsMessage;
import org.jeecg.common.constant.FillRuleConstant;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.mapper.YujiakejiMaterialsMapper;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.service.ISysCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.utils.Assert;


/**
 * @Description: 物料主数据
 * @Author: 舒有敬
 * @Date: 2024-06-08
 * @Version: V1.0
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class YujiakejiMaterialsServiceImpl extends ServiceImpl<YujiakejiMaterialsMapper, YujiakejiMaterials> implements IYujiakejiMaterialsService {
    @Autowired
    private ISysCategoryService sysCategoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEntity(YujiakejiMaterials yujiakejiMaterials) {
        /**
         * 校验入参
         */
        SysCategory category = sysCategoryService.getById(yujiakejiMaterials.getMaterialCategory());
        Assert.isTrue(ObjectUtils.isArray(category), TipsMessage.METERIALS_CODE_ERROR.getMessage());
        Assert.isTrue(category.getCode().length() != 6, TipsMessage.METERIALS_CODE_LENGTH_ERROR.getMessage());

        yujiakejiMaterials.setMaterialCategory(category.getCode());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("materialCategory", yujiakejiMaterials.getMaterialCategory());
        String materialCode = FillRuleUtil.executeRule(FillRuleConstant.MATERIAL, jsonObject).toString();
        yujiakejiMaterials.setMaterialCode(materialCode);
        baseMapper.insert(yujiakejiMaterials);
    }

    @Override
    public YujiakejiMaterials queryByMaterialCode(String materialCode, String warehouseCode) {
        return baseMapper.queryByMaterialCode(materialCode,warehouseCode);
    }

    @Override
    public YujiakejiMaterials queryByMaterialCodeInSale(String materialCode) {
        return  baseMapper.queryByMaterialCodeInSale(materialCode);
    }
}
