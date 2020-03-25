package com.aaa.mybatisplus.entity;

import com.aaa.mybatisplus.enums.GenderEnum;
import com.alibaba.fastjson.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Data
@Accessors(chain = true)
//1.枚举类如何转为json（在一个类的属性中，这个枚举类属性如何直接使用在接收参数和向数据库传递参数时需要自动转化）
//新的版本其实这两个注解不加也可以的
//@JSONType(serializeEnumAsJavaBean = true)
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
//@KeySequence(value = "mybatisKeyGenerator", clazz = String.class)
@TableName("user")
public class User {

    /**
     *
     * CREATE TABLE `user` (
     *   `id` varchar(50) DEFAULT NULL,
     *   `name` varchar(50) DEFAULT NULL,
     *   `age` int(1) DEFAULT NULL,
     *   `email` varchar(50) DEFAULT NULL,
     *   `status` tinyint(1) NOT NULL DEFAULT '0',
     *   `manager_id` tinyint(1) DEFAULT NULL,
     *   `is_del` tinyint(1) NOT NULL DEFAULT '0'
     * ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
     *
     * */
    /**
     *
     * 官网说： 只有 type = IdType.INPUT  时 外部自定义的主键生成器才能生效
     * 其实 只要设置了 @KeySequence(value = "mybatisKeyGenerator", clazz = String.class)
     * type = IdType.ASSIGN_ID  或者其他的
     * 外部仍然可以生效
     * 但是注意：单线程内测试是会重复id的
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;
    private String name;
    private GenderEnum age;
    private String email;

    @Version
    private Integer status;

    private Integer managerId;

    // @TableLogic
    /**
     *     实体类字段上加上@TableLogic注解
     *     使用mp自带方法删除和查找都会附带逻辑删除功能 (自己写的xml不会也包含注解)
     *     如果配置了 全局逻辑删除 该注解可以不加
     */
    private Integer isDel;

}

