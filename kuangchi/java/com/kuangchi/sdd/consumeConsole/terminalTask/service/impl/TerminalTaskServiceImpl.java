package com.kuangchi.sdd.consumeConsole.terminalTask.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.terminalTask.dao.TerminalTaskDao;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskHistoryModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.TerminalTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.service.TerminalTaskService;
@Service("terminalTaskServiceImpl")
public class TerminalTaskServiceImpl implements TerminalTaskService {
	@Resource(name="terminalTaskDaoImpl")
	private TerminalTaskDao terminalTaskDao;
	@Override
	public Grid<TerminalTaskModel> getAllTerminalTask(Map map) {
		Grid<TerminalTaskModel> grid = new Grid<TerminalTaskModel>();
		List<TerminalTaskModel> list =  terminalTaskDao.getTerminalTaskInfos(map);
		grid.setRows(list);
		if(null!=list){
			Integer count = terminalTaskDao.getAllTerminalTaskInfoCounts(map);
			grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	@Override
	public Grid<CardTaskModel> getAllCardTask(Map map) {
		Grid<CardTaskModel> grid = new Grid<CardTaskModel>();
		List<CardTaskModel> list =  terminalTaskDao.getCardTaskInfos(map);
		grid.setRows(list);
		if(null!=list){
			Integer count = terminalTaskDao.getCardTaskInfoCounts(map);
			grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	
	@Override
	public Grid<CardTaskHistoryModel> getAllCardTaskHistory(Map map) {
		Grid<CardTaskHistoryModel> grid = new Grid<CardTaskHistoryModel>();
		List<CardTaskHistoryModel> list =  terminalTaskDao.getCardTaskHistoryInfos(map);
		grid.setRows(list);
		if(null!=list){
			Integer count = terminalTaskDao.getAllCardTaskHistoryInfoCounts(map);
			grid.setTotal(count);
		}else{
			grid.setTotal(0);
		}
		return grid;
	}
	@Override
	public boolean delCardTaskHistory(String id) {
		return	terminalTaskDao.delCardTaskHistory(id);
	}
	@Override
	public boolean cancelDistributes(String id) {
		return terminalTaskDao.cancelDistributes(id);
	}
}
	
