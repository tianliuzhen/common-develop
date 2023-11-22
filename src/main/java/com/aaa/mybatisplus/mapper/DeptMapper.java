package com.aaa.mybatisplus.mapper;

import com.aaa.mybatisplus.domain.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 DeptMapper.java  2022/7/2 10:09
 */
@Repository
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    Integer insertDept(Dept dept);

    Integer returnInsertKey(Dept dept);

    /**
     * 可以不带：@Param("list")，老的版本需要指定list，且不能包含：@Param，新的版本都已兼容
     * 坑：
     *  useGeneratedKeys+keyProperty 才可以实现多个新增返回主键
     *  selectkey 方式才能实现多个新增返回主键
     *
     * @param list
     * @return
     */
    Integer batchReturnInsertKey(@Param("list") List<Dept> list);

    /**
     * 注解@SelectKey 跟 xml insert 标签配合使用是无效的
     *
     * @param dept
     * @return
     */
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = true, resultType = Long.class)
    Integer returnInsertKeyXml(Dept dept);

    /**
     * 注解@SelectKey必须配合@Insert 使用才有效
     *
     * @param dept
     * @return
     */
    @Insert(value = " INSERT INTO `dept`(`id`, `dept_name`, `dept_no`) VALUES (#{id},#{deptName},#{deptNo})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    Integer returnInsertKeyAnnotation(Dept dept);

    Dept lockByForUpdateNowait(Long deptNo);


    Dept lockByForUpdate(Long deptNo);
}
