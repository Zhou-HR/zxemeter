package com.gd.basic.common.dict;

import com.gd.model.po.Dict;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
public class DictTypeHandler extends BaseTypeHandler<Dict> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Dict parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public Dict getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return new Dict(rs.getString(columnName));
    }

    @Override
    public Dict getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return new Dict(rs.getString(columnIndex));
    }

    @Override
    public Dict getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return new Dict(cs.getString(columnIndex));
    }
}
