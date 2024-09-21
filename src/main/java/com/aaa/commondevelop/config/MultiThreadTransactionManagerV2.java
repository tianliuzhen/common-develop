package com.aaa.commondevelop.config;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多线程事务管理器
 * 基于分布式事务思想，采用2PC（Two-phase commit protocol）协议
 * 解决基于线程池的多线程事务一致性问题
 * <p>
 * （有问题，不推荐）
 *
 * @author liuzhen.tian
 * @version 1.0 MultiThreadTransactionManager.java  2024/3/19 21:32
 */
@Slf4j
public class MultiThreadTransactionManagerV2 {

    /**
     * 事务管理器
     */
    private final PlatformTransactionManager transactionManager;

    /**
     * 超时时间
     */
    private final long timeout;

    /**
     * 时间单位
     */
    private final TimeUnit unit;

    /**
     * 一阶段门闩，(第一阶段的准备阶段)，当所有子线程准备完成时（除“提交/回滚”操作以外的工作都完成），countDownLatch的值为0
     */
    private CountDownLatch oneStageLatch = null;

    /**
     * 二阶段门闩，(第二阶段的执行执行)，主线程将不再等待子线程执行，直接判定总的任务执行失败，执行第二阶段让等待确认的线程进行回滚
     */
    private final CountDownLatch twoStageLatch = new CountDownLatch(1);

    /**
     * 是否提交事务，默认是true（当任一线程发生异常时，isSubmit会被设置为false，即回滚事务）
     */
    private final AtomicBoolean isSubmit = new AtomicBoolean(true);

    /**
     * 构造方法
     *
     * @param transactionManager 事务管理器
     * @param timeout            超时时间
     * @param unit               时间单位
     */
    public MultiThreadTransactionManagerV2(PlatformTransactionManager transactionManager, long timeout, TimeUnit unit) {
        this.transactionManager = transactionManager;
        this.timeout = timeout;
        this.unit = unit;
    }

