package com.wjh.idservice.controller;


import com.wjh.idservice.utils.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Api(description = "用户相关接口")
@RestController
@RequestMapping("/id")
public class IdController  {
    @Value("${worker.id}")
    private String workerId;
    @Value("${worker.datacenterId}")
    private String datacenterId;

    Logger logger = LogManager.getLogger();
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
            logger.info("id生成器构建成功，workerIｄ:"+workerId+",datacenterId:"+datacenterId);
        }
    }


    @ApiOperation(value = "生成全局ID")
    @RequestMapping(value = "/generateId", method = RequestMethod.GET)
    public long generateId(HttpServletRequest httpServletRequest) {
        long id= snowflakeIdWorker.nextId();
        logger.info("id生成器"+workerId+"-"+datacenterId+"生成Id"+id);
        return id;
    }

}
