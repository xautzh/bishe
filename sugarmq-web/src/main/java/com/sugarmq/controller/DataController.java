package com.sugarmq.controller;

import com.sugarmq.request.NameRequest;
import com.sugarmq.response.ConsumerResponse;
import com.sugarmq.response.DataResponse;
import com.sugarmq.response.MessageResponse;
import com.sugarmq.service.PageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/index")
@CrossOrigin
/**
 * web端接口
 */
public class DataController {
    @Autowired
    private PageDataService pageDataService;

    /**
     * 主页数据接口
     * @return
     */
    @GetMapping("pageData")
    public DataResponse dataResponse() {
        return pageDataService.dataResponse();
    }

    /**
     * 消费者详情页接口
     * @param consumerRequest queueName
     * @return
     */
    @PostMapping("consumerData")
    public ConsumerResponse consumerResponse(@RequestBody NameRequest consumerRequest) {
        return pageDataService.consumerResponse(consumerRequest.getQueueName());
    }

    /**
     * 消息详情页接口
     * @param messageRequest queueName
     * @return
     */
    @PostMapping("messageData")
    public MessageResponse messageResponse(@RequestBody NameRequest messageRequest) {
        return pageDataService.messageResponse(messageRequest.getQueueName());
    }

}
