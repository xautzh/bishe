package com.sugarmq.message.bean;

import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;

public class SugarMQBytesMessage extends SugarMQMessage implements BytesMessage {
	private static final long serialVersionUID = -8641306528891219928L;

	public SugarMQBytesMessage() {
		super();
	}
	
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
	public boolean getBooleanProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte getByteProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDoubleProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloatProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIntProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getJMSCorrelationID() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getJMSDeliveryMode() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Destination getJMSDestination() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getJMSExpiration() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getJMSMessageID() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getJMSPriority() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getJMSRedelivered() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Destination getJMSReplyTo() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getJMSTimestamp() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getJMSType() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLongProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getObjectProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getPropertyNames() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShortProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStringProperty(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean propertyExists(String arg0) throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBooleanProperty(String arg0, boolean arg1)
			throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setByteProperty(String arg0, byte arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDoubleProperty(String arg0, double arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFloatProperty(String arg0, float arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setIntProperty(String arg0, int arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSCorrelationID(String arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSCorrelationIDAsBytes(byte[] arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSDeliveryMode(int arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSDestination(Destination arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSExpiration(long arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSMessageID(String arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSPriority(int arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSRedelivered(boolean arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSReplyTo(Destination arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSTimestamp(long arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setJMSType(String arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLongProperty(String arg0, long arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setObjectProperty(String arg0, Object arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setShortProperty(String arg0, short arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStringProperty(String arg0, String arg1) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public long getBodyLength() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean readBoolean() throws JMSException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte readByte() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readBytes(byte[] arg0) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readBytes(byte[] arg0, int arg1) throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char readChar() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double readDouble() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float readFloat() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readInt() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long readLong() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short readShort() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String readUTF() throws JMSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int readUnsignedByte() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int readUnsignedShort() throws JMSException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reset() throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBoolean(boolean arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeByte(byte arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBytes(byte[] arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBytes(byte[] arg0, int arg1, int arg2) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeChar(char arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeDouble(double arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeFloat(float arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeInt(int arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeLong(long arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeObject(Object arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeShort(short arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeUTF(String arg0) throws JMSException {
		// TODO Auto-generated method stub

	}

}
