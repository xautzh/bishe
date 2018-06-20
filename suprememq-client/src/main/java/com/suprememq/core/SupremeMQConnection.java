package com.suprememq.core;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suprememq.transport.MessageDispatcher;
import com.suprememq.transport.SupremeMQTransport;
import com.suprememq.util.SessionIdGenerate;

/**
 * 
 * @author xautzh
 *
 */
public class SupremeMQConnection implements Connection{
	
	private SupremeMQTransport SupremeMQTransport;
	
	private MessageDispatcher messageDispatcher;
	
	// key-sessionID，value-session
	private Map<String, SupremeMQSession> sessionMap = new ConcurrentHashMap<String, SupremeMQSession>();
	
	private AtomicBoolean isStarted = new AtomicBoolean(false);
	private AtomicBoolean isClosed = new AtomicBoolean(false);
	
	// 连接参数Map
	private ConcurrentMap<String, Object> params = new ConcurrentHashMap<String, Object>();
	
	// 消费者消费消息和发送应答消息的线程池执行器
	private ThreadPoolExecutor threadPoolExecutor;
	
	private Logger logger = LoggerFactory.getLogger(SupremeMQConnection.class);
	
	public SupremeMQConnection(SupremeMQTransport SupremeMQTransport) {
		if(SupremeMQTransport == null) {
			throw new IllegalArgumentException("SupremeMQTransport不能为空！");
		}
		
		this.SupremeMQTransport = SupremeMQTransport;
		messageDispatcher = new MessageDispatcher(SupremeMQTransport.getReceiveMessageQueue(),
				SupremeMQTransport.getSendMessageQueue());
		
	}

	@Override
	public void close() throws JMSException {
		synchronized (isClosed) {
			if(isClosed.get()) {
				return ;
			}
			
			isClosed.set(true);
		}

		logger.info("SupremeMQConnection即将关闭... ...");
		
		messageDispatcher.close();
		SupremeMQTransport.close();
		
		logger.info("SupremeMQConnection已经关闭！");
	}

	@Override
	public ConnectionConsumer createConnectionConsumer(Destination destination,
			String arg1, ServerSessionPool arg2, int arg3) throws JMSException {
		return null;
	}

	@Override
	public ConnectionConsumer createDurableConnectionConsumer(Topic arg0,
			String arg1, String arg2, ServerSessionPool arg3, int arg4)
			throws JMSException {
		return null;
	}

	@Override
	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException {
		SupremeMQTransport.setAcknowledgeType(acknowledgeMode);
		String sessionId = SessionIdGenerate.getNewSessionId();
		SupremeMQSession SupremeMQSession = new SupremeMQSession(sessionId, transacted, messageDispatcher);
		sessionMap.put(sessionId, SupremeMQSession);
		return SupremeMQSession;
	}

	@Override
	public String getClientID() throws JMSException {
		return null;
	}

	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return null;
	}

	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return null;
	}

	@Override
	public void setClientID(String arg0) throws JMSException {
	}

	@Override
	public void setExceptionListener(ExceptionListener arg0)
			throws JMSException {
	}

	@Override
	public void start() throws JMSException {
		synchronized (isStarted) {
			if(isStarted.get()) {
				return ;
			}
			
			logger.info("SupremeMQConnection开始启动！");
			SupremeMQTransport.start();
			messageDispatcher.start();
			threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
			
			for(SupremeMQSession session : sessionMap.values()) {
				session.start();
			}
			
			isStarted.set(true);
		}
	}

	@Override
	public void stop() throws JMSException {
		SupremeMQTransport.close();
	}

	public Object getParamValue(String key) {
		return params.get(key);
	}

	public void setParamsValue(String key, Object value) {
		synchronized (isStarted) {
			if(isStarted.get()) {
				throw new IllegalStateException("SupremeMQConnection已经开启，无法设置customerBatchAckNum！");
			}
			
			params.put(key, value);
		}
	}
}
