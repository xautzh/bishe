package com.suprememq.vo;

import lombok.Data;

@Data
public class QueueVo {
    //消息队列名称
    private String queueName;
    //生产了多少条消息 即剩余多少消息
    private Integer providerMessageNumber;
    //消费者数目
    private Integer consumerNumber;
    //结束标志
    private Integer waitingMessageNumber;

}
