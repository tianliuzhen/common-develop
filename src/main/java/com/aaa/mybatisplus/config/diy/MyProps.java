package com.aaa.mybatisplus.config.diy;

import com.aaa.mybatisplus.domain.entity.City;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/4/17
 */
@Component
@Data
@ConfigurationProperties(prefix = "my-props")
public class MyProps {

    private List<String> list;

    private List<City> cityList;

    private Map<String,String> maps;

    private Map<String,List<String>> varmaplist;
}

