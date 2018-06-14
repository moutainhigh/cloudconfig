package com.kuangchi.sdd.attendanceConsole.quartz.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.quartz.dao.CheckRecordDao;
import com.kuangchi.sdd.attendanceConsole.quartz.model.CardRecord;
import com.kuangchi.sdd.base.dao.SqlServerBaseDaoImpl;

@Repository("checkRecordDaoImpl")
public class CheckRecordDaoImpl extends SqlServerBaseDaoImpl<CardRecord> implements CheckRecordDao {



	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.dataSynchronize";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRemoteCardRecord(String firstID,String lastID) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("firstID", firstID);
		mapParam.put("lastID", lastID);
		getSqlMapClientTemplate().delete("deleteRemoteCardRecord",mapParam);
		
	}
	@Override
	public List<CardRecord> getSynchronizeCardRecordList() {
		return getSqlMapClientTemplate().queryForList("checkDataSynchronize");
	}
}
