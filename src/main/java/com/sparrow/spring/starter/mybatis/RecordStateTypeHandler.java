package com.sparrow.spring.starter.mybatis;

import com.sparrow.protocol.enums.StatusRecord;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class RecordStateTypeHandler implements TypeHandler<StatusRecord> {

    @Override public void setParameter(PreparedStatement statement, int i, StatusRecord parameter,
        JdbcType type) throws SQLException {
        statement.setInt(i, parameter.ordinal());
    }

    @Override
    public StatusRecord getResult(ResultSet rs, String arg) throws SQLException {
        return StatusRecord.values()[rs.getInt(arg)];
    }

    @Override
    public StatusRecord getResult(ResultSet rs, int arg) throws SQLException {
        return StatusRecord.values()[rs.getInt(arg)];
    }

    @Override
    public StatusRecord getResult(CallableStatement cs, int arg) throws SQLException {
        return StatusRecord.values()[cs.getInt(arg)];
    }

}