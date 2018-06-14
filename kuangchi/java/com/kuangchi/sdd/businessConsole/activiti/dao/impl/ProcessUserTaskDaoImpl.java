package com.kuangchi.sdd.businessConsole.activiti.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.activiti.dao.ProcessUserTaskDao;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;

@Repository("processUserTaskDao")
public class ProcessUserTaskDaoImpl extends BaseDaoImpl<ProcessUserTask>  implements ProcessUserTaskDao{

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return "common.Activiti";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessUserTask> getProcessUserTaskByProcessDefId(String processDefinitionId) {
		
		return this.getSqlMapClientTemplate().queryForList("getProcessUserTaskByProcessDefId",processDefinitionId);
	}

	@Override
	public void deleteProcessUserTaskByModelId(String modelId) {
		 this.getSqlMapClientTemplate().delete("deleteProcessUserTaskByModelId",modelId);
		
	}

	@Override
	public void insertProcessUserTask(ProcessUserTask processUserTask) {
		
		this.getSqlMapClientTemplate().insert("insertProcessUserTask",processUserTask);
		
	}

	@Override
	public ProcessUserTask  getProcessUserTaskByProcessDefIdAndTaskId(String processDefinitionId,
			String taskId) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("processDefinitionId", processDefinitionId);
		map.put("taskId", taskId);
		return (ProcessUserTask) this.getSqlMapClientTemplate().queryForObject("getProcessUserTaskByProcessDefIdAndTaskId",map);
		
	}

}
