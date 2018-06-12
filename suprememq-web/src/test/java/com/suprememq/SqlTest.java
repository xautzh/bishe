package com.suprememq;

import com.suprememq.constant.MessageContainerType;
import com.suprememq.constant.MessageType;
import com.suprememq.dao.MessageDao;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.message.bean.SupremeMQTextMessage;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.List;

public class SqlTest {
    @Test
    public void addMessage() throws JMSException {
        TextMessage textMessage = new SupremeMQTextMessage();
        Queue queue = new SupremeMQDestination("Supreme", MessageContainerType.QUEUE.getValue());
        textMessage.setJMSDestination(queue);
        textMessage.setJMSMessageID("43829158463782543435");
        textMessage.setText("还是乱码么");
        textMessage.setJMSExpiration(432432);
        textMessage.setJMSType(MessageType.PRODUCER_MESSAGE.getValue());
        textMessage.setJMSTimestamp(new Date().getTime());
        MessageDao dao = new MessageDao();
        dao.addMessage(textMessage);
    }

    @Test
    public void deleteMessage() throws JMSException {
        TextMessage textMessage = new SupremeMQTextMessage();
        textMessage.setJMSMessageID("2018061018062409241857919");
        new MessageDao().removeMessage(textMessage);
    }

    @Test
    public void findMessage() throws JMSException {
        String destination = "xautZH0";
        String type = "QUEUE";
        List<Message> messageInfoList = new MessageDao().queryMessage(destination, type);
        for (Message info : messageInfoList) {
            System.out.println(info.toString()+info.getJMSType());
        }
    }
    @Test
    public void findAll() throws JMSException {
        List<Message> messageInfoList = new MessageDao().queryMessage();
        for (Message m:messageInfoList){
            System.out.println(m.toString());
        }
    }
}
