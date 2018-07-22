package com.wjh.log4j2kafka.listener;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 这里为什么要加一个@Component注解，主要原因是为了让cloud.log4j2.kafka.bootstrapSevers的属性注入进来，
 * 如果不加的话，属性注入不进来
 */
@Component
@WebListener
public class Log4j2Listener implements ServletContextListener {


    @Value("${cloud.log4j2.kafka.bootstrapSevers}")
    String bootstrapServers;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
                try {
                    System.out.println(".............Log4j2Listener.......start..........");





            System.setProperty("cloud.log4j2.kafka.bootstrapSevers",bootstrapServers);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
