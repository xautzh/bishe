package com.sugarmq.response;

import com.sugarmq.vo.QueueVo;

import java.util.List;

public class DataResponse {
    List<QueueVo> queueResult;

    public List<QueueVo> getQueueResult() {
        return queueResult;
    }

    public void setQueueResult(List<QueueVo> queueResult) {
        this.queueResult = queueResult;
    }
}
