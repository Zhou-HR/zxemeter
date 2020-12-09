package com.gd.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 根据此注解来决定持久化对象是否具有单表增删改查的功能
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Crud {

    /**
     * 提供给外部服务的别名，见CrudController
     * 默认为短类名的全小写
     */
    String name() default "";

    /**
     * 数据库表名，不填默认为短类名的驼峰式结果，并且全小写
     */
    String table() default "";

    /**
     * 是否开放删除功能
     */
    boolean delete() default true;

    /**
     * 是否开放插入功能
     */
    boolean insert() default true;

    /**
     * 是否开放删除功能
     */
    boolean update() default true;

    /**
     * 是否开放查询功能
     */
    boolean select() default true;
}
