package com.suprememq;

import com.suprememq.serverInit.ServerInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@Configuration
public class SupremeMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupremeMQApplication.class, args);
        ServerInit.getServerInit().start();
    }
}
