package com.suprememq.dao;

import com.suprememq.entity.MessageInfo;

import java.util.List;

public interface MessageDaoBase {
    /**
     * 添加
     *
     * @param messageInfo
     */
    void addMessage(MessageInfo messageInfo);

    /**
     * 查询列表
     *
     * @param destination
     * @param messageType
     * @param isSend
     * @return
     */
    List<MessageInfo> queryMessage(String destination, String messageType, int isSend);

    /**
     * 删除
     *
     * @param id
     */
    void removeMessage(String id);
    /**
     * 查询所有未消费消息
     */
    List<MessageInfo> findAll();
}
