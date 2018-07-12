package com.wjh.idservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.wjh")
public class IdserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdserviceApplication.class,args);
    }
}
