package com.kuangchi.sdd.singleLogin.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.quartz.dao.CheckRecordDao;
import com.kuangchi.sdd.attendanceConsole.quartz.model.CardRecord;
import com.kuangchi.sdd.base.dao.SqlServerBaseDaoImpl;
import com.kuangchi.sdd.singleLogin.dao.SingleLoginDao;
import com.kuangchi.sdd.singleLogin.model.LoginSerialNum;

@Repository("singleLoginDaoImpl")
public class SingleLoginDaoImpl extends SqlServerBaseDaoImpl<LoginSerialNum> implements SingleLoginDao {



	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.singleLogin";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getLoginSerialNum(String localLoginSerialNum) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("localLoginSerialNum", localLoginSerialNum);
		return (Map) getSqlMapClientTemplate().queryForObject("getLoginSerialNum",map);
	}

}
