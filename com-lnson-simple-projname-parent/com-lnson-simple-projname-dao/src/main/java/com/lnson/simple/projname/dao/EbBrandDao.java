package com.lnson.simple.projname.dao;

import com.lnson.simple.projname.entity.EbBrand;

public interface EbBrandDao extends BaseDao<EbBrand> {

    public abstract int count(String sqlId);

}
