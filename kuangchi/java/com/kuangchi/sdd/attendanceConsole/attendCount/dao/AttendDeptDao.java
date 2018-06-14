package com.kuangchi.sdd.attendanceConsole.attendCount.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;

public interface AttendDeptDao {
	public List<AttendDeptModel> getAllAttendDept(Map map);
	public Integer countAllAttendDept(Map map);
	
	public List<AttendDeptModel> exportAllDeptToExcel(Map map);
	
	/**
	 * 查询部门本月总工时
	 * @author yuman.gao
	 */
	public Double getDeptAllWorkTime(Map map);
}
