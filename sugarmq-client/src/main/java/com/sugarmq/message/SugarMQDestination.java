package com.sugarmq.message;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.commons.lang3.StringUtils;

import com.sugarmq.constant.MessageContainerType;

public class SugarMQDestination implements Queue, Topic, Serializable {

	private static final long serialVersionUID = 4315929928684782158L;
	
	protected String name;
	protected String type;
	
	public SugarMQDestination(String name, String type) {
		if(StringUtils.isBlank(name) || StringUtils.isBlank(type)) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getTopicName() throws JMSException {
		return name;
	}

	@Override
	public String getQueueName() throws JMSException {
		return name;
	}
	
	public boolean isQueue() {
		return MessageContainerType.QUEUE.getValue().equals(type);
	}
	
	public boolean isTopic() {
		return MessageContainerType.TOPIC.getValue().equals(type);
	}
	
	
}
