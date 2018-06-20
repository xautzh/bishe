package com.suprememq.dispatch;

import com.suprememq.manager.SupremeMQConsumerManager;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.queue.SupremeMQMessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 类说明：消费者消息分发器
 * <p>
 * 类描述：消费者消息分发器,每个消息队列配置一个
 *
 * @author xautzh
 * <p>
 * 2018年5月16日
 */
public class SupremeMQConsumerDispatcher {
    private SupremeMQConsumerManager supremeMQCustomerManager;
    private SupremeMQMessageContainer supremeMQMessageContainer;
    private Thread dispatcherThread;
    private AtomicBoolean isStart = new AtomicBoolean(false);

    private static Logger logger = LoggerFactory.getLogger(SupremeMQConsumerDispatcher.class);

    public SupremeMQConsumerDispatcher(SupremeMQConsumerManager supremeMQCustomerManager,
                                       SupremeMQMessageContainer SupremeMQMessageContainer) {
        if (supremeMQCustomerManager == null) {
            throw new IllegalArgumentException("supremeMQCustomerManager不能为空！");
        }

        if (SupremeMQMessageContainer == null) {
            throw new IllegalArgumentException("SupremeMQMessageContainer不能为空！");
        }

        this.supremeMQCustomerManager = supremeMQCustomerManager;
        this.supremeMQMessageContainer = SupremeMQMessageContainer;
    }

    public void start() {
        logger.info("SupremeMQConsumerDispatcher准备开始工作... ...");

        dispatcherThread = new Thread(() -> {
            try {
                //先刷新队列
                supremeMQMessageContainer.flushQueue();
            } catch (JMSException e) {
                logger.error("刷新队列错误");
                e.printStackTrace();

            }
            Message message;
            while (true) {
                try {
                    message = supremeMQMessageContainer.takeMessage();
                    logger.debug("从SupremeMQMessageContainer中拉取了一条消息:【{}】", message);
                    SupremeMQDestination destination = (SupremeMQDestination) message.getJMSDestination();
                    System.out.println("消息类型"+destination.getType());
                    if (destination.isQueue()) {
                        logger.debug("开始发送队列消息");
                        supremeMQCustomerManager.putQueueMessageToCustomerQueue(message);
                    } else {
                        logger.debug("开始发送主题消息");
                        supremeMQCustomerManager.putTopicMessageToConsumerQueue(message);
                    }
                    supremeMQMessageContainer.putConsumeMessage(message);
                } catch (JMSException e) {
                    logger.info("supremeMQCustomerDispatcher被中断！");
                    break;
                }
            }

        });

        dispatcherThread.start();
        isStart.set(true);
    }

    /**
     * 关闭
     */
    public void stop() {
        if (dispatcherThread != null) {
            dispatcherThread.interrupt();
            isStart.set(false);
        }
    }

    public boolean isStart() {
        return isStart.get();
    }
}
