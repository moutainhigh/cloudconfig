package com.kuangchi.sdd.attendanceConsole.attendCount.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendCount.dao.AttendDateDao;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDateModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendViewDateInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.DutyInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.LeaveTimeModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.OutWorkModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;

@Repository("attendDateDao")
public class AttendDateDaoImpl extends BaseDaoImpl<Object> implements AttendDateDao {


	@Override
	public String getNameSpace() {
		return "attendanceConsole.attendCount";
	}

	@Override
	public String getTableName() {
		return null;
	}


	@Override
	public List<AttendDateModel> getAllAttendDate1(Map<String,Object> map) {
		
		return getSqlMapClientTemplate().queryForList("getAllAttendDate1", map);
	}

	@Override
	public Integer countAllAttendDate1(Map<String,Object> map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllAttendDate1",map);
	}

	@Override
	public List<AttendDateModel> exportAllDateToExcel(Map<String,Object> map) {
	
		return getSqlMapClientTemplate().queryForList("exportAllDateToExcel", map);
	}


	@Override
	public List<LeaveTimeModel> getLeaveDetailList(String staffNum,
			String startTime, String endTime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNum", staffNum);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return getSqlMapClientTemplate().queryForList("getLeaveDetailList", map);
	}

	@Override
	public List<OutWorkModel> getOutDetailList(String staffNum,
			String startTime, String endTime) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNum", staffNum);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return getSqlMapClientTemplate().queryForList("getOutDetailList", map);
	}

	@Override
	public AttendDateModel getAttendDateByStaffNumAndDutyId(String staffNum,
			String dutyId, String everyDate) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNum", staffNum);
		map.put("dutyId", dutyId);
		map.put("everyDate", everyDate);
		return (AttendDateModel) getSqlMapClientTemplate().queryForObject("getAttendDateByStaffNumAndDutyId", map);
	}

	@Override
	public DutyInfoModel getDutyModel(String dutyId) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("dutyId", dutyId);
		return (DutyInfoModel) getSqlMapClientTemplate().queryForObject("getDutyModel", map);
	}

	@Override
	public Map askIfMidCheck(String dutyId) {
		return (Map) getSqlMapClientTemplate().queryForObject("askIfMidCheck", dutyId);
	}

	@Override
	public List<Map> getLeaveInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getLeaveInfo", map);
	}
	
	@Override
	public List<Map> getOutInfo(Map map) {
		return   getSqlMapClientTemplate().queryForList("getOutInfo", map);
	}



}
