package com.gd.basic.crud;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class SqlBuilder {

    private TableMapping mapping;

    public SqlBuilder(TableMapping tableMapping) {
        this.mapping = tableMapping;
    }

    public String select() {
        String[] columns = getSelectColumns();
        return joinWhereWithId(new SQL().SELECT(columns).FROM(mapping.getTableName())).toString();
    }

    public String count(List<FilterRule> filterRules) {
        SQL sql = new SQL().SELECT("count(1)").FROM(mapping.getTableName());
        if (filterRules != null) {
            for (FilterRule rule : filterRules) {
                sql.WHERE(mapping.getAliasMapping().get(rule.getField()) + rule.getOp().getExpression() + getParamPattern(rule.getField()));
            }
        }
        return sql.toString();
    }

    /**
     * 生成带条件的sql
     *
     * @param queryInfo，条件集合；null，代表无任何条件
     * @return sql语句
     */
    public String selectWithOthers(QueryInfo queryInfo) {
        String[] columns = getSelectColumns();
        SQL sql = new SQL().SELECT(columns).FROM(mapping.getTableName());

        List<FilterRule> filterRules = queryInfo.getRules();
        Map<String,Integer> mapCount=new HashMap<String,Integer>();
        if (filterRules != null) {
            for (FilterRule rule : filterRules) {
            	if(rule!=null){
            		//如果有多个相同的字段查询，后面的字段参数加个数字，从1开始
            		Integer count=mapCount.get(rule.getField());
            		if(count==null){
            			mapCount.put(rule.getField(), 1);
            			sql.WHERE(mapping.getAliasMapping().get(rule.getField()) + rule.getOp().getExpression() + getParamPattern(rule.getField()));
            		}
            		else{
            			count++;
            			mapCount.put(rule.getField(), count);
            			sql.WHERE(mapping.getAliasMapping().get(rule.getField()) + rule.getOp().getExpression() + getParamPattern(rule.getField()+(count-1)));
            		}
//            		sql.WHERE(mapping.getAliasMapping().get(rule.getField()) + rule.getOp().getExpression() + getParamPattern(rule.getField()));
            	}
            }
        }

        StringBuilder s = new StringBuilder(sql.toString());

        // 拼接排序
        String[] sorts = StringUtils.split(queryInfo.getSort(), ",");
        if (ArrayUtils.isNotEmpty(sorts)) {
            s.append(" order by ");

            String[] orders = StringUtils.split(queryInfo.getOrder(), ",");
            int len = orders == null ? 0 : orders.length;

            for (int i = 0; i < sorts.length; i++) {
                s.append(mapping.getAliasMapping().get(sorts[i])).append(" ").append(i >= len ? "asc" : orders[i]).append(",");
            }
            s.deleteCharAt(s.length() - 1);
        }

        // 拼接分页，只有当rows大于0的时候才认为是分页
        if (queryInfo.getRows() > 0) {
            //s.append(" limit ").append((queryInfo.getPage() - 1) * queryInfo.getRows()).append(",").append(queryInfo.getRows());
        	//oracle分页
            StringBuilder sbOracle = new StringBuilder("select * from(select r.* ,ROWNUM  rn from( ");
            sbOracle.append(s);
            sbOracle.append(" ) r  where ROWNUM<=");
            sbOracle.append((queryInfo.getPage()) * queryInfo.getRows());
            sbOracle.append(" ) table_alias  where table_alias.rn>");
            sbOracle.append((queryInfo.getPage() - 1) * queryInfo.getRows());
            
            //System.out.println(sbOracle.toString());
            
            return sbOracle.toString();
        }
        
        
        
        
        return s.toString();
    }

    private String[] getSelectColumns() {
        Map<String, String> aliasMapping = mapping.getAliasMapping();
        String[] columns = new String[aliasMapping.size()];

        int index = 0;
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            if (entry.getValue().equals(mapping.toCamelCase(entry.getKey()))) {
                columns[index++] = entry.getValue();
            } else {
                columns[index++] = entry.getValue() + " as " + entry.getKey();
            }
        }
        return columns;
    }

    public String insert() {
        Map<String, String> aliasMapping = mapping.getAliasMapping();
        String[] columns = new String[aliasMapping.size()];
        String[] values = new String[aliasMapping.size()];

        int index = 0;
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            columns[index] = entry.getValue();
            values[index] = getParamPattern(entry.getKey());
            index++;
        }

        return new SQL().INSERT_INTO(mapping.getTableName()).INTO_COLUMNS(columns).INTO_VALUES(values).toString();
    }

    public String delete() {
        return joinWhereWithId(new SQL().DELETE_FROM(mapping.getTableName())).toString();
    }

    public String update() {
        Map<String, String> aliasMapping = mapping.getAliasMapping();
        // 排除id
        String[] sets = new String[aliasMapping.size() - 1];

        int index = 0;
        for (Map.Entry<String, String> entry : aliasMapping.entrySet()) {
            // 排除id
            if (entry.getKey().equals(mapping.getId())) {
                continue;
            }
            sets[index++] = entry.getValue() + " = " + getParamPattern(entry.getKey());
        }
        return joinWhereWithId(new SQL().UPDATE(mapping.getTableName()).SET(sets)).toString();
    }

    private SQL joinWhereWithId(SQL sql) {
        return sql.WHERE(mapping.getAliasMapping().get(mapping.getId()) + " = " + getParamPattern(mapping.getId()));
    }

    protected abstract String getParamPattern(String paramName);
}
