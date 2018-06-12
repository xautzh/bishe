/**
 * 
 */
package com.suprememq.transport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.suprememq.constant.TransportType;
import com.suprememq.transport.tcp.TcpSupremeMQTransportCenter;

/**
 * 服务端传送器工厂
 * @author xautzh
 *
 */
@Component
public class SupremeMQServerTransportFactory {
	private final static String REGEX_STR = "([a-z]{3})://([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}):([0-9]{1,6})"; // 解析提供者URL的正则表达式字符串
	private Logger logger = LoggerFactory.getLogger(SupremeMQTransportFactory.class);
	
	public SupremeMQTransportCenter createSupremeMQTransport(String uri) throws JMSException{
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
				TcpSupremeMQTransportCenter tcpSupremeMQTransportCenter = new
						TcpSupremeMQTransportCenter(InetAddress.getByAddress(ipBytes), port);
				
				return tcpSupremeMQTransportCenter;
				
			} catch (UnknownHostException e) {
				logger.error(e.getMessage());
				throw new JMSException(e.getMessage());
			}
			
			
		} else  {
			return null;
		}

	}
}
