package com.aaa.mybatisplus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuzhen.tian
 * @version 1.0 FactorRelation.java  2023/6/2 21:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FactorRelation {
    private Long factorId;
    private Long ruleId;
    private String name;
}
