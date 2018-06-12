package com.suprememq.dao;

import com.suprememq.constant.MessageContainerType;
import com.suprememq.dao.imp.MessageDaoBaseImp;
import com.suprememq.entity.MessageInfo;
import com.suprememq.message.SupremeMQDestination;
import com.suprememq.message.bean.SupremeMQTextMessage;

import javax.jms.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDao {
    MessageDaoBase messageDaoBase;

    /**
     * 插入消息
     *
     * @param message
     */
    public void addMessage(Message message) throws JMSException {
        messageDaoBase = new MessageDaoBaseImp();
        if (!exit(message)){
            MessageInfo messageInfo = buildMessageInfo(message);
            messageInfo.setMessageIssend(0);
            messageInfo.setMessageSendtime(timeString(new Date().getTime()));
            messageDaoBase.addMessage(messageInfo);
        }
    }


    public void removeMessage(Message message) {
        try {
            messageDaoBase = new MessageDaoBaseImp();
            System.out.println("需要删除的消息的ID" + message.getJMSMessageID());
            messageDaoBase.removeMessage(message.getJMSMessageID());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public List<Message> queryMessage(String destination, String type) throws JMSException {
        messageDaoBase = new MessageDaoBaseImp();
        List<Message> messageList = new ArrayList<>();
        List<MessageInfo> messageInfoList = messageDaoBase.queryMessage(destination, type, 0);
        for (MessageInfo messageInfo : messageInfoList) {
            SupremeMQTextMessage message = new SupremeMQTextMessage();
            Destination resultDestination = buildDestination(destination, type);
            message.setJMSDestination(resultDestination);
            message.setJMSExpiration(Long.parseLong(messageInfo.getMessageExpiration()));
            message.setJMSMessageID(messageInfo.getMessageId());
            message.setJMSTimestamp(timeLong(messageInfo.getMessageTimstamp()));
            message.setJMSType(messageInfo.getJmsType());
            message.setText(messageInfo.getMessageText());
            message.setJMSDeliveryMode(2);
            messageList.add(message);
        }
        return messageList;
    }

    public List<Message> queryMessage() throws JMSException {
        messageDaoBase = new MessageDaoBaseImp();
        List<Message> messageList = new ArrayList<>();
        List<MessageInfo> messageInfoList = messageDaoBase.findAll();
        for (MessageInfo messageInfo : messageInfoList) {
            SupremeMQTextMessage message = new SupremeMQTextMessage();
            System.out.println(messageInfo.getMessageDestination()+": "+messageInfo.getMessageType());
            Destination resultDestination = new SupremeMQDestination(messageInfo.getMessageDestination(), messageInfo.getMessageType());
            message.setJMSDestination(resultDestination);
            message.setJMSExpiration(Long.parseLong(messageInfo.getMessageExpiration()));
            message.setJMSMessageID(messageInfo.getMessageId());
            message.setJMSTimestamp(timeLong(messageInfo.getMessageTimstamp()));
            message.setJMSType(messageInfo.getJmsType());
            message.setText(messageInfo.getMessageText());
            message.setJMSDeliveryMode(2);
            messageList.add(message);
        }
        return messageList;
    }

    /**
     * 消息转换
     */
    private MessageInfo buildMessageInfo(Message message) {
        MessageInfo messageInfo = new MessageInfo();
        try {
            messageInfo.setMessageId(message.getJMSMessageID());
            messageInfo.setMessageText(((TextMessage) message).getText());
            SupremeMQDestination SupremeMQDestination = (SupremeMQDestination) message.getJMSDestination();
            messageInfo.setMessageDestination(SupremeMQDestination.getName());
            messageInfo.setMessageType(SupremeMQDestination.getType());
            messageInfo.setMessageTimstamp(timeString(message.getJMSTimestamp()));
            messageInfo.setMessageExpiration(String.valueOf(message.getJMSExpiration()));
            messageInfo.setJmsType(message.getJMSType());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return messageInfo;
    }

    private String timeString(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = simpleDateFormat.format(time);
        return timeString;
    }

    private Destination buildDestination(String destination, String type) {
        if (type.equals(MessageContainerType.QUEUE.getValue())) {
            Queue queue = new SupremeMQDestination(destination, type);
            return queue;
        } else {
            Topic topic = new SupremeMQDestination(destination, type);
            return topic;
        }
    }

    private long timeLong(String timeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeString);
        } catch (ParseException e) {
            System.out.println("无法处理的类型");
            e.printStackTrace();
        }
        return date.getTime();
    }
    private boolean exit(Message message) throws JMSException {
        boolean result = false;
        messageDaoBase = new MessageDaoBaseImp();
        List<MessageInfo> messageInfoList = messageDaoBase.findAll();
        for (MessageInfo m:messageInfoList){
            if (m.getMessageId().equals(message.getJMSMessageID())){
                result = true;
            }
        }
        return result;
    }
}
