package com.kuangchi.sdd.attendanceConsole.synchronizeData.service;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgDepartmentData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgUserAccountData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;

public interface OrgDataService {
	 List<SqlServerOrgUserAccountData>  getSqlServerOrgUserAccountData(String subDept);
	 List<SqlServerOrgDepartmentData>  getSqlServerOrgDepartmentData();
	  List<SqlServerCheckData>  getSqlServerCheckData(String startDate,String endDate);
	   public List<SqlServerOutData> getSqlServerOutData(String startDate,
				String endDate);
}
