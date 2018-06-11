package com.wjh.eureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
@ComponentScan("com.wjh")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}