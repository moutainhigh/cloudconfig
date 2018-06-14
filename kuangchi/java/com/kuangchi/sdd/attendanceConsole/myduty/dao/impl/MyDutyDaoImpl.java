package com.kuangchi.sdd.attendanceConsole.myduty.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;


import com.kuangchi.sdd.attendanceConsole.myduty.dao.MyDutyDao;
import com.kuangchi.sdd.attendanceConsole.myduty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.myduty.model.DutyUser;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
@Repository("myDutyDaoImpl")
public class MyDutyDaoImpl extends BaseDaoImpl<DutyUser> implements MyDutyDao {
	
	@Override
	public List<DutyUser> getDutyUserByParamPages(DutyUser dutyUser) {
		return getSqlMapClientTemplate().queryForList("getDuByParamPages", dutyUser);
	}

	
	@Override
	public int getDutyUserByParamCounts(DutyUser dutyUser) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDuByParamCounts", dutyUser);
	}
	

	@Override
	public Duty getDutyByDutyId(String duty_id) {
		return (Duty) getSqlMapClientTemplate().queryForObject("getDutyByDutyId", duty_id);
	}

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}


	



}
