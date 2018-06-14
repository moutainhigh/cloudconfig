package com.kuangchi.sdd.businessConsole.process.dao;

import java.util.List;

import com.kuangchi.sdd.businessConsole.process.model.UserTaskModel;

public interface UserTaskDao {
 List<UserTaskModel> getUserTask(UserTaskModel userTaskModel);
 Integer getUserTaskCount(UserTaskModel userTaskModel);
}
