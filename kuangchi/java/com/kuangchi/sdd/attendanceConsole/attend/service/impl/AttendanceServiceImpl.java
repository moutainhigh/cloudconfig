package com.kuangchi.sdd.attendanceConsole.attend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.attend.dao.IAttendanceDao;
import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.forgetcheckModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.outworkModel;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendanceService;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentNode;
@Transactional
@Service("attendanceServiceImpl")
public class AttendanceServiceImpl  implements IAttendanceService {
	private static final int CARD_ID_LENGTH = 6;
	
	@Resource(name = "attendanceDaoImpl")
	private IAttendanceDao attendanceDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	
	//查询忘记打卡
	@Override
	public Grid selectAllforgetchecks(forgetcheckModel forgetcheck,
			String page, String size) {
		Integer count=this.attendanceDao.getAllforgetcheckCount(forgetcheck);
		List<forgetcheckModel> forget=this.attendanceDao.selectAllforgetchecks(forgetcheck, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(forget);
		return grid;
		
	}

	//查询请假信息
	@Override
	public Grid selectAllleavetimes(LeavetimeModel leavetime, String page,
			String size) {
		Integer count=this.attendanceDao.getAllleavetimeCount(leavetime);
		List<LeavetimeModel> leave=this.attendanceDao.selectAllleavetimes(leavetime, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(leave);
		return grid;
	}
	//查询外出信息
	@Override
	public Grid selectAllOutwork(outworkModel outworks, String page, String rows) {
		Integer count=this.attendanceDao.getAllOutworksCount(outworks);
		List<outworkModel> outworklist=this.attendanceDao.selectAllOutwork(outworks,page,rows);
		Grid grid=new Grid();
		grid.setRows(outworklist);
		grid.setTotal(count);
		return grid;
	}
	
	//导出请假所有信息
	@Override
	public List<LeavetimeModel> exportLeavetime(LeavetimeModel leavetime) {
		List<LeavetimeModel> leave=this.attendanceDao.exportLeavetime(leavetime);
		return leave;
	}
	//导出外出所有信息
	@Override
	public List<outworkModel> exportOutwork(outworkModel outworks) {
		List<outworkModel> outwork=this.attendanceDao.exportOutwork(outworks);
		return outwork;
	}
	//导出忘打卡所有信息
	@Override
	public List<forgetcheckModel> exportforgetchecks(
			forgetcheckModel forgetcheck) {
		List<forgetcheckModel> forget=this.attendanceDao.exportforgetchecks(forgetcheck);
		return forget;
	}
	
	//根据ID查询请假信息
	@Override
	public Grid selectLeavetimeById(LeavetimeModel leavetime, String page, String rows) {
		Integer count=this.attendanceDao.selectLeavetimeByIdCount(leavetime);
		List<LeavetimeModel> leave=this.attendanceDao.selectLeavetimeById(leavetime, page, rows);
		Grid grid=new Grid();
		grid.setRows(leave);
		grid.setTotal(count);
		return grid;
	}
	//根据ID查询忘打卡信息
	@Override
	public Grid selectForgetcheckById(forgetcheckModel forgetcheck, String page, String rows) {
		Integer count=this.attendanceDao.selectForgetcheckByIdCount(forgetcheck);
		List<forgetcheckModel> forget=this.attendanceDao.selectForgetcheckById(forgetcheck,page,rows);
		Grid grid=new Grid();
		grid.setRows(forget);
		grid.setTotal(count);
		return grid;
	}
	//根据ID查询外出信息
	@Override
	public Grid selectOutworkById(outworkModel outworks, String page, String rows) {
		Integer count=this.attendanceDao.selectOutworkByIdCount(outworks);
		List<outworkModel> outwork=this.attendanceDao.selectOutworkById(outworks,page,rows);
		Grid grid=new Grid();
		grid.setRows(outwork);
		grid.setTotal(count);
		return grid;
	}

	@Override
	public Grid getOtRecords(Map map) {
		Grid grid=new Grid();
		grid.setRows(attendanceDao.getOtRecords(map));
		grid.setTotal(attendanceDao.countOtRecords(map));
		return grid;
	}

	@Override
	public Grid getAllOts(Map map) {
		Grid grid=new Grid();
		grid.setRows(attendanceDao.getAllOts(map));
		grid.setTotal(attendanceDao.countAllOts(map));
		return grid;
	}

	@Override
	public List<Map> exportAllOts(Map map) {
		List<Map> list=attendanceDao.exportAllOts(map);
		for(Map m:list){
			if("0".equals((String)m.get("otType"))){
				m.put("otType", "工作日");
			}else if("1".equals((String)m.get("otType"))){
				m.put("otType", "周末");
			}else{
				m.put("otType", "节假日");
			}
		}
		return list;
	}

	@Override
	public Grid getMyCancelLeaveTime(Map map) {
		Grid grid=new Grid();
		grid.setRows(attendanceDao.getMyCancelLeaveTime(map));
		grid.setTotal(attendanceDao.countMyCancelLeaveTime(map));
		return grid;
	}

	@Override
	public Grid getAllCancelLeaveTime(Map map) {
		Grid grid=new Grid();
		grid.setRows(attendanceDao.getAllCancelLeavetimes(map));
		grid.setTotal(attendanceDao.countAllCancelLeavetimes(map));
		return grid;
	}

	@Override
	public List<Map> exportAllCancelLeavetimes(Map map) {
		return attendanceDao.exportAllCancelLeavetimes(map);
	}
	
	
}
