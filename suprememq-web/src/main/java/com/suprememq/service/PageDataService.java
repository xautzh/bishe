package com.suprememq.service;

import com.suprememq.response.ConsumerResponse;
import com.suprememq.response.DataResponse;
import com.suprememq.response.MessageResponse;

public interface PageDataService {
    /**
     * 主页数据
     *
     * @return
     */
    DataResponse dataResponse();

    /**
     * 消费者详情
     *
     * @return
     */
    ConsumerResponse consumerResponse(String containerName);

    /**
     * 消息详情
     *
     * @param containerName
     * @return
     */
    MessageResponse messageResponse(String containerName);
}
