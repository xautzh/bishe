/**
 * 
 */
package com.suprememq.transport.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.suprememq.dispatch.SupremeMQDestinationDispatcher;
import com.suprememq.manager.SupremeMQConsumerManager;
import com.suprememq.manager.SupremeMQMessageManager;
import com.suprememq.transport.SupremeMQServerTransport;
import com.suprememq.transport.SupremeMQTransportCenter;

/**
 * 类说明：
 *
 * 类描述：
 * @author xautzh
 *
 * 2018年5月17日
 */
public class TcpSupremeMQTransportCenter implements SupremeMQTransportCenter{
	private InetAddress inetAddress;
	private int port;
	private ServerSocket serverSocket;
	
	private SupremeMQMessageManager SupremeMQMessageManager;
	private SupremeMQConsumerManager supremeMQCustomerManager;
	
	private ConcurrentHashMap<TcpSupremeMQServerTransport, SupremeMQDestinationDispatcher> transprotMap =
			new ConcurrentHashMap<TcpSupremeMQServerTransport, SupremeMQDestinationDispatcher>();
	
	private @Value("${transport-backlog}") int backlog;
	
	private Logger logger = LoggerFactory.getLogger(TcpSupremeMQTransportCenter.class);
	
	public TcpSupremeMQTransportCenter(InetAddress inetAddress, int port) {
		if(inetAddress == null) {
			logger.error("InetAddress为空，创建TcpSupremeMQTransportCenter失败！");
			throw new IllegalArgumentException("InetAddress不能为空！");
		}
		
		this.inetAddress = inetAddress;
		this.port = port;
	}
	
	public void start() throws JMSException {
		try {
			serverSocket = new ServerSocket(port, backlog, inetAddress);
		} catch (IOException e) {
			logger.error("ServerSocket初始化失败", e);
			throw new JMSException(String.format("TcpSupremeMQServerTransport绑定URI出错：【%s】【%s】【%s】",
					new Object[]{inetAddress, port, e.getMessage()}));
		}
		
		TcpSupremeMQServerTransport tcpSupremeMQServerTransport = null;
		SupremeMQDestinationDispatcher SupremeMQDestinationDispatcher = null;
		Socket socket = null;
		while(true) {
			try {
				socket = serverSocket.accept();
				tcpSupremeMQServerTransport = new TcpSupremeMQServerTransport(socket, this);
				SupremeMQDestinationDispatcher = new SupremeMQDestinationDispatcher(tcpSupremeMQServerTransport.getReceiveMessageQueue(),
						tcpSupremeMQServerTransport.getSendMessageQueue(), SupremeMQMessageManager, supremeMQCustomerManager);
				tcpSupremeMQServerTransport.start();
				SupremeMQDestinationDispatcher.start();
				transprotMap.put(tcpSupremeMQServerTransport, SupremeMQDestinationDispatcher);
			} catch (IOException e) {
				logger.error("TcpSupremeMQServerTransport启动失败：", e);
			}
		}
	}

	@Override
	public void close() throws JMSException {
		logger.info("TcpSupremeMQTransportCenter正在关闭... ...");
		for(Map.Entry<TcpSupremeMQServerTransport, SupremeMQDestinationDispatcher> entry : transprotMap.entrySet()) {
			entry.getKey().close();
			entry.getValue().stop();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			logger.error("关闭ServerSocket异常", e);
			throw new JMSException(String.format("关闭ServerSocket异常:{}", e.getMessage()));
		}
	}

	@Override
	public void setsupremeMQCustomerManager(
			SupremeMQConsumerManager supremeMQCustomerManager) {
		this.supremeMQCustomerManager = supremeMQCustomerManager;
		
	}

	@Override
	public void setSupremeMQMessageManager(
			SupremeMQMessageManager SupremeMQMessageManager) {
		this.SupremeMQMessageManager = SupremeMQMessageManager;
	}

	@Override
	public void remove(SupremeMQServerTransport SupremeMQServerTransport) {
		if(SupremeMQServerTransport == null) {
			throw new IllegalArgumentException("SupremeMQServerTransport不能为空！");
		}
		transprotMap.remove(SupremeMQServerTransport);
		logger.debug("TcpSupremeMQTransportCenter已经移除了【{}】", SupremeMQServerTransport);
	}

	public ConcurrentHashMap<TcpSupremeMQServerTransport, SupremeMQDestinationDispatcher> getTransprotMap() {
		return transprotMap;
	}
}
