package com.kuangchi.sdd.attendanceConsole.attend.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.forgetcheckModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.outworkModel;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface IAttendanceService {
	
	public Grid selectAllforgetchecks(forgetcheckModel forgetcheck,String page, String size);//模糊查询忘记打卡所有信息
	
	public Grid selectAllleavetimes(LeavetimeModel leavetime,String page, String size);//模糊查询请假所有信息
	
	public Grid selectAllOutwork(outworkModel outworks, String page, String rows);//模糊查询外出所有信息
	
	public List<LeavetimeModel> exportLeavetime(LeavetimeModel leavetime);//导出请假所有信息
	
	public List<outworkModel> exportOutwork(outworkModel outworks);//导出外出所有信息
	
	public List<forgetcheckModel> exportforgetchecks(forgetcheckModel forgetcheck);//导出忘记打卡所有信息
	
	public Grid selectLeavetimeById(LeavetimeModel leavetime,String page, String size);//通过员工编号查请假信息
	
	public Grid selectForgetcheckById(forgetcheckModel forgetcheck,String page, String size);//通过员工编号查忘打卡信息
	
	public Grid selectOutworkById(outworkModel outworks,String page, String size);//通过员工编号查外出信息
	
	/**
	 * 获取加班申请记录
	 * 用于 门户
	 * by gengji.yang
	 * @param map
	 * @return
	 */
	public Grid getOtRecords(Map map);
	
	/**
	 * 获取所有的加班申请记录
	 * 用于后台
	 * by gengji.yang
	 */
	public Grid getAllOts(Map map);
	
	/**
	 * 导出加班申请记录到Excel 
	 * by gengji.yang
	 * @param map
	 * @return
	 */
	public List<Map> exportAllOts(Map map);
	
	/**
	 * 获取我的销假申请记录
	 * by gengji.yang
	 */
	public Grid getMyCancelLeaveTime(Map map);
	
	/**
	 * 获取所有的销假申请记录
	 * by gengji.yang
	 */
	public Grid getAllCancelLeaveTime(Map map);
	
	/**
	 * 导出 销假申请记录
	 * by gengji.yang
	 */
	public List<Map> exportAllCancelLeavetimes(Map map);
	}
