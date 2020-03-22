package com.aaa.mybatisplus.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * description: redis 这里的list 更像 linkedList
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/22
 */
@RestController
public class TestRedisList {

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/testRedisList")
    public void testRedisList(){
        //1 .从左侧加入元素，返回的结果是下标
        Long aLong1 = redisTemplate.opsForList().leftPush("l:a", 1);
        redisTemplate.opsForList().leftPush("l:a", 2);
        redisTemplate.opsForList().leftPush("l:a", 3);
        System.out.println(aLong1);
        redisTemplate.opsForList().leftPush("l:a", 8);
        //2. 从左侧插入值，如果list不存在，则不操作
        redisTemplate.opsForList().leftPushIfPresent("l:b", 1);
        Long aLong = redisTemplate.opsForList().leftPush("l:a", 9);
        System.out.println(aLong);
        //3. 从左侧移除第一个元素，返回的结果是  删除元素的值
        Object o = redisTemplate.opsForList().leftPop("l:a");
        System.out.println(o);
        //4. 获取指下标的集合
        redisTemplate.opsForList().index("l:a", 1);
        //5. 获取总长度
        redisTemplate.opsForList().size("l:a");
        //6. 对列表进行修改，让列表只保留指定区间的元素，不在指定区间的元素就会被删除
        redisTemplate.opsForList().trim("l:a",1,2);
        //7. 指定索引的值
        redisTemplate.opsForList().set("l:a",1,890);
        //8. 获取自定范围的值
        redisTemplate.opsForList().range("l:a", 0, -1);
        redisTemplate.delete("l:a");
        //9. 从右边 移处一个元素生成到 新的集合里面
            /**
             * eg：我这里 如果设置了 时间的话，可以给个默认值 如： 10s
             * 如果在规定时间内 没有元素的话，可以等待10s ，超过十秒就自动结束
            * */
        Object o1 = redisTemplate.opsForList().rightPopAndLeftPush("l:a", "l:new",10,TimeUnit.SECONDS );
        System.out.println(o1);
    }

}
