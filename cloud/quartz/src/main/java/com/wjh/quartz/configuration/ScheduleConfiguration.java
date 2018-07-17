package com.wjh.quartz.configuration;

import com.wjh.quartz.jobfactory.JobFactory;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class ScheduleConfiguration {
//    @Autowired
//    private DataSource dataSource;
    @Autowired
    private JobFactory jobFactory;

//    @Bean(name = "SchedulerFactory")
//    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
//        //获取配置属性
//        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
//        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
//        //在quartz.properties中的属性被读取并注入后再初始化对象
//        propertiesFactoryBean.afterPropertiesSet();
//        //创建SchedulerFactoryBean
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setQuartzProperties(propertiesFactoryBean.getObject());
//        //使用数据源
//        factory.setDataSource(dataSource);
//        factory.setJobFactory(jobFactory);
//        return factory;
//    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try {
            schedulerFactoryBean.setQuartzProperties(quartzProperties());
            schedulerFactoryBean.setJobFactory(jobFactory);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }


        @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }



}