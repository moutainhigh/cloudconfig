package com.wjh.roleservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableEurekaClient
@ComponentScan("com.wjh")
@EnableFeignClients("com.wjh")
@MapperScan("com.wjh.roleservice.mapper")
public class RoleserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoleserviceApplication.class,args);
    }
}
