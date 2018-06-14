package com.kuangchi.sdd.attendanceConsole.attendAnalysis.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendAnalysis.dao.IAttendAnalysisDao;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendAnalysisDao")
public class AttendAnalysisDaoImpl extends BaseDaoImpl<Object> implements IAttendAnalysisDao {

	@Override
	public String getNameSpace() {
		return "attendanceConsole.attendAnalysis";
	}

	@Override
	public String getTableName() {
		return null;
	}

	//查询用户考勤数据
	public List<AttendanceDataDate> getAttendanceData(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getAttendanceData", map);
	}

	


}
