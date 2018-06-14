package com.xkd.mapper;

import com.xkd.model.Task;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskInfoMapper {

    void saveTask(Task task);

    List<Task> getTaskList(Map<String, Object> map);

    int getTaskListTotal(Map<String, Object> map);

    void saveTaskUser(@Param("taskId") String taskId,@Param("taskUser")List<String> taskUser);

    void deleteTaskUser(@Param("taskId") String taskId);

    void changeTask(Task task);

    void deleteTask(@Param("userId") String userId, @Param("taskUserList") List<String> taskUserList);

    Task getTaskById(@Param("taskId") String taskId);

    List<Map<String,String>> getTaskUserList(@Param("taskUserList") List<Task> taskUserList);
}
