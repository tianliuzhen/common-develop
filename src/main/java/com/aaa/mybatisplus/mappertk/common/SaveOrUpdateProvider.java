package com.aaa.mybatisplus.mappertk.common;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.provider.SaveProvider;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author liuzhen.tian
 * @version 1.0 SaveOrUpdateProvider.java  2023/12/6 22:20
 */
public class SaveOrUpdateProvider extends SaveProvider {
    public SaveOrUpdateProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 这里重写了现有的save方法，
     * 因为现有的save方法，对于带下划线的主键，无法转驼峰
     * <p>
     * 保存策略: 如果主键不为空则更新记录, 如果没有主键或者主键为空,则插入.
     *
     * @param ms
     * @return
     * @see SaveProvider#save(MappedStatement)
     */
    public String saveOrUpdate(MappedStatement ms) {

        Class<?> entityClass = getEntityClass(ms);
        Field[] fields = entityClass.getFields();
        StringBuilder sql = new StringBuilder();

        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = (EntityColumn) columnList.iterator().next();
            /**
             * org.apache.ibatis.reflection.ReflectionException:
             * There is no getter for property named 'user_id'
             * in 'class com.aaa.mybatisplus.domain.entity.WxUser'
             *
             * 这里没有转驼峰，如果主键是 user_id,那这里就会报错。
             * 所以重写此类
             */
            String id = column.getColumn();
            sql.append("<choose>");
            sql.append("<when test='" + toCamelCase(id) + "!=null'>");
            sql.append(this.updateByPrimaryKey(ms));
            sql.append("</when>");
            sql.append("<otherwise>");
            sql.append(insert(ms));
            sql.append("</otherwise>");
            sql.append("</choose>");
            return sql.toString();
        }
        return insert(ms);
    }

    /**
     * 设置允许更新不为空的值
     *
     * @param ms
     * @return
     */
    @Override
    public String updateByPrimaryKey(MappedStatement ms) {
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.updateSetColumns(entityClass, (String) null, true, true));
        sql.append(SqlHelper.wherePKColumns(entityClass, true));
        return sql.toString();
    }

    public static String toCamelCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        String[] words = s.split("_"); // 按照下划线分割字符串
        StringBuilder camelCase = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                camelCase.append(words[i].toLowerCase()); // 第一个单词首字母小写
            } else {
                camelCase.append(words[i].substring(0, 1).toUpperCase()); // 其他单词首字母大写
                camelCase.append(words[i].substring(1).toLowerCase()); // 其他单词首字母小写
            }
        }
        return camelCase.toString();
    }
}
