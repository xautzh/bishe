package com.suprememq.core;

import com.suprememq.constant.ConsumerState;
import com.suprememq.constant.MessageContainerType;
import com.suprememq.constant.MessageProperty;
import com.suprememq.constant.MessageType;
import com.suprememq.consumer.SupremeMQMessageConsumer;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.message.bean.SupremeMQTextMessage;
import com.suprememq.producer.SupremeMQMessageProducer;
import com.suprememq.transport.MessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SupremeMQSession implements Session {
    private String sessionId;
    private boolean transacted;    // 事务标记
    private MessageDispatcher messageDispatcher;
    // key-消费者ID，value-消费者对象
    private ConcurrentMap<String, SupremeMQMessageConsumer> consumerMap = new ConcurrentHashMap<String, SupremeMQMessageConsumer>();

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private Logger logger = LoggerFactory.getLogger(SupremeMQSession.class);

    public SupremeMQSession(String sessionId, boolean transacted, MessageDispatcher messageDispatcher) throws JMSException {
        this.sessionId = sessionId;
        this.transacted = transacted;
        this.messageDispatcher = messageDispatcher;
    }

    public void start() {
        synchronized (isStarted) {
            for (SupremeMQMessageConsumer consumer : consumerMap.values()) {
                consumer.start();
            }

            isStarted.set(true);
        }
    }

    public ConcurrentMap<String, SupremeMQMessageConsumer> getConsumerMap() {
        return consumerMap;
    }

    @Override
    public void close() throws JMSException {
        // TODO Auto-generated method stub
        TextMessage textMessage = new SupremeMQTextMessage();
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
        if (!(destination instanceof SupremeMQDestination)) {
            logger.warn("传入的Destination非法！");
            throw new JMSException("传入的Destination非法:" + destination);
        }
        SupremeMQMessageConsumer supremeQueueReceiver = new SupremeMQMessageConsumer(destination,
                messageDispatcher.getSendMessageQueue(), 10);
        supremeQueueReceiver.setState(ConsumerState.WORKING.getValue());
        messageDispatcher.addConsumer(supremeQueueReceiver);
        consumerMap.put(supremeQueueReceiver.getConsumerId(), supremeQueueReceiver);
        System.out.println("当前的consumerMap大小为" + consumerMap.size());
        supremeQueueReceiver.start();
        return supremeQueueReceiver;
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
        if (!(destination instanceof SupremeMQDestination)) {
            logger.warn("传入的Destination非法！");
            throw new JMSException("传入的Destination非法！");
        }
        SupremeMQMessageProducer supremeQueueSender = new SupremeMQMessageProducer(destination, messageDispatcher);
        return supremeQueueSender;
    }

    @Override
    public Queue createQueue(String name) throws JMSException {
        // TODO Auto-generated method stub
        return new SupremeMQDestination(name, MessageContainerType.QUEUE.getValue());
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
        SupremeMQTextMessage supremeTextMessage = new SupremeMQTextMessage();
        return supremeTextMessage;
    }

    @Override
    public TextMessage createTextMessage(String message) throws JMSException {
        SupremeMQTextMessage supremeTextMessage = new SupremeMQTextMessage(message);
        supremeTextMessage.setStringProperty(MessageProperty.SESSION_ID.getKey(), sessionId);
        return supremeTextMessage;
    }

    @Override
    public Topic createTopic(String s) throws JMSException {
        // TODO Auto-generated method stub
        return new SupremeMQDestination(s, MessageContainerType.TOPIC.getValue());
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
