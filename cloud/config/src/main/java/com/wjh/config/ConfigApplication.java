package com.wjh.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigServer
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.wjh")
public class ConfigApplication {
    public static void main(String[] args) {

        SpringApplication.run(ConfigApplication.class,args);

    }
}
