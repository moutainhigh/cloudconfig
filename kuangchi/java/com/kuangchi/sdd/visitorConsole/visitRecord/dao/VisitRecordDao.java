package com.kuangchi.sdd.visitorConsole.visitRecord.dao;

import java.util.List;
import java.util.Map;

public interface VisitRecordDao {
	
	public List<Map> getMainVisitRecords(Map map);
	
	public Integer countMainVisitRecords(Map map);
	
	public List<Map> getPVisitRecords(Map map);
	
	public Integer countPVisitRecords(Map map);
	
	public List<Map> getFVisitRecords(Map map);
	
	public Integer countFVisitRecords(Map map);
	
	public List<Map> getPassivePeople(Map map);
	
	public Integer countPassivePeople(Map map);
	
	
	/*----------------访客记录导出------------------------*/
	/* 来访记录查询(没分页)  by huixian.pan */
	public List<Map> getMainVisitRecordsNoLimit(Map map);
	/* 被访记录查询(没分页) by huixian.pan */
	public List<Map> getPVisitRecordsNoLimit(Map map);
	/* 随访记录查询(没分页)  by huixian.pan */
	public List<Map> getFVisitRecordsNoLimit(Map map);
	/* 被访人员查询(没分页) by huixian.pan */
	public List<Map> getPassivePeopleNoLimit(Map map);
	/*------------------------------------------------*/
	
}
