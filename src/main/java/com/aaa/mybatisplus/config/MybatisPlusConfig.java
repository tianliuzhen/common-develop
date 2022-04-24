package com.aaa.mybatisplus.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * description: Spring boot方式
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2019/12/21
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.aaa.mybatisplus.mapper*")
@Slf4j
public class MybatisPlusConfig {

  /*  @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);



        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 如果设置了全局逻辑删除、这里会失效，设置局部没关系已经测试
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser() {
            @Override
            public void processDelete(Delete delete) {
                // 如果你想自定义做点什么，可以重写父类方法像这样子
                if ("user_test".equals(delete.getTable().getName())) {
                    // 自定义跳过某个表，其他关联表可以调用 delete.getTables() 判断
                    log.info("跳过表:"+"user_test(这个表允许全部删除)");
                    return ;
                }
                super.processDelete(delete);
            }
        });
        paginationInterceptor.setSqlParserList(sqlParserList);

        return paginationInterceptor;
    }*/

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    // @Bean
    // public MybatisPlusInterceptor mybatisPlusInterceptor() {
    //     MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    //     interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
    //
    //         @Override
    //         public String getTenantIdColumn() {
    //             return "manager_id";
    //         }
    //
    //         @Override
    //         public Expression getTenantId() {
    //             return new LongValue(0);
    //         }
    //
    //         // 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
    //         @Override
    //         public boolean ignoreTable(String tableName) {
    //             return !"user".equalsIgnoreCase(tableName);
    //         }
    //     }));
    //     // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
    //     // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
    //     // interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
    //     return interceptor;
    // }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        /**
         * 1、【测试攻击 SQL】 阻断解析器、加入解析链
         *     自定义设置参考上面注释
         */
        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new BlockAttackSqlParser());


        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {
                // 该 where 条件 3.2.0 版本开始添加的，用于分区是否为在 where 条件中使用
                // 此判断用于支持返回多个租户 ID 场景，具体使用查看示例工程
                return new LongValue(0L);
            }

            @Override
            public String getTenantIdColumn() {
                //多租户自定义字段
                return "manager_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 这里可以判断是否过滤表
            /*
            if ("user".equals(tableName)) {
                return true;
            }*/
                return false;
            }
        });
        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
                // 意思允许 UserMapper下的这个 getAll 不用加  AND user.manager_id = 0
                if ("com.aaa.mybatisplus.mapper.UserMapper.getAll".equals(ms.getId())) {
                    return true;
                }
                return false;
            }
        });
        return paginationInterceptor;
    }


    /**
     * 乐观锁插件
     * 当要更新一条记录的时候，希望这条记录没有被别人更新
     * 乐观锁实现方式：
     * 取出记录时，获取当前version
     * 更新时，带上这个version
     * 执行更新时， set version = newVersion where version = oldVersion
     * 如果version不对，就更新失败
     * 乐观锁配置需要2步 记得两步
     * 1.插件配置
     * 2.注解实体字段 @Version 必须要!
     *
     * @return com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}


