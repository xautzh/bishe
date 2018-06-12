/**
 *
 */
package com.sugarmq.transport;

import com.sugarmq.manager.SugarMQConsumerManager;
import com.sugarmq.manager.SugarMQMessageManager;

import javax.jms.JMSException;

/**
 * 类说明：
 * <p>
 * 类描述：
 *
 * @author xautzh
 * <p>
 * 2018年5月17日
 */
public interface SugarMQTransprotCenter {
    void start() throws JMSException;

    void close() throws JMSException;

    void remove(SugarMQServerTransport sugarMQServerTransport);

    void setSugarMQCustomerManager(SugarMQConsumerManager sugarMQCustomerManager);

    void setSugarMQMessageManager(SugarMQMessageManager sugarMQMessageManager);
}
