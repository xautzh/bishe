/**
 * 
 */
package com.sugarmq.dispatch;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.manager.SugarMQConsumerManager;
import com.sugarmq.queue.SugarMQMessageContainer;

/**
 * 类说明：消费者消息分发器
 *
 * 类描述：消费者消息分发器,每个消息队列配置一个
 * @author manzhizhen
 *
 * 2014年12月12日
 */
public class SugarMQConsumerDispatcher {
	private SugarMQConsumerManager sugarMQCustomerManager;
	private SugarMQMessageContainer sugarMQMessageContainer;
	private Thread dispatcherThread;
	private AtomicBoolean isStart = new AtomicBoolean(false);
	
	private static Logger logger = LoggerFactory.getLogger(SugarMQConsumerDispatcher.class);

	public SugarMQConsumerDispatcher(SugarMQConsumerManager sugarMQCustomerManager, 
			SugarMQMessageContainer sugarMQMessageContainer) {
		if(sugarMQCustomerManager == null) {
			throw new IllegalArgumentException("SugarMQCustomerManager不能为空！");
		}
		
		if(sugarMQMessageContainer == null) {
			throw new IllegalArgumentException("SugarMQMessageContainer不能为空！");
		}
		
		this.sugarMQCustomerManager = sugarMQCustomerManager;
		this.sugarMQMessageContainer = sugarMQMessageContainer;
	}

	public void setSugarMQMessageContainer(SugarMQMessageContainer sugarMQMessageContainer) {
		if(sugarMQMessageContainer == null) {
			throw new IllegalArgumentException("SugarMQMessageContainer不能为空！");
		}
		
		this.sugarMQMessageContainer = sugarMQMessageContainer;
	}
	
	public void start() {
		logger.info("SugarMQConsumerDispatcher准备开始工作... ...");
		
		dispatcherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = null;
				while(true) {
					try {
						message = sugarMQMessageContainer.takeMessage();
						logger.debug("从SugarMQMessageContainer中拉取了一条消息:【{}】", message);
						sugarMQCustomerManager.putMessageToCustomerQueue(message);
					} catch (JMSException e) {
						logger.info("SugarMQCustomerDispatcher被中断！");
						break ;
					}
				}
				
			}
		});
		
		dispatcherThread.start();
		isStart.set(true);
	}
	
	/**
	 * 关闭
	 */
	public void stop() {
		if(dispatcherThread != null) {
			dispatcherThread.interrupt();
			isStart.set(false);
		}
	}
	
	public boolean isStart() {
		return isStart.get();
	}
}
