package com.suprememq.constant;

public enum  MessageState {
    CONSUMING("待消费"),
    CONSUMED("已消费");

    String value;
    MessageState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
