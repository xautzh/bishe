package com.suprememq.service.serviceImp;

import com.suprememq.constant.MessageState;
import com.suprememq.manager.SupremeMQServerManager;
import com.suprememq.queue.SupremeMQMessageContainer;
import com.suprememq.response.ConsumerResponse;
import com.suprememq.response.DataResponse;
import com.suprememq.response.MessageResponse;
import com.suprememq.serverInit.ServerInit;
import com.suprememq.service.PageDataService;
import com.suprememq.vo.ConsumerVo;
import com.suprememq.vo.MessageVo;
import com.suprememq.vo.QueueVo;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PageDataServiceImp implements PageDataService {
    ServerInit serverInit;
    SupremeMQServerManager SupremeMQServerManager;

    @Override
    public DataResponse dataResponse() {
        DataResponse dataResponse = new DataResponse();
        serverInit = ServerInit.getServerInit();
        SupremeMQServerManager = serverInit.getSupremeMQServerManager();
        ConcurrentHashMap<String, SupremeMQMessageContainer> messageMap = SupremeMQServerManager.getSupremeMQMessageManager().getMessageContainerMap();
        Set<String> messageSet = messageMap.keySet();
        Iterator iterator = messageSet.iterator();
        List<QueueVo> queueVoList = new ArrayList<>();
        while (iterator.hasNext()) {
            QueueVo queueVo = new QueueVo();
            String queueName = (String) iterator.next();
            queueVo.setQueueName(queueName);
            //总共生产了多少条消息
            queueVo.setProviderMessageNumber(messageMap.get(queueName).getMessageQueueSize());
            List<ConsumerVo> consumerVoList = consumerNumber(queueName);
            if (consumerVoList == null)
                queueVo.setConsumerNumber(0);
            else
                queueVo.setConsumerNumber(consumerVoList.size());
            queueVoList.add(queueVo);
        }
        dataResponse.setQueueResult(queueVoList);
        return dataResponse;
    }

    @Override
    public ConsumerResponse consumerResponse(String containerName) {
        List<ConsumerVo> consumerVoList = consumerNumber(containerName);
        String queueName = containerName;
        ConsumerResponse consumerResponse = new ConsumerResponse();
        List<ConsumerVo> resultList = new ArrayList<>();
        for (int i = 0; i < consumerVoList.size(); i++) {
            ConsumerVo consumerVo = new ConsumerVo();
            consumerVo.setQueueName(queueName);
            consumerVo.setConsumerID(consumerVoList.get(i).getConsumerID());
            consumerVo.setDate(consumerVoList.get(i).getDate());
            resultList.add(consumerVo);
        }
        consumerResponse.setConsumerResult(resultList);
        return consumerResponse;
    }

    @Override
    public MessageResponse messageResponse(String containerName) {
        MessageResponse messageResponse = new MessageResponse();
        List<MessageVo> messageVoList = new ArrayList<>();
        serverInit = ServerInit.getServerInit();
        SupremeMQServerManager = serverInit.getSupremeMQServerManager();
        List<Message> allMessageList = serverInit.
                getSupremeMQServerManager().
                getSupremeMQMessageManager().
                getAllMessageMap().
                get(containerName);
        System.out.println(allMessageList.size()+"所有消息");
        SupremeMQMessageContainer messageContainer = SupremeMQServerManager.
                getSupremeMQMessageManager().
                getMessageContainerMap().
                get(containerName);
        System.out.println("发送队列中的消息"+messageContainer.getMessageQueue().size());
        BlockingQueue<Message> sendQueue = messageContainer.getMessageQueue();
        for (Message message : allMessageList) {
            MessageVo messageVo = new MessageVo();
            messageVo.setQueueName(containerName);
            if (sendQueue.contains(message)){
                messageVo.setMessageState(MessageState.CONSUMING.getValue());
            }else {
                messageVo.setMessageState(MessageState.CONSUMED.getValue());
            }
            try {
                messageVo.setMessageID(message.getJMSMessageID());
                messageVo.setMessageText(((TextMessage) message).getText());
                messageVo.setSendTime(timeString(message.getJMSTimestamp()));
                messageVoList.add(messageVo);
            } catch (JMSException e) {
                //异常
                e.printStackTrace();
            }
        }
        messageResponse.setMessageVoList(messageVoList);
        return messageResponse;
    }

    private List<ConsumerVo> consumerNumber(String containerName) {
        serverInit = ServerInit.getServerInit();
        SupremeMQServerManager = serverInit.getSupremeMQServerManager();
        ConcurrentHashMap<String, List<ConsumerVo>> consumerMap =
                SupremeMQServerManager.getSupremeMQConsumerManager().getConsumerMap();
        return consumerMap.get(containerName);
    }

    private String timeString(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = simpleDateFormat.format(time);
        return timeString;
    }

}
