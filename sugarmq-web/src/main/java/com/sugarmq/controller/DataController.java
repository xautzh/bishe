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
public class DataController {
    @Autowired
    private PageDataService pageDataService;

    @GetMapping("pageData")
    public DataResponse dataResponse() {
        return pageDataService.dataResponse();
    }

    @PostMapping("consumerData")
    public ConsumerResponse consumerResponse(@RequestBody NameRequest consumerRequest) {
        return pageDataService.consumerResponse(consumerRequest.getQueueName());
    }

    @PostMapping("messageData")
    public MessageResponse messageResponse(@RequestBody NameRequest messageRequest) {
        return pageDataService.messageResponse(messageRequest.getQueueName());
    }

}
