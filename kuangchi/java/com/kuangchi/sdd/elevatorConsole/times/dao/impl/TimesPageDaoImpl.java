package com.kuangchi.sdd.elevatorConsole.times.dao.impl;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.times.dao.TimesPageDao;
import com.kuangchi.sdd.elevatorConsole.times.model.TimesPageModel;
import com.kuangchi.sdd.elevatorConsole.timesgroup.dao.TimesGroupDao;
@Repository("timesPageDaoImpl")
public class TimesPageDaoImpl extends BaseDaoImpl<Object> implements TimesPageDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean motifyTimesPage(TimesPageModel timesPageModel) {
		return update("motifyTimesPage",timesPageModel);
	}

	@Override
	public Integer getTimesPageCountByTimesGroupNum(String time_group_num) {
		return  (Integer) getSqlMapClientTemplate().queryForObject("getTimesPageCountByTimesGroupNum",time_group_num);
	}

	@Override
	public boolean insertTimesPage(TimesPageModel timesPageModel) {
		return insert("insertTimesPage",timesPageModel);
	}

}
