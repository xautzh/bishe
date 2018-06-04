package com.sugarmq.response;

import com.sugarmq.vo.MessageVo;
import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {
    List<MessageVo> messageVoList;
}
