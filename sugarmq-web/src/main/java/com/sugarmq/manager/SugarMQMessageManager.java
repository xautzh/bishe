package com.sugarmq.manager;

import com.sugarmq.constant.MessageContainerType;
import com.sugarmq.constant.MessageProperty;
import com.sugarmq.message.SugarMQDestination;
import com.sugarmq.message.bean.SugarMQMessage;
import com.sugarmq.queue.SugarMQMessageContainer;
import com.sugarmq.util.MessageIdGenerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对MOM的队列进行管理
 *
 * @author xautzh
 */
@Component
@PropertySource(value = "classpath:sugarmq-config.properties", ignoreResourceNotFound = true)
public class SugarMQMessageManager {
    private @Value("${max_queue_message_num}")
    int MAX_QUEUE_MESSAGE_CAPACITY; // 队列中所能容纳的消息最大数
    private @Value("${max_queue_num}")
    int MAX_QUEUE_NUM; // 队列数量的最大值

    // 消息队列
    private ConcurrentHashMap<String, SugarMQMessageContainer> messageContainerMap =
            new ConcurrentHashMap<String, SugarMQMessageContainer>();
    private ConcurrentHashMap<String, List<Message>> allMessageMap =
            new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, List<Message>> topicMessageMap =
            new ConcurrentHashMap<>();

    @Autowired
    private SugarMQConsumerManager sugarMQConsumerManager;

    private Logger logger = LoggerFactory.getLogger(SugarMQMessageManager.class);

    public ConcurrentHashMap<String, SugarMQMessageContainer> getMessageContainerMap() {
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
        SugarMQDestination destination = (SugarMQDestination) message.getJMSDestination();
        if (destination.isQueue()) {
            logger.debug("队列消息【{}】", message);
            // 将消息放入消息队列
            String name = destination.getQueueName();
            SugarMQMessageContainer queue = getSugarMQMessageContainer(name);
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
            SugarMQMessageContainer topic = getSugarMQMessageTopicContainer(name);
            List<Message> messageVoList = getTopicMessageList(name);
            if (messageContainerMap.size() >= MAX_QUEUE_NUM) {
                logger.warn("MOM中队列数已满，添加队列失败:【{}】", name);
                throw new JMSException("MOM中队列数已满，添加队列失败:【{}】", name);
            }
            message.setJMSDestination(topic);
            topic.putTopicMessage(message);
            messageVoList.add(message);
        }
    }

    /**
     * 内部调用 队列消息
     *
     * @param name
     * @return
     */
    public SugarMQMessageContainer getSugarMQMessageContainer(String name) {
        SugarMQMessageContainer queue = messageContainerMap.putIfAbsent(name, new SugarMQMessageContainer(name,
                MessageContainerType.QUEUE.getValue()));
        if (queue == null) {
            queue = messageContainerMap.get(name);
        }
        return queue;
    }

    public SugarMQMessageContainer getSugarMQMessageTopicContainer(String name) {
        SugarMQMessageContainer topic = messageContainerMap.putIfAbsent(name,
                new SugarMQMessageContainer(name, MessageContainerType.TOPIC.getValue()));
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
     * 主题
     *
     * @param name
     * @return
     */
    public List<Message> getTopicMessageList(String name) {
        List<Message> messageVoList = topicMessageMap.putIfAbsent(name,
                new ArrayList<>());
        if (messageVoList == null) {
            messageVoList = topicMessageMap.get(name);
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
        SugarMQDestination sugarQueue = (SugarMQDestination) message.getJMSDestination();
        SugarMQMessageContainer queue = messageContainerMap.get(sugarQueue.getQueueName());

        if (queue != null) {
            // 如果是持久化消息，需要将消息持久化。
            if (DeliveryMode.PERSISTENT == message.getJMSDeliveryMode()) {
                removePersistentMessage(message);
            }

            queue.removeMessage(message);
        } else {
            logger.error("不存在的队列名称【{}】，移除消息{}失败！", sugarQueue.getQueueName(), message);
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
    }

    /**
     * 将消息从持久化中移除
     *
     * @param message
     * @throws JMSException
     */
    private void removePersistentMessage(Message message) throws JMSException {
        //TODO
    }

    /**
     * 从生产者那里获取消息
     *
     * @param message
     * @throws JMSException
     */
    public Message receiveProducerMessage(Message message) throws JMSException {
//		if(!(message instanceof SugarMessage)) {
//			logger.error("接收到的生产者Message类型非法：" + message);
//			throw new JMSException("接收到的Message类型非法：" + message);
//		}

        // 获取客户端给消息设置的MessageId
        String clientMessageId = message.getJMSMessageID();

        if (!message.getBooleanProperty(MessageProperty.DISABLE_MESSAGE_ID.getKey())) {
            message.setJMSMessageID(MessageIdGenerate.getNewMessageId());
        } else {
            message.setJMSMessageID(null);
        }

        // 添加消息
        addMessage(message);

        Message acknowledgeMessage = new SugarMQMessage();
        acknowledgeMessage.setJMSMessageID(clientMessageId);

        return acknowledgeMessage;
    }

    /**
     * 从消费者那里接受消息应答
     *
     * @param message
     * @throws JMSException
     */
    public void receiveConsumerAcknowledgeMessage(Message message) throws JMSException {
        removeMessage(message);
    }

    public ConcurrentHashMap<String, List<Message>> getAllMessageMap() {
        return allMessageMap;
    }

    public ConcurrentHashMap<String, List<Message>> getTopicMessageMap() {
        return topicMessageMap;
    }
}
