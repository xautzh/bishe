package com.sugarmq.core;

import com.sugarmq.constant.ConsumerState;
import com.sugarmq.constant.MessageContainerType;
import com.sugarmq.constant.MessageProperty;
import com.sugarmq.constant.MessageType;
import com.sugarmq.consumer.SugarMQMessageConsumer;
import com.sugarmq.message.SugarMQDestination;
import com.sugarmq.message.bean.SugarMQTextMessage;
import com.sugarmq.producer.SugarMQMessageProducer;
import com.sugarmq.transport.MessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SugarMQSession implements Session {
    private String sessionId;
    private boolean transacted;    // 事务标记
    private MessageDispatcher messageDispatcher;
    // key-消费者ID，value-消费者对象
    private ConcurrentMap<String, SugarMQMessageConsumer> consumerMap = new ConcurrentHashMap<String, SugarMQMessageConsumer>();

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private Logger logger = LoggerFactory.getLogger(SugarMQSession.class);

    public SugarMQSession(String sessionId, boolean transacted, MessageDispatcher messageDispatcher) throws JMSException {
        this.sessionId = sessionId;
        this.transacted = transacted;
        this.messageDispatcher = messageDispatcher;
    }

    public void start() {
        synchronized (isStarted) {
            for (SugarMQMessageConsumer consumer : consumerMap.values()) {
                consumer.start();
            }

            isStarted.set(true);
        }
    }

    public ConcurrentMap<String, SugarMQMessageConsumer> getConsumerMap() {
        return consumerMap;
    }

    @Override
    public void close() throws JMSException {
        // TODO Auto-generated method stub
        TextMessage textMessage = new SugarMQTextMessage();
        textMessage.setJMSType(MessageType.DISCONNECT_MESSAGE.getValue());
        messageDispatcher.sendMessage(textMessage);
    }

    @Override
    public void commit() throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public QueueBrowser createBrowser(Queue arg0) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public QueueBrowser createBrowser(Queue arg0, String arg1)
            throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BytesMessage createBytesMessage() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageConsumer createConsumer(Destination destination) throws JMSException {
        if (!(destination instanceof SugarMQDestination)) {
            logger.warn("传入的Destination非法！");
            throw new JMSException("传入的Destination非法:" + destination);
        }
        SugarMQMessageConsumer sugarQueueReceiver = new SugarMQMessageConsumer(destination,
                messageDispatcher.getSendMessageQueue(), 10);
        sugarQueueReceiver.setState(ConsumerState.WORKING.getValue());
        messageDispatcher.addConsumer(sugarQueueReceiver);
        consumerMap.put(sugarQueueReceiver.getConsumerId(), sugarQueueReceiver);
        System.out.println("当前的consumerMap大小为" + consumerMap.size());
        sugarQueueReceiver.start();
        return sugarQueueReceiver;
    }

    @Override
    public MessageConsumer createConsumer(Destination arg0, String arg1)
            throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageConsumer createConsumer(Destination arg0, String arg1,
                                          boolean arg2) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1)
            throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TopicSubscriber createDurableSubscriber(Topic arg0, String arg1,
                                                   String arg2, boolean arg3) throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MapMessage createMapMessage() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Message createMessage() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectMessage createObjectMessage() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectMessage createObjectMessage(Serializable arg0)
            throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageProducer createProducer(Destination destination) throws JMSException {
        if (!(destination instanceof SugarMQDestination)) {
            logger.warn("传入的Destination非法！");
            throw new JMSException("传入的Destination非法！");
        }
        SugarMQMessageProducer sugarQueueSender = new SugarMQMessageProducer(destination, messageDispatcher);
        return sugarQueueSender;
    }

    @Override
    public Queue createQueue(String name) throws JMSException {
        // TODO Auto-generated method stub
        return new SugarMQDestination(name, MessageContainerType.QUEUE.getValue());
    }

    @Override
    public StreamMessage createStreamMessage() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemporaryQueue createTemporaryQueue() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemporaryTopic createTemporaryTopic() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextMessage createTextMessage() throws JMSException {
        SugarMQTextMessage sugarTextMessage = new SugarMQTextMessage();
        return sugarTextMessage;
    }

    @Override
    public TextMessage createTextMessage(String message) throws JMSException {
        SugarMQTextMessage sugarTextMessage = new SugarMQTextMessage(message);
        sugarTextMessage.setStringProperty(MessageProperty.SESSION_ID.getKey(), sessionId);
        return sugarTextMessage;
    }

    @Override
    public Topic createTopic(String s) throws JMSException {
        // TODO Auto-generated method stub
        return new SugarMQDestination(s, MessageContainerType.TOPIC.getValue());
    }

    @Override
    public int getAcknowledgeMode() throws JMSException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getTransacted() throws JMSException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void recover() throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollback() throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMessageListener(MessageListener arg0) throws JMSException {
        // TODO Auto-generated method stub

    }

    @Override
    public void unsubscribe(String arg0) throws JMSException {
        // TODO Auto-generated method stub
    }
}
