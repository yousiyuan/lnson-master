package com.lnson.simple.projname.common;

import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.stream.Collectors;

public final class PagingStreamUtils {

    private static final int DEFAULT_PAGE_SIZE = 50;
    private List<T> pageData = null;
    private int totalPage = 0;
    private int pageSize = 0;

    public PagingStreamUtils(List<T> pageResult) {
        this(pageResult, DEFAULT_PAGE_SIZE);
    }

    public PagingStreamUtils(List<T> data, int pageSize) {
        this.pageSize = pageSize;
        this.pageData = data;
        init(data, pageSize);
    }

    private void init(List<T> data, int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Paging size must be greater than zero.");
        }
        if (null == data) {
            throw new NullPointerException("Paging resource list must be not null.");
        }
        if (data.size() % pageSize > 0) {
            this.totalPage = (data.size() / pageSize) + 1;
        } else {
            this.totalPage = data.size() / pageSize;
        }
    }

    /**
     * 获取分页后，总的页数
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 返回是否还有下一页数据
     */
    public boolean hasNext() {
        return pageData.size() > 0;
    }

    /**
     * 获取当前页的数据
     */
    public List<T> next() {
        List<T> pagingData = pageData.stream().limit(pageSize).collect(Collectors.toList());
        pageData = pageData.stream().skip(pageSize).collect(Collectors.toList());
        return pagingData;
    }

    /**
     * 返回当前页数
     */
    public int getCurrentPageIndex() {
        return totalPage - getRemainingPage();
    }

    /**
     * 返回当前剩余页数
     */
    private int getRemainingPage() {
        if (pageData.size() % pageSize > 0) {
            return (pageData.size() / pageSize) + 1;
        } else {
            return pageData.size() / pageSize;
        }
    }
}
