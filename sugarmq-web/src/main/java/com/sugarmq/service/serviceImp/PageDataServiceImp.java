package com.sugarmq.service.serviceImp;

import com.sugarmq.manager.SugarMQServerManager;
import com.sugarmq.queue.SugarMQMessageContainer;
import com.sugarmq.response.ConsumerResponse;
import com.sugarmq.response.DataResponse;
import com.sugarmq.serverInit.ServerInit;
import com.sugarmq.service.PageDataService;
import com.sugarmq.vo.ConsumerVo;
import com.sugarmq.vo.QueueVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PageDataServiceImp implements PageDataService {
    ServerInit serverInit;
    SugarMQServerManager sugarMQServerManager;

    @Override
    public DataResponse dataResponse() {
        DataResponse dataResponse = new DataResponse();
        serverInit = ServerInit.getServerInit();
        sugarMQServerManager = serverInit.getSugarMQServerManager();
        ConcurrentHashMap<String, SugarMQMessageContainer> messageMap = sugarMQServerManager.getSugarMQMessageManager().getMessageContainerMap();
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
        for (int i = 0;i<consumerVoList.size();i++){
            ConsumerVo consumerVo = new ConsumerVo();
            consumerVo.setQueueName(queueName);
            consumerVo.setConsumerID(consumerVoList.get(i).getConsumerID());
            consumerVo.setDate(consumerVoList.get(i).getDate());
            resultList.add(consumerVo);
        }
        consumerResponse.setConsumerResult(resultList);
        return consumerResponse;
    }

    private List<ConsumerVo> consumerNumber(String containerName) {
        serverInit = ServerInit.getServerInit();
        sugarMQServerManager = serverInit.getSugarMQServerManager();
        ConcurrentHashMap<String, List<ConsumerVo>> consumerMap =
                sugarMQServerManager.getSugarMQConsumerManager().getConsumerMap();
        return consumerMap.get(containerName);
    }

}
