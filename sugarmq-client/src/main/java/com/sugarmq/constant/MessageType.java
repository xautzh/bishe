/**
 * 
 */
package com.sugarmq.constant;

/**
 * 类说明：消息类型
 *
 * 类描述：
 * @author manzhizhen
 *
 * 2014年12月10日
 */
public enum MessageType {
	PRODUCER_MESSAGE("PRODUCER_MESSAGE"),	// 生产者发送的消息
	PRODUCER_ACKNOWLEDGE_MESSAGE("PRODUCER_ACKNOWLEDGE_MESSAGE"),	// 生产者应答消息
	CUSTOMER_ACKNOWLEDGE_MESSAGE("CUSTOMER_ACKNOWLEDGE_MESSAGE"),	// 消费者应答消息
	CUSTOMER_MESSAGE_PULL("CUSTOMER_MESSAGE_PULL"),	// 消费者拉取消息
	CONNECTION_INIT_PARAM("CONNECTION_INIT_PARAM"), // 连接初始化参数消息
	CUSTOMER_REGISTER_MESSAGE("CUSTOMER_REGISTER_MESSAGE"),	// 消费者注册消息
	CUSTOMER_REGISTER_ACKNOWLEDGE_MESSAGE("CUSTOMER_REGISTER_ACKNOWLEDGE_MESSAGE");	// 消费者注册应答消息
	
	String value;
	private MessageType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
