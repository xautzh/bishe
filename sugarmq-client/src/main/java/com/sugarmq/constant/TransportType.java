/**
 * 
 */
package com.sugarmq.constant;

/**
 * 连接器类型
 * 目前只支持TCP和NIO
 * @author manzhizhen
 *
 */
public enum TransportType {
	TRANSPORT_TCP("tcp"),
	TRANSPORT_NIO("nio");
	
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
