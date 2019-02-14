package com.lnson.simple.projname.serviceLauncher.test;

import com.lnson.simple.projname.jms.service.impl.JmsProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.TextMessage;
import java.io.IOException;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class JmsMessageTest {

    @Autowired
    private JmsProducer jmsProducer;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * 生产者发送消息
     * 消费者自动接收消息并发送应答消息
     */
    @Test
    public void test2() throws IOException {
        jmsProducer.sendMessage(TextMessage.class,"问候","你还好吗？");
        System.in.read();
    }

}
