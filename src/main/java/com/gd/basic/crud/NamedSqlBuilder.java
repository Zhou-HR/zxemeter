package com.gd.basic.crud;


public class NamedSqlBuilder extends SqlBuilder {

    public NamedSqlBuilder(TableMapping tableMapping) {
        super(tableMapping);
    }

    @Override
    protected String getParamPattern(String paramName) {
        return ":" + paramName;
    }
}
