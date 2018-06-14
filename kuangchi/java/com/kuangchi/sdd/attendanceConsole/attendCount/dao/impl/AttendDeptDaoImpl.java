package com.kuangchi.sdd.attendanceConsole.attendCount.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendCount.dao.AttendDeptDao;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendDeptDao")
public class AttendDeptDaoImpl extends BaseDaoImpl<Object> implements AttendDeptDao {

	@Override
	public List<AttendDeptModel> getAllAttendDept(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllAttendDept", map);
	}

	@Override
	public String getNameSpace() {
		return "attendanceConsole.attendCount";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countAllAttendDept(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllAttendDept",map);
	}

	@Override
	public List<AttendDeptModel> exportAllDeptToExcel(Map map) {
		return getSqlMapClientTemplate().queryForList("exportAllDeptToExcel", map);
	}

	@Override
	public Double getDeptAllWorkTime(Map map) {
		return (Double) getSqlMapClientTemplate().queryForObject("getDeptAllWorkTime",map);
	}


}
