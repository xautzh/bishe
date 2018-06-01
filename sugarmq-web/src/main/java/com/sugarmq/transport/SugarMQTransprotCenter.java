/**
 * 
 */
package com.sugarmq.transport;

import javax.jms.JMSException;

import com.sugarmq.manager.SugarMQConsumerManager;
import com.sugarmq.manager.SugarMQMessageManager;

/**
 * 类说明：
 *
 * 类描述：
 * @author manzhizhen
 *
 * 2014年12月15日
 */
public interface SugarMQTransprotCenter {
	public void start() throws JMSException;
	
	public void close() throws JMSException;
	
	public void remove(SugarMQServerTransport sugarMQServerTransport);
	
	public void setSugarMQCustomerManager(SugarMQConsumerManager sugarMQCustomerManager);
	
	public void setSugarMQMessageManager(SugarMQMessageManager sugarMQMessageManager);
}
