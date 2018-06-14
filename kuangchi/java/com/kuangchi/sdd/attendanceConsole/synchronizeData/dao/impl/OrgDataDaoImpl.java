package com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.OrgDataDao;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgDepartmentData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOrgUserAccountData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.base.dao.SqlServerOrgBaseDaoImpl;

@Repository("orgDataDao")
public class OrgDataDaoImpl extends SqlServerOrgBaseDaoImpl<Object>  implements OrgDataDao {

	@Override
	public String getNameSpace() {
		return "sqlServerOrg.data";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SqlServerOrgUserAccountData> getSqlServerOrgUserAccountData(String subdept) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("subdept", subdept);
		return getSqlMapClientTemplate().queryForList("getSqlServerOrgUserAccountData",map);
	}

	@Override
	public List<SqlServerOrgDepartmentData> getSqlServerOrgDepartmentData() {
		Map<String, String> map=new HashMap<String, String>();

		return getSqlMapClientTemplate().queryForList("getSqlServerOrgDepartmentData",map);
	}
	@Override
	public List<SqlServerCheckData> getSqlServerCheckData(String startDate,String endDate) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return getSqlMapClientTemplate().queryForList("getSqlServerCheckData",map);
	}

	@Override
	public List<SqlServerOutData> getSqlServerOutData(String startDate,
			String endDate) {
		 
		Map<String, String> map=new HashMap<String, String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return getSqlMapClientTemplate().queryForList("getSqlServerOutData",map);
	}



}
