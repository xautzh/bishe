package com.sugarmq.service;

import com.sugarmq.response.ConsumerResponse;
import com.sugarmq.response.DataResponse;

public interface PageDataService {
    /**
     * 主页数据
     * @return
     */
    DataResponse dataResponse();

    /**
     * 消费者详情
     * @return
     */
    ConsumerResponse consumerResponse(String containerName);
}
