package com.gd.basic.crud;

import com.gd.basic.common.BeanFactoryUtil;
import com.gd.basic.common.dict.BeanHasDictRowMapper;
import com.gd.basic.common.dict.BeanHasDictSqlParameterSource;
import com.gd.basic.common.dict.DictUtil;

import lombok.extern.log4j.Log4j2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的单表增删改查实现
 *
 */
@Log4j2
public class CrudService {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    private TableMapping mapping;
    private SqlBuilder sqlBuilder;

    private static ConcurrentHashMap<Class, CrudService> instanceCache = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Class, BeanPropertyRowMapper> beanRowMapperCache = new ConcurrentHashMap<>();

    private CrudService() {
    }

    public static CrudService of(Class poBeanClass) {
        CrudService cached = instanceCache.get(poBeanClass);
        if (cached != null) {
            return cached;
        }

        CrudService service = new CrudService();
        service.jdbcTemplate = BeanFactoryUtil.getBean(NamedParameterJdbcTemplate.class);
        service.transactionTemplate = BeanFactoryUtil.getBean(TransactionTemplate.class);

        service.mapping = new TableMapping(poBeanClass);
        service.sqlBuilder = new NamedSqlBuilder(service.mapping);

        instanceCache.put(poBeanClass, service);
        return service;
    }

    public <T> T findById(Object id) {
        try {
            return (T) jdbcTemplate.queryForObject(sqlBuilder.select(),
                    new MapSqlParameterSource(mapping.getId(), id), getRowMapper(mapping.getCls()));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public <T> T find(FilterRule... rules) {
        List<Object> list = paging(new QueryInfo().filter(rules).paging(1, 1));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return ((T) list.get(0));
    }
    
    public <T> List<T> list(String sort,String order,FilterRule... rules) {
    	QueryInfo queryInfo=new QueryInfo().filter(rules);
    	queryInfo.setSort(sort);
    	queryInfo.setOrder(order);
        return paging(queryInfo);
    }

    public <T> List<T> list(FilterRule... rules) {
        return paging(new QueryInfo().filter(rules));
    }

    
    /**
     * 分页内容
     * @param queryInfo
     * @return
     */
    public <T> List<T> paging(QueryInfo queryInfo) {
        List<FilterRule> filterRules = queryInfo.getRules();
        SqlParameterSource parameterSource;

        if (filterRules != null) {
            parameterSource = new MapSqlParameterSource();
            Map<String,Integer> mapCount=new HashMap<String,Integer>();
            for (FilterRule filterRule : filterRules) {
            	if(filterRule!=null){
            		 switch (filterRule.getOp()) {
                     case contains:
                         filterRule.setValue("%" + filterRule.getValue() + "%");
                         break;
                     case beginwith:
                         filterRule.setValue("%" + filterRule.getValue());
                         break;
                     case endwith:
                         filterRule.setValue(filterRule.getValue() + "%");
                         break;
                 }
            		//如果有多个相同的字段查询，后面的字段参数加个数字，从1开始
             		Integer count=mapCount.get(filterRule.getField());
             		if(count==null){
             			mapCount.put(filterRule.getField(), 1);
             			((MapSqlParameterSource) parameterSource).addValue(filterRule.getField(), filterRule.getValue());
             		}
             		else{
             			count++;
             			mapCount.put(filterRule.getField(), count);
             			((MapSqlParameterSource) parameterSource).addValue(filterRule.getField()+(count-1), filterRule.getValue());
             		}
//            		 ((MapSqlParameterSource) parameterSource).addValue(filterRule.getField(), filterRule.getValue());
            	}
            }
        } else {
            parameterSource = EmptySqlParameterSource.INSTANCE;
        }
        
        return jdbcTemplate.query(sqlBuilder.selectWithOthers(queryInfo), parameterSource, getRowMapper(mapping.getCls()));
    }

    /**
     * 查询总数
     * @param filterRules
     * @return
     */
    public int count(List<FilterRule> filterRules) {
        SqlParameterSource parameterSource;

        if (filterRules != null) {
            parameterSource = new MapSqlParameterSource();
            for (FilterRule filterRule : filterRules) {
                ((MapSqlParameterSource) parameterSource).addValue(filterRule.getField(), filterRule.getValue());
            }
        } else {
            parameterSource = EmptySqlParameterSource.INSTANCE;
        }
        return jdbcTemplate.queryForObject(sqlBuilder.count(filterRules), parameterSource, Integer.class);
    }

    public <T> void insert(final T obj) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.update(sqlBuilder.insert(), getBeanSqlParameterSource(obj));
            }
        });
    }

    public <T> int insertAndRtnKey(final T obj) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.update(sqlBuilder.insert(), getBeanSqlParameterSource(obj), keyHolder);
            }
        });
        return keyHolder.getKey().intValue();
    }

    public <T> boolean delete(final Object id) {
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                Assert.isTrue(jdbcTemplate.update(sqlBuilder.delete(), new MapSqlParameterSource(mapping.getId(), id)) == 1);
                return Boolean.TRUE;
            }
        });
    }

    public <T> boolean update(final T obj) {
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                Assert.isTrue(jdbcTemplate.update(sqlBuilder.update(), getBeanSqlParameterSource(obj)) == 1);
                return Boolean.TRUE;
            }
        });
    }

    private RowMapper getRowMapper(Class mappedClass) {
        BeanPropertyRowMapper rowMapper = beanRowMapperCache.get(mappedClass);
        if (rowMapper != null) {
            return rowMapper;
        }

        rowMapper = DictUtil.hasDictField(mappedClass) ? new BeanHasDictRowMapper(mappedClass) : new BeanPropertyRowMapper(mappedClass);
        beanRowMapperCache.put(mappedClass, rowMapper);
        return rowMapper;
    }

    private SqlParameterSource getBeanSqlParameterSource(Object obj) {
        return DictUtil.hasDictField(obj) ? new BeanHasDictSqlParameterSource(obj) : new BeanPropertySqlParameterSource(obj);
    }
}
