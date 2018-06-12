package com.suprememq.response;

import com.suprememq.vo.QueueVo;
import lombok.Data;

import java.util.List;

@Data
public class DataResponse {
    List<QueueVo> queueResult;

}
