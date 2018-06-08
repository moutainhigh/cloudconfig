package com.wjh.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigServer
@SpringBootApplication
@ComponentScan("com.wjh")
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class,args);

    }
}
