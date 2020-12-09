package com.gd.basic.crud;

import com.gd.model.annotation.Alias;
import com.gd.model.annotation.Crud;
import com.gd.model.annotation.Exclude;
import com.gd.model.annotation.Id;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 表和Java对象的映射关系配置
 * <p>
 * 默认规则为：
 * 表名：类名转换
 * 主键：id
 * 列名：字段名转换
 * <p>
 * 驼峰式转换举例：
 * HelloWorld = hello_world
 *
 * @author ZhouHR
 */
@Log4j2
@Getter
public class TableMapping {

    /**
     * 持久化对象
     */
    private Class cls;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 主键字段名
     */
    private String id;
    /**
     * 字段与数据库列名映射关系
     */
    private Map<String, String> aliasMapping;

    public TableMapping(Class cls) {
        this.cls = cls;
        setTableName();
        setId();
        setAliasMapping();
    }

    private void setTableName() {
        Annotation annotation = cls.getAnnotation(Crud.class);
        if (annotation != null) {
            tableName = getDefaultValue(AnnotationUtils.getValue(annotation, "table"), "t_" + toCamelCase(ClassUtils.getShortClassName(this.cls)));
        } else {
            tableName = "t_" + toCamelCase(ClassUtils.getShortClassName(this.cls));
        }
    }

    private void setId() {
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                id = toCamelCase(field.getName());
                return;
            }
        }

        try {
            cls.getDeclaredField("id");
            this.id = "id";
            log.warn("class:" + cls.getName() + "，未显示指定@Id，默认认为是id");
        } catch (NoSuchFieldException e) {
            log.error(e);
            throw new ExceptionInInitializerError("class:" + cls.getName() + "，未显示指定@Id，也不存在id字段");
        }
    }

    private void setAliasMapping() {
        Field[] fields = cls.getDeclaredFields();
        aliasMapping = new HashMap<>(fields.length);

        for (Field field : fields) {
            Alias annotation = field.getAnnotation(Alias.class);
            if (field.getAnnotation(Exclude.class) != null) {
                continue;
            }
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            aliasMapping.put(field.getName(), getDefaultValue(AnnotationUtils.getValue(annotation, "column"), toCamelCase(field.getName())));
        }
    }

    private String getDefaultValue(Object value, String defaultVal) {
        if (value == null || "".equals(value)) {
            return defaultVal;
        } else {
            return String.valueOf(value);
        }
    }

    /**
     * 转换成驼峰式字符串
     *
     * @param from 需转换字符
     * @return 转换后字符
     */
    protected String toCamelCase(String from) {
        if (StringUtils.isAllLowerCase(from)) {
            return from;
        }
        return from.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public String toString() {
        return "Class:" + cls.getName()
                + "\r\ntable:" + tableName + ", id:" + id
                + "\r\nmapping:" + aliasMapping;
    }
}
