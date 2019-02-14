package com.lnson.simple.projname.jms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lnson.simple.projname.jms.service.IJmsMessageHandler;

import javax.jms.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Enumeration;

/**
 * 消费者处理消息
 */
public class DoConsumerMessage implements IJmsMessageHandler {

    @Override
    public String doMessage(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();

            System.out.println("消费者接收到：" + text);
        }
        if (message instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) message;
            Serializable object = objectMessage.getObject();

            System.out.println("消费者接收到：" + JSON.toJSONString(object, SerializerFeature.WRITE_MAP_NULL_FEATURES, SerializerFeature.QuoteFieldNames));
        }
        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;

            formatMessageResult(mapMessage);
        }
        if (message instanceof StreamMessage) {
            StreamMessage streamMessage = (StreamMessage) message;

            formatMessageResult(streamMessage);
        }
        if (message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage) message;

            formatMessageResult(bytesMessage);
        }

        return "ok";
    }

    private void formatMessageResult(Message message) throws JMSException {
        if (message instanceof MapMessage || message instanceof StreamMessage || message instanceof BytesMessage) {
            Enumeration propertyNames = message.getPropertyNames();
            while (propertyNames.hasMoreElements()) {
                String key = (String) propertyNames.nextElement();
                Object value = message.getObjectProperty(key);
                System.out.println(MessageFormat.format("{0} ===>> {1}", key, value));
            }
        }
    }

}
