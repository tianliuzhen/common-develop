package com.aaa.mybatisplus.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author liuzhen.tian
 * @version 1.0 Emp.java  2022/10/26 21:20
 */

@Table(name = "emp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp {
    @Id
    @KeySql(useGeneratedKeys = true) // 此注解表示 insert 后回填映射主键id
    private Long id;
    private String userName;
    private Long deptno;
    private Integer managerId;
}
