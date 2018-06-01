/**
 * 
 */
package com.sugarmq.constant;

/**
 * 类说明：消息分发类型
 *
 * 类描述：
 * @author manzhizhen
 *
 * 2014年12月10日
 */
public enum MessageDispatchType {
	IN_TURN("IN_TURN");
	
	String value;
	private MessageDispatchType(String value) {
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
