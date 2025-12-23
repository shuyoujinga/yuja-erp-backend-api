package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.constant.Constants;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.maindata.materials.entity.YujiakejiMaterials;
import org.jeecg.modules.maindata.materials.mapper.YujiakejiMaterialsMapper;
import org.jeecg.modules.maindata.materials.service.IYujiakejiMaterialsService;
import org.jeecg.modules.system.mapper.SysCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.utils.CommonMethodUtils;

@Component
@Slf4j
public class MaterialsCodeRule implements IFillRuleHandler {

    @Autowired
    private IYujiakejiMaterialsService yujiakejiMaterialsService;

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        log.info("编码信息：{}",params,"JSON入参：{}",formData);

        // Step1 获取分类编码
        String materialCategory = formData.getString("materialCategory");
        YujiakejiMaterialsMapper baseMapper = (YujiakejiMaterialsMapper) SpringContextUtils.getBean("yujiakejiMaterialsMapper");
        YujiakejiMaterials entity = baseMapper.selectOne(new QueryWrapper<YujiakejiMaterials>().lambda().like(YujiakejiMaterials::getMaterialCategory,materialCategory).orderByDesc(YujiakejiMaterials::getMaterialCode).last(Constants.CONST_SQL.LIMIT_ONE));
        String materialCode = CommonMethodUtils.convertMaterialCode(materialCategory);
        if (ObjectUtils.isEmpty(entity)) {
            return materialCode+"0001";
        }
        return CommonMethodUtils.incrementCode(entity.getMaterialCode());
    }
}
