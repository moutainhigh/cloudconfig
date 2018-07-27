package com.wjh.idservice.controller;


import com.wjh.common.model.ResponseModel;
import com.wjh.idservice.utils.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(description = "用户相关接口")
@RestController
@RequestMapping("/id")
public class IdController {
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
            logger.info("id生成器构建成功，workerIｄ:" + workerId + ",datacenterId:" + datacenterId);
        }
    }


    @ApiOperation(value = "生成全局ID")
    @RequestMapping(value = "/batchGenerateId", method = RequestMethod.GET)
    public ResponseModel<List<Long>> batchGenerateId(HttpServletRequest httpServletRequest) {

        int batchSize = 10000;
        List<Long> idList = new ArrayList<>(batchSize);
        for (int i = 0; i < batchSize; i++) {
            long id = snowflakeIdWorker.nextId();
            idList.add(id);
        }
        logger.debug("id生成器" + workerId + "-" + datacenterId + "生成{}个Id", batchSize);
        ResponseModel<List<Long>> responseModel = new ResponseModel();
        responseModel.setResModel(idList);
        return responseModel;
    }

}
