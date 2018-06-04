package com.sugarmq.vo;

import lombok.Data;

@Data
public class ConsumerVo {
    //队列名称
    private String queueName;
    //消费者id
    private String consumerID;
    //消费者创建日期
    private String date;
    //前端结束标志
    private int waitingMessageNumber;
}
