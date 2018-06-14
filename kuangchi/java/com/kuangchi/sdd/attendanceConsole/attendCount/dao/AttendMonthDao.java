package com.kuangchi.sdd.attendanceConsole.attendCount.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;

public interface AttendMonthDao {
	public List<AttendMonthModel> getAllAttendMonth(Map<String, Object> map);
	public Integer countAllAttendMonth(Map<String, Object> map);
	public List<AttendMonthModel> exportAllMonthToExcel(Map<String, Object> map);
	
}
