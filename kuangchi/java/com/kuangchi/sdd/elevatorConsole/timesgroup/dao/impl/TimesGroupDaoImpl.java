package com.kuangchi.sdd.elevatorConsole.timesgroup.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.timesgroup.dao.TimesGroupDao;
import com.kuangchi.sdd.elevatorConsole.timesgroup.model.TimesGroupModel;
@Repository("timesGroupDaoImpl")
public class TimesGroupDaoImpl extends BaseDaoImpl<Object> implements TimesGroupDao {
	@Override
	public String getNameSpace() {
		return null;
	}
	
	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<TimesGroupModel> getTimesGroupPageList(Map<String, Integer> map) {
		return getSqlMapClientTemplate().queryForList("getTimesGroupPageList", map);
	}

	@Override
	public Integer getTimesGroupPageCount(Map<String, Integer> map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getTimesGroupPageCount", map);
	}

	@Override
	public boolean insertTimesGroup(Map map) {
		return insert("insertTimesGroup", map);
	}

	@Override
	public Integer checkTimeGroupNumUnique(String time_group_num) {
		return (Integer) getSqlMapClientTemplate().queryForObject("checkTimeGroupNumUnique", time_group_num);
	}


}
