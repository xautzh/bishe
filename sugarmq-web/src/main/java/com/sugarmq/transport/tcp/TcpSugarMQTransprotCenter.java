/**
 * 
 */
package com.sugarmq.transport.tcp;

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

import com.sugarmq.dispatch.SugarMQDestinationDispatcher;
import com.sugarmq.manager.SugarMQConsumerManager;
import com.sugarmq.manager.SugarMQMessageManager;
import com.sugarmq.transport.SugarMQServerTransport;
import com.sugarmq.transport.SugarMQTransprotCenter;

/**
 * 类说明：
 *
 * 类描述：
 * @author manzhizhen
 *
 * 2014年12月11日
 */
public class TcpSugarMQTransprotCenter implements SugarMQTransprotCenter{
	private InetAddress inetAddress;
	private int port;
	private ServerSocket serverSocket;
	
	private SugarMQMessageManager sugarMQMessageManager;
	private SugarMQConsumerManager sugarMQCustomerManager;
	
	private ConcurrentHashMap<TcpSugarMQServerTransport, SugarMQDestinationDispatcher> transprotMap = 
			new ConcurrentHashMap<TcpSugarMQServerTransport, SugarMQDestinationDispatcher>();
	
	private @Value("${transport-backlog}") int backlog;
	
	private Logger logger = LoggerFactory.getLogger(TcpSugarMQTransprotCenter.class);
	
	public TcpSugarMQTransprotCenter(InetAddress inetAddress, int port) {
		if(inetAddress == null) {
			logger.error("InetAddress为空，创建TcpSugarMQTransprotCenter失败！");
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
			throw new JMSException(String.format("TcpSugarMQServerTransport绑定URI出错：【%s】【%s】【%s】", 
					new Object[]{inetAddress, port, e.getMessage()}));
		}
		
		TcpSugarMQServerTransport tcpSugarMQServerTransport = null;
		SugarMQDestinationDispatcher sugarMQDestinationDispatcher = null;
		Socket socket = null;
		while(true) {
			try {
				socket = serverSocket.accept();
				tcpSugarMQServerTransport = new TcpSugarMQServerTransport(socket, this);
				sugarMQDestinationDispatcher = new SugarMQDestinationDispatcher(tcpSugarMQServerTransport.getReceiveMessageQueue(), 
						tcpSugarMQServerTransport.getSendMessageQueue(), sugarMQMessageManager, sugarMQCustomerManager);
				tcpSugarMQServerTransport.start();
				sugarMQDestinationDispatcher.start();
				
				transprotMap.put(tcpSugarMQServerTransport, sugarMQDestinationDispatcher);
//				new Thread(new TcpReceiveThread(sugarQueueManager, this)).start();
			} catch (IOException e) {
				logger.error("TcpSugarMQServerTransport启动失败：", e);
			}
		}
	}

	@Override
	public void close() throws JMSException {
		logger.info("TcpSugarMQTransprotCenter正在关闭... ...");
		for(Map.Entry<TcpSugarMQServerTransport, SugarMQDestinationDispatcher> entry : transprotMap.entrySet()) {
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
	public void setSugarMQCustomerManager(
			SugarMQConsumerManager sugarMQCustomerManager) {
		this.sugarMQCustomerManager = sugarMQCustomerManager;
		
	}

	@Override
	public void setSugarMQMessageManager(
			SugarMQMessageManager sugarMQMessageManager) {
		this.sugarMQMessageManager = sugarMQMessageManager;
	}

	@Override
	public void remove(SugarMQServerTransport sugarMQServerTransport) {
		if(sugarMQServerTransport == null) {
			throw new IllegalArgumentException("SugarMQServerTransport不能为空！");
		}
		
		transprotMap.remove(sugarMQServerTransport);
		
		logger.debug("TcpSugarMQTransprotCenter已经移除了【{}】", sugarMQServerTransport);
	}
}
