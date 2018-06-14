package com.kuangchi.sdd.businessConsole.cron.dao.impl;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.cron.dao.ICronDao;
import com.kuangchi.sdd.businessConsole.cron.model.Cron;

@Repository("cronDaoImpl")
public class CronDaoImpl extends BaseDaoImpl<Object> implements ICronDao {

	@Override
	public String getNameSpace() {
		return "common.Cron";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public Cron selectIP(String sys_key) {
		return (Cron) getSqlMapClientTemplate().queryForObject("selectIP",
				sys_key);
	}

	@Override
	public boolean updateCronIP(Cron cron) {
		return update("updateCronIP", cron);
	}

	@Override
	public Integer compareIP(Cron cron) {
		return (Integer) getSqlMapClientTemplate().queryForObject("compareIP",
				cron);
	}

}
