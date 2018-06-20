/**
 *
 */
package com.suprememq.queue;

import com.suprememq.dao.MessageDao;
import com.suprememq.message.SupremeMQDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 队列和主题的消息容器
 *
 * @author xautzh
 */
public class SupremeMQMessageContainer extends SupremeMQDestination {
    private static final long serialVersionUID = 2122365866558582491L;

    // 待发送消息队列
    private transient BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
    // 已发送的消息队列
    private transient BlockingQueue<Message> consumeMessageQueue = new LinkedBlockingQueue<Message>();

    private static Logger logger = LoggerFactory.getLogger(SupremeMQMessageContainer.class);

    public SupremeMQMessageContainer(String name, String type) {
        super(name, type);
    }

    /**
     * 往消息队列中放入一条消息
     *
     * @throws JMSException
     */
    public void putMessage(Message message) throws JMSException {
        try {
            message.setJMSTimestamp(new Date().getTime());
            messageQueue.put(message);
            logger.debug("往队列【{}】添加一条消息:{}", name, message);
        } catch (InterruptedException e) {
            logger.error("往队列【{}】添加消息【{}】失败:{}", name, message, e);
            throw new JMSException(e.getMessage());
        }
    }
    /**
     * 从队列中获取一个消息
     * 没消息则阻塞
     *
     * @return
     * @throws JMSException
     */
    public Message takeMessage() throws JMSException {
        Message message = null;
        try {
            message = messageQueue.take();
            logger.debug("从队列【{}】取出一条消息:{}", name, message);
        } catch (InterruptedException e) {
            logger.error("从队列【{}】获取息失败:{}", name, e);
            throw new JMSException(e.getMessage());
        }

        return message;
    }

    /**
     * 从队列中获取最多指定数量消息
     * 没消息则阻塞，不保证能获取到指定数量的消息，但能保证至少返回一条消息
     *
     * @return
     * @throws JMSException
     */
    public List<Message> takeMessage(int messageSize) throws JMSException {
        if (messageSize <= 0) {
            throw new IllegalArgumentException("指定的消息数量必须大于0！");
        }

        List<Message> messageList = new ArrayList<Message>(messageSize);
        try {
            // 至少保证能取到一条消息
            messageList.add(messageQueue.take());

            // 尝试获取剩下的{@messageSize-1}条消息
            Message msg = null;
            for (int i = 1; i < messageSize; i++) {
                msg = messageQueue.poll();
                if (msg != null) {
                    messageList.add(msg);
                }
            }

            logger.debug("从队列【{}】尝试取出【{}】条消息:【{}】", name, messageSize, messageList);
        } catch (InterruptedException e) {
            logger.error("从队列【{}】获取消息失败:{}", name, e);
            throw new JMSException(e.getMessage());
        }

        return messageList;
    }

    public BlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    /**
     * 移除一条消息
     *
     * @param message
     */
    public void removeMessage(Message message) {
        consumeMessageQueue.remove(message);
    }

    public void putConsumeMessage(Message message) {
        try {
            consumeMessageQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void flushQueue() throws JMSException {
        MessageDao dao = new MessageDao();
        List<Message> messageList = dao.queryMessage(this.name,this.type);
        for(Message m:messageList){
            if (!messageQueue.contains(m)){
                try {
                    messageQueue.put(m);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取队列大小
     *
     * @return
     */
    public Integer getMessageQueueSize() {

        return this.messageQueue.size();
    }

    public BlockingQueue<Message> getConsumeMessageQueue() {
        return consumeMessageQueue;
    }
}
