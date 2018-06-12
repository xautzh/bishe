package com.suprememq.message.bean;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public class SupremeMQObjectMessage extends SupremeMQMessage implements ObjectMessage {
	/**
	 * @param
	 */
	public SupremeMQObjectMessage() {
		super();
	}

	private static final long serialVersionUID = -3848421894529662497L;

	@Override
	public void acknowledge() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearBody() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearProperties() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable getObject() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObject(Serializable arg0) throws JMSException {
		// TODO Auto-generated method stub
		
	}
}
