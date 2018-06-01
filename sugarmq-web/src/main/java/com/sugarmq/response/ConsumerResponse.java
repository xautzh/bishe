package com.sugarmq.response;

import com.sugarmq.vo.ConsumerVo;

import java.util.List;

public class ConsumerResponse {
    List<ConsumerVo> consumerResult;

    public List<ConsumerVo> getConsumerResult() {
        return consumerResult;
    }

    public void setConsumerResult(List<ConsumerVo> consumerResult) {
        this.consumerResult = consumerResult;
    }
}
