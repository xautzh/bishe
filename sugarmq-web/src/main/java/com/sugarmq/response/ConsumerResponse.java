package com.sugarmq.response;

import com.sugarmq.vo.ConsumerVo;
import lombok.Data;

import java.util.List;

@Data
public class ConsumerResponse {
    List<ConsumerVo> consumerResult;
}
