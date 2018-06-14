package com.kuangchi.sdd.businessConsole.activiti.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.activiti.dao.ProcessDefDao;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;

@Repository("processDefDao")
public class ProcessDefDaoImpl  extends BaseDaoImpl<ProcessDef> implements ProcessDefDao{
	@Override
	public void addProcessDef(ProcessDef processDef) {
		this.getSqlMapClientTemplate().insert("addProcessDef",processDef);
	}

	@Override
	public void deleteProcessDef(String id) {
		getSqlMapClientTemplate().delete("deleteProcessDef",id);	
	}

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.Activiti";
	}

	@Override
	public String getTableName() {
		
		return null;
	}

	@Override
	public List<ProcessDef> getAllProcessDefs() {
		
		return getSqlMapClientTemplate().queryForList("getAllProcessDefs");
	}

	@Override
	public void deleteProcessDefByModelId(String modelId) {
		  getSqlMapClientTemplate().delete("deleteProcessDefByModelId",modelId);
		
	}

	@Override
	public ProcessDef getProcessDefByModelId(String modelId) {
		
		return (ProcessDef) getSqlMapClientTemplate().queryForObject("getProcessDefByModelId",modelId);
	}

	@Override
	public List<ProcessDef> getAllProcessDefsByModelId(String modelId) {
	    
		return getSqlMapClientTemplate().queryForList("getAllProcessDefsByModelId",modelId);
	}

	@Override
	public ProcessDef getProcessDef(String prodefinitionId) {
		
		return (ProcessDef) getSqlMapClientTemplate().queryForObject("getProcessDef",prodefinitionId);
	}

	@Override
	public List<ProcessDef> getUserModelsExcludeDelegated(String staffNum) {
	    java.util.Map<String, String> map=new HashMap<String, String>();
	    map.put("staffNum", staffNum);
		return getSqlMapClientTemplate().queryForList("getUserModelsExcludeDelegated",map);
	}

	@Override
	public ProcessDef getProcessDefByName(String name) {
		return (ProcessDef) getSqlMapClientTemplate().queryForObject("getProcessDefByName",name);
	}
}
