package com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.synchronizeData.dao.CheckDataDao;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerBrushCardLog;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerCheckData;
import com.kuangchi.sdd.attendanceConsole.synchronizeData.model.SqlServerOutData;
import com.kuangchi.sdd.base.dao.SqlServerBaseDaoImpl;
import com.kuangchi.sdd.base.dao.SqlServerCheckBaseDaoImpl;
import com.kuangchi.sdd.singleLogin.dao.SingleLoginDao;
import com.kuangchi.sdd.singleLogin.model.LoginSerialNum;

@Repository("checkDataDao")
public class CheckDataDaoImpl extends SqlServerCheckBaseDaoImpl<Object>  implements CheckDataDao   {

	@Override
	public String getNameSpace() {
		 
		return "sqlServerCheck.data";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SqlServerBrushCardLog> getBrushCardLog(String startDate, String endDate,String employeeId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("employeeId",employeeId);
		return getSqlMapClientTemplate().queryForList("getBrushCardLog",map);
		
	}

	
	
	

}
