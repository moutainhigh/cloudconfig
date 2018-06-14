package com.kuangchi.sdd.consumeConsole.terminalTask.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskHistoryModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.TerminalTaskModel;

public interface TerminalTaskDao {
	public List<TerminalTaskModel> getTerminalTaskInfos(Map map);
	public Integer getAllTerminalTaskInfoCounts(Map map);
	public Integer getCardTaskInfoCounts(Map map);
	public List<CardTaskModel> getCardTaskInfos(Map map);
	public Integer getAllCardTaskHistoryInfoCounts(Map map);
	public List<CardTaskHistoryModel> getCardTaskHistoryInfos(Map map);
	public boolean delCardTaskHistory(String id);
	public boolean cancelDistributes(String id);
	
}
