package com.kuangchi.sdd.attendanceConsole.synchronizeData.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerBrushCardLog;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;

public interface CheckDataDao {
   public List<SqlServerBrushCardLog> getBrushCardLog(String startDate, String endDate,String employeeId);
}
