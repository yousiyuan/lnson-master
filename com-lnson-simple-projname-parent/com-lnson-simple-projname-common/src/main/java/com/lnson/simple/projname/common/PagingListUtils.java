package com.lnson.simple.projname.common;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

public final class PagingListUtils {

    public static List<T> getPagingData(List<T> dataSource, int pageSize, int currentPageIndex, Integer totalPage, Integer totalRecord) {
        if (dataSource == null)
            return new ArrayList<T>();

        totalRecord = dataSource.size();
        if (totalRecord == 0)
            return new ArrayList<T>();

        totalPage = totalRecord / pageSize;
        if (totalRecord % pageSize == 0)
            totalPage += 1;

        int fromIndex = pageSize * (currentPageIndex - 1);

        int toIndex = pageSize * currentPageIndex;
        if (toIndex > totalRecord)
            toIndex = totalRecord;

        List<T> pageData = dataSource.subList(fromIndex, toIndex);
        return pageData;
    }

    /**
     * 将集合切割为若干个想等长度的子集合
     *
     * @param list 集合
     * @param size 子集合的长度
     * @return 子集合的集合
     */
    public static <U> List<List<U>> SliceList(List<U> list, int size) {
        List<List<U>> coll = new ArrayList<List<U>>();
        List<U> divList = list.subList(0, Math.min(size, list.size()));
        coll.add(new ArrayList<U>(divList));
        divList.clear();
        if (list.size() > 0) {
            coll.addAll(SliceList(list, size));
        }
        return coll;
    }
}
