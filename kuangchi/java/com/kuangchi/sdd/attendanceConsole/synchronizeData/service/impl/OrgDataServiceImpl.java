package com.kuangchi.sdd.attendanceConsole.synchronizeData.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.OrgDataDao;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgDepartmentData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgUserAccountData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.service.OrgDataService;

@Service("orgDataService")
public class OrgDataServiceImpl implements OrgDataService {
@Resource(name="orgDataDao")
OrgDataDao orgDataDao;

@Override
public List<SqlServerOrgUserAccountData> getSqlServerOrgUserAccountData(String subDept) {

	return orgDataDao.getSqlServerOrgUserAccountData(subDept);
}

@Override
public List<SqlServerOrgDepartmentData> getSqlServerOrgDepartmentData() {
	 
	return orgDataDao.getSqlServerOrgDepartmentData();
}

@Override
public List<SqlServerCheckData> getSqlServerCheckData(String startDate,String endDate) {
	 
	return orgDataDao.getSqlServerCheckData(startDate,endDate);
}
@Override
public List<SqlServerOutData> getSqlServerOutData(String startDate,
		String endDate) {
	 return orgDataDao.getSqlServerOutData(startDate, endDate);
}

}
