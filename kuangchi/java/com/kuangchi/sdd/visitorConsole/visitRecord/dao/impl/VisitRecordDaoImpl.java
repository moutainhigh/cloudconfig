package com.kuangchi.sdd.visitorConsole.visitRecord.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.visitorConsole.visitRecord.dao.VisitRecordDao;

@Repository("visitRecordDao")
public class VisitRecordDaoImpl extends BaseDaoImpl<Map> implements VisitRecordDao {

	@Override
	public String getNameSpace() {
		return "visitConsole.visitRecord";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map> getMainVisitRecords(Map map) {
		return getSqlMapClientTemplate().queryForList("getMainVisitRecords", map);
	}

	@Override
	public Integer countMainVisitRecords(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countMainVisitRecords", map);
	}

	@Override
	public List<Map> getPVisitRecords(Map map) {
		return getSqlMapClientTemplate().queryForList("getPVisitRecords", map);
	}

	@Override
	public Integer countPVisitRecords(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countPVisitRecords", map);
	}

	@Override
	public List<Map> getFVisitRecords(Map map) {
		return getSqlMapClientTemplate().queryForList("getFVisitRecords", map);
	}

	@Override
	public Integer countFVisitRecords(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countFVisitRecords", map);
	}

	@Override
	public List<Map> getPassivePeople(Map map) {
		return getSqlMapClientTemplate().queryForList("getPassivePeople", map);
	}

	@Override
	public Integer countPassivePeople(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countPassivePeople", map);
	}

	/*---------------- 访客记录导出  ------------------------*/
	/* 来访记录查询(没分页)  by huixian.pan */
	@Override
	public List<Map> getMainVisitRecordsNoLimit(Map map) {
		return getSqlMapClientTemplate().queryForList("getMainVisitRecordsNoLimit", map);
	}
    
	/* 被访记录查询(没分页) by huixian.pan */
	@Override
	public List<Map> getPVisitRecordsNoLimit(Map map) {
		return getSqlMapClientTemplate().queryForList("getPVisitRecordsNoLimit", map);
	}

	/* 随访记录查询(没分页)  by huixian.pan */
	@Override
	public List<Map> getFVisitRecordsNoLimit(Map map) {
		return getSqlMapClientTemplate().queryForList("getFVisitRecordsNoLimit", map);
	}

	/* 被访人员查询(没分页) by huixian.pan */
	@Override
	public List<Map> getPassivePeopleNoLimit(Map map) {
		return getSqlMapClientTemplate().queryForList("getPassivePeopleNoLimit", map);
	}
	
	/*----------------------------------------------------*/
	

}
