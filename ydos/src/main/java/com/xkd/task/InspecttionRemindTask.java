package com.xkd.task;

import com.xkd.service.InspectionTaskService;
import com.xkd.service.ObjectNewsService;
import com.xkd.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 巡检提醒任务
 */
@Component
public class InspecttionRemindTask {

    @Autowired
    InspectionTaskService inspectionTaskService;
    @Autowired
    ObjectNewsService objectNewsService;
    static Semaphore lock = new Semaphore(1);


    @Scheduled(cron = "*/10 * * * * ?")//每2分钟触发
    public void remindInspectionTask() {
        boolean getLock = false;
        try {
            getLock = lock.tryAcquire(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getLock) {
            try {
                List<Map<String, Object>> userMapList = inspectionTaskService.selectToRemindInspectionTechinican();
                List<String> taskIdList = new ArrayList<>();
                List<Map<String, Object>> newsList = new ArrayList<>();
                for (int i = 0; i < userMapList.size(); i++) {
                    String id = (String) userMapList.get(i).get("id");
                    String userId = (String) userMapList.get(i).get("userId");
                    taskIdList.add(id);

                    Map<String, Object> newsMap = new HashMap<>();
                    newsMap.put("id", UUID.randomUUID().toString());
                    newsMap.put("objectId", id);
                    newsMap.put("appFlag", 3);
                    newsMap.put("userId", userId);
                    newsMap.put("title", "提醒消息");
                    newsMap.put("content", "您的巡检任务还有15分钟即将开始，点击查看");
                    newsMap.put("createDate", new Date());
                    newsMap.put("createdBy", "000000");
                    newsMap.put("status", 0);
                    newsMap.put("flag", 0);
                    newsMap.put("pushFlag", 0);
                    newsMap.put("newsType", 5);
                    newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/msg.png");

                    newsList.add(newsMap);
                }
                if (userMapList.size() > 0) {
                    objectNewsService.saveObjectNews(newsList);
                    inspectionTaskService.updateRemindStatusByIds(taskIdList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.release();
            }

        }
    }
}
