package com.kuangchi.sdd.baseConsole.countattendance.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.countattendance.dao.ICountAttendanceDao;
import com.kuangchi.sdd.baseConsole.countattendance.model.CountAttendance;
import com.kuangchi.sdd.baseConsole.countattendance.service.ICountAttendanceService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

@Transactional
@Service("countAttendanceServiceImpl")
public class CountAttendanceServiceImpl implements ICountAttendanceService {

	@Resource(name = "countAttendanceDaoImpl")
	private ICountAttendanceDao countAttendanceDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public Grid getAllCountAttendByParam(Param param,String page, String size) {
		Integer count=countAttendanceDao.getCountFromCountAttend(param);//去数据库中查符合条件的记录条数
		List<CountAttendance> card =this.countAttendanceDao.getAllCountAttendByParam(param,page,size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(card);
		return grid;
	}

	@Override
	public List<CountAttendance> reportData(Param param) {
		return countAttendanceDao.reportData(param);
	}

}
