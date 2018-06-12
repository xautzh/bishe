package com.suprememq.response;

import com.suprememq.vo.ConsumerVo;
import lombok.Data;

import java.util.List;

@Data
public class ConsumerResponse {
    List<ConsumerVo> consumerResult;
}
