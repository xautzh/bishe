package com.suprememq.manager;

import com.suprememq.transport.SupremeMQServerTransportFactory;
import com.suprememq.transport.SupremeMQTransportCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Web端核心管理类
 *
 * @author xautzh
 */
@Component("supremeMQServerManager")
@PropertySource(value = "classpath:suprememq-config.properties", ignoreResourceNotFound = true)
public class SupremeMQServerManager {
    private Logger logger = LoggerFactory
            .getLogger(SupremeMQServerManager.class);

    private AtomicBoolean START_STATUS = new AtomicBoolean(false);

    private @Value("${server_uri}")
    String uri;

    @Autowired
    private SupremeMQMessageManager SupremeMQMessageManager;
    @Autowired
    private SupremeMQConsumerManager SupremeMQConsumerManager;
    @Autowired
    private ConnectionPoolManager connectionPoolManager;
    @Autowired
    private SupremeMQServerTransportFactory SupremeMQServerTransportFactory;

    private SupremeMQTransportCenter SupremeMQTransportCenter;

    /**
     * 启动supreme提供者
     *
     * @throws JMSException
     */
    public void start() throws JMSException {
        if (START_STATUS.get()) {
            logger.info("supreme已经成功启动！");
            return;
        }

        connectionPoolManager.init();
        SupremeMQTransportCenter = SupremeMQServerTransportFactory
                .createSupremeMQTransport(uri);
        SupremeMQTransportCenter.setsupremeMQCustomerManager(SupremeMQConsumerManager);
        SupremeMQTransportCenter.setSupremeMQMessageManager(SupremeMQMessageManager);

        new Thread(() -> {
            try {
                SupremeMQTransportCenter.start();
            } catch (JMSException e) {
                logger.error(e.getMessage());
            }
        }).start();

        START_STATUS.set(true);
        logger.info("supreme启动完毕:{}", uri);

    }

    public String getUri() {
        return uri;
    }

    public SupremeMQMessageManager getSupremeMQMessageManager() {
        return SupremeMQMessageManager;
    }

    public SupremeMQConsumerManager getSupremeMQConsumerManager() {

        return SupremeMQConsumerManager;
    }

    public SupremeMQTransportCenter getSupremeMQTransportCenter() {
        return SupremeMQTransportCenter;
    }
}
