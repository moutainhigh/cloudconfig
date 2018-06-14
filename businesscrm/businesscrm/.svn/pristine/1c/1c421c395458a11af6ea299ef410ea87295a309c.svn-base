package com.xkd.service;

import com.xkd.mapper.ScheduleUserMapper;
import com.xkd.mapper.TaskInfoMapper;
import com.xkd.model.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskInfoService {
    @Autowired
    TaskInfoMapper taskMapper;

    @Autowired
    PagerFileService pagerFileService;

    @Autowired
    UserDynamicService userDynamicService;

    @Autowired
    private ScheduleUserMapper scheduleColleagueMapper;

    public void saveTask(Task task) {
        task.setId(UUID.randomUUID().toString());
        List<String> taskUser = new ArrayList(Arrays.asList((task.getTaskUserIds().trim()).split(",")));
        taskMapper.saveTaskUser(task.getId(), taskUser);
        taskMapper.saveTask(task);
        pagerFileService.LodingPagerFile(task.getTaskName(),"3",task.getId(),task.getCreatedBy(),"任务",task.getDepartmentId());
        userDynamicService.addUserDynamic(task.getCreatedBy(), task.getId(), null, "添加", "添加了 任务\"" + task.getTaskName() + "\"", 0, null, null, null);
        if(StringUtils.isNotBlank(task.getTaskUserIds().trim())){
            Map<String,Object> historyUserInfo = new HashMap<>();
            historyUserInfo.put("myUserId",task.getCreatedBy());
            int time = 0;
            for (String userId:taskUser) {
                historyUserInfo.put("userId", userId);
                historyUserInfo.put("time",time++);
                scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
            }
        }
        List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(task.getCreatedBy());
        if(historyUser.size() > 20){
            for (int i = 20; i < historyUser.size(); i++) {
                scheduleColleagueMapper.deleteHistoryUser(task.getCreatedBy(),historyUser.get(i).get("userId"));
            }
        }
    }

    public void changeTask(Task task) {
        taskMapper.deleteTaskUser(task.getId());
        List<String> taskUser = new ArrayList(Arrays.asList((task.getTaskUserIds().trim()).split(",")));
        taskMapper.saveTaskUser(task.getId(), taskUser);
        taskMapper.changeTask(task);
        pagerFileService.updatePagerFileName(task.getId(),task.getUpdatedBy(),task.getTaskName());
        userDynamicService.addUserDynamic(task.getUpdatedBy(), task.getId(), null, "更新", "修改了 任务\"" + task.getTaskName() + "\"", 0, null, null, null);
        if(StringUtils.isNotBlank(task.getTaskUserIds().trim())){
            Map<String,Object> historyUserInfo = new HashMap<>();
            historyUserInfo.put("myUserId",task.getUpdatedBy());
            int time = 0;
            for (String userId:taskUser) {
                historyUserInfo.put("userId", userId);
                historyUserInfo.put("time",time++);
                scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
            }
        }
        List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(task.getCreatedBy());
        if(historyUser.size() > 20){
            for (int i = 20; i < historyUser.size(); i++) {
                scheduleColleagueMapper.deleteHistoryUser(task.getCreatedBy(),historyUser.get(i).get("userId"));
            }
        }
    }

    public List<Task> getTaskList(Map<String, Object> map) {
        List<Task> taskList = taskMapper.getTaskList(map);
        if(taskList!= null && taskList.size()>0 ){
            List<Map<String,String>> taskUserAllList = taskMapper.getTaskUserList(taskList);
            for (Task task:taskList) {
                for (Map<String,String> user:taskUserAllList) {
                    if(task.getId().equals(user.get("taskId"))){
                        task.getTaskUserList().add(user);
                        task.setTaskUserNames(StringUtils.isNotBlank(task.getTaskUserNames())? task.getTaskUserNames()+","+user.get("uname"):user.get("uname"));
                        task.setTaskUserIds(StringUtils.isNotBlank(task.getTaskUserIds())? task.getTaskUserIds()+","+user.get("id"):user.get("id"));
                    }
                }
            }
        }


        return taskList;
    }

    public int getTaskListTotal(Map<String, Object> map) {
        return taskMapper.getTaskListTotal(map);
    }


    public void deleteTask(String taskIds, String userId) {
        List<String> taskUserList = new ArrayList(Arrays.asList((taskIds.trim()).split(",")));
        taskMapper.deleteTask(userId, taskUserList);
    }

    public Task getTaskById(String taskId) {
        List<Task> taskList = new ArrayList<>();
        Task task = taskMapper.getTaskById(taskId);
        taskList.add(task);
        task.setTaskUserList(taskMapper.getTaskUserList(taskList));
        return  task;
    }
}
