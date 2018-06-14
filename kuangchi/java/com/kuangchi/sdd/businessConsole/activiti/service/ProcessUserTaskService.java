package com.kuangchi.sdd.businessConsole.activiti.service;

import java.util.List;

import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;

public interface ProcessUserTaskService {
	public List<ProcessUserTask> getProcessUserTaskByProcessDefId(String processDefinitionId);
	public void deleteProcessUserTaskByModelId(String modelId);
	public ProcessUserTask getProcessUserTaskByProcessDefIdAndTaskId(String processDefinitionId,String taskId);
	public void insertProcessUserTask(ProcessUserTask processUserTask);
}
