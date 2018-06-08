package com.wjh.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import zipkin.server.EnableZipkinServer;

@EnableZipkinServer
@SpringBootApplication
@ComponentScan("com.wjh")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
