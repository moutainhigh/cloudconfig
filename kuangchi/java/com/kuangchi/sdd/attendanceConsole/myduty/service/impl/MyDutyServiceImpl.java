package com.kuangchi.sdd.attendanceConsole.myduty.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.myduty.dao.MyDutyDao;
import com.kuangchi.sdd.attendanceConsole.myduty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.myduty.model.DutyUser;
import com.kuangchi.sdd.attendanceConsole.myduty.service.MyDutyService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

@Service("myDutyServiceImpl")
public class MyDutyServiceImpl implements MyDutyService {
	
	@Resource(name = "myDutyDaoImpl")
	MyDutyDao myDutyDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public Grid<DutyUser> getDutyUserByParamPages(DutyUser dutyUser) {
		Grid<DutyUser> grid = new Grid<DutyUser>();
		List<DutyUser> resultList = myDutyDao.getDutyUserByParamPages(dutyUser);
		grid.setRows(resultList);
		if(null != resultList){
			grid.setTotal(myDutyDao.getDutyUserByParamCounts(dutyUser));
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	
	

	@Override
	public Duty getDutyByDutyId(String duty_id) {
		return myDutyDao.getDutyByDutyId(duty_id);
	}
	
}
