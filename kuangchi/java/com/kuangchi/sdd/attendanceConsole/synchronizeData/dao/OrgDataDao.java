package com.kuangchi.sdd.attendanceConsole.synchronizeData.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgDepartmentData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgUserAccountData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;

public interface OrgDataDao {
	 List<SqlServerOrgUserAccountData>  getSqlServerOrgUserAccountData(String subdept);
	 List<SqlServerOrgDepartmentData>  getSqlServerOrgDepartmentData();
	 List<SqlServerCheckData>  getSqlServerCheckData(String startDate,String endDate);
	    
	    List<SqlServerOutData> getSqlServerOutData(String startDate,String endDate);
	 
}
