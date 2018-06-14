package com.kuangchi.sdd.attendanceConsole.quartz.dao.impl;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.quartz.dao.AttendDataRecordDao;
import com.kuangchi.sdd.attendanceConsole.quartz.model.AttendDataRecord;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.books.model.Books;

@Repository("attendDataRecordDao")
public class AttendDataRecordDaoImpl extends BaseDaoImpl<AttendDataRecord> implements AttendDataRecordDao {

	@Override
	public void insertAttendDataRecord(AttendDataRecord attendDataRecord) {
		this.getSqlMapClientTemplate().insert("addAttendDataRecord", attendDataRecord);
		
	}
	
	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
