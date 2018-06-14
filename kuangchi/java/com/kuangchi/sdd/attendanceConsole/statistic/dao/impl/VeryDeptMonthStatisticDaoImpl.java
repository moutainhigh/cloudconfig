package com.kuangchi.sdd.attendanceConsole.statistic.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;
import com.kuangchi.sdd.attendanceConsole.statistic.dao.VeryDeptMonthStatisticDao;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
@Repository("veryDeptMonthStatisticDao")
public class VeryDeptMonthStatisticDaoImpl extends BaseDaoImpl<Object> implements VeryDeptMonthStatisticDao {

	@Override
	public AttendDeptModel calculateDeptAttendInfo(String deptNum, String month) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deptNum", deptNum);
		map.put("month", month);
		return (AttendDeptModel) getSqlMapClientTemplate().queryForObject("calculateDeptAttendInfo", map);
	}

	@Override
	public void insertAttendDept(AttendDeptModel attendDeptModel) {
		getSqlMapClientTemplate().insert("insertAttendDept", attendDeptModel);
		
	}

	@Override
	public void delOldAttendDeptInfo(String deptNum, String month) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("deptNum", deptNum);
		map.put("month", month);
		getSqlMapClientTemplate().delete("delOldAttendDeptInfo", map);
	}

	@Override
	public String getNameSpace() {
		return "attendanceConsole.VeryDeptStatistic";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<String> getAllDept() {
		return getSqlMapClientTemplate().queryForList("getAllDept");
	}

}
