package com.kuangchi.sdd.businessConsole.activiti.dao;

import java.util.List;

import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;

public interface ProcessDefDao {
	 public void addProcessDef(ProcessDef processDef);
	 public void deleteProcessDef(String id);
	 public List<ProcessDef> getAllProcessDefs();
	 public void deleteProcessDefByModelId(String modelId);
	 public ProcessDef getProcessDefByModelId(String modelId);
	 public List<ProcessDef> getAllProcessDefsByModelId(String modelId);
	 public ProcessDef getProcessDef(String prodefinitionId);
	 public ProcessDef getProcessDefByName(String name);
	 
	 public List<ProcessDef> getUserModelsExcludeDelegated(String staffNum);

}
