package com.sugarmq.clientStart;

import javax.jms.*;
import java.util.Scanner;

public class Client {
    private Session session = SupremeConnectionFactory.connection("tcp://127.0.0.1:1314");
    private MessageProducer producer;
    private MessageConsumer consumer;
    private Queue queue;


    //单例模式
    private static Client client;

    private Client() {
    }

    public static Client getClient() {
        if (client == null)
            client = new Client();
        return client;
    }


    public void producerStart() {
        try {
            producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage();
            message.setText("do you love me");
            for (int i = 0; i < 10; i++) {
                producer.send(message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void consumerStart() {
        try {
            Queue consumerQueue = session.createQueue("xautZH0");
            consumer = session.createConsumer(consumerQueue);
            setConsumer(consumer);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("consumer:[xautXH0]收到消息："+((TextMessage) message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void singleProducerStart() {
        try {
            Queue producerQueue = session.createQueue("xautZH0");
            producer = session.createProducer(producerQueue);
            TextMessage message = session.createTextMessage();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("请输入需要发送的消息");
                String text = scanner.nextLine();
                message.setText(text);
                message.setJMSDeliveryMode(2);
                producer.send(message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MessageProducer getProducer() {
        return producer;
    }

    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(String s) throws JMSException {
        this.queue = session.createQueue(s);
    }
}
