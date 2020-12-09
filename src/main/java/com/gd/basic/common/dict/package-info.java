/**
 * 这个包里的内容是为了处理Dict类型的字段
 * <p>
 * DictFormatter：spring mvc使用，使其可以把string类型转为Dict
 * <p>
 * 下面三个类是给mybatis使用
 * ObjectWrapperFactoryImpl：扩展默认的DefaultObjectWrapperFactory，使其对包含Dict类型字段的类，用BeanHasDictWrapper和DictTypeHandler进行处理
 * BeanHasDictWrapper：扩展BeanWrapper，填充Dict对象的内容
 * DictTypeHandler：使mybatis能够进行string到Dict类型的转换
 * <p>
 * 下面两个类是给spring jdbc使用
 * BeanHasDictRowMapper：扩展BeanPropertyRowMapper，处理String到Dict的转换
 * BeanHasDictSqlParameterSource：扩展BeanSqlParameterSource，处理Dict到String的转换
 * <p>
 * DictUtil：Dict的帮助类，缓存数据库字典表的内容
 */
package com.gd.basic.common.dict;