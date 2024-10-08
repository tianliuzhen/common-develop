package com.aaa.commondevelop.service.impl;

import com.aaa.commondevelop.domain.entity.Entity;
import com.aaa.commondevelop.service.BaseService;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuzhen.tian
 * @version 1.0 AaBaseService.java  2022/1/18 21:40
 */

@Service
public class AaBaseService implements BaseService {


    @Override
    public List<Entity> queryData() {
        List<Entity> list = Lists.newArrayList();
        list.add(new Entity("id=1"));
        list.add(new Entity("id=3"));
        list.add(new Entity("id=4"));
        return list;
    }
}
