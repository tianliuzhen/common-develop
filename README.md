# mybatis-plus
### 这里对接远程仓库
https://github.com/tianliuzhen/dynamic_dataSource/tree/master/mybatis-plus <br/>
延续子模块 mybatis-plus 继续完善 <br/>
   ## <b>全局配置</b>
1、整理了 `动态数据源` 可切换 hikariCP和druid<br/>
2、基于@ExceptionHandler配置了，全局异常输出<br/>
3、基于@ControllerAdvice配置了，controller 统一状态码返回<br/>
   ## <b>mybatis-plus配置</b>
1、配置代码生成器<br/>
2、使用`P6Spy` 监控sql<br/>
3、配置通用类枚举<br/>
4、配置主键生成Id （默认是雪花算法）<br/>
5、配置myabtis-plus 分页插件<br/>
6、配置乐观锁<br/>
7、配置多租户sql解析器<br/>
8、配置条件构造器<br/>
   ##  <b>redis配置</b>
1、redisTemplate 常用接口测试<br/>
2、redis  多数据库配置、采用fastjson 序列化、redis注解设置<br/>
3、redisTemplate + lua 分布式锁设计<br/>
   ##  <b>自定义注解配置</b>
1、@SysLog 统计执行时间<br/>
2、@ParameterInfo 通过 ParameterInfoInterceptor 自定义参数拦截器<br/>
3、@Limit 接口限流 通过 LimitAspect 基于 aop 和 redis +lua 实现接口限流<br/>
4、@AccessLimit 通过AccessLimitInterceptor 实现防止 API 接口防刷和IP拦截<br/>
   ##  <b>测试部分</b>
1、基于 CountDownLatch和Semaphore及线程池实现并发测试。

......
