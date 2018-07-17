package com.wjh.quartz;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@SpringBootApplication
@ComponentScan("com.wjh")

public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class,args);
    }

}
