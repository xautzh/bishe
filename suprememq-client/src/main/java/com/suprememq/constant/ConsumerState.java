/**
 * 
 */
package com.suprememq.constant;

/**
 * 类说明：消费者状态
 *
 * 类描述：
 * @author xautzh
 *
 * 2018年5月17日
 */
public enum ConsumerState {
	CREATE("0"),
	WORKING("1"),
	DEATH("2");
	
	String value;
	ConsumerState(String value) {
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
