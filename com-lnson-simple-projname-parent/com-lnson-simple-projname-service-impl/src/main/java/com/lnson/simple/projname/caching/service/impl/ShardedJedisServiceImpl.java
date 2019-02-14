package com.lnson.simple.projname.caching.service.impl;

import com.lnson.simple.projname.caching.service.ShardedJedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("shardedJedisService")
public class ShardedJedisServiceImpl implements ShardedJedisService {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    private ShardedJedis shardedJedis;

    public ShardedJedisServiceImpl() {
    }

    private ShardedJedis getShardedJedis() {
        if (shardedJedis == null)
            shardedJedis = shardedJedisPool.getResource();
        return shardedJedis;
    }

    @Override
    protected void finalize() {
        if (shardedJedis != null)
            shardedJedis.close();
    }

    /**
     * 保存表主键
     *
     * @param tableName        数据库表名
     * @param pkFieldName      主键字段名
     * @param pkFieldValueList 主键字段值集合
     */
    @Override
    public void savePrimaryKey(String tableName, String pkFieldName, String... pkFieldValueList) {
        getShardedJedis().rpush(MessageFormat.format("{0}:{1}", tableName, pkFieldName), pkFieldValueList);
    }

    /**
     * 保存表主键
     *
     * @param tableName        数据库表名
     * @param pkFieldName      主键字段名
     * @param pkFieldValueList 主键字段值集合
     */
    @Override
    public void savePrimaryKey(String tableName, String pkFieldName, List<String> pkFieldValueList) {
        getShardedJedis().rpush(MessageFormat.format("{0}:{1}", tableName, pkFieldName),
                pkFieldValueList.toArray(new String[pkFieldValueList.size()]));
    }

