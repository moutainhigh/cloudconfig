package com.kuangchi.sdd.baseConsole.log.dao.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.log.model.Log;

@Repository("LogDaoImpl")
public class LogDaoImpl extends  BaseDaoImpl<Object>  implements LogDao{

	@Override
	public void addLog(Map<String, String> parameter) {
		getSqlMapClientTemplate().insert("common.User.addLog", parameter);
		
	}

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	//按条件查询日志
	public List<Log> getLogListByParams(Map map){
		
		
		return getSqlMapClientTemplate().queryForList("getLogList",map);
	}

	//获取日志条数
	public int getListRecords(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("getLogRecords", map);
	}

	@Override
	public List<Map<String, Object>> getAllLogList() {
		
		return getSqlMapClientTemplate().queryForList("getAllLogList");
	}

	@Override
	public List<String> getLogTableColumns() {
		
		return getSqlMapClientTemplate().queryForList("getLogTableColumns");
	}

	@Override
	public void deleteLogById(Map<String, Object> map) {
		
		 getSqlMapClientTemplate().delete("deleteLogById",map);
		
	}

	@Override
	public List<Log> getOp_type() {
		return this.getSqlMapClientTemplate().queryForList("getOp_type");
	}

	@Override
	public List<Log> getOp_function() {
		return this.getSqlMapClientTemplate().queryForList("getOp_function");
	}

	@Override
	public List<Log> exportLogList(Log log) {
		return getSqlMapClientTemplate().queryForList("exportLogList", log);
	}

	@Override
	public List<Log> getLogInterval(Map map){
		return getSqlMapClientTemplate().queryForList("getLogInterval", map);
	}
	
	@Override
	public boolean delLogInterval(Map map){
		return delete("delLogInterval", map);
	}

}
