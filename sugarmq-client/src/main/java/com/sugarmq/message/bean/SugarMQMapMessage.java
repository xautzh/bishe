package com.sugarmq.message.bean;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.jms.JMSException;
import javax.jms.MapMessage;

public class SugarMQMapMessage extends SugarMQMessage implements MapMessage{
	private ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();
	
	public SugarMQMapMessage() {
		super();
	}

	private static final long serialVersionUID = 2190580576217128868L;

	@Override
	public boolean getBoolean(String key) throws JMSException {
		return (Boolean) map.get(key);
	}

	@Override
	public byte getByte(String key) throws JMSException {
		return (Byte) map.get(key);
	}

	@Override
	public byte[] getBytes(String key) throws JMSException {
		return (byte[]) map.get(key);
	}

	@Override
	public char getChar(String key) throws JMSException {
		return (Character) map.get(key);
	}

	@Override
	public double getDouble(String key) throws JMSException {
		return (Double) map.get(key);
	}

	@Override
	public float getFloat(String key) throws JMSException {
		return (Float) map.get(key);
	}

	@Override
	public int getInt(String key) throws JMSException {
		return (Integer) map.get(key);
	}

	@Override
	public long getLong(String key) throws JMSException {
		return (Long) map.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<String> getMapNames() throws JMSException {
		return (Enumeration<String>) map.keySet();
	}

	@Override
	public Object getObject(String key) throws JMSException {
		return map.get(key);
	}

	@Override
	public short getShort(String key) throws JMSException {
		return (Short) map.get(key);
	}

	@Override
	public String getString(String key) throws JMSException {
		return (String) map.get(key);
	}

	@Override
	public boolean itemExists(String key) throws JMSException {
		return map.containsKey(key);
	}

	@Override
	public void setBoolean(String key, boolean value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setByte(String key, byte value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setBytes(String key, byte[] value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setBytes(String key, byte[] value, int from, int to)
			throws JMSException {
		map.put(key, Arrays.copyOfRange(value, from, to));
	}

	@Override
	public void setChar(String key, char value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setDouble(String key, double value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setFloat(String key, float value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setInt(String key, int value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setLong(String key, long value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setObject(String key, Object value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setShort(String key, short value) throws JMSException {
		map.put(key, value);
	}

	@Override
	public void setString(String key, String value) throws JMSException {
		map.put(key, value);
	}

}
