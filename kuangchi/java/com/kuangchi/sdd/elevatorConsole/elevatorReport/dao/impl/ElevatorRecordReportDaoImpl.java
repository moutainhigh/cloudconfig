package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.ElevatorRecordReportDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorDeviceInfo;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.ElevatorRecordInfo;

@Repository("elevatorRecordReportDaoImpl")
public class ElevatorRecordReportDaoImpl extends BaseDaoImpl<ElevatorDeviceInfo> implements ElevatorRecordReportDao {

	
	@Override
	public List<ElevatorRecordInfo> getElevatorRecordinfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getElevatorRecordinfo", map);
		
	}

	@Override
	public Integer getElevatorRecordinfoCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getElevatorRecordinfoCount", map);
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
	public List<ElevatorRecordInfo> exportElevatorRecordinfo(Map map) {
		return getSqlMapClientTemplate().queryForList("exportElevatorRecordinfo", map);
	}

	@Override
	public List<ElevatorRecordInfo> latestElevatorRecord() {
		return getSqlMapClientTemplate().queryForList("latestElevatorRecord");
	}
	
	@Override
	public boolean insertEventRecord(Map map){
		return insert("insertEventRecord", map);
	}
	
	@Override
	public HashMap getStaffInfoByCard(String card_num){
		return (HashMap) getSqlMapClientTemplate().queryForObject("getStaffByCard", card_num);
	}
	
	@Override
	public String getFloorName(Map map){
		return (String) getSqlMapClientTemplate().queryForObject("getFloorName", map);
	}
	
	@Override
	public String getCardTypeName(String card_num){
		return (String) getSqlMapClientTemplate().queryForObject("getCardTypeName", card_num);
	}
	
}
