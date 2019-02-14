package com.lnson.simple.projname.jms.service.impl;

import com.lnson.simple.projname.jms.service.IJmsMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.*;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * activemq生产者
 */
public class JmsProducer implements SessionAwareMessageListener {

    /**
     * java消息服务模板对象
     */
    private JmsTemplate jmsTemplate;

    /**
     * 用于发送消息的消息队列
     */
    private Destination requestDestination;

    /**
     * 用于接收应答消息的消息队列
     */
    private Destination responseDestination;

    /**
     * 接收到应答消息后进行后续处理（生产者不强制实现该接口）
     */
    private IJmsMessageHandler jmsMessageHandler;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setRequestDestination(Destination requestDestination) {
        this.requestDestination = requestDestination;
    }

    public void setResponseDestination(Destination responseDestination) {
        this.responseDestination = responseDestination;
    }

    public void setJmsMessageHandler(IJmsMessageHandler jmsMessageHandler) {
        this.jmsMessageHandler = jmsMessageHandler;
    }

    /**
     * 发送消息
     *
     * @param clazz      Message类型
     * @param identity   dataObject唯一标识
     * @param dataObject 使用mq传输的数据
     * @param <T>        TextMessage，ObjectMessage，MapMessage，StreamMessage，BytesMessage
     * @param <V>        String字符串，实现了Serializable接口的对象，Map，字节数组，字节数组
     */
    public <T extends Message, V extends Serializable> void sendMessage(Class<? extends T> clazz, final String identity, V dataObject) {
        jmsTemplate.send(requestDestination, session -> {
            Message message = null;
            if (clazz.equals(TextMessage.class)) {
                message = session.createTextMessage((String) dataObject);
            }
            if (clazz.equals(ObjectMessage.class)) {
                message = session.createObjectMessage(dataObject);
            }
            if (clazz.equals(MapMessage.class)) {
                MapMessage mapMessage = session.createMapMessage();
                Map<Object, Object> maps = (Map<Object, Object>) dataObject;
                for (Map.Entry entry : maps.entrySet())
                    mapMessage.setObject((String) entry.getKey(), entry.getValue());
                message = mapMessage;
            }
            if (clazz.equals(StreamMessage.class)) {
                byte[] bytes = (byte[]) dataObject;
                StreamMessage streamMessage = session.createStreamMessage();
                streamMessage.writeBytes(bytes);
                message = streamMessage;
            }
            if (clazz.equals(BytesMessage.class)) {
                byte[] bytes = (byte[]) dataObject;
                BytesMessage bytesMessage = session.createBytesMessage();
                bytesMessage.writeBytes(bytes);
                message = bytesMessage;
            }
            if (message != null) {
                message.setJMSCorrelationID(identity == null || "".equals(identity) ? UUID.randomUUID().toString() : identity);
                message.setJMSReplyTo(this.responseDestination);
            }
            return message;
        });
    }

    /**
     * 处理应答消息
     */
    @Override
    public void onMessage(Message message, Session session) {
        try {
            if (jmsMessageHandler != null)
                jmsMessageHandler.doMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
