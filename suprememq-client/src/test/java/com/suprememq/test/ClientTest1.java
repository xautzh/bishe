package com.suprememq.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.suprememq.constant.MessageContainerType;
import com.suprememq.core.SupremeMQConnectionFactory;
import com.suprememq.message.SupremeMQDestination;

public class ClientTest1 {
	public static void main(String[] args) {
		try {
			SupremeMQConnectionFactory facotory = new SupremeMQConnectionFactory("tcp://127.0.0.1:1314");
			Connection connection = facotory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Queue queue = new SupremeMQDestination("supreme", MessageContainerType.QUEUE.getValue());
			
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
					try {
						System.out.println("consumer1：" + ((TextMessage)msg).getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			MessageConsumer consumer1 = session.createConsumer(queue);
			consumer1.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
					try {
						System.out.println("consumer2：" + ((TextMessage)msg).getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			MessageConsumer consumer2 = session.createConsumer(queue);
			consumer2.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
					try {
						System.out.println("consumer3：" + ((TextMessage)msg).getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			
			System.out.println("消费者创建完毕！");
			
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("Do you love me?");
			
			MessageProducer sender = session.createProducer(queue);
			for(int i = 0; i < 6; i++) {
				sender.send(textMessage);
			}
			System.out.println("消息发送完毕！！！");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while(true) {
				reader.readLine();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
