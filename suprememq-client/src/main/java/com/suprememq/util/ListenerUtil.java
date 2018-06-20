package com.suprememq.util;

import javax.jms.*;

public class ListenerUtil {
    public static MessageListener setListener(MessageConsumer consumer, final String destination) throws JMSException {
        MessageListener listener = consumer.getMessageListener();
        if (listener!=null){
            return listener;
        }else {
            listener = new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("消费者从"+destination+"收到消息:"+((TextMessage)message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        return listener;
    }
}
