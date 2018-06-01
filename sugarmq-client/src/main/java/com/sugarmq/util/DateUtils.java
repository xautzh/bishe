/**
 * 
 */
package com.sugarmq.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 类说明：日期工具类
 *
 * 类描述：日期工具类
 * @author manzhizhen
 * 2014-11-14
 */
public class DateUtils {
	public static final String DATE_FORMAT_TYPE1 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_TYPE2 = "yyyyMMddHHmmss";
	public static final String DATE_FORMAT_TYPE3 = "yyyyMMddHHmmssSSS";	
	/**
	 * 格式化日期成字符串
	 * 线程安全
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = (StringUtils.isBlank(pattern) ? 
				new SimpleDateFormat() : new SimpleDateFormat(pattern));
		return sdf.format(date);
	}
	
	public static String formatDate(String pattern) {
		return formatDate(new Date(), pattern);
	}
	
	/**
	 * 字符串转化成日期
	 * 线程安全
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException 
	 */
	public static Date formatDate(String date, String pattern) throws ParseException {
		SimpleDateFormat sdf = (StringUtils.isBlank(pattern) ? 
				new SimpleDateFormat() : new SimpleDateFormat(pattern));
		return sdf.parse(date);
	}
}
