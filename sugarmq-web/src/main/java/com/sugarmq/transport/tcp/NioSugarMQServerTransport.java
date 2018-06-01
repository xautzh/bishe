/**
 * 
 */
package com.sugarmq.transport.tcp;

import java.util.concurrent.BlockingQueue;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sugarmq.transport.SugarMQServerTransport;

/**
 * @author manzhizhen
 *
 */
public class NioSugarMQServerTransport implements SugarMQServerTransport{
	private static Logger logger = LoggerFactory.getLogger(NioSugarMQServerTransport.class);

	@Override
	public void start() throws JMSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws JMSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BlockingQueue<Message> getReceiveMessageQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockingQueue<Message> getSendMessageQueue() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
