package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.PersonInfoSystemDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoSystem;

@Repository("personInfoSystemDaoImpl")
public class PersonInfoSystemDaoImpl extends BaseDaoImpl<PersonInfoSystem> implements PersonInfoSystemDao {

	@Override
	public List<PersonInfoSystem> getPersonInfoSystem(Map map) {
		return getSqlMapClientTemplate().queryForList("getPersonInfoSystem", map);
	}

	@Override
	public Integer getPersonInfoSystemCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getPersonInfoSystemCount", map);
	}

	@Override
	public String getNameSpace() {
		return "common.elevatorReport";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<PersonInfoSystem> exportPersonInfoSystem(Map map) {
		return getSqlMapClientTemplate().queryForList("exportPersonInfoSystem", map);
	}

}
