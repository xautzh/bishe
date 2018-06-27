/**
 * 
 */
package com.suprememq.transport;

import java.util.concurrent.BlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * 服务端传送器接口
 * @author xautzh
 *
 */
public interface SupremeMQServerTransport {
	/**
	 * 开启传送通道
	 * @throws JMSException
	 */
	void start() throws JMSException;
	
	/**
	 * 关闭传送通道
	 * @throws JMSException
	 */
	void close() throws JMSException;
	
	/**
	 * 获取收到的消息的队列
	 * @return
	 */
	BlockingQueue<Message> getReceiveMessageQueue();
	
	/**
	 * 获取要发送消息的队列
	 * @return
	 */
	BlockingQueue<Message> getSendMessageQueue();
}
