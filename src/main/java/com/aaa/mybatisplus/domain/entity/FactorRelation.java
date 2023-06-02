package com.aaa.mybatisplus.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @author liuzhen.tian
 * @version 1.0 FactorRelation.java  2023/6/2 21:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactorRelation {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long factorId;
    @Id
    private Long ruleId;
    private String name;
}
