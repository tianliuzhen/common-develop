package com.aaa.mybatisplus.web;

import com.aaa.mybatisplus.annotation.SysLog;
import com.aaa.mybatisplus.config.configRespone.ObjectResultResponse;
import com.aaa.mybatisplus.entity.PageDto;
import com.aaa.mybatisplus.entity.User;
import com.aaa.mybatisplus.enums.GenderEnum;
import com.aaa.mybatisplus.mapper.UserMapper;
import com.aaa.mybatisplus.service.User2Service;
import com.aaa.mybatisplus.service.UserService;
import com.aaa.mybatisplus.test.extendsAndSuper.B;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@Api(tags = "mybatis-plus 文档测试接口")
//@Transactional(rollbackFor = Exception.class)
@Slf4j
@SysLog
public class TestCrudController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private User2Service user2Service;

    @ApiOperation(value = "分页测试", notes = "插件测试")
    @ApiImplicitParam(name = "page", value = "分页参数", required = true)
    @PostMapping("/testSelectPage")
    public ObjectResultResponse<?> testSelectPage(@RequestBody PageDto pageDto ) {



        Page page=new Page();
        System.out.println("分页测试：：：");
        if(page==null){
             page = new Page();
        }

        // 每页数量、当前页
        page.setSize(pageDto.getSize()).setCurrent(pageDto.getCurrent());
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        IPage<User> iPage=user2Service.selectUserPage(page, "tom");
        List<User> users=iPage.getRecords();
        users.forEach(System.out::println);
        log.info("1211");


        return new ObjectResultResponse(page);
    }


    @ApiOperation(value = "测试逻辑删除")
    @GetMapping("/testUpdateLocal")
    public void testUpdateLocal() {
        // 测试逻辑删除
        //使用mp自带方法删除和查找都会附带逻辑删除功能 (自己写的xml不会包括注解)
        user2Service.removeById(2);
        user2Service.getById(1);
        System.out.println(1/0);
        //全表删除或更新
        userMapper.delete(null);
    }
    @ApiOperation(value = "测试全部删除",notes = "攻击 SQL 阻断解析器")
    @GetMapping("/testDeleteAll")
    public void testDeleteAll() {
        //全表删除或更新
        userMapper.delete(null);
    }
    @ApiOperation(value = "测试序列化33", notes = "mybatis-plus通用枚举")
    @PostMapping("/testSelect")
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        System.out.println("测试序列化：：：：");
        String jsonString = JSONObject.toJSONString(userList);
        List<User> list = JSON.parseObject(jsonString, new TypeReference<List<User>>(){});
        list.forEach(System.out::println);
    }
    @ApiOperation(value = "普通测试", notes = "")
    @GetMapping("/testSelect2")
    public void testSelect2() {
        userService.getById(1);
        userMapper.getAll();
        System.out.println(userService.list().size());
        System.out.println("---------");
        System.out.println(user2Service.list().size());
    }
    @ApiOperation(value = "批量添加")
    @GetMapping("/testAdds")
    public void testAdds() {
        List<User> list=new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            User user=new User();
            user.setName("tom"+i);
            if(i%2==0){
                user.setAge(GenderEnum.MALE);
            }else {
                user.setAge(GenderEnum.FEMALE);
            }
            list.add(user);
        }
        User user1=new User();
        user1.setEmail("74@qq.com").setAge(GenderEnum.FEMALE).setId("1");
        //测试单个update
        //userService.updateById(user1);
        //测试批量添加
         userService.saveBatch(list);
    }
    @ApiOperation(value = "乐观锁测试")
    @GetMapping("/optimisticLocker")
    public void optimisticLocker() {
        // 数据库标识
        int version = 0;
        User u = new User();
        u.setId("1208249557103063041");
        u.setStatus(version);
        u.setEmail("test@qq.com");
        if(userService.updateById(u)){
            System.out.println("Update successfully");
        }else{
            System.out.println("由于被其他人修改，更新失败(Update failed due to modified by others)");
        }

    }

    @ApiOperation(value = "测试返回值")
    @GetMapping("/response")
    public B<?> getB(){
        System.out.println(new B().num);


          return new B<>("我是字符串");
    }

    @ApiOperation(value = "测试多数据源事物")
    @PostMapping ("/updateBy")
    @Transactional(rollbackFor = Exception.class)
    public void update() throws IllegalAccessException {
        User byId = userService.getById("1242815762603823105");
        byId.setName("tom80");
        userService.updateById(byId);
        User byId1 = user2Service.getById("1242815762603823105");
        byId1.setName("tom88");
        user2Service.updateById(byId1);
        throw new IllegalAccessException("异常测试");

    }

}
