package com.kuangchi.sdd.attendanceConsole.attendCount.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendCount.dao.AttendMonthDao;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendMonthDao")
public class AttendMonthDaoImpl extends BaseDaoImpl<Object> implements AttendMonthDao {


	@Override
	public String getNameSpace() {
		return "attendanceConsole.attendCount";
	}

	@Override
	public String getTableName() {
		return null;
	}

	
	
	@Override
	public List<AttendMonthModel> getAllAttendMonth(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("getAllAttendMonth",map);
	}

	@Override
	public Integer countAllAttendMonth(Map<String, Object> map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllAttendMonth",map);
	}

	@Override
	public List<AttendMonthModel> exportAllMonthToExcel(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("exportAllMonthToExcel", map);
	}

	
}
