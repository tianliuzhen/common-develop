package com.aaa.commondevelop.web;

import com.aaa.commondevelop.config.global.exceptions.BizException;
import com.aaa.commondevelop.config.snowflakeId.SnowflakeComponent;
import com.aaa.commondevelop.domain.annotation.SysTimeLog;
import com.aaa.commondevelop.domain.entity.Dept;
import com.aaa.commondevelop.domain.entity.User;
import com.aaa.commondevelop.domain.enums.GenderEnum;
import com.aaa.commondevelop.mapper.DeptMapper;
import com.aaa.commondevelop.mapper.UserMapper;
import com.aaa.commondevelop.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.assertj.core.util.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author liuzhen.tian
 * @version 1.0 TestCrudBatchController.java  2022/4/24 20:56
 */

@RestController
@RequestMapping(value = "batch")
public class TestCrudBatchController {
    public static final int COUNT = 3 * 10;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private SnowflakeComponent snowflakeComponent;

    @Autowired
    private DataSource dataSource;

    /**
     * 批量更新：基于注解
     */
    @GetMapping("/batchUpdateUser22")
    public void batchUpdateUser22() {
        ArrayList<User> users = Lists.newArrayList(
                new User("1", "123@qq.com"),
                new User("2", "456@qq.com"));
        boolean b = userService.saveOrUpdateBatch(users);
        System.out.println("b = " + b);
    }

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
        List<User> users1 = getUsers(10000 * 10);
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
        List<User> userList = getUsers(10000 * 10);
        // Integer res = userMapper.batchAddUser2(userList);
        // 批量执行插入
        List<User> usersTemp = Lists.newArrayList();
        for (int i = 0; i < userList.size(); i++) {
            usersTemp.add(userList.get(i));
            if (i % 5000 == 0 && i != 0) {
                // todo：  <foreach INSERT 比 <foreach VALUES 插入要慢
                userMapper.batchAddUser(usersTemp);
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
     * 部分参考：https://blog.csdn.net/qq_42093488/article/details/124956848
     */
    @PostMapping("/batchAddUser3")
    public void batchAddUser3() {
        StopWatch stopWatch = new StopWatch("batchAddUser3");
        stopWatch.start();
        SqlSession sqlSession = null;
        try {
            sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

            // todo 注意：******* 这里必须要从 sqlSession.getMapper 获得 mapper
            // todo 否则，Executor不是BatchExecutor
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            List<User> users = getUsers(COUNT);
            for (int i = 0; i < users.size(); i++) {
                mapper.insert(users.get(i));
                if (i % 10 == 0 && i != 0) {
                    /*  commit 核心执行逻辑
                     *  sqlSession.commit();
                     *  ||
                     *  org.apache.ibatis.executor.BatchExecutor.doFlushStatements
                     *  ||
                     *  com.mysql.cj.jdbc.StatementImpl.executeBatch (stmt.executeBatch())
                     */
                    sqlSession.commit();
                }
            }
            // 如果中间不执行 flushStatements(),commit 方法只会一次性执行
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
        stopWatch.stop();
        double totalTimeSeconds = stopWatch.getTotalTimeSeconds();
        System.out.println("totalTimeSeconds = " + totalTimeSeconds);
        System.out.println(stopWatch.prettyPrint());
    }


    /**
     * JDBC分批次批量插入
     * 同：com.aaa.commondevelop.web.TestCrudBatchController#batchAddUser3() 它只是对jdbc的封装
     *
     * @throws IOException
     */
    @PostMapping("/batchAddUser4")
    public void batchAddUser4() throws IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        String databaseURL = "jdbc:mysql://localhost:3306/master?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true";
        String user = "root";
        String password = "123456";

        try {
            connection = DriverManager.getConnection(databaseURL, user, password);
            // 关闭自动提交事务，改为手动提交
            connection.setAutoCommit(false);
            System.out.println("===== 开始插入数据 =====");
            long startTime = System.currentTimeMillis();
            String sqlInsert = "INSERT INTO user ( name, age) VALUES ( ?, ?)";
            preparedStatement = connection.prepareStatement(sqlInsert);

            Random random = new Random();
            for (int i = 1; i <= 30; i++) {
                preparedStatement.setString(1, "aaa:" + i);
                preparedStatement.setInt(2, random.nextInt(100));
                // 添加到批处理中
                preparedStatement.addBatch();

                if (i % 10 == 0) {
                    // 每1000条数据提交一次
                    preparedStatement.executeBatch();
                    connection.commit();
                    System.out.println("成功插入第 " + i + " 条数据");
                }

            }
            // 处理剩余的数据
            preparedStatement.executeBatch();
            connection.commit();
            long spendTime = System.currentTimeMillis() - startTime;
            System.out.println("成功插入 30 万条数据,耗时：" + spendTime + "毫秒");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<User> getUsers(int count) {
        List result = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            result.add(new User(UUID.randomUUID().toString(), "aaa" + i, GenderEnum.FEMALE, 11L, "123@qq.com", 0, null, 1, LocalDateTime.now()));
        }
        return result;
    }

    @PostMapping("/addUser")
    public void addUser() {
        for (int i = 0; i < 1; i++) {
            User user = new User();
            user.setStatus(1);
            user.setIsDel(0);
            user.setOnlineTime(LocalDateTime.now());
            userMapper.insertUser(user);
        }
    }

    @PostMapping("/getOneUser")
    public void getOneUser() {
        // mybatis
        // userMapper.getOne("1");
        // mybatis-plus (自定义typeHandle 无法生效)
        userMapper.selectById("1");

    }

    @PostMapping("/getAllUserByPage")
    public void getAllUserByPage() {
        PageHelper.startPage(1, 10);
        List<User> users = userMapper.selectList(null);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        System.out.println(userPageInfo.getPageNum());  // 获取当前页码
        System.out.println(userPageInfo.getPageSize());  // 获取页面长度
        System.out.println(userPageInfo.getNextPage());  // 获取下一页页码
        System.out.println(userPageInfo.getPages());  // 获取页数
        System.out.println(userPageInfo.getTotal());  // 获取数据数量
    }


    @PostMapping("/returnInsertKey")
    public void returnInsertKey() {
        for (int i = 0; i < 1; i++) {
            Dept aaa = new Dept(null, "aaa", i);
            deptMapper.insertDept(aaa);
            System.out.println(aaa);
        }
    }

    @PostMapping("/batchReturnInsertKey")
    public void batchReturnInsertKey() {
        List<Dept> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add(new Dept(null, "aaa", i));
        }
        deptMapper.batchReturnInsertKey(list);
    }


    /**
     * 声明式事务
     * 用数据库 for update  实现排他锁
     */
    @GetMapping("/lockByForUpdate")
    @Transactional(rollbackFor = Exception.class)
    public void lockByForUpdate() {
        try {
            System.out.println(Thread.currentThread().getId() + ": 竞争锁...");
            deptMapper.lockByForUpdate(1234L);
            System.out.println(Thread.currentThread().getId() + ": 抢锁成功...");
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("当前请求正在执行，稍后再试！", 200);
        }

    }

    /**
     * 声明式事务
     * 用数据库 for update nowait 实现排他锁
     */
    @GetMapping("/lockByForUpdateNowait")
    @Transactional(rollbackFor = Exception.class)
    public void lockByForUpdateNowait() {
        try {
            deptMapper.lockByForUpdateNowait(1234L);
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("当前请求正在执行，稍后再试！", 200);
        }

    }

    /**
     * 编程式事务
     * 用数据库 for update nowait 实现排他锁
     */
    @GetMapping("/lockByForUpdateNowaitV2")
    public void lockByForUpdateNowaitV2() {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                deptMapper.lockByForUpdateNowait(1234L);
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BizException("当前请求正在执行，稍后再试！", 200);
            }
        });
    }


    /**
     * for update 脱离事务是无效的
     */
    @GetMapping("/lockByForUpdateNowaitV3")
    public void lockByForUpdateNowaitV3() {
        try {
            deptMapper.lockByForUpdateNowait(1234L);
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("当前请求正在执行，稍后再试！", 200);
        }
    }

    /**
     * 测试多线程事务共享提交的问题
     *
     * @return
     * @throws InterruptedException
     */
    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "testTran3")
    public void testTranAsync() throws InterruptedException {
        ConnectionHolder resource = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        deptMapper.insert(new Dept(1L, "部门1", 1));
        // 要配置： @EnableAspectJAutoProxy(exposeProxy = true)
        TestCrudBatchController proxy = (TestCrudBatchController) AopContext.currentProxy();
        Thread thread = new Thread(() -> {
            System.out.println("testTranAsync");
            TransactionSynchronizationManager.bindResource(dataSource, resource);
            /* 绑定完资源后,在走到 getTransaction 时，不再创建Transaction，复用之前的Transaction
             * org.springframework.transaction.support.AbstractPlatformTransactionManager#getTransaction(org.springframework.transaction.TransactionDefinition)
             * org.springframework.transaction.support.AbstractPlatformTransactionManager.isExistingTransaction
             * org.springframework.transaction.support.AbstractPlatformTransactionManager.handleExistingTransaction
             */
            proxy.testTranAsyncInner();
        });
        thread.start();
        thread.join(); // 当前线程（即 Spring 控制器方法所在的 HTTP 请求处理线程）暂停执行，等待上面那个子线程执行完毕，否则无效
        System.out.println("testTranAsyncInner");
    }

    @Transactional(rollbackFor = Exception.class)
    public void testTranAsyncInner() {
        deptMapper.insert(new Dept(2L, "部门2", 1));
        throw new RuntimeException("我异常了");
    }
}
