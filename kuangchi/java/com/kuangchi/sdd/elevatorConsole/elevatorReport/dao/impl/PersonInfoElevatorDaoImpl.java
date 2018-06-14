package com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.dao.PersonInfoElevatorDao;
import com.kuangchi.sdd.elevatorConsole.elevatorReport.model.PersonInfoElevator;

@Repository("personInfoElevatorDaoImpl")
public class PersonInfoElevatorDaoImpl extends BaseDaoImpl<PersonInfoElevator> implements PersonInfoElevatorDao {

	@Override
	public String getNameSpace() {
		return "common.elevatorReport";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public List<PersonInfoElevator> getPersonInfoElevator(Map map) {
		return getSqlMapClientTemplate().queryForList("getPersonInfoElevator", map);
	}

	@Override
	public Integer getPersonInfoElevatorCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getPersonInfoElevatorCount", map);
	}
	@Override
	public List<PersonInfoElevator> exportPersonInfoElevator(Map map) {
		return getSqlMapClientTemplate().queryForList("exportPersonInfoElevator", map);
	}

}
