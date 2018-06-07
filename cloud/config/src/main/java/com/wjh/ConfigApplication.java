package com.wjh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import sun.security.krb5.Config;

@EnableConfigServer
@SpringBootApplication
public class ConfigApplication {
    public static void main(String[] args) {

        SpringApplication.run(ConfigApplication.class,args);

    }
}
