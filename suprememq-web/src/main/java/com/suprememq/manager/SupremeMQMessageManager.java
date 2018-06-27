package com.suprememq.manager;

import com.suprememq.constant.MessageContainerType;
import com.suprememq.dao.MessageDao;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.queue.SupremeMQMessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对MOM的队列进行管理
 *
 * @author xautzh
 */
@Component
@PropertySource(value = "classpath:suprememq-config.properties", ignoreResourceNotFound = true)
public class SupremeMQMessageManager {
    private @Value("${max_queue_message_num}")
    int MAX_QUEUE_MESSAGE_CAPACITY; // 队列中所能容纳的消息最大数
    private @Value("${max_queue_num}")
    int MAX_QUEUE_NUM; // 队列数量的最大值
    // 消息队列
    private ConcurrentHashMap<String, SupremeMQMessageContainer> messageContainerMap =
            new ConcurrentHashMap<String, SupremeMQMessageContainer>();
    private ConcurrentHashMap<String, List<Message>> allMessageMap =
            new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(SupremeMQMessageManager.class);

    public ConcurrentHashMap<String, SupremeMQMessageContainer> getMessageContainerMap() {
        return messageContainerMap;
    }

    /**
     * 将一个消息放入队列中
     *
     * @param message
     */
    public void addMessage(Message message) throws JMSException {
        // 如果是持久化消息，需要将消息持久化。
        if (DeliveryMode.PERSISTENT == message.getJMSDeliveryMode()) {
            logger.info("持久化消息:{}", message);
            persistentMessage(message);
        }
        SupremeMQDestination destination = (SupremeMQDestination) message.getJMSDestination();
        if (destination.isQueue()) {
            logger.debug("队列消息【{}】", message);
            // 将消息放入消息队列
            String name = destination.getQueueName();
            SupremeMQMessageContainer queue = getSupremeMQMessageContainer(name);
            List<Message> messageVoList = getMessageList(name);
            if (messageContainerMap.size() >= MAX_QUEUE_NUM) {
                logger.warn("MOM中队列数已满，添加队列失败:【{}】", name);
                throw new JMSException("MOM中队列数已满，添加队列失败:【{}】", name);
            }
            message.setJMSDestination(queue);
            logger.debug("将消息放入分发队列:【{}】", message);
            queue.putMessage(message);
            messageVoList.add(message);
        } else if (destination.isTopic()) {
            logger.debug("主题消息【{}】", message);
            String name = destination.getTopicName();
            SupremeMQMessageContainer topic = getSupremeMQMessageTopicContainer(name);
            if (messageContainerMap.size() >= MAX_QUEUE_NUM) {
                logger.warn("MOM中队列数已满，添加队列失败:【{}】", name);
                throw new JMSException("MOM中队列数已满，添加队列失败:【{}】", name);
            }
            logger.debug("将消息放入分发主题队列:【{}】", message);
            topic.putMessage(message);
        }
    }

    /**
     * 内部调用 队列消息
     *
     * @param name
     * @return
     */
    public SupremeMQMessageContainer getSupremeMQMessageContainer(String name) {
        SupremeMQMessageContainer queue = messageContainerMap.putIfAbsent(name, new SupremeMQMessageContainer(name,
                MessageContainerType.QUEUE.getValue()));
        if (queue == null) {
            queue = messageContainerMap.get(name);
        }
        return queue;
    }

    public SupremeMQMessageContainer getSupremeMQMessageTopicContainer(String name) {
        SupremeMQMessageContainer topic = messageContainerMap.putIfAbsent(name,
                new SupremeMQMessageContainer(name, MessageContainerType.TOPIC.getValue()));
        if (topic == null) {
            topic = messageContainerMap.get(name);
        }
        return topic;
    }

    /**
     * web层调用 队列
     */
    public List<Message> getMessageList(String name) {
        List<Message> messageVoList = allMessageMap.putIfAbsent(name,
                new ArrayList<>());
        if (messageVoList == null) {
            messageVoList = allMessageMap.get(name);
        }
        return messageVoList;
    }

    /**
     * 将一个消息从消息队列中移除
     *
     * @param message
     */
    public void removeMessage(Message message) throws JMSException {
        // 将消息放入消息队列
        SupremeMQDestination supremeQueue = (SupremeMQDestination) message.getJMSDestination();
        SupremeMQMessageContainer queue = messageContainerMap.get(supremeQueue.getQueueName());
        Message removeMessage = null;
        if (queue != null) {
            // 如果是持久化消息，需要将消息持久化。
            BlockingQueue<Message> consumeQueue = queue.getConsumeMessageQueue();
            for (Message m : consumeQueue) {
                if (m.getJMSMessageID().equals(message.getJMSCorrelationID())) {
                    removeMessage = m;
                    break;
                }
            }
            if (removeMessage != null) {
                if (DeliveryMode.PERSISTENT == removeMessage.getJMSDeliveryMode()) {
                    logger.debug("是持久化消息，需要从数据库中删除");
                    removePersistentMessage(removeMessage);
                }
            }
            queue.removeMessage(removeMessage);
        } else {
            logger.error("不存在的队列名称【{}】，移除消息{}失败！", supremeQueue.getQueueName(), message);
        }

    }

    /**
     * 持久化一条消息
     *
     * @param message
     * @throws JMSException
     */
    private void persistentMessage(Message message) throws JMSException {
        //TODO
        new MessageDao().addMessage(message);
    }

    /**
     * 将消息从持久化中移除
     *
     * @param message
     * @throws JMSException
     */
    private void removePersistentMessage(Message message) throws JMSException {
        //TODO
        new MessageDao().removeMessage(message);
    }

    public ConcurrentHashMap<String, List<Message>> getAllMessageMap() {
        return allMessageMap;
    }

}
