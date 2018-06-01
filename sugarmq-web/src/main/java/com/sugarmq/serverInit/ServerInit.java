package com.sugarmq.serverInit;

import com.sugarmq.manager.SugarMQServerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.JMSException;

public class ServerInit {
    private ApplicationContext applicationContext =
            new ClassPathXmlApplicationContext("applicationContext.xml");
    ;
    private SugarMQServerManager sugarMQServerManager =
            (SugarMQServerManager) applicationContext.getBean("sugarMQServerManager");
    private String url;

    private volatile static ServerInit serverInit;

    private ServerInit() {
    }

    public static ServerInit getServerInit() {
        if (serverInit == null) {
            synchronized (ServerInit.class) {
                if (serverInit == null) {
                    serverInit = new ServerInit();
                }
            }
        }
        return serverInit;
    }

    public void start() {
        try {
            sugarMQServerManager.start();
            System.out.println("当前服务器url为：" + getUrl());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        this.url = null;
        this.url = sugarMQServerManager.getUri();
        return url;
    }

    public SugarMQServerManager getSugarMQServerManager() {
        return sugarMQServerManager;
    }
}
