package com.kuangchi.sdd.ZigBeeConsole.task.dao.impl;




import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.ZigBeeConsole.task.dao.ITaskDao;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

/**
 * 光子锁任务管理 - dao实现类
 * @author yuman.gao
 */
@Repository("ZigBeeTaskDaoImpl")
public class TaskDaoImpl extends BaseDaoImpl<Object> implements ITaskDao{

	@Override
	public String getNameSpace() {
		return "common.ZigBeeTask";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map> getZBTaskByParamPage(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("getZBTaskByParamPage", param);
	}

	@Override
	public Integer getZBTaskByParamCount(Map<String, Object> param) {
		return (Integer) this.find("getZBTaskByParamCount", param);
	}

	@Override
	public List<Map> getZBTaskHisByParamPage(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("getZBTaskHisByParamPage", param);
	}

	@Override
	public Integer getZBTaskHisByParamCount(Map<String, Object> param) {
		return(Integer) this.find("getZBTaskHisByParamCount", param);
	}
	
	@Override
	public List<Map> getAuthorityPage(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("getAuthorityPage", param);
	}
	
	@Override
	public Integer getAuthorityPageCount(Map<String, Object> param) {
		return(Integer) this.find("getAuthorityPageCount", param);
	}

	

	
}
