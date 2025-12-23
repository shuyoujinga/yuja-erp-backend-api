package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.mapper.SysCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author scott
 * @Date 2019/12/9 11:32
 * @Description: 分类字典编码生成规则
 */
@Slf4j
@Component
public class CategoryCodeRule implements IFillRuleHandler {

    public static final String ROOT_PID_VALUE = "0";

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        log.info("系统自定义编码规则[category_code_rule]，params：{} ，formData： {}", params, formData);

        String categoryPid = ROOT_PID_VALUE;
        String categoryCode = null;

        // 获取父节点ID，优先从formData获取，其次从params获取
        if (formData != null && formData.size() > 0) {
            Object obj = formData.get("pid");
            if (oConvertUtils.isNotEmpty(obj)) {
                categoryPid = obj.toString();
            }
        } else if (params != null) {
            Object obj = params.get("pid");
            if (oConvertUtils.isNotEmpty(obj)) {
                categoryPid = obj.toString();
            }
        }

        /*
         * 根据父节点ID确定新的编码，分为三种情况：
         * 1. 数据库无数据，调用 YouBianCodeUtil.getNextYouBianCode(null);
         * 2. 添加子节点，无兄弟元素，调用 YouBianCodeUtil.getSubYouBianCode(parentCode, null);
         * 3. 添加子节点，有兄弟元素，调用 YouBianCodeUtil.getNextYouBianCode(lastCode);
         */
        // 获取同类节点中的最大编码值
        SysCategoryMapper baseMapper = (SysCategoryMapper) SpringContextUtils.getBean("sysCategoryMapper");
        Page<SysCategory> page = new Page<>(1, 1);
        List<SysCategory> list = baseMapper.getMaxCategoryCodeByPage(page, categoryPid);

        if (list == null || list.isEmpty()) {
            if (ROOT_PID_VALUE.equals(categoryPid)) {
                // 情况1：无数据时生成新编码
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // 情况2：无兄弟元素时生成子节点编码
                SysCategory parent = baseMapper.selectSysCategoryById(categoryPid);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // 情况3：有兄弟元素时生成新编码
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }

        return categoryCode;
    }

}
