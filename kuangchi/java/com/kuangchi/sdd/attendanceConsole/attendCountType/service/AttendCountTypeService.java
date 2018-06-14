package com.kuangchi.sdd.attendanceConsole.attendCountType.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCountType.model.AttendCountTypeModel;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.StaffAttendCount;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface AttendCountTypeService {
	public List<AttendCountTypeModel> getLeaveStatis(AttendCountTypeModel model);//请假统计
	public List<AttendCountTypeModel> getAttendExceptionTotalTime(AttendCountTypeModel model);//考勤异常汇总
	public List<AttendCountTypeModel> getAttendExceptionTotalNumber(AttendCountTypeModel model);//考勤分类统计
	public List<AttendCountTypeModel> getDeptExceptionTotalNumber(AttendCountTypeModel model);//考勤异常部门汇总
	
	public Grid getAttendLeaveCount(Map map); //查询农行请假统计
	public Grid getAttendOutWorkCount(Map map); //查询农行外出统计
	public List<Map> exportAttendOutWorkCount(Map map);//导出农行外出统计
	public List<Map> exportAttendLeaveCount(Map map);//导出农行请假统计
	
	
	// 查询员工出勤登记
	public Grid<StaffAttendCount> getStaffAttendCountList(Map map);

}
