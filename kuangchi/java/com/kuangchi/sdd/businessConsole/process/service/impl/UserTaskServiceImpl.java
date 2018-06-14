package com.kuangchi.sdd.businessConsole.process.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.process.dao.UserTaskDao;
import com.kuangchi.sdd.businessConsole.process.model.UserTaskModel;
import com.kuangchi.sdd.businessConsole.process.service.UserTaskService;
import com.kuangchi.sdd.businessConsole.test.model.TestBean;

@Service("userTaskService")
public class UserTaskServiceImpl  implements UserTaskService{

	@Resource(name="userTaskDao")
	UserTaskDao userTaskDao;
	@Override
	public Grid<UserTaskModel> getUserTask(UserTaskModel userTaskModel) {
		   Grid<UserTaskModel> grid = new Grid<UserTaskModel>();
	        List<UserTaskModel> resultList = userTaskDao
	              .getUserTask(userTaskModel);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(userTaskDao.getUserTaskCount(userTaskModel));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
	}

}
