package com.kuangchi.sdd.attendanceConsole.attendCountType.dao;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCountType.model.AttendCountTypeModel;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.StaffAttendCount;

public interface AttendCountTypeDao {
	public List<AttendCountTypeModel> getLeaveStatis(AttendCountTypeModel model); //请假统计
	//public List<AttendCountTypeModel> getAbsenteeism(AttendCountTypeModel model); //查找旷工人员 //员工考情统计不需要查询旷工人员了，该方法启用
	
	public List<AttendCountTypeModel> getAttendExceptionTotalTime(AttendCountTypeModel model); //考勤汇总
	public List<AttendCountTypeModel> getAttendExceptionTotalNumber(AttendCountTypeModel model); //迟到、早退、旷工情况统计
	public List<AttendCountTypeModel> getExceptionTotalTimeByStaffNum(String staff_num,String exception_type,String begin_time,String end_time); //根据员工编号和异常类型查询日期总数

	public List<Map> getAttendLeaveCountList(Map map);
	public Integer getAttendLeaveCount(Map map);
	
	public List<Map> getAttendOutWorkCountList(Map map);
	public Integer getAttendOutWorkCount(Map map);
	public List<Map> exportAttendOutWorkCount(Map map);
	public List<Map> exportAttendLeaveCount(Map map);
	
	
	// 查询部门异常汇总
	public List<AttendCountTypeModel> getExceptionByDept(Map map);
	
	// 查询员工出勤登记
	public List<StaffAttendCount> getStaffAttendCountList(Map map);
	// 查询员工出勤登记总数
	public Integer getStaffAttendCount(Map map);
	
	// 根据员工和时间查询当前上班时间
	public Map getFristCheckByTime(Map map);
	
	// 根据员工和时间查询当前下班时间
	public Map getLastCheckByTime(Map map);
	
	// 根据单号查询请假时间
	public List<Double> getLeaveTimeById(Map map);
}
