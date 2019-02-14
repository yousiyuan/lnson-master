package com.lnson.simple.projname.serviceLauncher.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.MessageFormat;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ZookeeperTest {

    @Value("${dubbo.registry.development}")
    private String registryDevelopmentAddress;

    @Value("${dubbo.registryId}")
    private String registryId;

    @Value("${redis.conn.hostName}")
    private String redisHostName;

    @Value("${redis.conn.port}")
    private String redisPort;

    @Value("${activemq.brokerURL}")
    private String brokerUrl;

    @Value("${activemq.jaas.userName}")
    private String jmsJaasUser;

    @Value("${activemq.jaas.password}")
    private String jmsJaasPassword;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * 测试是否可以正常取值
     */
    @Test
    public void test() {
        System.out.println(MessageFormat.format("dubbo.registry.development={0}", registryDevelopmentAddress));
        System.out.println(MessageFormat.format("dubbo.registryId={0}", registryId));

        System.out.println(MessageFormat.format("redis.conn.hostName={0}", redisHostName));
        System.out.println(MessageFormat.format("redis.conn.port={0}", redisPort));

        System.out.println(MessageFormat.format("activemq.brokerURL={0}",brokerUrl));
        System.out.println(MessageFormat.format("activemq.jaas.userName={0}",jmsJaasUser));
        System.out.println(MessageFormat.format("activemq.jaas.password={0}",jmsJaasPassword));
    }

}
