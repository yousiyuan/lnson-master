package com.lnson.simple.projname.utils.excel.test;

import com.lnson.simple.projname.utils.excel.PoiExcelUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.List;
import java.util.TreeMap;

public class PoiExcelUtilsTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void readExcelTest() {
        PoiExcelUtils poi = PoiExcelUtils.getPoi();
        List<TreeMap<String, String>> treeMaps = poi.readExcel("C:/Users/Administrator/IdeaProjects/SIMPLE_PROJECT/com-lnson-simple-projname-serviceLauncher/com-lnson-simple-projname-common/src/main/resources/readExcelTest.xlsx", "Sheet1", "C3", "J21");
        treeMaps.forEach(map -> {
            map.forEach((key, value) -> {
                System.out.print(MessageFormat.format("{0}:{1};", key, value));
            });
            System.out.println();
        });
    }
}
