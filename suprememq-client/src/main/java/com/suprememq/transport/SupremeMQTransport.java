/**
 *
 */
package com.suprememq.transport;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.concurrent.BlockingQueue;


/**
 * 客户端传送器的接口
 *
 * @author xautzh
 */
public abstract class SupremeMQTransport {
    public int acknowledgeType; // 消息应答类型

    public void setAcknowledgeType(int acknowledgeType) {
        this.acknowledgeType = acknowledgeType;
    }

    public int getAcknowledgeType() {
        return acknowledgeType;
    }

    /**
     * 开启传送通道
     *
     * @throws JMSException
     */
    public abstract void start() throws JMSException;

    /**
     * 关闭传送通道
     *
     * @throws JMSException
     */
    public abstract void close() throws JMSException;

    /**
     * 获取收到的消息的队列
     *
     * @return
     */
    public abstract BlockingQueue<Message> getReceiveMessageQueue();

    /**
     * 获取要发送消息的队列
     *
     * @return
     */
    public abstract BlockingQueue<Message> getSendMessageQueue();
}
