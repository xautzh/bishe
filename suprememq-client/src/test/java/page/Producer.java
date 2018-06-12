package page;

import com.suprememq.core.SupremeMQConnectionFactory;

import javax.jms.*;

public class Producer {
    public static void main(String[] args) {
        try {
            SupremeMQConnectionFactory facotory = new SupremeMQConnectionFactory("tcp://127.0.0.1:1314");
            Connection connection = facotory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue1 = session.createQueue("zh");
            Queue queue2 = session.createQueue("supreme");
            Queue queue3 = session.createQueue("xaut");
            MessageProducer producer1 = session.createProducer(queue1);
            MessageProducer producer2 = session.createProducer(queue2);
            MessageProducer producer3 = session.createProducer(queue3);
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("Do you love me?");
            for (int i = 0; i < 10; i++) {
                producer1.send(textMessage);
                producer2.send(textMessage);
                producer3.send(textMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
