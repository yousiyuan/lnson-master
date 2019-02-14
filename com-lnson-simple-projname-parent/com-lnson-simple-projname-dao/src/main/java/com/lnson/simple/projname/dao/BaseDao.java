package com.lnson.simple.projname.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<TEntity> {

	/**
	 * 查询Sql
	 * 
	 * @param sqlId
	 *            Statement语句Id
	 * @param object
	 *            参数
	 * @return 编译后的Sql语句
	 */
	public abstract String getSqlStatement(String sqlId, TEntity object);

	/**
	 * 新增
	 */
	public abstract void insertObject(String sqlId, TEntity object);

	/**
	 * 查询
	 * 
	 * @param primaryKey
	 *            主键
	 */
	public abstract TEntity queryForObject(String sqlId, Object primaryKey);

	/**
	 * 集合查询
	 */
	public List<TEntity> queryForList(String sqlId, TEntity object);

	/**
	 * 更新
	 */
	public abstract void updateObject(String sqlId, TEntity object);

	/**
	 * 键值对更新
	 */
	public abstract void updateObject(String sqlId, Map<String, Object> map);

	/**
	 * 删除
	 * 
	 * @param primaryKey
	 *            主键
	 */
	public abstract void deleteObject(String sqlId, Object primaryKey);

}
