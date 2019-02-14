package com.lnson.simple.projname.jms.service;

import javax.jms.JMSException;
import javax.jms.Message;

public interface IJmsMessageHandler {

    /**
     * 生产者通过doMessage处理消费者发送过来的应答消息
     * 消费者通过doMessage处理生产者发送过来的消息
     */
    public default String doMessage(Message message) throws JMSException {
        throw new JMSException("ERROR:invoke unimplements method");
    }

}
