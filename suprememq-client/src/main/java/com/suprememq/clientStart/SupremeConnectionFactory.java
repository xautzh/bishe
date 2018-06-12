package com.suprememq.clientStart;

import com.suprememq.core.SupremeMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class SupremeConnectionFactory {
    static SupremeMQConnectionFactory facotory;
    public static Session connection(String url) {
        Session session = null;
        try {
             facotory = new SupremeMQConnectionFactory(url);
            Connection connection = facotory.createConnection();
            connection.start();
            session =
                    connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return session;
    }
    public static void close(){
        facotory.close();
    }
}
