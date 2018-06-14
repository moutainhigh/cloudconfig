package com.kuangchi.sdd.businessConsole.activiti.dao;

import java.util.List;

import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;

public interface ProcessUserTaskDao {
	
	public List<ProcessUserTask> getProcessUserTaskByProcessDefId(String processDefinitionId);
	public void deleteProcessUserTaskByModelId(String modelId);
	public ProcessUserTask getProcessUserTaskByProcessDefIdAndTaskId(String processDefinitionId,String taskId);
	public void insertProcessUserTask(ProcessUserTask processUserTask);
}
