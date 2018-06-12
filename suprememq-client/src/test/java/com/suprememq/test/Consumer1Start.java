package com.suprememq.test;

import com.suprememq.clientStart.Client;

public class Consumer1Start {
    public static void main(String[] args) {
        Client client = Client.getClient();
        //消费者
        client.consumerStart();
    }
}
