/**
 * 
 */
package com.sugarmq.util;

import java.util.Random;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session 的 ID 生成器
 * ID总位数为21位
 * @author manzhizhen
 *
 */
public class SessionIdGenerate {
	private static Logger logger = LoggerFactory.getLogger(SessionIdGenerate.class);
	
	/**
	 * 返回一个唯一的Session ID
	 * @return
	 */
	public static String getNewSessionId() throws JMSException{
		String startNum = DateUtils.formatDate(DateUtils.DATE_FORMAT_TYPE3);
		Random random = new Random();
		int next = random.nextInt(1000000);
		
		String result = startNum + String.format("%1$06d", next);
		logger.debug("生成的会话ID:{}", result);
		
		return result;
	}
}
