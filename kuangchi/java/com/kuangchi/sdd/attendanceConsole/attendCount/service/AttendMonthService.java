package com.kuangchi.sdd.attendanceConsole.attendCount.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface AttendMonthService {
	
	/**
	 *  支持多条件模糊查询，可传null
	 * @param staffNum
	 * @param staffName
	 * @param month,传1—12月这样的参数
	 * @return
	 */
	public Grid<AttendMonthModel> getAllAttendMonth(Map<String, Object> map);
	public List<AttendMonthModel> exportAllToExcel(Map<String, Object> map);
}
