package com.gd.basic.crud;

import com.gd.model.po.Permission;


/**
 * @author ZhouHR
 */
public class MybatisSqlBuilder extends SqlBuilder {

    public MybatisSqlBuilder(TableMapping tableMapping) {
        super(tableMapping);
    }

    @Override
    protected String getParamPattern(String paramName) {
        return "#{" + paramName + "}";
    }

    public static void main(String[] args) {
        System.out.println(new MybatisSqlBuilder(new TableMapping(Permission.class)).insert());
    }
}
