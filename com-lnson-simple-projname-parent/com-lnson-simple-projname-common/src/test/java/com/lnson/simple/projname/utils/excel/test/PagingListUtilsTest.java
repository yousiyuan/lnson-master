package com.lnson.simple.projname.utils.excel.test;

import com.lnson.simple.projname.common.PagingListUtils;
import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PagingListUtilsTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test() throws ParseException {
        Date startDate = parseDate("2018-10-29");
        Date endDate = parseDate("2019-05-31");
        List<String> dateList = getDateList(startDate, endDate);

        // 每次创建10张数据库表
        List<List<String>> tableNameArrayList = PagingListUtils.SliceList(dateList, 10);
        for (List<String> tableNameArray : tableNameArrayList) {
            //批量创建数据库表
            System.out.println(tableNameArray);
        }
    }

    /**
     * 获取日期的字符串列表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 返回日期的字符串列表
     */
    public List<String> getDateList(@NotNull final Date startDate, @NotNull final Date endDate) {
        List<String> list = new ArrayList<String>();
        if (startDate.getTime() > endDate.getTime()) {
            return list;
        }

        list.add(formatDate(startDate));
        if (startDate.getTime() == endDate.getTime()) {
            return list;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        list.addAll(getDateList(calendar.getTime(), endDate));
        return list;
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return 返回日期的字符串形式
     */
    public String formatDate(Date date) {
        return "T_ORDER_" + new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 格式化日期
     *
     * @param source 日期的字符串形式
     * @return 返回日期
     */
    public Date parseDate(String source) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(source);
    }

}
