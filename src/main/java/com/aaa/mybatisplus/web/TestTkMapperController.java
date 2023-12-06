package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.domain.entity.Emp;
import com.aaa.mybatisplus.domain.entity.FactorRelation;
import com.aaa.mybatisplus.domain.entity.WxUser;
import com.aaa.mybatisplus.mappertk.EmpTkMapper;
import com.aaa.mybatisplus.mappertk.FactorRelationMapper;
import com.aaa.mybatisplus.mappertk.WxUserTkMapper;
import com.aaa.mybatisplus.util.MyFunUtil;
import com.aaa.mybatisplus.web.base.CommonBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private FactorRelationMapper factorRelationMapper;

    @Autowired
    private CommonBean commonBean;


    @PostMapping("/insert")
    public Object insert() {
        Emp emp = new Emp(null, "x", 1L, 1);
        empTkMapper.insert(emp);


        // 返回新增的主键id
        return emp.getId();
    }

    @PostMapping("/update")
    public Object update() {

        Example example = new Example(Emp.class);//要查询的表对应的实体类
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("userName", "%c%");
        empTkMapper.selectByExample(example);

        Emp emp = new Emp();
        emp.setId(1L);
        emp.setUserName("爱上大时代");
        empTkMapper.updateByPrimaryKeySelective(emp);

        return null;
    }


    @PostMapping("/insertList")
    public Object insertList() {
        Emp aaa = new Emp(null, "aaa", 20L, 1);
        Emp bbb = new Emp(null, "bbb", 20L, 1);
        ArrayList<Emp> recordList = Lists.newArrayList(aaa, bbb);
        empTkMapper.batchInsert(recordList);
        return recordList;
    }

    @PostMapping("/updateList")
    public Object updateList() {
        Emp aaa = new Emp(10L, "xx", 20L, 1);
        Emp bbb = new Emp(11L, "xx", 20L, 1);
        ArrayList<Emp> recordList = Lists.newArrayList(aaa, bbb);
        empTkMapper.batchUpdateByPrimary(recordList);
        return recordList;
    }

    @PostMapping("/selectByExample")
    public Object selectByExample() {
        // 分页拦截
        PageHelper.startPage(1, 2);

        WeekendSqls<Emp> custom = WeekendSqls.custom();
        custom.andLike(Emp::getUserName, "%爱上%");
        Example build = new Example.Builder(Emp.class)
                .select(MyFunUtil.getFieldName((Emp::getUserName))) // 可指定字段
                .where(custom)
                .orderByDesc("id")
                .build();
        List<Emp> emps = empTkMapper.selectByExample(build);

        PageInfo<Emp> userPageInfo = new PageInfo<>(emps);
        return userPageInfo;
    }

    @PostMapping("/selectByExample2")
    public Object selectByExample2() {
        // 分页拦截
        PageHelper.startPage(1, 2);

        Example example = new Example(Emp.class);//要查询的表对应的实体类
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("userName", "%c%");
        List<Emp> emps = empTkMapper.selectByExample(example);

        PageInfo<Emp> userPageInfo = new PageInfo<>(emps);
        return userPageInfo;
    }

    @PostMapping("/insertTwoPrimary")
    public Object insertTwoPrimary() {
        FactorRelation factorRelation = new FactorRelation(1L, 2L, "a");
        factorRelationMapper.deleteByPrimaryKey(factorRelation);

        factorRelationMapper.insert(factorRelation);

        FactorRelation req = new FactorRelation(1L, 2L, null);
        factorRelationMapper.selectByPrimaryKey(req);
        return factorRelation;
    }

    @Autowired
    private WxUserTkMapper wxUserTkMapper;

    @PostMapping("/saveOrUpdate")
    public Object saveOrUpdate() {
        // 如果存在id，执行更新
        // 如果不存在id，就执行新增
        wxUserTkMapper.saveOrUpdate(new WxUser(1L, "tom",""));

        return null;
    }
}
