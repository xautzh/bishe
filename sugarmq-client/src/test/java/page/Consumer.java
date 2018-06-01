package page;

import com.sugarmq.core.SugarMQConnectionFactory;

import javax.jms.*;

public class Consumer {
    public static void main(String[] args) {
        try {
            SugarMQConnectionFactory facotory = new SugarMQConnectionFactory("tcp://127.0.0.1:1314");
            Connection connection = facotory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("zh");
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    System.out.println("haha" + message);
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
