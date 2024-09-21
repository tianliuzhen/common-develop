package com.aaa.commondevelop.service.impl;

import com.aaa.commondevelop.domain.entity.Entity;
import com.aaa.commondevelop.service.AsyncService;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author liuzhen.tian
 * @version 1.0 AsyncServiceImpl.java  2020/10/20 23:36
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private AsyncTask asyncTask;

    @SneakyThrows
    @Override
    public List<Entity> queryData() {
        List<Entity> entityList = getEntityList();
        CountDownLatch countDownLatch = new CountDownLatch(entityList.size());
        for (Entity entity : entityList) {
            // 模拟异步调用接口修改，增加返回值
            asyncTask.queryTask(entity,countDownLatch);
        }
        countDownLatch.await();
        return entityList;
    }

    private List<Entity> getEntityList() {
        List<Entity> list = Lists.newArrayList();
        list.add(new Entity("id=1"));
        list.add(new Entity("id=3"));
        list.add(new Entity("id=4"));
        return list;
    }
}
