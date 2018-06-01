/**
 * 
 */
package com.sugarmq.transport;

import java.util.concurrent.BlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * 服务端传送器接口
 * @author manzhizhen
 *
 */
public interface SugarMQServerTransport {
	/**
	 * 开启传送通道
	 * @throws JMSException
	 */
	public void start() throws JMSException;
	
	/**
	 * 关闭传送通道
	 * @throws JMSException
	 */
	public void close() throws JMSException;
	
	/**
	 * 获取收到的消息的队列
	 * @return
	 */
	public BlockingQueue<Message> getReceiveMessageQueue();
	
	/**
	 * 获取要发送消息的队列
	 * @return
	 */
	public BlockingQueue<Message> getSendMessageQueue();
}
