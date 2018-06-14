package com.kuangchi.sdd.attendanceConsole.statistic.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.statistic.dao.StatisticDao;
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
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticCalService;

@Transactional
@Service("statisticCalService")
public class StatisticCalServiceImpl implements StatisticCalService {
	@Resource(name="statisticDao")
	StatisticDao statisticDao;



	@Override
	public List<LeaveTime> getLeaveTimeListByStaff(Map<String, String> map) {
		return statisticDao.getLeaveTimeListByStaff(map);
	}


	@Override
	public List<OutWork> getOutWorkListByStaff(Map<String, String> map) {
		
		return statisticDao.getOutWorkListByStaff(map);
	}


	@Override
	public List<AttendanceCheck> getAttendanceCheckListByStaff(
			Map<String, String> map) {
		return statisticDao.getAttendanceCheckListByStaff(map);
	}


	@Override
	public List<Holiday> getHolidayList(Map<String, String> map) {
 		return statisticDao.getHolidayList(map);
	}


	@Override
	public AttendanceDuty getAttendanceDutyByStaff(Map<String, String> map) {
		 
		return statisticDao.getAttendanceDutyByStaff(map);
	}


	@Override
	public void insertAttendanceException(
			AttendanceException attendanceException) {
		 statisticDao.insertAttendanceException(attendanceException);
		
	}


	@Override
	public List<AttendanceException> getAttendanceExceptionList(
			Map<String, String> map) {
		 
		return statisticDao.getAttendanceExceptionList(map);
	}


	@Override
	public Integer getAttendanceCountByMonth(Map<String, String> map) {
		 
		return statisticDao.getAttendanceCountByMonth(map);
	}


	@Override
	public AttendanceDataDate getAttendanceDataDateByStaffAndEveryDate(
			Map<String, String> map) {
		 
		return statisticDao.getAttendanceDataDateByStaffAndEveryDate(map);
	}


	@Override
	public Map<String, Object> getStatisticMap(Map<String, String> map) {
		 
		return statisticDao.getStatisticMap(map);
	}


	@Override
	public AttendanceDuty getAttendanceDutyById(Map<String, Integer> map) {
		 
		return statisticDao.getAttendanceDutyById(map);
	}


	@Override
	public List<AttendanceDutyUser> getAttendanceDutyUserListByStaffAndTime(
			Map<String, String> map) {
		 
		return statisticDao.getAttendanceDutyUserListByStaffAndTime(map);
	}


	@Override
	public void addStaffOt(Map map) {
		 statisticDao.addStaffOt(map);
		
	}


	@Override
	public List<Map> getOtListByStaff(Map<String, String> map) {
		 
		return statisticDao.getOtListByStaff(map);
	}


	@Override
	public void delStaffLeaveRecord(String oldId) {
		 statisticDao.delStaffLeaveRecord(oldId);
		
	}


	@Override
	public void insertCancelRecord(Map map) {
		statisticDao.insertCancelRecord(map);
		
	}


	@Override
	public List<NoCheckSet> getNoCheckSetByStaffNum(String staff_num,
			String checkDate) {
	    
		return statisticDao.getNoCheckSetByStaffNum(staff_num, checkDate);
	}


	@Override
	public List<DeptNoCheckSet> getNoCheckSetByDeptNum(String dept_num,
			String checkDate, String heritedToSubDept) {
		 
		return statisticDao.getNoCheckSetByDeptNum(dept_num, checkDate, heritedToSubDept);
	}


	@Override
	public List<String> getAttendanceDutyUserDeptsByStaffNum(String staff_num,
			String yearMonth) {
		 
		return statisticDao.getAttendanceDutyUserDeptsByStaffNum(staff_num, yearMonth);
	}


	@Override
	public void deleteAttendanceDataDateByStaffNumAndDate(String staff_num,
			String every_date) {
		 statisticDao.deleteAttendanceDataDateByStaffNumAndDate(staff_num, every_date);
		
	}
	
	@Override
	public void insertAttendanceDataDate(AttendanceDataDate attendanceDataDate) {
		  statisticDao.insertAttendanceDataDate(attendanceDataDate);
		
	}
	

	@Override
	public void updateAttendanceDataDate(AttendanceDataDate attendanceDataDate) {
		statisticDao.updateAttendanceDataDate(attendanceDataDate);
		
	}
	@Override
	public List<Holiday> getExtralWorkDayList(Map<String, String> map) {
		 
		return statisticDao.getExtralWorkDayList(map);
	}
}
