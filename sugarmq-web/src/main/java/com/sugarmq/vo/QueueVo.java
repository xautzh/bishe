package com.sugarmq.vo;

public class QueueVo {
    //消息队列名称
    private String queueName;
    //生产了多少条消息 即剩余多少消息
    private Integer providerMessageNumber;

    //消费者数目
    private Integer consumerNumber;
    //结束标志
    private Integer waitingMessageNumber;

    public Integer getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(Integer consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Integer getProviderMessageNumber() {
        return providerMessageNumber;
    }

    public void setProviderMessageNumber(Integer providerMessageNumber) {
        this.providerMessageNumber = providerMessageNumber;
    }

    public Integer getWaitingMessageNumber() {
        return waitingMessageNumber;
    }

    public void setWaitingMessageNumber(Integer waitingMessageNumber) {
        this.waitingMessageNumber = waitingMessageNumber;
    }
}
