package com.lnson.simple.projname.jms.service.impl;

import com.lnson.simple.projname.jms.service.IJmsMessageHandler;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.text.MessageFormat;

/**
 * 生产者处理应答消息
 */
public class DoProducerMessage implements IJmsMessageHandler {

    @Override
    public String doMessage(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        String key = textMessage.getJMSCorrelationID();
        String doMessageResult = textMessage.getText();
        String result = MessageFormat.format("生产者得到的应答===>>{0}的处理结果：{1}", key, doMessageResult);
        System.out.println(result);
        return result;
    }

}
