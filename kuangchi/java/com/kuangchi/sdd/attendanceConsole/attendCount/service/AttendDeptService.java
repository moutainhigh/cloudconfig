package com.kuangchi.sdd.attendanceConsole.attendCount.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface AttendDeptService {
	/**
	 *  支持多条件模糊查询，页面没有输入则传null
	 * @param deptNum
	 * @param deptName
	 * @param month
	 * @return
	 */
	public Grid<AttendDeptModel> getAllAttendDept(Map map);
	public List<AttendDeptModel> exportAllToExcel(Map map);
}
