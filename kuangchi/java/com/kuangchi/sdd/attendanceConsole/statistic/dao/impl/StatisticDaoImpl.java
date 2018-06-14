package com.kuangchi.sdd.attendanceConsole.statistic.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.statistic.dao.StatisticDao;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceCheck;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDuty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDutyUser;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDutyVocation;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceException;
import com.kuangchi.sdd.attendanceConsole.statistic.model.DeptNoCheckSet;
import com.kuangchi.sdd.attendanceConsole.statistic.model.Holiday;
import com.kuangchi.sdd.attendanceConsole.statistic.model.LeaveTime;
import com.kuangchi.sdd.attendanceConsole.statistic.model.NoCheckSet;
import com.kuangchi.sdd.attendanceConsole.statistic.model.OutWork;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("statisticDao")
public class StatisticDaoImpl extends BaseDaoImpl<Object> implements StatisticDao {

	
	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.statistic";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LeaveTime> getLeaveTimeListByStaff(Map<String, String> map) {
		
		return getSqlMapClientTemplate().queryForList("getLeaveTimeListByStaff", map);
	}

	@Override
	public List<OutWork> getOutWorkListByStaff(Map<String, String> map) {
		return getSqlMapClientTemplate().queryForList("getOutWorkListByStaff", map);
	}

	@Override
	public List<AttendanceCheck> getAttendanceCheckListByStaff(
			Map<String, String> map) {
		return getSqlMapClientTemplate().queryForList("getAttendanceCheckListByStaff", map);
	}

	@Override
	public List<Holiday> getHolidayList(Map<String, String> map) {
		return getSqlMapClientTemplate().queryForList("getHolidayList", map);

	}

	@Override
	public AttendanceDuty getAttendanceDutyByStaff(
			Map<String, String> map) {
		return (AttendanceDuty) getSqlMapClientTemplate().queryForObject("getAttendanceDutyByStaff", map);

	}

	@Override
	public void insertAttendanceException(
			AttendanceException attendanceException) {	 
	   getSqlMapClientTemplate().insert("insertAttendanceException",attendanceException);	
	}

	@Override
	public List<AttendanceException> getAttendanceExceptionList(Map<String, String> map) {
		 
		return getSqlMapClientTemplate().queryForList("getAttendanceExceptionList",map);
	}

	@Override
	public Integer getAttendanceCountByMonth(Map<String, String> map) {
		
		return (Integer) getSqlMapClientTemplate().queryForObject("getAttendanceCountByMonth",map);
	}

	@Override
	public void insertAttendanceDataDate(AttendanceDataDate attendanceDataDate) {
		  getSqlMapClientTemplate().insert("insertAttendanceDataDate",attendanceDataDate);		
	}

	@Override
	public AttendanceDataDate getAttendanceDataDateByStaffAndEveryDate(
			Map<String, String> map) {
		// TODO Auto-generated method stub
		return (AttendanceDataDate) getSqlMapClientTemplate().queryForObject("getAttendanceDataDateByStaffAndEveryDate",map);
	}

	@Override
	public Map<String,Object> getStatisticMap(Map<String, String> map) {
	
		return (Map<String,Object>) getSqlMapClientTemplate().queryForObject("getStatisticMap",map);
	}

	@Override
	public void updateAttendanceDataDate(AttendanceDataDate attendanceDataDate) {
		getSqlMapClientTemplate().update("updateAttendanceDataDate",attendanceDataDate);
		
	}

	@Override
	public AttendanceDuty getAttendanceDutyById(Map<String, Integer> map) {
		
		return (AttendanceDuty) getSqlMapClientTemplate().queryForObject("getAttendanceDutyById",map);
	}

	@Override
	public List<AttendanceDutyUser> getAttendanceDutyUserListByStaffAndTime(
			Map<String, String> map) {
		 
		return getSqlMapClientTemplate().queryForList("getAttendanceDutyUserByStaffAndTime",map);
	}

	@Override
	public void addStaffOt(Map map) {
		getSqlMapClientTemplate().insert("addStaffOt", map);
	}

	@Override
	public List<Map> getOtListByStaff(Map<String, String> map) {
		return getSqlMapClientTemplate().queryForList("getOtListByStaff", map);
	}

	@Override
	public void delStaffLeaveRecord(String oldId) {
		getSqlMapClientTemplate().delete("delStaffLeaveRecord", oldId);
	}

	@Override
	public void insertCancelRecord(Map map) {
		getSqlMapClientTemplate().insert("insertCancelRecord", map);
	}

	@Override
	public List<NoCheckSet> getNoCheckSetByStaffNum(String staff_num,String checkDate) {
           Map<String, String> map=new HashMap<String, String>();
           map.put("staff_num", staff_num);
           map.put("checkDate", checkDate);
		return  getSqlMapClientTemplate().queryForList("getNoCheckSetByStaffNum",map);
	}

	@Override
	public List<DeptNoCheckSet> getNoCheckSetByDeptNum(String dept_num,String checkDate,String heritedToSubDept) {
           Map<String, String> map=new HashMap<String, String>();
           map.put("dept_num", dept_num);
           map.put("checkDate", checkDate);
           map.put("heritedToSubDept", heritedToSubDept);
		return  getSqlMapClientTemplate().queryForList("getNoCheckSetByDeptNum",map);
	}

	@Override
	public List<String> getAttendanceDutyUserDeptsByStaffNum(String staff_num,
			String yearMonth) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("yearMonth", yearMonth);
		 map.put("staff_num", staff_num);
		return getSqlMapClientTemplate().queryForList("getAttendanceDutyUserDeptsByStaffNum",map);
	}

	@Override
	public void deleteAttendanceDataDateByStaffNumAndDate(String staff_num,
			String every_date) {
		 
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("staff_num", staff_num);
		 map.put("every_date", every_date);
		 getSqlMapClientTemplate().delete("deleteAttendanceDataDateByStaffNumAndDate", map);
	}
	
	
	@Override
	public List<Holiday> getExtralWorkDayList(Map<String, String> map) {
		return getSqlMapClientTemplate().queryForList("getExtralWorkDayList",map);
	}
	
   
}
;