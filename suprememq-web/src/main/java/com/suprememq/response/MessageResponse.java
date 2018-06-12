package com.suprememq.response;

import com.suprememq.vo.MessageVo;
import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {
    List<MessageVo> messageVoList;
}
