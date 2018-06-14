package com.kuangchi.sdd.businessConsole.activiti.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.businessConsole.activiti.dao.ProcessDefDao;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessDefService;

@Service("processDefService")
public class ProcessDefServiceImpl implements ProcessDefService {

	
	@Resource(name="processDefDao")
	ProcessDefDao processDefDao;
	@Override
	public void addProcessDef(ProcessDef processDef) {
		 processDefDao.addProcessDef(processDef);
		
	}

	@Override
	public void deleteProcessDef(String id) {
		processDefDao.deleteProcessDef(id);
		
	}

	@Override
	public List<ProcessDef> getAllProcessDefs() {
	   return     processDefDao.getAllProcessDefs();
	}

	@Override
	public void deleteProcessDefByModelId(String modelId) {
		processDefDao.deleteProcessDefByModelId(modelId);
		
	}

	@Override
	public ProcessDef getProcessDefByModelId(String modelId) {
		return processDefDao.getProcessDefByModelId(modelId);
	}

	@Override
	public List<ProcessDef> getAllProcessDefsByModelId(String modelId) {
 		return processDefDao.getAllProcessDefsByModelId(modelId);
	}

	@Override
	public ProcessDef getProcessDef(String prodefinitionId) {
		return processDefDao.getProcessDef(prodefinitionId);
	}

	@Override
	public List<ProcessDef> getUserModelsExcludeDelegated(String staffNum) {
	
		return processDefDao.getUserModelsExcludeDelegated(staffNum);
	}

	@Override
	public ProcessDef getProcessDefByName(String name) {
	 
		return processDefDao.getProcessDefByName(name);
	}

}