    /**
     * 线程池方式执行任务，可保证线程间的事务一致性
     *
     * @param runnableList 任务列表
     * @param executor     线程池
     * @return
     */
    public boolean execute(List<Runnable> runnableList, ExecutorService executor) {
        // 属性初始化
        innit(runnableList.size());

        /**
         * at org.springframework.transaction.support.TransactionSynchronizationManager.unbindResource(TransactionSynchronizationManager.java:198)
         * 如果不复制事务的一些属性，会直接报错
         */
        Map<TransactionStatus, TransactionResource> transactionResourceMap = new ConcurrentHashMap<>();

        // 遍历任务列表并放入线程池
        for (Runnable runnable : runnableList) {
            // 创建线程
            executor.execute((() -> {
                // 如果别的线程执行失败，则该任务就不需要再执行了
                if (!isSubmit.get()) {
                    log.info("当前子线程执行中止，因为线程事务中有子线程执行失败");
                    oneStageLatch.countDown();
                    return;
                }
                //  继续事务资源复制,因为在sql执行是会产生新的资源对象
                transactionResourceMap.put(transactionManager.getTransaction(new DefaultTransactionDefinition()),
                        TransactionResource.copyTransactionResource());
                try {
                    // 执行业务逻辑
                    runnable.run();
                } catch (Exception e) {
                    // 执行体发生异常，设置回滚
                    isSubmit.set(false);
                    log.error("线程{}:业务发生异常,执行体:{}", Thread.currentThread().getName(), runnable);
                    log.error("业务发生异常", e);
                }
                // 计数器减一
                oneStageLatch.countDown();
            }));
        }

        /**
         * 主线程担任协调者，当第一阶段所有参与者准备完成，oneStageLatch的计数为0
         * 主线程发起第二阶段，执行阶段（提交或回滚）,根据
         */
        try {
            // 主线程等待所有线程执行完成，超时时间设置为五秒
            oneStageLatch.await(timeout, unit);
            long count = oneStageLatch.getCount();
            System.out.println("countDownLatch值：" + count);
            // 主线程等待超时，子线程可能发生长时间阻塞，死锁
            if (count > 0) {
                // 设置为回滚
                isSubmit.set(false);
                log.info("主线线程等待超时,任务即将全部回滚");
            }
            if (!isSubmit.get()) {
                log.info("发生异常,全部事务回滚");
                transactionResourceMap.forEach((transactionStatus, transactionResource) -> {
                    transactionResource.autoWiredTransactionResource();
                    Map<Object, Object> rollBackResourceMap = TransactionSynchronizationManager.getResourceMap();
                    log.info("回滚前事务资源size{}，本身{}", rollBackResourceMap.size(), rollBackResourceMap);
                    transactionManager.rollback(transactionStatus);
                    transactionResource.removeTransactionResource();
                });
            } else {
                log.info("全部事务正常提交");
                transactionResourceMap.forEach((transactionStatus, transactionResource) -> {
                    transactionResource.autoWiredTransactionResource();
                    Map<Object, Object> commitResourceMap = TransactionSynchronizationManager.getResourceMap();
                    log.info("提交前事务资源size{}，本身{}", commitResourceMap.size(), commitResourceMap);
                    transactionManager.commit(transactionStatus);
                    transactionResource.removeTransactionResource();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 返回结果，是否执行成功，事务提交即为执行成功，事务回滚即为执行失败
        return isSubmit.get();
    }

    /**
     * 初始化属性
     *
     * @param size 任务数量
     */
    private void innit(int size) {
        oneStageLatch = new CountDownLatch(size);
    }


    /**
     * 保存当前事务资源,用于线程间的事务资源COPY操作
     */
    @Builder
    private static class TransactionResource {
        //事务结束后默认会移除集合中的DataSource作为key关联的资源记录
        private Map<Object, Object> resources = new HashMap<>();

        //下面五个属性会在事务结束后被自动清理,无需我们手动清理
        private Set<TransactionSynchronization> synchronizations = new HashSet<>();

        private String currentTransactionName;

        private Boolean currentTransactionReadOnly;

        private Integer currentTransactionIsolationLevel;

        private Boolean actualTransactionActive;

        public static TransactionResource copyTransactionResource() {
            return TransactionResource.builder()
                    //返回的是不可变集合，这里为了更加灵活，copy出一个集合过来
                    .resources(new HashMap<>(TransactionSynchronizationManager.getResourceMap()))
                    //如果需要注册事务监听者,这里记得修改--我们这里不需要,就采用默认负责--spring事务内部默认也是这个值
                    .synchronizations(new LinkedHashSet<>(TransactionSynchronizationManager.getSynchronizations()))
                    .currentTransactionName(TransactionSynchronizationManager.getCurrentTransactionName())
                    .currentTransactionReadOnly(TransactionSynchronizationManager.isCurrentTransactionReadOnly())
                    .currentTransactionIsolationLevel(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel())
                    .actualTransactionActive(TransactionSynchronizationManager.isActualTransactionActive())
                    .build();
        }

        //装配事务资源，为提交/回滚做储备
        public void autoWiredTransactionResource() {
            //获取当前线程事务资源
            Map<Object, Object> resourceMap = TransactionSynchronizationManager.getResourceMap();
            for (Object o : resourceMap.keySet()) {
                if (resourceMap.containsKey(o)) {
                    //移除重复事务资源key，避免绑定报错
                    resources.remove(o);
                }

            }
            boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
            //绑定事务资源，注意 绑定是绑定到当前主线程上，记得最后释放交换主线程，再由主线程收回原有事务自选
            resources.forEach(TransactionSynchronizationManager::bindResource);
            //如果需要注册事务监听者,这里记得修改--我们这里不需要,就采用默认负责--spring事务内部默认也是这个值
            //避免重复激活或者事务未激活
            if (!synchronizationActive) {
                TransactionSynchronizationManager.initSynchronization();
            }

            TransactionSynchronizationManager.setActualTransactionActive(actualTransactionActive);
            TransactionSynchronizationManager.setCurrentTransactionName(currentTransactionName);
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(currentTransactionIsolationLevel);
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(currentTransactionReadOnly);
        }

        public void removeTransactionResource() {
            Map<Object, Object> resourceMap = new HashMap<>(TransactionSynchronizationManager.getResourceMap());

            //事务结束后默认会移除集合中的DataSource作为key关联的资源记录
            //DataSource如果重复移除,unbindResource时会因为不存在此key关联的事务资源而报错
            resources.keySet().forEach(key -> {
                if (resourceMap.containsKey(key)) {
                    TransactionSynchronizationManager.unbindResource(key);
                }
            });

            // 释放ThreadLocal 防止内存泄漏
            TransactionSynchronizationManager.clear();
        }
    }

}


