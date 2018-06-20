/**
 * 
 */
package com.suprememq.dispatch;

import java.util.concurrent.BlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suprememq.constant.ConnectionProperty;
import com.suprememq.constant.MessageProperty;
import com.suprememq.constant.MessageType;
import com.suprememq.manager.SupremeMQConsumerManager;
import com.suprememq.manager.SupremeMQMessageManager;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.message.bean.SupremeMQMapMessage;
import com.suprememq.message.bean.SupremeMQMessage;

/**
 * 消息目的地分发器
 * 每个Transprot连接器配置一个该分发器来分发消息到目的地
 * @author xautzh
 *
 */
public class SupremeMQDestinationDispatcher {
	private BlockingQueue<Message> receiveMessageQueue;
	private BlockingQueue<Message> sendMessageQueue;
	private SupremeMQMessageManager SupremeMQMessageManager;
	private SupremeMQConsumerManager supremeMQCustomerManager;
	private Thread dispatcherThread;
	
	private static Logger logger = LoggerFactory.getLogger(SupremeMQDestinationDispatcher.class);
	
	public SupremeMQDestinationDispatcher(BlockingQueue<Message> receiveMessageQueue,
			BlockingQueue<Message> sendMessageQueue, SupremeMQMessageManager SupremeMQMessageManager,
			SupremeMQConsumerManager supremeMQCustomerManager) {
		if(receiveMessageQueue == null || sendMessageQueue == null || SupremeMQMessageManager == null
				|| supremeMQCustomerManager == null) {
			throw new IllegalArgumentException();
		}
		
		this.receiveMessageQueue = receiveMessageQueue;
		this.sendMessageQueue = sendMessageQueue;
		this.SupremeMQMessageManager = SupremeMQMessageManager;
		this.supremeMQCustomerManager = supremeMQCustomerManager;
	}
	
	/**
	 * 开始工作...
	 */
	public void start() {
		logger.info("SupremeMQDestinationDispatcher准备开始工作... ...");
		
		dispatcherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = null;
				while(!Thread.currentThread().isInterrupted()) {
					try {
						message = receiveMessageQueue.take();
					} catch (InterruptedException e) {
						logger.info("supremeMQQueueDispatcher被中断，即将退出.");
						break ;
					}
					
					logger.debug("开始处理消息【{}】", message);
					
					try {
						// 生产者消息
						if(MessageType.PRODUCER_MESSAGE.getValue().
								equals(message.getJMSType())) {
							logger.debug("生产者消息【{}】", message);
							
							SupremeMQMessageManager.addMessage(message);
							// 创建应答消息
							SupremeMQMessage answerMessage = new SupremeMQMessage();
							answerMessage.setJMSType(MessageType.PRODUCER_ACKNOWLEDGE_MESSAGE.getValue());
							answerMessage.setJMSMessageID(message.getJMSMessageID());
							try {
								sendMessageQueue.put(answerMessage);
							} catch (InterruptedException e) {
								logger.info("生产者应答消息发送被中断.");
							}
						
						// 消费者应答消息
						} else if(MessageType.CUSTOMER_ACKNOWLEDGE_MESSAGE.getValue().
								equals(message.getJMSType())) {
							logger.debug("消费者应答消息【{}】", message);
							SupremeMQMessageManager.removeMessage(message);
						
						// 消费者注册消息
						} else if(MessageType.CUSTOMER_REGISTER_MESSAGE.getValue().
								equals(message.getJMSType())) {
							logger.debug("消费者注册消息【{}】", message);
							
							SupremeMQDestination dest = (SupremeMQDestination) message.getJMSDestination();
							message.setJMSDestination(SupremeMQMessageManager.getSupremeMQMessageContainer(dest.getName()));
							supremeMQCustomerManager.addCustomer(message, sendMessageQueue);
						
						// 消费者拉取消息
						} else if(MessageType.CUSTOMER_MESSAGE_PULL.getValue().
								equals(message.getJMSType())) {
							logger.debug("消费者拉取消息【{}】", message);
							SupremeMQDestination dest = (SupremeMQDestination) message.getJMSDestination();
							message.setJMSDestination(SupremeMQMessageManager.getSupremeMQMessageContainer(dest.getName()));
							supremeMQCustomerManager.updateConsumerState(dest.getName(),
									message.getStringProperty(MessageProperty.CUSTOMER_ID.getKey()), true);
						// 连接初始化参数的消息
						} else if(MessageType.CONNECTION_INIT_PARAM.getValue().
								equals(message.getJMSType())) {
							logger.debug("连接初始化参数的消息【{}】", message);
							SupremeMQMapMessage mapMessage = (SupremeMQMapMessage) message;
							if(mapMessage.propertyExists(ConnectionProperty.CLIENT_MESSAGE_BATCH_ACK_QUANTITY.getKey())) {
								supremeMQCustomerManager.setClientMessageBatchSendAmount(mapMessage.
										getInt(ConnectionProperty.CLIENT_MESSAGE_BATCH_ACK_QUANTITY.getKey()));
							}
							
						} else {
							logger.error("未知消息类型，无法处理【{}】", message);
						}
						
					} catch (JMSException e) {
						logger.error("supremeMQQueueDispatcher消息处理失败:【{}】", message, e);
					}
				}
			}
		});
		
		dispatcherThread.start();
		logger.info("SupremeMQDestinationDispatcher已经开始工作");
	}
	
	/**
	 * 关闭
	 */
	public void stop() {
		if(dispatcherThread != null) {
			dispatcherThread.interrupt();
		}
	}
}
