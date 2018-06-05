/**
 *
 */
package com.sugarmq.producer;

import com.sugarmq.constant.MessageProperty;
import com.sugarmq.constant.MessageType;
import com.sugarmq.message.SugarMQDestination;
import com.sugarmq.transport.MessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 抽象的消息生产者
 *
 * @author xautzh
 */
public class SugarMQMessageProducer implements MessageProducer {
    protected volatile AtomicInteger deliveryMode = new AtomicInteger(Message.DEFAULT_DELIVERY_MODE); // 持久性和非持久性
    protected volatile AtomicLong timeToLive = new AtomicLong(Message.DEFAULT_TIME_TO_LIVE); // 消息有效期
    protected volatile AtomicInteger priority = new AtomicInteger(Message.DEFAULT_PRIORITY);    // 消息优先级

    protected volatile AtomicBoolean disableMessageId = new AtomicBoolean(false);

    private MessageDispatcher messageDispatcher;
    private Destination destination;

    private Logger logger = LoggerFactory.getLogger(SugarMQMessageProducer.class);

    public SugarMQMessageProducer(Destination destination, MessageDispatcher messageDispatcher) {
        this.destination = destination;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public int getDeliveryMode() throws JMSException {
        return deliveryMode.get();
    }

    @Override
    public int getPriority() throws JMSException {
        return priority.get();
    }

    @Override
    public long getTimeToLive() throws JMSException {
        return timeToLive.get();
    }

    @Override
    public void send(Message message) throws JMSException {
        logger.debug("即将发送一条消息:{}", message);
        message.setJMSType(MessageType.PRODUCER_MESSAGE.getValue()); // 设置消息类型
        message.setJMSDestination(destination);
        if (((SugarMQDestination)destination).isTopic()){
            //设置持续时间 默认为一天
            message.setJMSExpiration(1000*60*60*24);
        }
        message.setBooleanProperty(MessageProperty.DISABLE_MESSAGE_ID.getKey(), disableMessageId.get());
        messageDispatcher.sendMessage(message);
    }

    @Override
    public void send(Destination arg0, Message arg1) throws JMSException {
        // TODO Auto-generated method stub
    }

    @Override
    public void send(Message message, int arg1, int arg2, long time)
            throws JMSException {
        // TODO Auto-generated method stub
        logger.debug("即将发送一条消息:{}", message);
        message.setJMSType(MessageType.PRODUCER_MESSAGE.getValue()); // 设置消息类型
        message.setJMSDestination(destination);
        message.setJMSExpiration(time);
        message.setBooleanProperty(MessageProperty.DISABLE_MESSAGE_ID.getKey(), disableMessageId.get());
        messageDispatcher.sendMessage(message);
    }

    @Override
    public void send(Destination arg0, Message arg1, int arg2, int arg3,
                     long arg4) throws JMSException {
        // TODO Auto-generated method stub
    }

    @Override
    public void setDeliveryMode(int deliveryMode) throws JMSException {
        this.deliveryMode.set(deliveryMode);
    }

    @Override
    public void setPriority(int priority) throws JMSException {
        this.priority.set(priority);
    }

    @Override
    public void setTimeToLive(long timeToLive) throws JMSException {
        this.timeToLive.set(timeToLive);
    }

    @Override
    public void close() throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public Destination getDestination() throws JMSException {
        // TODO Auto-generated method stub
        return this.destination;
    }

    @Override
    public boolean getDisableMessageID() throws JMSException {
        return this.disableMessageId.get();
    }

    @Override
    public boolean getDisableMessageTimestamp() throws JMSException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setDisableMessageID(boolean disableMessageId) throws JMSException {
        this.disableMessageId.set(disableMessageId);

    }

    @Override
    public void setDisableMessageTimestamp(boolean arg0) throws JMSException {
        // TODO Auto-generated method stub

    }
}
