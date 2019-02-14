package com.lnson.simple.projname.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lnson.simple.projname.dao.EbBrandDao;
import com.lnson.simple.projname.entity.EbBrand;
import com.lnson.simple.projname.service.EbBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ebBrandService")
public class EbBrandServiceImpl extends BaseServiceImpl<EbBrand> implements EbBrandService {

	@Autowired
	private EbBrandDao ebBrandDao;

	public EbBrandServiceImpl() {
		this.setInsertSqlId("mapper.EbBrandMapper.insertSelective");
		this.setDeleteSqlId("mapper.EbBrandMapper.deleteByPrimaryKey");
		this.setUpdateSqlId("mapper.EbBrandMapper.updateByPrimaryKeySelective");
		this.setQuerySqlId("mapper.EbBrandMapper.selectByPrimaryKey");
	}

	@Override
	public int count() {
		return ebBrandDao.count("mapper.EbBrandMapper.count");
	}

	/**
	 * 动态查询列表
	 */
	@Override
	public List<EbBrand> queryForList(String sqlId, EbBrand object) {
		logger.debug("query list parameter sqlId：" + sqlId + ", object：" + JSON.toJSONString(object,
				SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));
		return ebBrandDao.queryForList(sqlId, object);
	}

}
