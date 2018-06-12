package topic;

import com.suprememq.clientStart.SupremeConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.util.Scanner;

public class TopicTest {
    Session session = SupremeConnectionFactory.connection("tcp://127.0.0.1:1314");
    Topic topic = session.createTopic("supreme");

    public TopicTest() throws JMSException {
    }

    @Test
    public void producer() {
        try {
            MessageProducer producer = session.createProducer(topic);
            System.out.println(producer.getDestination() instanceof Topic);
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("hello this is topic");
            producer.send(textMessage);
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String ling = scanner.nextLine();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consumer() {
        try {
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("消费者1收到消息：" + ((TextMessage) message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String ling = scanner.nextLine();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void consumer1() {
        try {
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println("消费者2收到消息：" + ((TextMessage) message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String ling = scanner.nextLine();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
