package com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.dao.AllSynchronizeDao;

@Repository("allSynchronizeDao")
public class AllSynchronizeDaoImpl extends BaseDaoImpl<Object> implements AllSynchronizeDao {

	@Override
	public String getNameSpace() {
		return "common.SynchronizeFromComm";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public Map getMacAndType(String deviceNum) {
		return (Map)getSqlMapClientTemplate().queryForObject("getMacAndType", deviceNum);
	}

	@Override
	public void addSynAuthority(Map map) {
		getSqlMapClientTemplate().insert("addSynAuthority", map);
	}

	@Override
	public void delSynAuthority(Map map) {
		getSqlMapClientTemplate().delete("delSynAuthority", map);
	}

}
