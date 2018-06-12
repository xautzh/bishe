package com.suprememq.dao.imp;

import com.suprememq.dao.MessageDaoBase;
import com.suprememq.entity.MessageInfo;
import com.suprememq.jdbc.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoBaseImp implements MessageDaoBase {
    @Override
    public void addMessage(MessageInfo messageInfo) {
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement ps = null;
//        String sql = "INSERT INTO t_message(message_id,message_destination,message_type," +
//                ",message_text,message_expiration,message_isSend,message_timstamp," +
//                "message_sendTime,message_updateTime) " +
//                "VALUES (?,?,?,?,?,?,?,?,?)";
        String sql = "INSERT INTO t_message(message_id,message_destination," +
                "message_type,message_jmsType,message_text,message_expiration,message_isSend" +
                ",message_timstamp,message_sendTime,message_updateTime)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, messageInfo.getMessageId());
            ps.setString(2, messageInfo.getMessageDestination());
            ps.setString(3, messageInfo.getMessageType());
            ps.setString(4, messageInfo.getJmsType());
            ps.setString(5, messageInfo.getMessageText());
            ps.setString(6, messageInfo.getMessageExpiration());
            ps.setInt(7, messageInfo.getMessageIssend());
            ps.setString(8, messageInfo.getMessageTimstamp());
            ps.setString(9, messageInfo.getMessageSendtime());
            ps.setString(10, messageInfo.getMessageUpdatetime());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<MessageInfo> queryMessage(String destination, String messageType, int isSend) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM t_message WHERE message_destination = ? and message_type = ? and message_isSend = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MessageInfo> messageInfoList = new ArrayList<>();
        try {
            ps = connection.prepareStatement(sql);
            ps.setString(1, destination);
            ps.setString(2, messageType);
            ps.setInt(3, isSend);
            rs = ps.executeQuery();
            while (rs.next()) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMessageId(rs.getString("message_id"));
                messageInfo.setMessageDestination(rs.getString("message_destination"));
                messageInfo.setMessageType(rs.getString("message_type"));
                messageInfo.setMessageText(rs.getString("message_text"));
                messageInfo.setMessageTimstamp(rs.getString("message_timstamp"));
                messageInfo.setMessageExpiration(rs.getString("message_expiration"));
                messageInfo.setJmsType(rs.getString("message_jmsType"));
                messageInfoList.add(messageInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return messageInfoList;
    }

    @Override
    public void removeMessage(String id) {
        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "update t_message set message_isSend = ? WHERE message_id = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, 1);
            ps.setString(2, id);
            ps.executeUpdate();
            System.out.println("删除接口执行完毕");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<MessageInfo> findAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<MessageInfo> messageInfoList = new ArrayList<>();
        String sql = "select * from t_message where message_isSend = ?";
        try {
            connection = ConnectionUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,0);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMessageId(resultSet.getString("message_id"));
                messageInfo.setMessageDestination(resultSet.getString("message_destination"));
                messageInfo.setMessageType(resultSet.getString("message_type"));
                messageInfo.setMessageText(resultSet.getString("message_text"));
                messageInfo.setMessageTimstamp(resultSet.getString("message_timstamp"));
                messageInfo.setMessageExpiration(resultSet.getString("message_expiration"));
                messageInfo.setJmsType(resultSet.getString("message_jmsType"));
                messageInfoList.add(messageInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageInfoList;
    }
}
