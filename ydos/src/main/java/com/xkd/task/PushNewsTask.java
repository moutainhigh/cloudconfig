package com.xkd.task;

import com.mysql.jdbc.TimeUtil;
import com.xkd.service.CompanyContactorService;
import com.xkd.service.ObjectNewsService;
import com.xkd.service.ObjectNewsTaskService;
import com.xkd.service.UserService;
import com.xkd.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by dell on 2018/3/7.
 */
@Component
public class PushNewsTask {

    public static  LinkedList<String> taskIdQueue=new LinkedList();

    @Autowired
    CompanyContactorService companyContactorService;
    @Autowired
    UserService userService;

    @Autowired
    ObjectNewsService objectNewsService;

    @Autowired
    ObjectNewsTaskService objectNewsTaskService;

    static Semaphore lock = new Semaphore(1);

    static Integer batchSize=100;


    @Scheduled(cron = "*/10 * * * * ?")//每2分钟触发
    private void toUserSchedule() {
        System.out.println("启动消息推送线程.........");
        boolean result = false;
        try {
            result = lock.tryAcquire(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (result == true) {
             System.out.println("消息推送线程获得锁.........");
            try {
                while (true) {
                    String taskId = taskIdQueue.poll();
                    if (taskId == null) {
                        break;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", taskId);
                    map.put("flag", 2);
                    try {
                        Map<String, Object> taskMap = objectNewsTaskService.select(taskId);
                        Integer objectType = (Integer) taskMap.get("objectType");
                        String objectId = (String) taskMap.get("objectId");
                        String pcCompanyId = (String) taskMap.get("pcCompanyId");
                        String content = (String) taskMap.get("content");
                        List<String> userIdList = new ArrayList<>();
                        List<String> serverAccountIdList=new ArrayList<>();
                        Integer appFlag = 2;
                        if (0 == objectType) {//如果推送对象类型为客户
                            String companyId = objectId;
                            userIdList = companyContactorService.selectAllUserIdByPcCompanyIdAndCompanyId(pcCompanyId, companyId);
                            appFlag = 4;
                        } else if (1 == objectType) { //如果推送对象类型为员工
                            List<Map<String,Object>> userMapList = userService.selectAllUserIdByPcCompanyId(pcCompanyId);
                            for (int i = 0; i <userMapList.size() ; i++) {
                                Map<String,Object> uM=userMapList.get(i);
                                if ("2".equals(uM.get("roleId"))){
                                       serverAccountIdList.add((String) uM.get("id")) ;
                                }else {
                                       userIdList.add((String) uM.get("id")) ;
                                       appFlag = 3;
                                }

                            }
                        }

                        ExecutorService executorService = Executors.newFixedThreadPool(4);
                        List<Future<Boolean>> futureList = new ArrayList<>();


                        /**
                         * 发送服务端帐号
                         */
                        futureList.add(executorService.submit(new NewsCallable(taskId,content,2,serverAccountIdList,"000000")));



                        List<String> taskUserIdList = new ArrayList<>();
                        for (int i = 1; i <= userIdList.size(); i++) {
                            taskUserIdList.add(userIdList.get(i - 1));
                            if (i % batchSize == 0) {
                                List<String> idList = new ArrayList<>();
                                idList.addAll(taskUserIdList);
                                Future<Boolean> future = executorService.submit(new NewsCallable(taskId, content, appFlag, idList, "000000"));
                                futureList.add(future);
                                taskUserIdList.clear();
                             }


                        }
                        //尾数
                        if (taskUserIdList.size() > 0) {
                            Future<Boolean> future = executorService.submit(new NewsCallable(taskId, content, appFlag, taskUserIdList, "000000"));
                            futureList.add(future);
                        }


                        executorService.shutdown();

                        for (int i = 0; i < futureList.size(); i++) {
                            try {
                                futureList.get(i).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    objectNewsTaskService.update(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.release();
                System.out.println("消息推送线程释放锁.........");

            }
        }
        System.out.println("消息推送线程结束.........");

    }


    class NewsCallable implements Callable<Boolean> {
        String taskId;
        String content;
        Integer appFlag;
        List<String> userIdList;
        String loginUserId;

        public NewsCallable(String taskId, String content, Integer appFlag, List<String> userIdList, String loginUserId) {
            this.taskId = taskId;
            this.content = content;
            this.appFlag = appFlag;
            this.userIdList = userIdList;
            this.loginUserId = loginUserId;
        }

        @Override
        public Boolean call() throws Exception {
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (int i = 0; i < userIdList.size(); i++) {
                Map<String, Object> newsMap = new HashMap<>();
                newsMap.put("id", UUID.randomUUID().toString());
                newsMap.put("objectId", taskId);
                newsMap.put("appFlag", appFlag);
                newsMap.put("userId", userIdList.get(i));
                newsMap.put("title", "推送消息");
                newsMap.put("content", content);
                newsMap.put("createDate", new Date());
                newsMap.put("createdBy", loginUserId);
                newsMap.put("status", 0);
                newsMap.put("flag", 0);
                newsMap.put("pushFlag", 0);
                newsMap.put("newsType", 3);
                newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/horn.png");

                mapList.add(newsMap);
            }
            objectNewsService.saveObjectNews(mapList);
            return true;
        }
    }

}
