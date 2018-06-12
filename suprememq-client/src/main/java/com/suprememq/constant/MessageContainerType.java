/**
 * 
 */
package com.suprememq.constant;

/**
 * 类说明：消息容器类型
 *
 * 类描述：
 * @author xautzh
 *
 * 2018年5月17日
 */
public enum MessageContainerType {
	QUEUE("QUEUE"),	// 队列
	TOPIC("TOPIC");	// 主题
	
	String value;
	MessageContainerType(String value) {
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
