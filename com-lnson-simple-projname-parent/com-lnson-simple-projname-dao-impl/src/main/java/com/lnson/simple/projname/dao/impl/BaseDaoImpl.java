package com.lnson.simple.projname.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lnson.simple.projname.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public abstract class BaseDaoImpl<TEntity> extends SqlSessionDaoSupport implements BaseDao<TEntity> {

	protected static Logger logger = LogManager.getLogger(BaseDaoImpl.class);

	@Autowired
	protected SqlSession sqlSession;

	@Autowired
	protected SqlSessionFactory sqlSessionFactory;

	@Autowired
	@Override
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	@Override
	public String getSqlStatement(String sqlId, TEntity object) {
		return sqlSessionFactory.getConfiguration().getMappedStatement(sqlId).getBoundSql(object).getSql();
	}

	@Override
	public void insertObject(String sqlId, TEntity object) {
		logger.debug("insert parameter sqlId:" + sqlId + ", object：" + JSON.toJSONString(object,
				SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));

		String sqlStatement = this.getSqlStatement(sqlId, object);
		logger.debug(sqlStatement);
		sqlSession.insert(sqlId, object);
	}

	@Override
	public TEntity queryForObject(String sqlId, Object primaryKey) {
		logger.debug("query parameter sqlId：" + sqlId + ", primaryKey：" + primaryKey);

		String sqlStatement = this.getSqlStatement(sqlId, null);
		logger.debug(sqlStatement);
		return sqlSession.selectOne(sqlId, primaryKey);
	}

	@Override
	public void updateObject(String sqlId, TEntity object) {
		logger.debug("update parameter sqlId：" + sqlId + ", object：" + JSON.toJSONString(object,
				SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));

		String sqlStatement = this.getSqlStatement(sqlId, object);
		logger.debug(sqlStatement);
		sqlSession.update(sqlId, object);
	}

	@Override
	public void updateObject(String sqlId, Map<String, Object> map) {
		logger.debug("update parameter sqlId：" + sqlId + ", map："
				+ JSON.toJSONString(map, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));

		String sqlStatement = this.getSqlStatement(sqlId, null);
		logger.debug(sqlStatement);
		sqlSession.update(sqlId, map);
	}

	@Override
	public void deleteObject(String sqlId, Object primaryKey) {
		logger.debug("delete parameter sqlId：" + sqlId + ", primaryKey：" + primaryKey);

		String sqlStatement = this.getSqlStatement(sqlId, null);
		logger.debug(sqlStatement);
		sqlSession.delete(sqlId, primaryKey);
	}

	@Override
	public List<TEntity> queryForList(String sqlId, TEntity object) {
		logger.debug("querylist parameter sqlId:" + sqlId + ", object：" + JSON.toJSONString(object,
				SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));

		String sqlStatement = this.getSqlStatement(sqlId, object);
		logger.debug(sqlStatement);
		return sqlSession.selectList(sqlId, object);
	}

}
