package com.suprememq.test;

import com.suprememq.clientStart.Client;

public class ProducerTestMysqlStart {
    public static void main(String[] args) {
        Client client = Client.getClient();
        //生产者
        client.singleProducerStart();
    }
}
