package com.aaa.mybatisplus.mapper;

import com.aaa.mybatisplus.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    IPage<User>   selectPageVo(Page page, @Param("name") String name);

    @Delete({" delete from user  "})
    void  deleteAll();


    @Select({ " select * from user   limit 11   " })
    List<User> getAll();

    /**
     * @param id
     * @return
     */
    User getOne(String id);
}

