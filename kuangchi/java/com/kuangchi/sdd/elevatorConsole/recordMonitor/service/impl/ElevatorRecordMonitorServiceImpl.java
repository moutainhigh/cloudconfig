package com.kuangchi.sdd.elevatorConsole.recordMonitor.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.recordMonitor.dao.IElevatorRecordMonitorDao;
import com.kuangchi.sdd.elevatorConsole.recordMonitor.service.IElevatorRecordMonitorService;

/**
 * 实时监控（梯控刷卡记录）
 * @author yuman.gao
 */
@Transactional
@Service("elevatorRecordMonitorServiceImpl")
public class ElevatorRecordMonitorServiceImpl implements IElevatorRecordMonitorService{
	
	@Resource(name = "elevatorRecordMonitorDaoImpl")
	private IElevatorRecordMonitorDao levatorRecordMonitorDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public List<Map<String,Object>> getAllRecord(Map<String, Object> map) {
		return levatorRecordMonitorDao.getAllRecord(map);
	}

	@Override
	public Integer getAllRecordCount(Map<String, Object> map) {
		return levatorRecordMonitorDao.getAllRecordCount(map);
	}

}
