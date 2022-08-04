package com.aaa.mybatisplus.mapper;

import com.aaa.mybatisplus.domain.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

/**
 * @author liuzhen.tian
 * @version 1.0 DeptMapper.java  2022/7/2 10:09
 */
@Repository
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    Integer insertDept(Dept dept);

    Integer insertDept2(Dept dept);

    /**
     * 注解@SelectKey 跟 xml insert 标签配合使用是无效的
     *
     * @param dept
     * @return
     */
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = true, resultType = Long.class)
    Integer insertDept3(Dept dept);

    /**
     * 注解@SelectKey必须配合@Insert 使用才有效
     *
     * @param dept
     * @return
     */
    @Insert(value = " INSERT INTO `dept`(`id`, `dept_name`, `dept_no`) VALUES (#{id},#{deptName},#{deptNo})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    Integer insertDept4(Dept dept);

    Dept lockByForUpdateNowait(Long deptNo);


    Dept lockByForUpdate(Long deptNo);
}
