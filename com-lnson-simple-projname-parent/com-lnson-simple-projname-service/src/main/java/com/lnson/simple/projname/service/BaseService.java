package com.lnson.simple.projname.service;

import java.util.List;
import java.util.Map;

public interface BaseService<TEntity> {

	/**
	 * 获取Sql语句
	 */
	public abstract String getSqlStatement(String sqlId, TEntity object);

	/**
	 * 新增
	 */
	public abstract void insertObject(TEntity object);

	/**
	 * 查询
	 * 
	 * @param primaryKey
	 *            主键
	 */
	public abstract TEntity queryForObject(Object primaryKey);

	/**
	 * 更新
	 */
	public abstract void updateObject(TEntity object);

	/**
	 * 键值对更新
	 */
	public default void updateObject(String sqlId, Map<String, Object> map) throws Exception {
		throw new Exception("调用了未实现的方法");
	}

	/**
	 * 删除
	 * 
	 * @param primaryKey
	 *            主键
	 */
	public abstract void deleteObject(Object primaryKey);

	/**
	 * 集合查询
	 */
	public default List<TEntity> queryForList(String sqlId, TEntity object) throws Exception {
		throw new Exception("调用了未实现的方法");
	}

}
