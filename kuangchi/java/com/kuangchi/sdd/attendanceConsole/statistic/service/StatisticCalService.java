package com.kuangchi.sdd.attendanceConsole.statistic.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceCheck;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDuty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDutyUser;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceException;
import com.kuangchi.sdd.attendanceConsole.statistic.model.DeptNoCheckSet;
import com.kuangchi.sdd.attendanceConsole.statistic.model.Holiday;
import com.kuangchi.sdd.attendanceConsole.statistic.model.LeaveTime;
import com.kuangchi.sdd.attendanceConsole.statistic.model.NoCheckSet;
import com.kuangchi.sdd.attendanceConsole.statistic.model.OutWork;

public interface StatisticCalService  {
	public	List<LeaveTime> getLeaveTimeListByStaff(Map<String, String> map);
	public	List<OutWork> getOutWorkListByStaff(Map<String, String> map);
	public	List<AttendanceCheck> getAttendanceCheckListByStaff(Map<String, String> map);
	public	List<Holiday > getHolidayList(Map<String, String> map);
	public	AttendanceDuty getAttendanceDutyByStaff(Map<String, String> map);
	public	void insertAttendanceException(AttendanceException attendanceException);
	public  List<AttendanceException> getAttendanceExceptionList(Map<String, String> map);
	public Integer getAttendanceCountByMonth(Map<String, String> map);
	public void insertAttendanceDataDate(AttendanceDataDate attendanceDataDate);
	public AttendanceDataDate getAttendanceDataDateByStaffAndEveryDate(Map<String, String> map);
	public Map<String,Object> getStatisticMap(Map<String, String> map); 
	public void updateAttendanceDataDate(AttendanceDataDate attendanceDataDate);
	public AttendanceDuty getAttendanceDutyById(Map<String, Integer> map);
	
	public List<AttendanceDutyUser> getAttendanceDutyUserListByStaffAndTime(Map<String, String> map);
	
	public void addStaffOt(Map map);
	
	// by gengji.yang
	public List<Map> getOtListByStaff(Map<String,String> map);
	
	public void delStaffLeaveRecord(String oldId);
	
	public void insertCancelRecord(Map map);
	
	public List<NoCheckSet>  getNoCheckSetByStaffNum(String staff_num,String checkDate);
	public List<DeptNoCheckSet>  getNoCheckSetByDeptNum(String dept_num,String checkDate,String heritedToSubDept);
	
	public List<String> getAttendanceDutyUserDeptsByStaffNum(String staff_num,String yearMonth);
	public void deleteAttendanceDataDateByStaffNumAndDate(String staff_num,String every_date);
	public List<Holiday> getExtralWorkDayList(Map<String, String> map);
}
