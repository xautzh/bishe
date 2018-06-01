/**
 * 
 */
package com.sugarmq.constant;

/**
 * 提供者用到的消息属性
 * @author manzhizhen
 * 
 */
public enum MessageProperty {
	DISABLE_MESSAGE_ID("_#_disableMessageId", false),
	SESSION_ID("_#_sessionId", null),
	CUSTOMER_ID("_#_customerId", null),		// 服务端给客户端消费者设置的ID
	CUSTOMER_CLIENT_ID("_#_customerClientId", null),	// 客户端消费者自己设置的ID
	CUSTOMER_BATCH_ACK_ID("_#_customerBatchAckId", null);	// 客户端消费者批量应答的消息组ID
	
	String key;
	Object value;
	private MessageProperty(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return key;
	}
}
