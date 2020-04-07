package com.aaa.mybatisplus.test.annotation;

import com.aaa.mybatisplus.entity.City;
import com.aaa.mybatisplus.entity.City2;
import com.aaa.mybatisplus.entity.People;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description: 测试：注解--@ConditionalOnBean、@ConditionalOnClass
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/7
 */
@Slf4j
@Configuration
public class Config {

    @Bean
    public City city() {
        City city = new City();
        city.setCityName("千岛湖");
        return city;
    }
    @Bean
    public City2 city2() {
        City2 city2 = new City2();
        city2.setCityName("北极");
        return city2;
    }
    /**
     * 这里加了ConditionalOnBean注解，就代表如果city存在才实例化people
     */

    @Bean
    @ConditionalOnMissingBean(name = "city")
    //@Autowired那就默认代表当前Bean一定是已经存在的，如果为null，会报错。所以这里要修改下。
    public People people(City city) {
        //这里如果city实体没有成功注入 这里就会报空指针
//        city.setCityCode(301701);
        return new People("小小1", 1, city);
    }

    @Bean
    @ConditionalOnMissingBean(name = "city2")
    //@Autowired那就默认代表当前Bean一定是已经存在的，如果为null，会报错。所以这里要修改下。
    public People people2(City city) {
        //这里如果city实体没有成功注入 这里就会报空指针
//        city.setCityCode(301701);
        return new People("小小2", 2, city);
    }
}
