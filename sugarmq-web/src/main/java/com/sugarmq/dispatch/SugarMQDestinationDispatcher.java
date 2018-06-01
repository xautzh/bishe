/**
 * 
 */
package com.sugarmq.dispatch;

import java.util.concurrent.BlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.constant.ConnectionProperty;
import com.sugarmq.constant.MessageProperty;
import com.sugarmq.constant.MessageType;
import com.sugarmq.manager.SugarMQConsumerManager;
import com.sugarmq.manager.SugarMQMessageManager;
import com.sugarmq.message.SugarMQDestination;
import com.sugarmq.message.bean.SugarMQMapMessage;
import com.sugarmq.message.bean.SugarMQMessage;

/**
 * 消息目的地分发器
 * 每个Transprot连接器配置一个该分发器来分发消息到目的地
 * @author manzhizhen
 *
 */
public class SugarMQDestinationDispatcher {
	private BlockingQueue<Message> receiveMessageQueue;
	private BlockingQueue<Message> sendMessageQueue;
	private SugarMQMessageManager sugarMQMessageManager;
	private SugarMQConsumerManager sugarMQCustomerManager;
	private Thread dispatcherThread;
	
	private static Logger logger = LoggerFactory.getLogger(SugarMQDestinationDispatcher.class);
	
	public SugarMQDestinationDispatcher(BlockingQueue<Message> receiveMessageQueue, 
			BlockingQueue<Message> sendMessageQueue, SugarMQMessageManager sugarMQMessageManager, 
			SugarMQConsumerManager sugarMQCustomerManager) {
		if(receiveMessageQueue == null || sendMessageQueue == null || sugarMQMessageManager == null
				|| sugarMQCustomerManager == null) {
			throw new IllegalArgumentException();
		}
		
		this.receiveMessageQueue = receiveMessageQueue;
		this.sendMessageQueue = sendMessageQueue;
		this.sugarMQMessageManager = sugarMQMessageManager;
		this.sugarMQCustomerManager = sugarMQCustomerManager;
	}
	
	/**
	 * 开始工作...
	 */
	public void start() {
		logger.info("SugarMQDestinationDispatcher准备开始工作... ...");
		
		dispatcherThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Message message = null;
				while(!Thread.currentThread().isInterrupted()) {
					try {
						message = receiveMessageQueue.take();
					} catch (InterruptedException e) {
						logger.info("SugarMQQueueDispatcher被中断，即将退出.");
						break ;
					}
					
					logger.debug("开始处理消息【{}】", message);
					
					try {
						// 生产者消息
						if(MessageType.PRODUCER_MESSAGE.getValue().
								equals(message.getJMSType())) {
							logger.debug("生产者消息【{}】", message);
							
							sugarMQMessageManager.addMessage(message);
							// 创建应答消息
							SugarMQMessage answerMessage = new SugarMQMessage();
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
							
							sugarMQMessageManager.removeMessage(message);
						
						// 消费者注册消息
						} else if(MessageType.CUSTOMER_REGISTER_MESSAGE.getValue().
								equals(message.getJMSType())) {
							logger.debug("消费者注册消息【{}】", message);
							
							SugarMQDestination dest = (SugarMQDestination) message.getJMSDestination();
							message.setJMSDestination(sugarMQMessageManager.getSugarMQMessageContainer(dest.getName()));
							sugarMQCustomerManager.addCustomer(message, sendMessageQueue);
						
						// 消费者拉取消息
						} else if(MessageType.CUSTOMER_MESSAGE_PULL.getValue().
								equals(message.getJMSType())) {
							logger.debug("消费者拉取消息【{}】", message);
							
							SugarMQDestination dest = (SugarMQDestination) message.getJMSDestination();
							message.setJMSDestination(sugarMQMessageManager.getSugarMQMessageContainer(dest.getName()));
							
							sugarMQCustomerManager.updateConsumerState(dest.getName(), 
									message.getStringProperty(MessageProperty.CUSTOMER_ID.getKey()), true);
						
						// 连接初始化参数的消息
						} else if(MessageType.CONNECTION_INIT_PARAM.getValue().
								equals(message.getJMSType())) {
							logger.debug("连接初始化参数的消息【{}】", message);
							SugarMQMapMessage mapMessage = (SugarMQMapMessage) message;
							if(mapMessage.propertyExists(ConnectionProperty.CLIENT_MESSAGE_BATCH_ACK_QUANTITY.getKey())) {
								sugarMQCustomerManager.setClientMessageBatchSendAmount(mapMessage.
										getInt(ConnectionProperty.CLIENT_MESSAGE_BATCH_ACK_QUANTITY.getKey()));
							}
							
						} else {
							logger.error("未知消息类型，无法处理【{}】", message);
						}
						
					} catch (JMSException e) {
						logger.error("SugarMQQueueDispatcher消息处理失败:【{}】", message, e);
					}
				}
			}
		});
		
		dispatcherThread.start();
		logger.info("SugarMQDestinationDispatcher已经开始工作");
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
