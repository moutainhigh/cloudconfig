package com.wjh.quartz.job;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @DisallowConcurrentExecution 表示禁止并发的执行相同的JobDetail
 */
@Component
@DisallowConcurrentExecution
public class MyJob  implements Job{
    Logger logger = LogManager.getLogger();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info(jobExecutionContext.getJobDetail().getKey() + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>job start  at " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            Thread.currentThread().sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info(jobExecutionContext.getJobDetail().getKey()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>job  end "+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

    }
}
