package com.aaa.mybatisplus.redis;

import com.aaa.mybatisplus.entity.User;
import com.aaa.mybatisplus.entity.UserDto;
import com.aaa.mybatisplus.enums.GenderEnum;
import com.aaa.mybatisplus.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/22
 */
@RestController
public class TestRedisHash {

    @Autowired
    RedisTemplate redisTemplate;



    @PostMapping("/testRedisHash")
    public void testRedisHash(){
        redisTemplate.opsForHash().put("h:z","adr","bj");
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name", "tom");
        map.put("sex", "man");
        // hash 存入一个对象
        redisTemplate.opsForHash().put("h:z","userDto",map);
        Object userDto1 = redisTemplate.opsForHash().get("h:z", "userDto");
        // 将map 转实体
        UserDto userDto2 = JSONObject.parseObject(JSONObject.toJSONString(userDto1), UserDto.class);
        System.out.println(userDto2.toString());
        //测试枚举转换
        testString();
        //测试事物
        testMulti();
    }
    public void testString() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId("1");
        user.setAge(GenderEnum.FEMALE);
        User user2 = new User();
        user2.setId("2");
        user2.setAge(GenderEnum.MALE);
        list.add(user);
        list.add(user2);
        String string = JSONObject.toJSONString(list);
        List<User> users = JSON.parseObject(string, new TypeReference<List<User>>() {
        });

    }
    public void testMulti(){
        //开启事务支持，在同一个 Connection 中执行命令
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.execute(new SessionCallback<Object>(){

            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                //开启事务
                operations.multi();
                for(int i = 1; i <= 5; i++) {
                    redisTemplate.convertAndSend("channel:test", String.format("我是消息{%d}号: %tT", i, new Date()));
                    redisTemplate.opsForValue().set("key:" + i,"key" + i);
                }
                if (true){
                    //测试事物异常是否会提交
//                    throw new IllegalArgumentException("测试");
                }


                //执行事务
                operations.exec();
                return null;
            }
        });
    }
}
