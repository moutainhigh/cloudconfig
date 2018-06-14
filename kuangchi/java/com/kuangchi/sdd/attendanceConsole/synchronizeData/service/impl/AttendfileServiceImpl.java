package com.kuangchi.sdd.attendanceConsole.synchronizeData.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.AttendFileDao;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.AttendData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.AttendFileService;

@Service("attendFileService")
public class AttendfileServiceImpl implements AttendFileService {
@Resource(name="attendFileDao")
AttendFileDao attendFileDao;

	@Override
	public List<SqlServerCheckData> selectAttendfile(String startDate,
			String endDate,String employeeId) {
	 
		return attendFileDao.selectAttendfile(startDate, endDate,employeeId);
	}

	@Override
	public void deleteAttendFile(String id) {
		 attendFileDao.deleteAttendFile(id);
		
	}

	@Override
	public void insertAttendFile(SqlServerCheckData sqlServerCheckData) {
		 attendFileDao.insertAttendFile(sqlServerCheckData);
		
	}

	@Override
	public void deleteOutworkByInstanceId(String id) {
		 attendFileDao.deleteOutworkByInstanceId(id);
		
	}
	
	
	@Override
	public void deleteLeaveTimeByInstanceId(String id) {
		attendFileDao.deleteLeaveTimeByInstanceId(id);
		
	}

	@Override
	public void deleteDataRecordByTime(String startDate, String endDate) {
		 
		 attendFileDao.deleteDataRecordByTime(startDate+" 00:00:00", endDate+" 23:59:59");
		
	}

	@Override
	public void insertAttendDataRecord(AttendData attendData) {
		 attendFileDao.insertAttendDataRecord(attendData);
		
	}

	@Override
	public void insertAttendFile2(SqlServerOutData sqlServerOutData) {
	    attendFileDao.insertAttendFile2(sqlServerOutData);
		
	}

	@Override
	public List<SqlServerOutData> selectAttendfile2(String startDate,
			String endDate,String employeeId) {
		   
		return attendFileDao.selectAttendfile2(startDate, endDate,employeeId);
	}

	@Override
	public void deleteAttendFile2(String id) {
		 attendFileDao.deleteAttendFile2(id);
		
	}

	@Override
	public List<String> selectAdminDM() {
		
		return attendFileDao.selectAdminDM();
	}

}
