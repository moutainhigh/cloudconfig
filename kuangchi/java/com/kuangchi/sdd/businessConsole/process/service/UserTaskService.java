package com.kuangchi.sdd.businessConsole.process.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.process.model.UserTaskModel;

public interface UserTaskService {
	 Grid<UserTaskModel> getUserTask(UserTaskModel userTaskModel);
}
