package com.kuangchi.sdd.businessConsole.activiti.service;

import java.util.List;

import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;

public interface ProcessDefService {
	 public void addProcessDef(ProcessDef processDef);
	 public void deleteProcessDef(String id);
	 public List<ProcessDef> getAllProcessDefs();
	 public void deleteProcessDefByModelId(String modelId);
	 public ProcessDef getProcessDefByModelId(String modelId);
	 public List<ProcessDef> getAllProcessDefsByModelId(String modelId);
	 public ProcessDef getProcessDef(String prodefinitionId);
	 public List<ProcessDef> getUserModelsExcludeDelegated(String staffNum);
	 public ProcessDef getProcessDefByName(String name);


}
