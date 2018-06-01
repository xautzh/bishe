package com.sugarmq.vo;

public class ConsumerVo {
    private String queueName;
    private String consumerID;
    private String date;
    private int waitingMessageNumber;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getConsumerID() {
        return consumerID;
    }

    public void setConsumerID(String consumerID) {
        this.consumerID = consumerID;
    }

    public int getWaitingMessageNumber() {
        return waitingMessageNumber;
    }

    public void setWaitingMessageNumber(int waitingMessageNumber) {
        this.waitingMessageNumber = waitingMessageNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
