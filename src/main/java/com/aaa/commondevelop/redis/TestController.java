package com.aaa.commondevelop.redis;

import com.aaa.commondevelop.domain.dto.AdReadMonitorDTO;
import com.aaa.commondevelop.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/3/17
 */
@RestController
public class TestController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    ValueOperations<String, AdReadMonitorDTO> listOperations;

    public TestController(RedisTemplate redisTemplate ) {
        this.listOperations = redisTemplate.opsForValue();;
    }

    @PostMapping("/testRedis")
    public void testRedisPushAndPop(){
        redisTemplate.opsForList().leftPush("test-tlz1", JSONObject.toJSON(new AdReadMonitorDTO(1L,1L)));
        redisTemplate.opsForList().leftPush("test-tlz2", JSONObject.toJSON(new AdReadMonitorDTO(2L,2L)));
        redisTemplate.opsForList().leftPush("test-tlz", JSONObject.toJSON(new AdReadMonitorDTO(3L,3L)));
        Object str=redisTemplate.opsForList().leftPop("test-tlz");;
        while (str!=null){
         AdReadMonitorDTO adReadMonitorDTO = JSON.parseObject(str.toString(), AdReadMonitorDTO.class);
         System.out.println(adReadMonitorDTO.toString());
            str=   redisTemplate.opsForList().leftPop("test-tlz");
     }

    }

    @GetMapping("/testRedisOperations")
    public void testRedisOperations(){
        listOperations.set("asd",new AdReadMonitorDTO(3L,3L));
        AdReadMonitorDTO asd = listOperations.get("asd");
        System.out.println(asd.toString());
    }

}
