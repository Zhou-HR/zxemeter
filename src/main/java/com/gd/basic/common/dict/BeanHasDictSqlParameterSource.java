package com.gd.basic.common.dict;

import com.gd.model.po.Dict;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

/**
 * @author ZhouHR
 */
public class BeanHasDictSqlParameterSource extends BeanPropertySqlParameterSource {

    private final Object dictObject;

    public BeanHasDictSqlParameterSource(Object object) {
        super(object);
        dictObject = object;
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        Object value = super.getValue(paramName);
        if (value instanceof Dict) {
            return ((Dict) value).getValue();
        }
        //处理oracle sequence
//        if("id".equals(paramName)){
//        	String seqName=dictObject.getClass().getSimpleName()+"_seq.Nextval";
//        	return seqName;
//        }
        return value;
    }
}
