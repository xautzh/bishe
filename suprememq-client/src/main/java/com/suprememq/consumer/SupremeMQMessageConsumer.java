package com.suprememq.consumer;


import com.suprememq.constant.ConsumerState;
import com.suprememq.constant.MessageProperty;
import com.suprememq.constant.MessageType;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.message.bean.SupremeMQMessage;
import com.suprememq.util.ListenerUtil;
import com.suprememq.util.MessageIdGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class SupremeMQMessageConsumer implements MessageConsumer {
    private String consumerId;    // 消费者ID，可以由客户端设置，但最终由服务端来决定

    private String state;    // 消费者的状态

    private String messageSelector;
    private Destination destination;

    private Runnable consumeMessageTask;
    private Runnable ackMessageTask;

    // 未消费的消息队列
    private BlockingQueue<Message> messageQueue;
    // 已消费但还未应答的消息队列
    private BlockingQueue<Message> ackMessageQueue;

    // SupremeMQTransport中的消息发送队列
    private BlockingQueue<Message> sendMessageQueue;

    private MessageListener messageListener;

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private Logger logger = LoggerFactory.getLogger(SupremeMQMessageConsumer.class);

    public void setState(String state) {
        this.state = state;
    }

    /**
     * @param destination
     * @param sendMessageQueue SupremeMQTransport中的sendMessageQueue
     * @param cacheSize        消息缓冲队列的大小
     */
    public SupremeMQMessageConsumer(Destination destination, BlockingQueue<Message> sendMessageQueue, int cacheSize) {
        if (destination == null) {
            throw new IllegalArgumentException("创建消费者失败，Destination为空！");
        }

        if (cacheSize <= 0) {
            throw new IllegalArgumentException("创建消费者失败，cacheSize必须大于0！");
        }

        this.destination = destination;

        messageQueue = new LinkedBlockingQueue<Message>(cacheSize);
        ackMessageQueue = new LinkedBlockingQueue<Message>(cacheSize);
        this.sendMessageQueue = sendMessageQueue;

        consumeMessageTask = new ConsumeMessageTask(this);
        ackMessageTask = new AckMessageTask(this);

        // 设置状态为创建状态
        state = ConsumerState.CREATE.getValue();
        logger.debug("新建立了一个消费者");
    }

    /**
     * 开启一个消费者
     */
    public void start() {
        // TODO:
        new Thread(consumeMessageTask).start();
        new Thread(ackMessageTask).start();
    }

    /**
     * 给消费者分配一条消息
     *
     * @param message
     */
    public void putMessage(Message message) {
        if (ConsumerState.WORKING.getValue().equals(state)) {
            try {
                messageQueue.put(message);
            } catch (InterruptedException e) {
                logger.error("给消费者【{}】分配消息【{}】失败【{}】", this, message, e);
            }
        } else {
            logger.error("给消费者【{}】分配消息【{}】失败，消费者状态错误", this, message);
        }
    }

    @Override
    public void close() throws JMSException {
        state = ConsumerState.DEATH.getValue();
        messageQueue.clear();
        messageQueue = null;
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return this.messageListener;
    }

    @Override
    public String getMessageSelector() throws JMSException {
        return messageSelector;
    }

    @Override
    public Message receive() throws JMSException {
        return receive(0);
    }

    @Override
    public Message receive(long time) throws JMSException {
        return null;
    }

    @Override
    public Message receiveNoWait() throws JMSException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setMessageListener(MessageListener messageListener) throws JMSException {
        if (messageListener == null) {
            throw new JMSException("消息监听器不能为空！");
        }

        this.messageListener = messageListener;
    }


    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }


    public Destination getDestination() {
        return destination;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    public String getState() {
        return state;
    }

    public BlockingQueue<Message> getAckMessageQueue() {
        return ackMessageQueue;
    }


    /**
     * 类说明：消费消息线程
     * <p>
     * 类描述：
     *
     * @author zh
     * <p>
     * 2014年12月17日
     */
    class ConsumeMessageTask implements Runnable {
        private SupremeMQMessageConsumer consumer;

        private Logger logger = LoggerFactory.getLogger(ConsumeMessageTask.class);

        public ConsumeMessageTask(SupremeMQMessageConsumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            try {
                MessageListener listener = consumer.getMessageListener();
                if (listener == null) {
                    SupremeMQDestination destination = (SupremeMQDestination) consumer.destination;
                    logger.error("消费者{}没有配置消息监听器！", consumer);
                    listener = ListenerUtil.setListener(consumer, destination.getName());
                }
                BlockingQueue<Message> queue = consumer.getMessageQueue();
                Message message = null;
                while (!Thread.currentThread().isInterrupted() &&
                        ConsumerState.WORKING.getValue().equals(consumer.getState())) {
                    try {
                        message = queue.poll();
                        // 如果消息缓存空了，就向服务端发送拉取消息
                        if (message == null) {
                            // 发送拉取消息
                            SupremeMQMessage pullMessage = new SupremeMQMessage();
                            pullMessage.setStringProperty(MessageProperty.CUSTOMER_ID.getKey(), consumerId);
                            pullMessage.setJMSMessageID(MessageIdGenerate.getNewMessageId());
                            pullMessage.setJMSType(MessageType.CUSTOMER_MESSAGE_PULL.getValue());
                            pullMessage.setJMSDestination(consumer.getDestination());
                            sendMessageQueue.put(pullMessage);

                            logger.debug("拉取消息【{}】已被放入发送队列。", pullMessage);

                            message = queue.take();
                        }

                        // 消费消息
                        listener.onMessage(message);
                        // 放入应答队列
                        consumer.getAckMessageQueue().put(message);

                    } catch (InterruptedException e) {
                        logger.error("消费者【{}】消费消息线程被中断【{}】", consumer, e);
                        break;
                    }
                }

            } catch (JMSException e) {
                logger.error("消费者【{}】消费消息失败：【{}】", consumer, e);
            }
        }
    }

    /**
     * 类说明：应答消息线程
     * <p>
     * 类描述：
     *
     * @author zh
     * <p>
     * 2014年12月17日
     */
    class AckMessageTask implements Runnable {
        private SupremeMQMessageConsumer consumer;

        private Logger logger = LoggerFactory.getLogger(ConsumeMessageTask.class);

        public AckMessageTask(SupremeMQMessageConsumer consumer) {
            this.consumer = consumer;
        }

        @Override
        public void run() {
            Message message = null;
            while (!Thread.currentThread().isInterrupted() &&
                    ConsumerState.WORKING.getValue().equals(consumer.getState())) {
                try {
                    message = consumer.getAckMessageQueue().take();
                    // 创建应答消息
                    SupremeMQMessage ackMessage = new SupremeMQMessage();
                    ackMessage.setJMSType(MessageType.CUSTOMER_ACKNOWLEDGE_MESSAGE.getValue());
                    ackMessage.setJMSCorrelationID(message.getJMSMessageID());
                    ackMessage.setJMSMessageID(MessageIdGenerate.getNewMessageId());
                    ackMessage.setJMSDestination(message.getJMSDestination());
                    ackMessage.setJMSDestination(message.getJMSDestination());
                    sendMessageQueue.put(ackMessage);
                    logger.debug("应答消息【{}】已被放入发送队列。", ackMessage);

                } catch (InterruptedException e) {
                    logger.debug("AckMessageTask 被中断");
                } catch (JMSException e) {
                    logger.error("【{}】发送应消息【{}】失败：【{}】", consumer, message, e);
                }
            }

        }
    }

}
