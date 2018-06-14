package com.kuangchi.sdd.businessConsole.process.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.process.dao.UserTaskDao;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceBean;
import com.kuangchi.sdd.businessConsole.process.model.UserTaskModel;

@Repository("userTaskDao")
public class UserTaskDaoImpl  extends BaseDaoImpl<UserTaskModel> implements UserTaskDao {

	@Override
	public List<UserTaskModel> getUserTask(UserTaskModel userTaskModel) {
		
		return getSqlMapClientTemplate().queryForList("getUserTask",userTaskModel);
	}

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.Activiti";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getUserTaskCount(UserTaskModel userTaskModel) {
		
		return (Integer) getSqlMapClientTemplate().queryForObject("getUserTaskCount",userTaskModel);
	}

}
