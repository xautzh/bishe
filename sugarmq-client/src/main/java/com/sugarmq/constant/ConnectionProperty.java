/**
 * 
 */
package com.sugarmq.constant;

/**
 * 连接器相关属性
 * 
 * @author 625288
 *
 */
public enum ConnectionProperty {
//	ACKNOWLEDGE_TYPE("ACKNOWLEDGE_TYPE", null),	// 客户端应答类型
	CLIENT_MESSAGE_BATCH_ACK_QUANTITY("CLIENT_MESSAGE_BATCH_ACK_QUANTITY", 10);	// 客户端消息批量应答数量
	
	
	String key;
	Object value;
	private ConnectionProperty(String key, Object value) {
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
