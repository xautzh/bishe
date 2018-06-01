package com.sugarmq.message;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;




public abstract class Message implements javax.jms.Message, Serializable{
	private static final long serialVersionUID = -7369304352831197854L;
	
	protected String messageId;
	protected String correlationId;	//	关联ID
	protected int deliverMode;	// 持久模式和非持久模式
	protected long timestamp;
	protected long expiration;		// 有效期
	protected int priority;		// 优先级
	protected boolean redelivered;	// 重传标记
	protected String messageType;	// 消息类型
	protected SugarMQDestination destination;
	protected SugarMQDestination replyDestination;
	
	protected Map<String, Boolean> booleanMap = new HashMap<String, Boolean>(5);
	protected Map<String, Byte> byteMap = new HashMap<String, Byte>(5);
	protected Map<String, Short> shortMap = new HashMap<String, Short>(5);
	protected Map<String, Integer> intMap = new HashMap<String, Integer>(5);
	protected Map<String, Long> longMap = new HashMap<String, Long>(5);
	protected Map<String, Float> floatMap = new HashMap<String, Float>(5);
	protected Map<String, Double> doubleMap = new HashMap<String, Double>(5);
	protected Map<String, String> stringMap = new HashMap<String, String>(5);
	protected Map<String, Object> objectMap = new HashMap<String, Object>(5);
	
	public static final int OBJECT_BYTE_SIZE = 2048;
	
	public Message() {
	}

	@Override
	public void setJMSReplyTo(Destination replyDestination) throws JMSException {
		this.replyDestination = (SugarMQDestination) replyDestination;
	}
	
	@Override
	public Destination getJMSReplyTo() throws JMSException {
		return replyDestination;
	}
	
	@Override
	public String getJMSCorrelationID() throws JMSException {
		return correlationId;
	}
	
	@Override
	public void setJMSCorrelationID(String correlationId) throws JMSException {
		this.correlationId = correlationId;
	}

	@Override
	public long getJMSTimestamp() throws JMSException {
		return timestamp;
	}

	@Override
	public void setJMSTimestamp(long timestamp) throws JMSException {
		this.timestamp = timestamp;
	}

	@Override
	public void setJMSExpiration(long expiration) throws JMSException {
		this.expiration = expiration;
	}
	
	@Override
	public long getJMSExpiration() throws JMSException {
		return expiration;
	}
	
	@Override
	public void setJMSPriority(int priority) throws JMSException {
		this.priority = priority;
	}
	
	@Override
	public int getJMSPriority() throws JMSException {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Override
	public void setJMSRedelivered(boolean redelivered) throws JMSException {
		this.redelivered = redelivered;
	}
	
	@Override
	public boolean getJMSRedelivered() throws JMSException {
		return redelivered;
	}
	
	@Override
	public void setJMSDeliveryMode(int deliverMode) throws JMSException {
		this.deliverMode = deliverMode;
	}
	
	@Override
	public int getJMSDeliveryMode() throws JMSException {
		return deliverMode;
	}
	
	@Override
	public void setJMSDestination(Destination destination) throws JMSException {
		this.destination = (SugarMQDestination) destination;
	}
	
	@Override
	public Destination getJMSDestination() throws JMSException {
		return destination;
	}
	
	@Override
	public void setJMSMessageID(String messageId) throws JMSException {
		this.messageId = messageId;
	}
	
	@Override
	public String getJMSMessageID() throws JMSException {
		return messageId;
	}
	
	@Override
	public void setJMSType(String messageType) throws JMSException {
		this.messageType = messageType;
	}
	
	@Override
	public String getJMSType() throws JMSException {
		return messageType;
	}
	
	@Override
	public boolean getBooleanProperty(String key) throws JMSException {
		return booleanMap.get(key);
	}

	@Override
	public byte getByteProperty(String key) throws JMSException {
		return byteMap.get(key);
	}

	@Override
	public double getDoubleProperty(String key) throws JMSException {
		return doubleMap.get(key);
	}

	@Override
	public float getFloatProperty(String key) throws JMSException {
		return floatMap.get(key);
	}

	@Override
	public int getIntProperty(String key) throws JMSException {
		return intMap.get(key);
	}

	@Override
	public long getLongProperty(String key) throws JMSException {
		return longMap.get(key);
	}

	@Override
	public Object getObjectProperty(String key) throws JMSException {
		return objectMap.get(key);
	}

	@Override
	public short getShortProperty(String key) throws JMSException {
		return shortMap.get(key);
	}
	
	@Override
	public String getStringProperty(String key) throws JMSException {
		return stringMap.get(key);
	}

	@Override
	public void setBooleanProperty(String key, boolean value)
			throws JMSException {
		booleanMap.put(key, value);
	}

	@Override
	public void setByteProperty(String key, byte value) throws JMSException {
		byteMap.put(key, value);
	}

	@Override
	public void setDoubleProperty(String key, double value) throws JMSException {
		doubleMap.put(key, value);
	}

	@Override
	public void setFloatProperty(String key, float value) throws JMSException {
		floatMap.put(key, value);
	}

	@Override
	public void setIntProperty(String key, int value) throws JMSException {
		intMap.put(key, value);
	}

	@Override
	public void setLongProperty(String key, long value) throws JMSException {
		longMap.put(key, value);
	}

	@Override
	public void setObjectProperty(String key, Object value) throws JMSException {
		objectMap.put(key, value);
	}

	@Override
	public void setShortProperty(String key, short value) throws JMSException {
		shortMap.put(key, value);
	}

	@Override
	public void setStringProperty(String key, String value) throws JMSException {
		stringMap.put(key, value);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getPropertyNames() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean propertyExists(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}
}
