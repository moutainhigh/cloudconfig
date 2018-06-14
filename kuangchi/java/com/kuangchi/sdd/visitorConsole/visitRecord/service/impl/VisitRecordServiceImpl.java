package com.kuangchi.sdd.visitorConsole.visitRecord.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.visitorConsole.visitRecord.dao.VisitRecordDao;
import com.kuangchi.sdd.visitorConsole.visitRecord.service.VisitRecordService;
@Service("visitRecordService")
public class VisitRecordServiceImpl implements VisitRecordService{

	@Autowired
	private VisitRecordDao visitRecordDao;

	@Override
	public Grid getMainVisitRecords(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitRecordDao.getMainVisitRecords(map));
		grid.setTotal(visitRecordDao.countMainVisitRecords(map));
		return grid;
	}

	@Override
	public Grid getPVisitRecords(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitRecordDao.getPVisitRecords(map));
		grid.setTotal(visitRecordDao.countPVisitRecords(map));
		return grid;
	}

	@Override
	public Grid getFVisitRecords(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitRecordDao.getFVisitRecords(map));
		grid.setTotal(visitRecordDao.countFVisitRecords(map));
		return grid;
	}

	@Override
	public Grid getPassivePeople(Map map) {
		Grid grid=new Grid();
		grid.setRows(visitRecordDao.getPassivePeople(map));
		grid.setTotal(visitRecordDao.countPassivePeople(map));
		return grid;
	}

	/*----------------访客记录导出------------------------*/
	/**
	 *  来访记录查询(没分页)  
	 *  by huixian.pan 
	 */
	@Override
	public List<Map> getMainVisitRecordsNoLimit(Map map) {
		return visitRecordDao.getMainVisitRecordsNoLimit(map);
	}

	/**
	 * 被访记录查询(没分页) 
	 * by huixian.pan 
	 */
	@Override
	public List<Map> getPVisitRecordsNoLimit(Map map) {
		return visitRecordDao.getPVisitRecordsNoLimit(map);
	}

	/**
	 *  随访记录查询(没分页)  
	 *  by huixian.pan 
	 */
	@Override
	public List<Map> getFVisitRecordsNoLimit(Map map) {
		return visitRecordDao.getFVisitRecordsNoLimit(map);
	}

	/**
	 *  被访人员查询(没分页) 
	 *  by huixian.pan 
	 */
	@Override
	public List<Map> getPassivePeopleNoLimit(Map map) {
		return visitRecordDao.getPassivePeopleNoLimit(map);
	}
	/*------------------------------------------------*/
}
