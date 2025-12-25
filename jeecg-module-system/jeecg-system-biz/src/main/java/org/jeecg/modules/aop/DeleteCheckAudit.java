package org.jeecg.modules.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteCheckAudit {
    Class<?> service(); // 指定具体的业务 Service
    Class<?> entity();   // 对应实体类
}
