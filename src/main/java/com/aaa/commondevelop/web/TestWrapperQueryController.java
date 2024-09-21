package com.aaa.commondevelop.web;

import com.aaa.commondevelop.domain.entity.User;
import com.aaa.commondevelop.mapper.UserMapper;
import com.aaa.commondevelop.service.User2Service;
import com.aaa.commondevelop.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/17
 */
@RestController
@Slf4j
@RequestMapping("/TestWrapperQueryController")
public class TestWrapperQueryController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private User2Service user2Service;

    @GetMapping("/getWrapper")
    public void  getWrapper(){

        /**
         * 1、普通查询
         * eg:  SELECT id, name, age, email, status, manager_id, is_del FROM user WHERE is_del = 0 AND (name = 'tom20') AND user.manager_id = 0
         */
        QueryWrapper<User> wrapper = new QueryWrapper<User>().like("name", "Allen");
        wrapper.ne("name", "Allen1");
        List<User> plainUsers = userMapper.selectList(wrapper);
        List<User> lambdaUsers = userMapper.selectList(new QueryWrapper<User>().lambda().eq(User::getName,"Allen1"));
        /**
         * 2、带子查询(sql注入)
         * eg:
         *    SELECT id, name, age, email, status, manager_id, is_del FROM user
         *    WHERE is_del = 0 AND (age IN (SELECT id FROM sax WHERE id = 1)) AND user.manager_id = 0
         */
        List<User> plainUsers1 = userMapper.selectList(new QueryWrapper<User>()
                .inSql("age", "select id from user where id=1 "));
        List<User> lambdaUsers1 = userMapper.selectList(new QueryWrapper<User>().lambda()
                .inSql(User::getAge, "select id from user where id=1  "));

        /**
         * 3、带嵌套查询
         * eg: SELECT id, name, age, email, status, manager_id, is_del FROM user WHERE is_del = 0
         *     AND ((id = '1217713709169131523' OR id = '1217713709169131524'))
         *     AND ((age >= 0)) AND user.manager_id = 0
         */
        List<User> plainUsers3 = userMapper.selectList(new LambdaQueryWrapper<User>()
                .nested(i -> i.eq(User::getId, "1217713709169131523").or().eq(User::getId, "1217713709169131524"))
                .and(i -> i.ge(User::getAge, 0)));
        List<User> lambdaUsers3 = userMapper.selectList(new QueryWrapper<User>().lambda()
                .nested(i -> i.eq(User::getId, "1217713709169131523").or().eq(User::getId, "1217713709169131524"))
                .and(i -> i.ge(User::getAge, 0)));
        /**
         * 自定义(sql注入)
         * eg:
         *   SELECT id, name, age, email, status, manager_id, is_del FROM user WHERE is_del = 0 AND (age = 1) AND user.manager_id = 0
         */
        List<User> plainUsers4 = userMapper.selectList(new QueryWrapper<User>()
                .apply("age = 1"));
        print(plainUsers4);

        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.set("name", null);
        uw.eq("id", "1217713708011503617");
        userMapper.update(new User(), uw);
        User u4 = userMapper.selectById("1217713708011503617");
        System.out.println();
    }

    private <T> void print(List<T> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(System.out::println);
        }
    }
    @PostMapping("/getWrapper2")
    public void  getWrapper2(@RequestBody User user){
        /**
         * 全部查询,拼接一个where过滤条件
         */
        Map map=new HashMap();
        map.put("id","1217713708011503617");
        map.put("name",null);
        // 默认不忽略是空，如果忽略空加 false 即可
        List<User> plainUsers = userService.list(new QueryWrapper<User>().allEq(map,false));
        System.out.println();
    }
}
