package com.lnson.simple.projname.caching.service;

import java.util.List;
import java.util.Map;

/**
 * -- 数据库表之间的关系：
 * -- 1、一对多
 * -- 2、多对一
 * -- 3、多对多
 * -- 4、一对一
 *
 * -- 如何在redis中存储关系型数据库表中的数据：
 * -- 1、针对每张表的数据，使用list存储所有的主键。列表list的key命名规范：表名:[主键字段名]
 * -- 2、每张表的每条记录使用hash存储。哈希hash的key命名规范：表名:[主键字段值]
 * -- 3、建立一对多的关系。Key的命名规范：[主键表名称]:[外键字段的值]:[外键表名称]
 */
public interface ShardedJedisService {

    public abstract void savePrimaryKey(String tableName, String pkFieldName, String... pkFieldValueList);

    public abstract void savePrimaryKey(String tableName, String pkFieldName, List<String> pkFieldValueList);

    public abstract void saveOne2MultipleRelation(String pkTableName, String fkTableName, String fkValue, String... fkTableIdList);

    public abstract void saveOne2MultipleRelation(String pkTableName, String fkTableName, String fkValue, List<String> fkTableIdList);

    public abstract void saveObject(final Object entity, String tableName, String pkFieldValue) throws Exception;

    public abstract void saveObject(final Map<String, String> entity, String tableName, String pkFieldValue);

    public abstract void deleteForeignKeyRelation(String pkTableName, String fkTableName, String fkValue, String fkTableId);

    public abstract void deleteObject(String tableName, String pkFieldName, String pkFieldValue);

    public abstract List<String> queryPrimaryKeyFieldValueList(String tableName, String pkFieldName);

    public abstract Map<String, String> queryForObject(String tableName, String pkFieldValue);

    public abstract <T> T queryForObject(String tableName, String pkFieldValue, Class<? extends T> clazz) throws Exception;

    public abstract List<Map<String, String>> queryForList(String tableName, String pkFieldName);

    public abstract <T> List<T> queryForList(String tableName, String pkFieldName, Class<? extends T> clazz) throws Exception;

    public abstract List<Map<String, String>> queryForList(String pkTableName, String fkTableName, String fkValue);

    public abstract <T> List<T> queryForList(String pkTableName, String fkTableName, String fkValue, Class<? extends T> clazz) throws Exception;

}
