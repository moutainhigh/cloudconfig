package com.xkd.task;

import com.xkd.service.CompanyService;
import com.xkd.service.SolrService;
import javafx.scene.control.SeparatorMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 创建人 巫建辉
 * 功能：主要用于更新数据库中公司列表索引，有些修改不能及时更新到索引库，如修改顾问人员姓名以后，该顾问对应的索引库中的名字也应该修改，
 * 但是如果修改名称的时候同步修改，由于涉及到大量数据，修改起来不现实，所以采用异步任务的方式来更新，一天更新一次
 */
@Component
public class RefreshCompanyIndexTask {

    static Semaphore lock = new Semaphore(1);

    @Autowired
    CompanyService companyService;
    @Autowired
    SolrService solrService;

        @Scheduled(cron = "0 0 23 * * ?")//每天23点
//        @Scheduled(cron = "0 */1 * * * ?")//每周一凌晨9点
    public void refresh() {
        boolean getLock = false;
            try {
                getLock=lock.tryAcquire(1,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getLock) {
            try {

                List<String> idList = companyService.selectAllCompanyIds();


                List<String>  newIdList=new ArrayList<>();
                for (int i = 0; i < idList.size(); i++) {
                    newIdList.add(idList.get(i));
                    if ((i+1)%50==0){
                        solrService.updateCompanyIndex(newIdList);
                        newIdList.clear();
                        System.out.println("...当前更新到第"+(i+1)+"条索引，总共"+idList.size()+"条...");
                        Thread.currentThread().sleep(1000);

                    }
                }


                //尾数
                solrService.updateCompanyIndex(newIdList);
                System.out.println("...更新索引完成....");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.release();
            }
        }
    }
}
