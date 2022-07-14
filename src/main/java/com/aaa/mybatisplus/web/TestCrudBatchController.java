package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.SysTimeLog;
import com.aaa.mybatisplus.domain.entity.Dept;
import com.aaa.mybatisplus.domain.entity.User;
import com.aaa.mybatisplus.domain.enums.GenderEnum;
import com.aaa.mybatisplus.mapper.DeptMapper;
import com.aaa.mybatisplus.mapper.UserMapper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 TestCrudBatchController.java  2022/4/24 20:56
 */

@RestController
@RequestMapping(value = "batch")
public class TestCrudBatchController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 批量更新：基于注解
     */
    @PostMapping("/batchUpdateUser")
    public void batchUpdateUser() {
        ArrayList<User> users = Lists.newArrayList(
                new User("1", "123@qq.com"),
                new User("2", "456@qq.com"));
        Integer res = userMapper.batchUpdateUser(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量更新：基于xml （普通for）
     */
    @PostMapping("/batchUpdateUser2")
    public void batchUpdateUser2() {
        User user = new User("1", "123@qq.com");
        user.setAge(11L);
        ArrayList<User> users = Lists.newArrayList(
                user,
                new User("2", "456@qq.com"));
        Integer res = userMapper.batchUpdateUser2(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量更新：基于xml （case when）
     */
    @PostMapping("/batchUpdateUser3")
    public void batchUpdateUser3() {
        ArrayList<User> users = Lists.newArrayList(
                new User("1", "123@qq.com"),
                new User("2", "456@qq.com"));
        Integer res = userMapper.batchUpdateUser3(users);
        System.out.println("res = " + res);
    }

    /**
     * 批量新增：基于xml （foreach insert values）
     */
    @PostMapping("/batchAddUser")
    @SysTimeLog
    public void batchAddUser() {
        ArrayList<User> users = Lists.newArrayList(
                new User("3", "aaa", GenderEnum.FEMALE, 11L, "123@qq.com", 0, 0, 1, LocalDateTime.now()),
                new User("4", "bbb", GenderEnum.FEMALE, 12L, "123@qq.com", 0, 0, 1, LocalDateTime.now()));
        List<User> users1 = getUsers(100);
        userMapper.batchAddUser(users1);
    }

    /**
     * 批量新增：基于xml （foreach insert）
     */
    @SysTimeLog
    @PostMapping("/batchAddUser2")
    public void batchAddUser2() {
        ArrayList<User> users = Lists.newArrayList(
                new User("3", "aaa", GenderEnum.FEMALE, 11L, "123@qq.com", 0, 0, 1, LocalDateTime.now()),
                new User("4", "bbb", GenderEnum.FEMALE, 12L, "123@qq.com", 0, 0, 1, LocalDateTime.now()));
        List<User> users1 = getUsers(10000);
        // Integer res = userMapper.batchAddUser2(users1);
        // 批量执行插入
        List<User> usersTemp = Lists.newArrayList();
        for (int i = 0; i < users1.size(); i++) {
            usersTemp.add(users1.get(i));
            if (i % 500 == 0) {
                userMapper.batchAddUser2(users1);
                usersTemp.clear();
            }
        }
    }

    /**
     * 批量新增：基于xml （批处理）
     * <p>
     * 设置ExecutorType.BATCH原理：把SQL语句发个数据库，数据库预编译好，
     * 数据库等待需要运行的参数，接收到参数后一次运行，
     * ExecutorType.BATCH只打印一次SQL语句，多次设置参数步骤，
     * <p>
     * 也是官方针对批量数据插入优化的方法之一
     */
    @PostMapping("/batchAddUser3")
    public void batchAddUser3() {
        StopWatch stopWatch = new StopWatch("batchAddUser3");
        stopWatch.start();
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            List<User> users = getUsers(10000);
            for (User user : users) {
                mapper.insert(user);
            }
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
        stopWatch.stop();
        double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
        System.out.println("totalTimeSeconds = " + totalTimeSeconds);
        System.out.println(stopWatch.prettyPrint());
    }

    private List<User> getUsers(int count) {
        List result = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            result.add(new User(i + "", "aaa", GenderEnum.FEMALE, 11L, "123@qq.com", 0, null, 1, LocalDateTime.now()));
        }
        return result;
    }

    @PostMapping("/addUser")
    public void addUser() {
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId(i + "");
            user.setStatus(1);
            user.setIsDel(1);
            userMapper.insertUser(user);
        }
    }

    @PostMapping("/returnInsertKey")
    public void returnInsertKey() {
        for (int i = 0; i < 5; i++) {
            Dept aaa = new Dept(null, "aaa", i);
            deptMapper.insertDept4(aaa);
            System.out.println(aaa);
        }
    }
}
