package com.kuangchi.sdd.attendanceConsole.statistic.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDuty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.DeptNoCheckSet;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;

public interface StatisticService {
	//员工考勤每天统计
		public void personalAttendanceDayStatistic(Date statisticDatePoint,Employee employee,AttendanceDuty attendanceDuty);
		
		//员工考勤每天统计
				public void personalAttendanceDayStatistic();
		
		//员工考勤月统计
			public void personalAttendanceMonthStatistic();
		
			//部门考勤月统计
			public void departmentAttendanceMonthStatistic();
			
			//计算请假时间
			public  Double getLeaveTimeTotal(Date startTime, Date endTime,String staffNum);
			//计算请假时间
			public   Map<String, Object> getLeaveTimeTotal_new(Date startTime, Date endTime,String staffNum);
			
			public void reStatisticByStaff(Date startTime, Date endTime,
					String staffNum);
			//添加员工 加班记录 by gengji.yang
			public void addStaffOt(Map map);
			
			//删除员工请假申请记录 by gengji.yang
			public void delStaffLeaveRecord(String oldId);
	    	
			//添加员工销假申请记录 by gengji.yang
	    	public void insertCancelRecord(Map map);
	    	
	    	//往上获取某一员工第一个免打卡设置的部门编号
	    	public List<DeptNoCheckSet> getFirstNoCheckSetParentDepartmentsByBmDm(String bmDm,String checkDate);
	    	


}
