package com.kuangchi.sdd.consumeConsole.accountDetail.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.accountDetail.dao.HistoricalBalanceDao;
import com.kuangchi.sdd.consumeConsole.accountDetail.model.HistoricalBalanceModel;
@Repository("historicalBalanceDaoImpl")
public class HistoricalBalanceDaoImpl extends BaseDaoImpl<Object> implements HistoricalBalanceDao {

	@Override
	public List<HistoricalBalanceModel> getHistoricalBalanceInfoList(Map map) {
		return getSqlMapClientTemplate().queryForList("getHistoricalBalanceInfoList", map);
	}

	@Override
	public Integer getHistoricalBalanceInfoCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getHistoricalBalanceInfoCount", map);
	}

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<HistoricalBalanceModel> getHistoricalBalanceList() {
		return getSqlMapClientTemplate().queryForList("getHistoricalBalanceList");
	}

	

}
