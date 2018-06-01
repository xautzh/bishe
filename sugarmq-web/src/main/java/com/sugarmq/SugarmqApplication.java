package com.sugarmq;

import com.sugarmq.serverInit.ServerInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.JMSException;

@ComponentScan
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@Configuration
public class SugarmqApplication {

    public static void main(String[] args) throws JMSException {
        SpringApplication.run(SugarmqApplication.class, args);

        ServerInit.getServerInit().start();
    }
}
