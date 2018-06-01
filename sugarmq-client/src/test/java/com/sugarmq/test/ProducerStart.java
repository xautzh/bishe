package com.sugarmq.test;

import com.sugarmq.clientStart.Client;

import javax.jms.JMSException;

public class ProducerStart {
    public static void main(String[] args) throws JMSException {
        Client client = Client.getClient();
        for (int i = 0; i < 10; i++) {
            String queueName = randomString((int) (Math.random() * 6+5));
            client.setQueue(queueName);
            client.producerStart();
        }

    }

    private static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
        System.out.println(str.length());
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * 52);
            stringBuffer.append(str.charAt(number));
        }
        String resultString = new String(stringBuffer);
        return resultString;
    }
}
