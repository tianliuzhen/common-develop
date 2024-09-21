package com.aaa.commondevelop.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author liuzhen.tian
 * @version 1.0 Dept.java  2022/7/2 10:08
 */

@Data
@Accessors(chain = true)
@TableName("user")
@NoArgsConstructor
@AllArgsConstructor
public class Dept {

    /**
     * 如果不加此注解，设置 type = IdType.AUTO，mybatis_plus 默认是雪花id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String deptName;
    private Integer deptNo;

}
