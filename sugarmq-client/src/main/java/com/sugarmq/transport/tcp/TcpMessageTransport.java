/**
 *
 */
package com.sugarmq.transport.tcp;

import com.sugarmq.transport.SugarMQTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 采用Socket来传输对象
 *
 * @author xautzh
 */
public class TcpMessageTransport extends SugarMQTransport {
    private InetAddress inetAddress;
    private int port;
    private Socket socket;

    // 收消息的队列
    private BlockingQueue<Message> receiveMessageQueue = new LinkedBlockingQueue<Message>();
    // 发消息的队列
    private BlockingQueue<Message> sendMessageQueue = new LinkedBlockingQueue<Message>();

    private Thread sendMessageThread;
    private Thread receiveMessageThread;

    private byte[] objectByte = new byte[com.sugarmq.message.Message.OBJECT_BYTE_SIZE];
    private Logger logger = LoggerFactory.getLogger(TcpMessageTransport.class);

    public TcpMessageTransport(InetAddress inetAddress, int port) {
        if (inetAddress == null) {
            throw new IllegalArgumentException("InetAddress不能为空！");
        }

        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    public void close() throws JMSException {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("关闭Socket出错：", e.getMessage());
                throw new JMSException("关闭Socket出错：", e.getMessage());
            }
        }
    }

    @Override
    public void start() throws JMSException {
        try {
            socket = new Socket(inetAddress, port);
            if (!socket.isConnected()) {
                logger.error("Socket未连接，TcpMessageTransport启动失败！");
                throw new JMSException("Socket未连接，TcpMessageTransport启动失败！");
            }

            // 消息接收线程
            if (!socket.isInputShutdown()) {
                receiveMessageThread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                logger.debug("TcpMessageTransport消息接收线程启动【{}】", this);
                                receiveMessage();
                            }
                        }
                );

                receiveMessageThread.start();
            } else {
                logger.debug("Socket未连接，TcpMessageTransport开启消息接收线程失败！");
            }

            // 消息发送线程
            if (!socket.isOutputShutdown()) {
                sendMessageThread = new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                logger.debug("TcpMessageTransport消息发送线程启动【{}】", this);
                                sendMessage();
                            }
                        }
                );

                sendMessageThread.start();
            } else {
                logger.debug("Socket未连接，TcpMessageTransport开启消息发送线程失败！");
            }


        } catch (IOException e) {
            logger.error("Socket对象启动失败", e);
            throw new JMSException("TcpMessageTransport Socket对象启动失败:" + e.getMessage());
        }

    }

    @Override
    public BlockingQueue<Message> getReceiveMessageQueue() {
        return receiveMessageQueue;
    }

    @Override
    public BlockingQueue<Message> getSendMessageQueue() {
        return sendMessageQueue;
    }

    /**
     * 从Socket中接收消息
     */
    private void receiveMessage() {
        try {
            ObjectInputStream objectInputStream = null;
            Message message = null;
            Object rcvMsgObj = null;
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed() && !socket.isInputShutdown()) {
                int byteNum = socket.getInputStream().read(objectByte);
                if (byteNum <= 0) {
                    continue;
                }

                objectInputStream = new ObjectInputStream(new ByteArrayInputStream(objectByte, 0, byteNum));
                rcvMsgObj = objectInputStream.readObject();

                if (!(rcvMsgObj instanceof Message)) {
                    logger.warn("客户端接收到一个非法消息：" + rcvMsgObj);
                    continue;
                }

                message = (Message) rcvMsgObj;
                logger.info("客户端接收到一条消息:{}", message);

                receiveMessageQueue.put(message);
            }

            logger.error("Socket状态异常，TcpMessageTransport接收消息线程结束！");
        } catch (Exception e) {
            logger.error("TcpMessageTransport消息接收线程错误", e);
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        Message message = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        while (true) {
            try {
                message = sendMessageQueue.take();
                logger.debug("即将发送消息：【{}】", message);
            } catch (InterruptedException e1) {
                logger.info("TcpMessageTransport消息发送线程被要求停止！");
                break;
            }

            if (!Thread.currentThread().isInterrupted() && !socket.isClosed() && !socket.isOutputShutdown()) {
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                    socket.getOutputStream().write(byteArrayOutputStream.toByteArray());
                    byteArrayOutputStream.flush();
                    logger.debug("消息发送完毕：【{}】", message);
                } catch (IOException e) {
                    logger.error("消息【{}】发送失败失败：{}", message, e);

                } finally {
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e) {
                        }
                    }

                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }

        }

        logger.info("TcpMessageTransport消息发送线程结束！");
    }
}
