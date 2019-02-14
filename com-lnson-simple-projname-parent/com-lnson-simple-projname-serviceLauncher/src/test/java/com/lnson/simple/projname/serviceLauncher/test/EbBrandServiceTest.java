package com.lnson.simple.projname.serviceLauncher.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lnson.simple.projname.entity.EbBrand;
import com.lnson.simple.projname.service.EbBrandService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class EbBrandServiceTest {

    @Autowired
    private EbBrandService ebBrandService;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * 统计数据
     */
    @Test
    public void countTest() {
        int count = ebBrandService.count();
        System.out.println(count);
    }

    /**
     * 查询列表
     */
    @Test
    public void queryForListTest() throws Exception {
        EbBrand entity = new EbBrand();
        List<EbBrand> ebBrandList = ebBrandService.queryForList("mapper.EbBrandMapper.queryEbBrandListSelective",
                entity);
        String jsonStr = JSON.toJSONString(ebBrandList, SerializerFeature.WRITE_MAP_NULL_FEATURES,
                SerializerFeature.QuoteFieldNames);
        System.out.println(jsonStr);
    }

    /**
     * 查询
     */
    @Test
    public void queryForObjectTest() {
        EbBrand ebBrand = ebBrandService.queryForObject(3020L);
        System.out.println(ebBrand);
    }

    /**
     * 插入
     */
    @Test
    public void insertObjectTest() {
        EbBrand entity = new EbBrand();
        // entity.setBrandId(888l);
        entity.setBrandName("线程撕裂者3");
        entity.setBrandDesc("CPU3");
        entity.setImgs("www.baidu.com/img555");
        entity.setWebsite("百度333");
        entity.setBrandSort(11);

        ebBrandService.insertObject(entity);
        Long brandId = entity.getBrandId();
        System.out.println(brandId);
    }

    /**
     * 删除
     */
    @Test
    public void deleteObjectTest() {
        ebBrandService.deleteObject(3080L);
    }

    /**
     * 更新数据
     */
    @Test
    public void updateObjectTest() {
        EbBrand entity = new EbBrand();
        entity.setBrandId(3029L);
        entity.setBrandName("未来人类345");
        entity.setBrandDesc("笔记本电脑789");
        entity.setImgs("这是图片666");
        entity.setWebsite("京东111222");
        entity.setBrandSort(5);
        ebBrandService.updateObject(entity);
    }

}
