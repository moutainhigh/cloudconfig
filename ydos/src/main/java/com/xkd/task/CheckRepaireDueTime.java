package com.xkd.task;

import com.xkd.mapper.DeviceMapper;
import com.xkd.mapper.YDrepaireMapper;
import com.xkd.service.DeviceService;
import com.xkd.service.ObjectNewsService;
import com.xkd.service.YDrepaireService;
import com.xkd.utils.PropertiesUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CheckRepaireDueTime {

    @Autowired
    private YDrepaireService  service;
    @Autowired
    private YDrepaireMapper repaireMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private ObjectNewsService objectNewsService;

    //@Scheduled(cron = "0 0-59/1 * * * ?")//每1分钟触发一次
    @Scheduled(cron = "0 0 9 * * ?")//每天上午11点触发一次,只发送昨天0-23:59的逾期工单
    private void toUserSchedule() {
        System.out.println("66666666666666666666666======has into repaire out of date task=====666666666666666666666666666");
        try {
            List<Map<String,Object>> list = service.selectOutOfDateRepaire();
            for(Map<String,Object> map : list){

                String repaireId = map.get("id").toString();
                String repaireNo = map.get("repaireNo").toString();
                List<Map<String,Object>> technicians = repaireMapper.selectTechniciansByRepaireId(repaireId);
                List<String> deviceIds = repaireMapper.selectDeviceIdListByRepaireId(repaireId);

                if(technicians != null && technicians.size() > 0){
                    List<Map<String,Object>> returnList = new ArrayList<>();
                    //将消息保存给该维修单的所有技师
                    for (Map<String, Object> technician : technicians) {
                        Map<String, Object> p = new HashedMap();
                        p.put("id", UUID.randomUUID().toString());
                        p.put("appFlag", 3);
                        p.put("objectId", repaireId);
                        p.put("userId", (String) technician.get("id"));
                        p.put("title", "维修工单已逾期");
                        p.put("content", "维修工单"+repaireNo+"已逾期，请及时处理");
                        p.put("createDate", new Date());
                        p.put("flag", 0);
                        p.put("status", 0);
                        p.put("newsType", 0);
                        p.put("pushFlag", "0");

                        returnList.add(p);
                    }

                    //多个设备可能查出一个用户，去重
                    Set<String> sets = new HashSet<>();
                    for(String deviceId : deviceIds){
                        List<String> uids = deviceMapper.selectRoleUserByDeviceId(deviceId, "2");
                        sets.addAll(uids);
                    }

                    for (String uid : sets) {
                        /*
                        向服务端发送消息
                         */
                        Map<String, Object> m = new HashedMap();
                        m.put("id", UUID.randomUUID().toString());
                        m.put("userId", uid);
                        m.put("objectId", repaireId);
                        m.put("appFlag", 2);
                        m.put("title", "维修工单已逾期");
                        m.put("content", "维修工单"+repaireNo+"已逾期，请及时处理");
                        m.put("createDate",new Date());
                        m.put("flag", 0);
                        m.put("status", 0);
                        m.put("newsType", 0);
                        m.put("pushFlag", "0");
                        m.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/repaire.png");
                        returnList.add(m);
                    }

                    //逾期消息，已经发送了就不要发送了
                    List<Map<String, Object>> newList = new ArrayList<>();
                    List<String> userIds = repaireMapper.getUserIdsHasOutOfDateNews(repaireId);
                    if(userIds != null && userIds.size() > 0) {
                        for (Map<String, Object> m : returnList) {
                            String userId = (String) m.get("userId");
                            if (!userIds.contains(userId)) {
                                newList.add(m);
                            }
                        }

                        if(newList != null && newList.size() > 0){
                            objectNewsService.saveObjectNews(newList);
                        }
                    }else{
                        objectNewsService.saveObjectNews(returnList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
