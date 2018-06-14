package com.kuangchi.sdd.attendanceConsole.quartz.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.quartz.dao.AttendDataRecordDao;
import com.kuangchi.sdd.attendanceConsole.quartz.model.AttendDataRecord;
import com.kuangchi.sdd.attendanceConsole.quartz.service.AttendDataRecordService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

@Transactional
@Service("attendDataRecordService")
public class AttendDataRecordServiceImpl implements AttendDataRecordService {

	@Resource(name="attendDataRecordDao")
	private AttendDataRecordDao attendDataRecordDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	
	@Override
	public void insertAttendDataRecord(AttendDataRecord attendDataRecord) {
		attendDataRecordDao.insertAttendDataRecord(attendDataRecord);
	}

}
