package com.kuangchi.sdd.elevatorConsole.recordMonitor.dao.impl;




import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.recordMonitor.dao.IElevatorRecordMonitorDao;

/**
 * 实时监控（梯控刷卡记录）
 * @author yuman.gao
 */
@Repository("elevatorRecordMonitorDaoImpl")
public class ElevatorRecordMonitorDaoImpl extends BaseDaoImpl<Object> implements IElevatorRecordMonitorDao{

	@Override
	public String getNameSpace() {
		return "common.recordMonitor";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map<String, Object>> getAllRecord(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getAllRecord", map);
	}
	
	@Override
	public Integer getAllRecordCount(Map<String, Object> map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getAllRecordCount", map);
	}


}
