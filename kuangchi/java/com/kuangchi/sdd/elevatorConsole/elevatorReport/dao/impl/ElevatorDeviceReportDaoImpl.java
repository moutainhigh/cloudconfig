package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.ElevatorDeviceReportDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorDeviceInfo;

@Repository("elevatorDeviceReportDaoImpl")
public class ElevatorDeviceReportDaoImpl extends BaseDaoImpl<ElevatorDeviceInfo> implements ElevatorDeviceReportDao {

	
	@Override
	public List<Map<String, Object>> getElevatorDeviceinfo(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("getElevatorDeviceinfo", map);
		
	}

	@Override
	public Integer getElevatorDeviceinfoCount(Map<String, Object> map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getElevatorDeviceinfoCount", map);
	}

	@Override
	public String getNameSpace() {
		return "common.elevatorReport";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map<String, Object>> exportElevatorDeviceinfo(Map<String, Object> map) {
		return getSqlMapClientTemplate().queryForList("exportElevatorDeviceinfo", map);
	}

}
