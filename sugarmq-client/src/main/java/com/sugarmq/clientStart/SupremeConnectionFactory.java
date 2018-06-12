package com.sugarmq.clientStart;

import com.sugarmq.core.SugarMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public class SupremeConnectionFactory {
    static SugarMQConnectionFactory facotory;
    public static Session connection(String url) {
        Session session = null;
        try {
             facotory = new SugarMQConnectionFactory(url);
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
