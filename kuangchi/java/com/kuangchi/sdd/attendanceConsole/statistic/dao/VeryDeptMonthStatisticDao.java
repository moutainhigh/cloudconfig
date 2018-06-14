package com.kuangchi.sdd.attendanceConsole.statistic.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;

public interface VeryDeptMonthStatisticDao {
	
	
	public AttendDeptModel calculateDeptAttendInfo(String deptNum,String month);
	public void insertAttendDept(AttendDeptModel attendDeptModel);
	public void delOldAttendDeptInfo(String deptNum,String month);
	public List<String> getAllDept();
}