    /**
     * 主键表(tableNamePK)的主键字段fkValue在外键表(tableNameFK)中做外键，
     * 外键表(tableNameFK)有一个依赖表(tableNamePK)的字段fkValue。
     *
     * @param pkTableName   主键表的名称
     * @param fkTableName   外键表的名称
     * @param fkValue       外键字段值
     * @param fkTableIdList 外键表中主键标识列值的集合
     */
    @Override
    public void saveOne2MultipleRelation(String pkTableName, String fkTableName, String fkValue, String... fkTableIdList) {
        String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);
        getShardedJedis().rpush(key, fkTableIdList);
    }

    /**
     * 主键表(tableNamePK)的主键字段fkValue在外键表(tableNameFK)中做外键，
     * 外键表(tableNameFK)有一个依赖表(tableNamePK)的字段fkValue。
     *
     * @param pkTableName   主键表的名称
     * @param fkTableName   外键表的名称
     * @param fkValue       外键字段值
     * @param fkTableIdList 外键表中主键标识列值的集合
     */
    @Override
    public void saveOne2MultipleRelation(String pkTableName, String fkTableName, String fkValue, List<String> fkTableIdList) {
        String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);
        getShardedJedis().rpush(key, fkTableIdList.toArray(new String[fkTableIdList.size()]));
    }

    /**
     * 保存记录
     *
     * @param entity       数据实体
     * @param tableName    数据库表名
     * @param pkFieldValue 数据库表主键字段值
     */
    @Override
    public void saveObject(final Object entity, String tableName, String pkFieldValue) throws Exception {
        String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);
        Class<? extends Object> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String getMethodName = MessageFormat.format("get{0}{1}", Character.toUpperCase(name.charAt(0)),
                    name.substring(1));
            Method method = clazz.getDeclaredMethod(getMethodName, new Class<?>[]{});
            Object value = method.invoke(entity, new Object[]{});
            getShardedJedis().hset(key, name, value.toString());
        }
    }

    /**
     * 保存记录
     *
     * @param entity       数据实体
     * @param tableName    数据库表名
     * @param pkFieldValue 数据库表主键字段值
     */
    @Override
    public void saveObject(final Map<String, String> entity, String tableName, String pkFieldValue) {
        String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);

        Set<String> fields = entity.keySet();
        for (String field : fields) {
            String value = entity.get(field);
            getShardedJedis().hset(key, field, value);
        }
    }

    /**
     * 删除外键关系
     *
     * @param pkTableName 主键表名称
     * @param fkTableName 外键表名称
     * @param fkValue     外键字段值
     * @param fkTableId   外键表的主键字段值
     */
    @Override
    public void deleteForeignKeyRelation(String pkTableName, String fkTableName, String fkValue, String fkTableId) {
        String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);
        getShardedJedis().lrem(key, 0, fkTableId);// 删除对应的外键关系
    }

    /**
     * 删除一条记录
     *
     * @param tableName    数据库表名
     * @param pkFieldName  数据库表主键字段名
     * @param pkFieldValue 数据库表主键字段值
     */
    @Override
    public void deleteObject(String tableName, String pkFieldName, String pkFieldValue) {
        String pkListKey = MessageFormat.format("{0}:{1}", tableName, pkFieldName);
        getShardedJedis().lrem(pkListKey, 0, pkFieldValue);// 删除对应的主键信息

        String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);
        getShardedJedis().del(key);// 删除主键对应的数据集
    }

    /**
     * 获取主键集合
     *
     * @param tableName   数据库表名
     * @param pkFieldName 主键字段名
     * @return 数据库表主键值列表
     */
    @Override
    public List<String> queryPrimaryKeyFieldValueList(String tableName, String pkFieldName) {
        String key = MessageFormat.format("{0}:{1}", tableName, pkFieldName);
        List<String> primaryKeyList = getShardedJedis().lrange(key, 0, -1);
        return primaryKeyList;
    }

    /**
     * 获得一条记录
     *
     * @param tableName    数据库表名
     * @param pkFieldValue 数据库表主键字段值
     * @return 返回 Field-Value 对集合
     */
    @Override
    public Map<String, String> queryForObject(String tableName, String pkFieldValue) {
        String key = MessageFormat.format("{0}:{1}", tableName, pkFieldValue);
        Map<String, String> fieldValue = getShardedJedis().hgetAll(key);
        return fieldValue;
    }

    /**
     * 获得一条记录
     *
     * @param tableName    数据库表名
     * @param pkFieldValue 数据库表主键字段值
     * @param clazz        POJO类型
     */
    @Override
    public <T> T queryForObject(String tableName, String pkFieldValue, Class<? extends T> clazz) throws Exception {
        Map<String, String> objMap = this.queryForObject(tableName, pkFieldValue);

        Constructor<?> constructor = clazz.getDeclaredConstructor(new Class<?>[]{});
        Object instance = constructor.newInstance(new Object[]{});
        Field[] fieldArray = clazz.getDeclaredFields();
        for (Field field : fieldArray) {
            Class<?> fieldType = field.getType();
            String name = field.getName();
            String value = objMap.get(name);
            String setMethodName = MessageFormat.format("set{0}{1}", Character.toUpperCase(name.charAt(0)),
                    name.substring(1));
            Method method = clazz.getDeclaredMethod(setMethodName, new Class<?>[]{fieldType});

            if (fieldType.equals(Short.class))
                method.invoke(instance, Short.valueOf(value.toString()));
            else if (fieldType.equals(Integer.class))
                method.invoke(instance, Integer.valueOf(value.toString()));
            else if (fieldType.equals(Long.class))
                method.invoke(instance, Long.valueOf(value.toString()));
            else if (fieldType.equals(Float.class))
                method.invoke(instance, Float.valueOf(value.toString()));
            else if (fieldType.equals(Double.class))
                method.invoke(instance, Double.valueOf(value.toString()));
            else if (fieldType.equals(Boolean.class))
                method.invoke(instance, Boolean.valueOf(value.toString()));
            else if (fieldType.equals(String.class))
                method.invoke(instance, value.toString());
            else if (fieldType.equals(Date.class))
                method.invoke(instance,
                        new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(value.toString()));
        }

        T entity = clazz.cast(instance);
        return entity;
    }

    /**
     * 查询数据库表所有记录的列表
     *
     * @param tableName   数据库表名称
     * @param pkFieldName 数据库表主键字段名
     */
    @Override
    public List<Map<String, String>> queryForList(String tableName, String pkFieldName) {
        List<String> pkFieldValueList = this.queryPrimaryKeyFieldValueList(tableName, pkFieldName);
        List<Map<String, String>> list = new LinkedList<Map<String, String>>();
        for (String pkValue : pkFieldValueList) {
            Map<String, String> objectMap = this.queryForObject(tableName, pkValue);
            list.add(objectMap);
        }
        return list;
    }

    /**
     * 查询数据库表所有记录的列表
     *
     * @param tableName   数据库表名称
     * @param pkFieldName 数据库表主键字段名称
     * @param clazz       POJO类型
     */
    @Override
    public <T> List<T> queryForList(String tableName, String pkFieldName, Class<? extends T> clazz) throws Exception {
        List<String> pkFieldValueList = this.queryPrimaryKeyFieldValueList(tableName, pkFieldName);
        List<T> list = new LinkedList<T>();
        for (String pkValue : pkFieldValueList) {
            T object = queryForObject(tableName, pkValue, clazz);
            list.add(object);
        }
        return list;
    }

    /**
     * 根据外键查询列表
     *
     * @param pkTableName 主键表名称
     * @param fkTableName 外键表名称
     * @param fkValue     外键字段的值
     */
    @Override
    public List<Map<String, String>> queryForList(String pkTableName, String fkTableName, String fkValue) {
        String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);

        List<Map<String, String>> list = new LinkedList<Map<String, String>>();
        List<String> fkTableIdList = getShardedJedis().lrange(key, 0, -1);
        for (String fkTableId : fkTableIdList) {
            Map<String, String> objMap = this.queryForObject(fkTableName, fkTableId);
            list.add(objMap);
        }
        return list;
    }

    /**
     * 根据外键查询列表
     *
     * @param pkTableName 主键表名称
     * @param fkTableName 外键表名称
     * @param fkValue     外键字段的值
     * @param clazz       外键表在java中对应的数据实体类型
     */
    @Override
    public <T> List<T> queryForList(String pkTableName, String fkTableName, String fkValue, Class<? extends T> clazz) throws Exception {
        String key = MessageFormat.format("{0}:{1}:{2}", pkTableName, fkValue, fkTableName);

        List<T> list = new LinkedList<T>();
        List<String> fkTableIdList = getShardedJedis().lrange(key, 0, -1);
        for (String fkTableId : fkTableIdList) {
            T object = this.queryForObject(fkTableName, fkTableId, clazz);
            list.add(object);
        }
        return list;
    }

}
