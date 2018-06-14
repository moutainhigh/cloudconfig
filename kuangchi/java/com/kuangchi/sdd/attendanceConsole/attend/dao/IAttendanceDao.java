package com.kuangchi.sdd.attendanceConsole.attend.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.forgetcheckModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.outworkModel;
public interface IAttendanceDao {
	
	public List<forgetcheckModel> selectAllforgetchecks(forgetcheckModel forgetcheck,String page, String size);//模糊查询考勤所有信息
	
	public Integer getAllforgetcheckCount(forgetcheckModel forgetcheck);//查询总的行数
	
	public List<LeavetimeModel> selectAllleavetimes(LeavetimeModel leavetime,String page, String size);//模糊查询请假所有信息
	
	public Integer getAllleavetimeCount(LeavetimeModel leavetime);//查询总的行数
	
	public List<outworkModel> selectAllOutwork(outworkModel outworks,String page, String rows);//模糊查询外出所有信息
	
	public Integer getAllOutworksCount(outworkModel outworks);//查询总的行数
	
	public List<LeavetimeModel> exportLeavetime(LeavetimeModel leavetime);//导出请假所有信息
	
	public List<outworkModel> exportOutwork(outworkModel outworks);//导出外出所有信息
	
	public List<forgetcheckModel> exportforgetchecks(forgetcheckModel forgetcheck);//导出忘记打卡所有信息
	
	public List<LeavetimeModel> selectLeavetimeById(LeavetimeModel leavetime,String page, String size);//通过员工编号查请假信息
	public Integer selectLeavetimeByIdCount(LeavetimeModel leavetime);//查询请假总的行数
	
	public List<forgetcheckModel> selectForgetcheckById(forgetcheckModel forgetcheck,String page, String size);//通过员工编号查忘打卡信息
	public Integer selectForgetcheckByIdCount(forgetcheckModel forgetcheck);//查询忘打卡总的行数
	
	public List<outworkModel> selectOutworkById(outworkModel outworks,String page, String size);//通过员工编号查外出信息
	public Integer selectOutworkByIdCount(outworkModel outworks);//查询外出总的行数
	
	public List<Map> getOtRecords(Map map);
	public Integer countOtRecords(Map map);
	
	public List<Map> getAllOts(Map map);
	public Integer countAllOts(Map map);
	
	public List<Map> exportAllOts(Map map);
	
	public List<Map> getMyCancelLeaveTime(Map map);
	public Integer countMyCancelLeaveTime(Map map);
	
	public List<Map> getAllCancelLeavetimes(Map map);
	public Integer countAllCancelLeavetimes(Map map);
	
	public List<Map> exportAllCancelLeavetimes(Map map);
	
	
	}
