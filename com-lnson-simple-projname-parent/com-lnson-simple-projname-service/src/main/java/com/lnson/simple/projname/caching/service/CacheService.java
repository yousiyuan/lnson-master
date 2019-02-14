package com.lnson.simple.projname.caching.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface CacheService<T> {

    public abstract void set(String var1, T var2);

    public abstract <E> void setList(String var1, List<E> var2);

    public abstract <E> List<E> getList(String var1, Class<E> var2);

    public abstract void set(String var1, T var2, long var3, TimeUnit var5);

    public abstract void multiSet(Map<String, String> var1);

    public abstract Long increment(String var1, long var2);

    public abstract <E> E get(String var1, Class<E> var2);

    public abstract String getString(String var1);

    public abstract List<Object> multiGet(Collection<String> var1);

    public abstract void delete(Collection<String> var1);

    public abstract Boolean hasKey(String var1);

    public abstract Boolean expire(String var1, long var2, TimeUnit var4);

    public abstract Boolean expireAt(String var1, Date var2);

    public abstract Long getExpire(String var1, TimeUnit var2);

    public abstract String randomKey();

    public abstract void rename(String var1, String var2);

    public abstract String typeof(String var1);

    public abstract byte[] dump(String var1);

    public abstract void killClient(String var1, int var2);

    public abstract void setEnableTransactionSupport(boolean var1);

}
