package com.sugarmq.vo;

import lombok.Data;

@Data
public class MessageVo {
    //队列名称
    String queueName;
    //消息id
    String messageID;
    //消息文本
    String messageText;
    //消息状态
    String messageState;
    //发送时间
    String sendTime;
    //结束标志
    private Integer waitingMessageNumber;
}
