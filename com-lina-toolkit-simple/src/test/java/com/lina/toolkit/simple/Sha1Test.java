package com.lina.toolkit.simple;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class Sha1Test {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * 节点 /projname/manager 的登录密码是：123456
     */
    @Test
    public void test() throws NoSuchAlgorithmException {
        //7c4a8d09ca3762af61e59520943dc26494f8941b
        String digst = Sha1Utils.SHA1("123456");
        System.out.println(digst);
    }

}
