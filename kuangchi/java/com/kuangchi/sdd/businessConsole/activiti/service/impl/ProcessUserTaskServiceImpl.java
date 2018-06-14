package com.kuangchi.sdd.businessConsole.activiti.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.businessConsole.activiti.dao.ProcessUserTaskDao;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessUserTaskService;

@Service("processUserTaskService")
public class ProcessUserTaskServiceImpl implements ProcessUserTaskService {

	@Resource(name="processUserTaskDao")
	ProcessUserTaskDao  processUserTaskDao;
	
	@Override
	public List<ProcessUserTask> getProcessUserTaskByProcessDefId(String processDefinitionId) {
		
		return processUserTaskDao.getProcessUserTaskByProcessDefId(processDefinitionId);
	}

	@Override
	public void deleteProcessUserTaskByModelId(String modelId) {
		processUserTaskDao.deleteProcessUserTaskByModelId(modelId);
		
	}

	@Override
	public void insertProcessUserTask(ProcessUserTask processUserTask) {
		processUserTaskDao.insertProcessUserTask(processUserTask);
	}

	@Override
	public ProcessUserTask getProcessUserTaskByProcessDefIdAndTaskId(String processDefinitionId,
			String taskId) {
		
		return processUserTaskDao.getProcessUserTaskByProcessDefIdAndTaskId(processDefinitionId, taskId);
	}

}
