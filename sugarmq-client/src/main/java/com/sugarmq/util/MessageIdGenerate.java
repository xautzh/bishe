/**
 * 
 */
package com.sugarmq.util;

import java.util.Random;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来生成消息ID
 * ID总位数为21位
 * @author manzhizhen
 */
public class MessageIdGenerate {
	private static Logger logger = LoggerFactory.getLogger(MessageIdGenerate.class);
	
	/**
	 * 返回一个唯一的消息ID
	 * @return
	 */
	public static String getNewMessageId() throws JMSException{
		String startNum = DateUtils.formatDate(DateUtils.DATE_FORMAT_TYPE3);
		Random random = new Random();
		int next = random.nextInt(100000000);
		
		String result = startNum + String.format("%1$08d", next);
		logger.debug("生成的消息ID:{}", result);
		
		return result;
	}
	
}
