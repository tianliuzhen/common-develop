package com.aaa.commondevelop.mapper;

import com.aaa.commondevelop.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/17
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * <p>
     * 查询 : 根据state状态查询用户列表，分页显示
     * 注意!!: 如果入参是有多个,需要加注解指定参数名才能在xml中取值
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param name 模糊查询
     * @return 分页对象
     */
    @Select({" select * from user where `name` LIKE  concat(concat('%',#{name}),'%')  "})
    IPage<User> selectPageVo(Page page, @Param("name") String name);

    @Delete({" delete from user  "})
    void deleteAll();


    @Select({" select * from user   limit 11   "})
    List<User> getAll();

    @Select({" select * from user  where create_time > #{beginTime} and create_time <= #{endTime}  "})
    List<User> selectByCreateTime(HashMap map);

    @Select({" select * from user  where update_time > #{beginTime} and update_time <= #{endTime}  "})
    List<User> selectByUpdateTime(HashMap map);

    /**
     * @param id
     * @return
     */
    User getOne(String id);

    /**
     * 根据条件动态查询
     *
     * @param map
     * @return
     */
    List<User> selectByCondition(HashMap map);
    List<User> selectByConditionV2(@Param("map")  HashMap map);


    /**
     * 批量更新（注解版）
     * 注：需要配置 &allowMultiQueries=true，否则会报错
     * <p>
     * 如果只有一个参数可以不加 @Param("userList")
     *
     * @param userList userList
     * @return Integer
     */
    @Update({"<script>" +
            "<foreach collection='userList' item='item' open='' close=''  separator=';'>" +
            " update user  set email=#{item.email} where id= #{item.id} " +
            "</foreach> " +
            " </script>"})
    Integer batchUpdateUser(List<User> userList);


    /**
     * 批量更新（xml版）
     * 注：需要配置 &allowMultiQueries=true，否则会报错
     *
     * @param userList userList
     * @return Integer
     */
    Integer batchUpdateUser2(List<User> userList);

    /**
     * 批量更新（xml版）
     * 注：***不需要***配置 &allowMultiQueries=true
     * <p>
     * 使用case，when最后会变成一条sql语句，但是每次都得去遍历list集合，数据量大了会影响效率问题
     *
     * @param userList userList
     * @return Integer
     */
    Integer batchUpdateUser3(@Param("userList") List<User> userList);


    /**
     * 批量插入
     * 注：***不需要***配置 &allowMultiQueries=true
     *
     * @param userList userList
     * @return Integer
     */
    Integer batchAddUser(List<User> userList);

    /**
     * 批量插入2
     * 注：需要配置 &allowMultiQueries=true
     *
     * @param userList userList
     * @return Integer
     */
    Integer batchAddUser2(List<User> userList);

    /**
     * 单个插入
     *
     * @param user user
     * @return Integer
     */
    Integer insertUser(User user);

    /**
     * 单个插入
     *
     * @param map
     * @return Integer
     */
    Integer updateUserByCondition(Map<Object, Object> map);
}

