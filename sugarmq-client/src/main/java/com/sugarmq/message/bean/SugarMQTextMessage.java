package com.sugarmq.message.bean;


import javax.jms.JMSException;
import javax.jms.TextMessage;

public class SugarMQTextMessage extends SugarMQMessage implements TextMessage {
	private static final long serialVersionUID = 630846251491739491L;
	
	private String textMessage;
	
	public SugarMQTextMessage() {
		super();
	}
	
	public SugarMQTextMessage(String textMessage) {
		super();
		this.textMessage = textMessage;
	}
	

	@Override
	public String getText() throws JMSException {
		return textMessage;
	}

	@Override
	public void setText(String textMessage) throws JMSException {
		this.textMessage = textMessage;

	}

}
