package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.domain.entity.Emp;
import com.aaa.mybatisplus.mapper2.EmpTkMapper;
import com.aaa.mybatisplus.web.base.CommonBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Setter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 TestTkMapperControlrlt.java  2022/10/26 21:14
 */
@RestController
@RequestMapping(value = "tkMapper")
public class TestTkMapperController {

    @Resource
    private EmpTkMapper empTkMapper;

    @Setter
    private CommonBean commonBean;

    @PostMapping("/insert")
    public Object insert() {
        empTkMapper.insert(new Emp(5L, "x", 1L, 1));

        return null;
    }

    @PostMapping("/update")
    public Object update() {

        Example example=new Example(Emp.class);//要查询的表对应的实体类
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("userName", "%c%");
        empTkMapper.selectByExample(example);

        Emp emp = new Emp();
        emp.setId(1L);
        emp.setUserName("爱上大时代");
        empTkMapper.updateByPrimaryKeySelective(emp);

        return null;
    }

    @PostMapping("/selectByExample")
    public Object selectByExample() {
        // 分页拦截
        PageHelper.startPage(1, 2);

        Example example=new Example(Emp.class);//要查询的表对应的实体类
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("userName", "%c%");
        List<Emp> emps = empTkMapper.selectByExample(example);

        PageInfo<Emp> userPageInfo = new PageInfo<>(emps);
        return userPageInfo;
    }
}
