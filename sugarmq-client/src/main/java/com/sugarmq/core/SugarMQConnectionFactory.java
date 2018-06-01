package com.sugarmq.core;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.SugarMQTransport;
import com.sugarmq.transport.SugarMQTransportFactory;

public class SugarMQConnectionFactory implements ConnectionFactory,
		QueueConnectionFactory, TopicConnectionFactory {

	public final static int OVERTIME = 10000; // 完成连接的超时时间（单位毫秒）

	private Logger logger = LoggerFactory.getLogger(SugarMQConnectionFactory.class);
	private SugarMQTransport sugarMQTransport;
	

	/**
	 * 通过IP地址和端口创建SugarMQConnectionFactory对象
	 * 
	 * @param ipAddress
	 * @param port
	 */
	public SugarMQConnectionFactory(String providerURl) throws JMSException {
		sugarMQTransport = SugarMQTransportFactory.createSugarMQTransport(providerURl);
	}

	@Override
	public Connection createConnection() throws JMSException {
		return new SugarMQConnection(sugarMQTransport);

	}

	@Override
	public Connection createConnection(String userName, String password)
			throws JMSException {
		return null;
	}

	@Override
	public TopicConnection createTopicConnection() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TopicConnection createTopicConnection(String userName,
			String password) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueConnection createQueueConnection() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueueConnection createQueueConnection(String userName,
			String password) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

}
