package com.lnson.simple.projname.jms.service.impl;

import com.lnson.simple.projname.jms.service.IJmsMessageHandler;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;

/**
 * activemq消费者
 */
public class JmsConsumer implements MessageListener {

    /**
     * java消息服务模板对象
     */
    private JmsTemplate jmsTemplate;

    /**
     * 接收到应答消息后进行后续处理(消费者必须实现该接口)
     */
    private IJmsMessageHandler jmsMessageHandler;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setJmsMessageHandler(IJmsMessageHandler response) {
        this.jmsMessageHandler = response;
    }

    /**
     * 处理生产者发送过来的消息并对生产者发送应答消息
     */
    @Override
    public void onMessage(Message message) {
        try {
            final String key = message.getJMSCorrelationID();
            final String doMessageResult = jmsMessageHandler.doMessage(message);

            jmsTemplate.send(message.getJMSReplyTo(),session -> {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(doMessageResult);
                textMessage.setJMSCorrelationID(key);
                return textMessage;
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
