package org.jeecg.modules.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class DeleteAuditAspect {

    @Resource
    private ApplicationContext applicationContext; // Spring 上下文，用于获取 Service Bean

    @Pointcut("@annotation(deleteCheckAudit)")
    public void deleteCheckPointcut(DeleteCheckAudit deleteCheckAudit) {
    }

    @Before("deleteCheckPointcut(deleteCheckAudit) && args(ids,..)")
    public void beforeDelete(JoinPoint joinPoint, DeleteCheckAudit deleteCheckAudit, String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));

        // 获取 Service Bean
        Object serviceBean = applicationContext.getBeansOfType(deleteCheckAudit.service())
                .values().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到指定 Service Bean"));

        Class<?> serviceClass = serviceBean.getClass().getSuperclass();
        Method getByIdMethod = serviceClass.getMethod("getById", Serializable.class);

        Class<?> entityClass = deleteCheckAudit.entity();
        Field auditField = entityClass.getDeclaredField("audit");
        auditField.setAccessible(true);

        for (String idStr : idList) {
            // 根据实体主键类型转换 id
            Serializable id;
            Field idField = entityClass.getDeclaredField("id"); // 假设主键字段叫 id
            Class<?> idType = idField.getType();
            if (idType == Long.class) {
                id = Long.valueOf(idStr);
            } else if (idType == Integer.class) {
                id = Integer.valueOf(idStr);
            } else {
                id = idStr;
            }

            Object entity = getByIdMethod.invoke(serviceBean, id);
            if (entity == null) {
                throw new RuntimeException("单据不存在，id=" + idStr);
            }

            Object auditValue = auditField.get(entity);
            if ("1".equals(String.valueOf(auditValue))) {
                throw new RuntimeException("操作失败：存在已审核单据，不允许删除！");
            }
        }
    }
}
