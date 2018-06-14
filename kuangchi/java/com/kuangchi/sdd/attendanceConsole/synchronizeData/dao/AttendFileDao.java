package com.kuangchi.sdd.attendanceConsole.synchronizeData.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.AttendData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;

public interface AttendFileDao {
    public List<SqlServerCheckData> selectAttendfile(String startDate,String endDate,String employeeId);
    public void deleteAttendFile(String id);
    public void insertAttendFile(SqlServerCheckData sqlServerCheckData);
    
    public void deleteOutworkByInstanceId(String id);
	public void deleteLeaveTimeByInstanceId(String id);
    public void deleteDataRecordByTime(String startDate,String endDate);
    
    public void insertAttendDataRecord(AttendData attendData);
    
    public void insertAttendFile2(SqlServerOutData sqlServerOutData);
    
    public List<SqlServerOutData> selectAttendfile2(String startDate,String endDate,String employeeId);
    
    public void deleteAttendFile2(String id);
    public  List<String>  selectAdminDM();
}
