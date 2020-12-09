package com.gd.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置持久化对象字段与列的映射关系
 *
 * @author ZhouHR
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {

    /**
     * 对应的列名，默认为字段名的驼峰式结果，并且全小写
     */
    String column() default "";
}
