package com.wjh.idservice.thread;

import com.wjh.common.model.RedisKeyConstant;
import com.wjh.idservice.utils.SnowflakeIdWorker;
import com.wjh.utils.redis.RedisCacheUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class IdThread extends Thread {
    Logger logger = LogManager.getLogger();
    RedisCacheUtil redisCacheUtil;
    SnowflakeIdWorker snowflakeIdWorker;

    public IdThread(RedisCacheUtil redisCacheUtil, SnowflakeIdWorker snowflakeIdWorker) {
        this.redisCacheUtil = redisCacheUtil;
        this.snowflakeIdWorker = snowflakeIdWorker;
    }

    @Override
    public void run() {
        logger.info("......启动Id缓存生成线程.........."+new Date());
        Long[] ids=new Long[10000];
        for (int i = 0; i < ids.length; i++) {
            long  id=snowflakeIdWorker.nextId();
            ids[i]=id;
        }
        redisCacheUtil.leftPush(RedisKeyConstant.ID_QUEUE,ids);
        logger.info("......结束Id缓存生成线程.........."+new Date());

    }
}
