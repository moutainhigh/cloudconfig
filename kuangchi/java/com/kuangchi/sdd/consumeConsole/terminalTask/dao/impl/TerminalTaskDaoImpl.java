package com.kuangchi.sdd.consumeConsole.terminalTask.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.consumeConsole.terminalTask.dao.TerminalTaskDao;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskHistoryModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.TerminalTaskModel;

@Repository("terminalTaskDaoImpl")
public class TerminalTaskDaoImpl extends BaseDaoImpl<Object> implements TerminalTaskDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<TerminalTaskModel> getTerminalTaskInfos(Map map) {
		return getSqlMapClientTemplate().queryForList("getTerminalTaskInfos", map);
	}

	@Override
	public Integer getAllTerminalTaskInfoCounts(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getAllTerminalTaskInfoCounts", map);
	}

	@Override
	public Integer getCardTaskInfoCounts(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCardTaskInfoCounts", map);
	}

	@Override
	public List<CardTaskModel> getCardTaskInfos(Map map) {
		return getSqlMapClientTemplate().queryForList("getCardTaskInfos", map);
	}

	@Override
	public Integer getAllCardTaskHistoryInfoCounts(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getAllCardTaskHistoryInfoCounts", map);
	}

	@Override
	public List<CardTaskHistoryModel> getCardTaskHistoryInfos(Map map) {
		return getSqlMapClientTemplate().queryForList("getCardTaskHistoryInfos", map);
	}

	@Override
	public boolean delCardTaskHistory(String id) {
		return  delete("delCardTaskHistory", id);
	}

	@Override
	public boolean cancelDistributes(String id) {
		 return update("cancelDistributes",id);
	}

	
}
