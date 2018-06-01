package com.sugarmq.test;

import com.sugarmq.clientStart.Client;

public class Producer1Start {
    public static void main(String[] args) {
        Client client = Client.getClient();
        //生产者
        client.singleProducerStart();
    }
}
