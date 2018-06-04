package com.sugarmq.response;

import com.sugarmq.vo.QueueVo;
import lombok.Data;

import java.util.List;

@Data
public class DataResponse {
    List<QueueVo> queueResult;

}
