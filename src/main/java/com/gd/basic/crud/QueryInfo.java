package com.gd.basic.crud;

import com.gd.basic.common.JsonUtil;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 分页查询参数
 *
 */
@Data
public class QueryInfo {

    /**
     * 每页显示行数
     */
    private int rows;

    /**
     * 当前页数
     */
    private int page;

    /**
     * 过滤规则集合
     */
    private List<FilterRule> rules;

    /**
     * 需排序字段，多个以，分割
     */
    private String sort;

    /**
     * 升序或降序，多个以，分割，与sort中内容一一对应
     */
    private String order;

    /**
     * 设置排序参数
     *
     * @param sort  字段名，多个字段以","分割，如：createTime,ordered
     * @param order 升降序，与sort中内容对应，多个以","分割，如：desc,asc
     */
    public QueryInfo orderBy(String sort, String order) {
        this.sort = sort;
        this.order = order;
        return this;
    }

    /**
     * 设置分页参数
     *
     * @param page 当前第几页，从1开始
     * @param rows 一页显示多少行
     */
    public QueryInfo paging(int page, int rows) {
        this.page = page;
        this.rows = rows;
        return this;
    }

    /**
     * 条件过滤参数
     *
     * @param rules 过滤规则
     */
    public QueryInfo filter(FilterRule... rules) {
        this.rules = Arrays.asList(rules);
        return this;
    }

    /**
     * 设置过滤规则
     * <p>
     * 目前支持两种格式，有其他情况可进行扩展
     * <p/>
     * 1、json类型，例如[{"field":"status","op":"equal","value":"enable"}]
     * 2、以_分割，例如：status_equal_enable|name_contains_zh，意思为status等于enable并且name包含zh
     *
     * @param filterRules 规则字符串
     */
    public void setFilterRules(String filterRules) {
        if (StringUtils.isBlank(filterRules) || StringUtils.equals(filterRules, "[]")) {
            return;
        }

        if (filterRules.startsWith("[{")) {// 代表是json类型的字符串
            this.rules = (JsonUtil.fromJsonToList(filterRules, FilterRule.class));
        } else {
            String[] strings = StringUtils.split(filterRules, "|");
            for (String string : strings) {
                // 最大分割成三个字符串，防止value中存在下划线
                String[] split = StringUtils.split(string, "_", 3);
                addRule(new FilterRule(split[0], FilterRule.RuleOp.valueOf(split[1]), split[2]));
            }
        }
    }

    private void addRule(FilterRule filterRule) {
        if (rules == null) {
            rules = new ArrayList<>();
        }

        rules.add(filterRule);
    }
}
