package com.lnson.simple.projname.caching.service.impl;

import com.alibaba.fastjson.JSON;
import com.lnson.simple.projname.caching.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("cacheService")
public class CacheServiceImpl<T> implements CacheService<T> {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public CacheServiceImpl() {
    }

    @Override
    public void set(String key, T value) {
        this.redisTemplate.opsForValue().set(key, JSON.toJSONString(value), 30L, TimeUnit.DAYS);
    }

    @Override
    public void set(String key, T value, long timeout, TimeUnit unit) {
        this.redisTemplate.opsForValue().set(key, JSON.toJSONString(value), timeout, unit);
    }

    @Override
    public <E> void setList(String key, List<E> value) {
        this.redisTemplate.opsForValue().set(key, JSON.toJSONString(value), 30L, TimeUnit.DAYS);
    }

    @Override
    public void multiSet(Map<String, String> m) {
        this.redisTemplate.opsForValue().multiSet(m);
    }

    @Override
    public Long increment(String key, long delta) {
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public <E> E get(String key, Class<E> clazz) {
        String result = (String) this.redisTemplate.opsForValue().get(key);
        return result != null && !result.equals("") ? JSON.parseObject(result, clazz) : null;
    }

    @Override
    public String getString(String key) {
        return (String) this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public <E> List<E> getList(String key, Class<E> clazz) {
        String result = (String) this.redisTemplate.opsForValue().get(key);
        return result != null && !result.equals("") ? JSON.parseArray(result, clazz) : null;
    }

    @Override
    public List<Object> multiGet(Collection<String> keys) {
        List<String> resultList = this.redisTemplate.opsForValue().multiGet(keys);
        List<Object> result = new ArrayList<Object>();
        Iterator var4 = resultList.iterator();

        while (var4.hasNext()) {
            String stringItem = (String) var4.next();
            result.add(JSON.toJavaObject(JSON.parseObject(stringItem), Object.class));
        }

        return result;
    }

    @Override
    public void delete(Collection<String> keys) {
        this.redisTemplate.delete(keys);
    }

    @Override
    public Boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return this.redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return this.redisTemplate.expireAt(key, date);
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        return this.redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public String randomKey() {
        return (String) this.redisTemplate.randomKey();
    }

    @Override
    public void rename(String oldKey, String newKey) {
        this.redisTemplate.rename(oldKey, newKey);
    }

    @Override
    public String typeof(String key) {
        return this.redisTemplate.type(key).name();
    }

    @Override
    public byte[] dump(String key) {
        return this.redisTemplate.dump(key);
    }

    @Override
    public void killClient(String host, int port) {
        this.redisTemplate.killClient(host, port);
    }

    @Override
    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        this.redisTemplate.setEnableTransactionSupport(enableTransactionSupport);
    }

}
