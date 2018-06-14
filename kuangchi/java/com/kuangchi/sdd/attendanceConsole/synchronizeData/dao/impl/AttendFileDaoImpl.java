package com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.AttendFileDao;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.AttendData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendFileDao")
public class AttendFileDaoImpl  extends BaseDaoImpl<Object>  implements AttendFileDao{

	@Override
	public String getNameSpace() {
		 
		return "attendanceConsole.attendfile";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public List<SqlServerCheckData> selectAttendfile(String startDate,
			String endDate,String employeeId) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("startDate", startDate);
		 map.put("endDate", endDate);
		 map.put("employeeId", employeeId);
		return getSqlMapClientTemplate().queryForList("selectAttendfile",map);
	}

	@Override
	public void deleteAttendFile(String id) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("id", id);
		getSqlMapClientTemplate().delete("deleteAttendFile",map);
	}

	@Override
	public void insertAttendFile(SqlServerCheckData sqlServerCheckData) {
		 getSqlMapClientTemplate().insert("insertAttendFile",sqlServerCheckData);
		
	}

	@Override
	public void deleteOutworkByInstanceId(String id) {
		 Map<String, String> map=new HashMap<String, String>();

		 map.put("processInstanceId", id);
		getSqlMapClientTemplate().delete("deleteOutworkByInstanceId",map);
		
	}
	@Override
	public void deleteLeaveTimeByInstanceId(String id) {
		 Map<String, String> map=new HashMap<String, String>();

		 map.put("processInstanceId", id);
		 getSqlMapClientTemplate().delete("deleteLeaveTimeByInstanceId",map);
		
	}

	@Override
	public void deleteDataRecordByTime(String startDate, String endDate) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("startDate", startDate);
		 map.put("endDate", endDate);
		 getSqlMapClientTemplate().delete("deleteDataRecordByTime",map);
		
	}

	@Override
	public void insertAttendDataRecord(AttendData attendData) {
		 getSqlMapClientTemplate().insert("insertAttendDataRecord",attendData);
		
	}

	@Override
	public void insertAttendFile2(SqlServerOutData sqlServerOutData) {
		 getSqlMapClientTemplate().insert("insertAttendFile2",sqlServerOutData);
		
	}

	@Override
	public List<SqlServerOutData>  selectAttendfile2(String startDate, String endDate,String employeeId) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("startDate", startDate);
		 map.put("endDate", endDate);
		 map.put("employeeId", employeeId);
		 return getSqlMapClientTemplate().queryForList("selectAttendfile2",map);
		
	}

	@Override
	public void deleteAttendFile2(String id) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("id", id);
		getSqlMapClientTemplate().delete("deleteAttendFile2",map);
	}

	@Override
	public List<String> selectAdminDM() {
	     
		return getSqlMapClientTemplate().queryForList("selectAdminDM");
	}

}
