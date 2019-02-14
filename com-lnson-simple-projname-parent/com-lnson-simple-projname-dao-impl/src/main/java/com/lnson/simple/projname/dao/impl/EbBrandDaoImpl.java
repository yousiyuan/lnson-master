package com.lnson.simple.projname.dao.impl;

import com.lnson.simple.projname.dao.EbBrandDao;
import com.lnson.simple.projname.entity.EbBrand;
import org.springframework.stereotype.Repository;

@Repository
public class EbBrandDaoImpl extends BaseDaoImpl<EbBrand> implements EbBrandDao {

    @Override
    public int count(String sqlId) {
        logger.debug("count parameter sqlId：" + sqlId);
        return sqlSession.selectOne(sqlId);
    }

}
