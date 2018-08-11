package com.szjk.base.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IdGenerator {
    @Value("${worker.id}")
    private String workerId;
    @Value("${worker.datacenterId}")
    private String datacenterId;
    static SnowflakeIdWorker snowflakeIdWorker = null;


    @PostConstruct
    private void init() {
        if (snowflakeIdWorker == null) {
            int workerId_int = 0;
            int datacenterId_int = 0;
            if (workerId == null) {
                workerId_int = Integer.valueOf(workerId);
            }
            if (datacenterId == null) {
                datacenterId_int = Integer.valueOf(datacenterId);
            }
            snowflakeIdWorker = new SnowflakeIdWorker(workerId_int, datacenterId_int);
         }
    }

    public Long generateId(){
        long id = snowflakeIdWorker.nextId();
        return id;
    }
}
