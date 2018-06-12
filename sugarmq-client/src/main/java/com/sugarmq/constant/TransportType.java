/**
 * 
 */
package com.sugarmq.constant;

/**
 * 连接器类型
 * 目前只支持TCP
 * @author xautzh
 *
 */
public enum TransportType {
	TRANSPORT_TCP("tcp");
	
	String value;
	TransportType(String value) {
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
