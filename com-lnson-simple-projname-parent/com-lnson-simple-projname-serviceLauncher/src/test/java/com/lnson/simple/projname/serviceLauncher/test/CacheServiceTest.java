package com.lnson.simple.projname.serviceLauncher.test;

import com.lnson.simple.projname.caching.service.CacheService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CacheServiceTest {

    @Autowired
    private CacheService<String> cacheService;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * 设置值
     */
    @Test
    public void test1() {
        cacheService.set("mylover", "anybody");
        System.out.println("设置一个字符串");
        cacheService.set("song", "喝醉以后才明白");
        System.out.println("设置一首歌");
    }

    /**
     * 取值
     */
    @Test
    public void test2() {
        String mylover = cacheService.get("mylover", String.class);
        System.out.println(mylover);
        mylover = cacheService.get("song", String.class);
        System.out.println(mylover);
    }

    /**
     * Redis集群版错误：CROSSSLOT Keys in request don't hash to the same slot
     * 意思是mylover和song不能在一次请求中同时删除，要求他们需要有共同的slot
     */
    @Test
    public void test3() {
        List<String> list = new ArrayList<String>();
        list.add("mylover");
        cacheService.delete(list);

        list = new ArrayList<String>();
        list.add("song");
        cacheService.delete(list);
    }

}
