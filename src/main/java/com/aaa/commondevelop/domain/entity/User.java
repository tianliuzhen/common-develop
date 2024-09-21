package com.aaa.commondevelop.domain.entity;

import com.aaa.commondevelop.config.typeHandler.TimeConvertHandler;
import com.aaa.commondevelop.domain.enums.GenderEnum;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;


/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Data
@Accessors(chain = true)
// 在注解@TableName中增加autoResultMap = true表示使用xml中的映射配置
@TableName(value = "user", autoResultMap = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     *
     CREATE TABLE `user` (
     `id` varchar(50) NOT NULL,
     `name` varchar(50) DEFAULT NULL,
     `age` int(11) DEFAULT NULL,
     `sex` int(1) DEFAULT NULL,
     `email` varchar(50) DEFAULT NULL,
     `status` tinyint(1) NOT NULL DEFAULT '0',
     `manager_id` tinyint(1) DEFAULT NULL,
     `is_del` tinyint(1) NOT NULL DEFAULT '0',
     `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
     `online_time` bigint DEFAULT NULL COMMENT '上线时间',
     PRIMARY KEY (`id`),
     KEY `name` (`name`)
     ) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
     *
     * */

    /**
     * 官网说： 只有 type = IdType.ASSIGN_ID  时 外部自定义的主键生成器才能生效
     * 如果不设置：默认使用雪花算法+UUID(不含中划线)，即：默认 type = IdType.ASSIGN_ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    private String name;
    private GenderEnum sex;
    private Long age;
    private String email;

    @Version
    private Integer status;

    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer managerId;

    // @TableLogic
    /**
     * 实体类字段上加上@TableLogic注解
     * 使用mp自带方法删除和查找都会附带逻辑删除功能 (自己写的xml不会也包含注解)
     * 如果配置了 全局逻辑删除 该注解可以不加
     */
    private Integer isDel;

    private LocalDateTime createTime;
    private Date updateTime;

    /**
     * 上线时间
     */
    @TableField(typeHandler = TimeConvertHandler.class)
    private LocalDateTime onlineTime;

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User(String id, String name, GenderEnum sex, Long age, String email, Integer status, Integer managerId, Integer isDel, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.email = email;
        this.status = status;
        this.managerId = managerId;
        this.isDel = isDel;
        this.createTime = createTime;
    }
}
