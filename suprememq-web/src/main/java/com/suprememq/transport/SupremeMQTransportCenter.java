/**
 *
 */
package com.suprememq.transport;

import com.suprememq.manager.SupremeMQConsumerManager;
import com.suprememq.manager.SupremeMQMessageManager;

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
public interface SupremeMQTransportCenter {
    void start() throws JMSException;

    void close() throws JMSException;

    void remove(SupremeMQServerTransport SupremeMQServerTransport);

    void setsupremeMQCustomerManager(SupremeMQConsumerManager supremeMQCustomerManager);

    void setSupremeMQMessageManager(SupremeMQMessageManager SupremeMQMessageManager);
}
