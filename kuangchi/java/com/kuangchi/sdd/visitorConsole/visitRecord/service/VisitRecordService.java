package com.kuangchi.sdd.visitorConsole.visitRecord.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;

public interface VisitRecordService {

	/**
	 * 查询来访记录
	 * by gengji.yang
	 */
	public Grid getMainVisitRecords(Map map);
	
	/**
	 * 查询被访记录
	 * by gengji.yang
	 */
	public Grid getPVisitRecords(Map map);
	
	/**
	 * 查询随访记录
	 * by gengji.yang
	 */
	public Grid getFVisitRecords(Map map);
	
	/**
	 * 查询被访人员
	 * by gengji.yang
	 */
	public Grid getPassivePeople(Map map);
	
	
	/*----------------访客记录导出------------------------*/
	/**
	 *  来访记录查询(没分页)  
	 *  by huixian.pan 
	 */
	public List<Map> getMainVisitRecordsNoLimit(Map map);
	/**
	 * 被访记录查询(没分页) 
	 * by huixian.pan 
	 */
	public List<Map> getPVisitRecordsNoLimit(Map map);
	/**
	 *  随访记录查询(没分页)  
	 *  by huixian.pan 
	 */
	public List<Map> getFVisitRecordsNoLimit(Map map);
	/**
	 *  被访人员查询(没分页) 
	 *  by huixian.pan 
	 */
	public List<Map> getPassivePeopleNoLimit(Map map);
	/*------------------------------------------------*/
	
}
