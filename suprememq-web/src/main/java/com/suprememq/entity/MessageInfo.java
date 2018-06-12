package com.suprememq.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageInfo implements Serializable {
    private Integer id;

    private String messageId;

    private String messageDestination;

    private String messageType;

    private String messageText;

    private String messageExpiration;

    private Integer messageIssend;

    private String messageTimstamp;

    private String messageSendtime;

    private String messageUpdatetime;

    private String messageProperty;

    private String jmsType;

    private static final long serialVersionUID = 1L;
}