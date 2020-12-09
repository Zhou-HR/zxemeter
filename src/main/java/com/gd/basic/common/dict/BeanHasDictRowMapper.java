package com.gd.basic.common.dict;

import com.gd.model.po.Dict;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class BeanHasDictRowMapper extends BeanPropertyRowMapper {

    public BeanHasDictRowMapper(Class mappedClass) {
        super(mappedClass);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        if (pd.getPropertyType() == Dict.class) {
            Class mappedClass = this.getMappedClass();
            String fieldName = pd.getName();
            String value = String.valueOf(JdbcUtils.getResultSetValue(rs, index, String.class));
            return DictUtil.getDict(mappedClass, fieldName, value);
        }
        return super.getColumnValue(rs, index, pd);
    }
}
