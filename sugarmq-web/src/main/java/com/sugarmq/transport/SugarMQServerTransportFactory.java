/**
 * 
 */
package com.sugarmq.transport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sugarmq.constant.TransportType;
import com.sugarmq.transport.tcp.TcpSugarMQTransprotCenter;

/**
 * 服务端传送器工厂
 * @author manzhizhen
 *
 */
@Component
public class SugarMQServerTransportFactory {
	private final static String REGEX_STR = "([a-z]{3})://([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]{1,6})"; // 解析提供者URL的正则表达式字符串
	private Logger logger = LoggerFactory.getLogger(SugarMQTransportFactory.class);
	
	public SugarMQTransprotCenter createSugarMQTransport(String uri) throws JMSException{
		if (uri == null) {
			throw new JMSException("服务端URI为空！！！");
		}

		Pattern pattern = Pattern.compile(REGEX_STR);
		Matcher matcher = pattern.matcher(uri);
		if (!matcher.matches()) {
			throw new JMSException("URI格式错误:" + uri);
		}

		String transportType = matcher.group(1);
		String ipAddress = matcher.group(2);
		int port = Integer.parseInt(matcher.group(3));
		
		String[] ipAddressArray = ipAddress.trim().split("\\.");
		byte[] ipBytes = new byte[] {
				(byte) Integer.parseInt(ipAddressArray[0]),
				(byte) Integer.parseInt(ipAddressArray[1]),
				(byte) Integer.parseInt(ipAddressArray[2]),
				(byte) Integer.parseInt(ipAddressArray[3]) };
		
		if(TransportType.TRANSPORT_TCP.getValue().equals(transportType)) {
			try {
				TcpSugarMQTransprotCenter tcpSugarMQTransprotCenter = new 
						TcpSugarMQTransprotCenter(InetAddress.getByAddress(ipBytes), port);
				
				return tcpSugarMQTransprotCenter;
				
			} catch (UnknownHostException e) {
				logger.error(e.getMessage());
				throw new JMSException(e.getMessage());
			}
			
			
		} else if(TransportType.TRANSPORT_NIO.getValue().equals(transportType)) {
			return null;
		}
		
		throw new JMSException("无法找到匹配的传输器类型：" + uri);
	}
}
