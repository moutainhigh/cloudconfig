package com.kuangchi.sdd.ZigBeeConsole.record.dao.impl;




import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.ZigBeeConsole.record.dao.IRecordDao;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

/**
 * 光子锁记录模块 - dao实现类
 * @author yuman.gao
 */
@Repository("ZigBeeRecordDaoImpl")
public class RecordDaoImpl extends BaseDaoImpl<Object> implements IRecordDao{

	@Override
	public String getNameSpace() {
		return "common.ZigBeeRecord";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map> getRecordByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getZigBeeRecordByParamPage", map);
	}
	
	@Override
	public Integer getRecordByParamCount(Map<String, Object> map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getZigBeeRecordByParamCount", map);
	}
	
	@Override
	public List<Map> getWarningRecordByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getWarningRecordByParamPage", map);
	}
	
	@Override
	public Integer getWarningRecordByParamCount(Map<String, Object> map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getWarningRecordByParamCount", map);
	}

	
}
