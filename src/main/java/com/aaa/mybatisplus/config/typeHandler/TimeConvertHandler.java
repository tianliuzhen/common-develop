package com.aaa.mybatisplus.config.typeHandler;

import lombok.NoArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 输入的是时间戳，到数据库中的是 timestamp 格式 输出的又是时间戳
 *
 * @author liuzhen.tian
 * @version 1.0 TimeConvertHandler.java  2022/10/23 11:26
 */
// MappedJdbcTypes注解，设置处理的数据库类型
@MappedJdbcTypes(JdbcType.TIMESTAMP)
// 注解配置 java 类型
@MappedTypes(Long.class)
@NoArgsConstructor
public class TimeConvertHandler extends BaseTypeHandler<LocalDateTime> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        long time = parameter.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        ps.setLong(i, time);
    }

    //用于定义通过字段名称获取字段数据时，如何把数据库类型转换为对应的Java类型
    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Long timestamp = rs.getLong(columnName);
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    //用于定义通过字段索引获取字段数据时，如何把数据库类型转换为对应的Java类型
    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Long timestamp = rs.getLong(columnIndex);
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }

}
