package com.kuangchi.sdd.businessConsole.process.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.activiti.dao.ProcessDefDao;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;
import com.kuangchi.sdd.businessConsole.process.dao.ProcessInstanceDao;
import com.kuangchi.sdd.businessConsole.process.model.ForgetCheckModel;
import com.kuangchi.sdd.businessConsole.process.model.LeaveModel;
import com.kuangchi.sdd.businessConsole.process.model.OutWorkModel;
import com.kuangchi.sdd.businessConsole.process.model.ProcessHistory;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceSubmit;
import com.kuangchi.sdd.businessConsole.process.model.ProcessStartTaskDraft;
import com.kuangchi.sdd.businessConsole.process.model.ProcessUserDelegationBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessVariable;
import com.kuangchi.sdd.businessConsole.process.model.TaskTransfered;

@Repository("processInstanceDao")
public class ProcessInstanceDaoImpl  extends BaseDaoImpl<ProcessInstanceBean>  implements ProcessInstanceDao {

	@Override
	public List<ProcessInstanceBean> getProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		return getSqlMapClientTemplate().queryForList("getProcessInstance",processInstanceBean);
	}

	@Override
	public String getNameSpace() {		
		return "common.Activiti";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getProcessInstanceCount(
			ProcessInstanceBean processInstanceBean) {
		
		return (Integer) getSqlMapClientTemplate().queryForObject("getProcessInstanceCount",processInstanceBean);
	}

	@Override
	public List<ProcessInstanceBean> getFinishedProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		// TODO Auto-generated method stub
		return getSqlMapClientTemplate().queryForList("getFinishedProcessInstance",processInstanceBean);
	}

	@Override
	public Integer getFinishedProcessInstanceCount(
			ProcessInstanceBean processInstanceBean) {
		// TODO Auto-generated method stub
		return (Integer) getSqlMapClientTemplate().queryForObject("getFinishedProcessInstanceCount",processInstanceBean);
	}

	@Override
	public void insertLeaveRecord(LeaveModel leaveModel) {
		getSqlMapClientTemplate().insert("insertLeaveRecord",leaveModel);
		
	}

	@Override
	public List<Map<String, String>> getLeaveCategoryList() {
		 
		return getSqlMapClientTemplate().queryForList("getLeaveCategoryList");
	}

	@Override
	public void insertForgetCheck(ForgetCheckModel forgetCheckModel) {
		getSqlMapClientTemplate().insert("insertForgetCheck",forgetCheckModel);
	}

	@Override
	public void outApplication(OutWorkModel outWorkModel) {
		getSqlMapClientTemplate().insert("insertOutWorkRecord",outWorkModel);		
	}

	@Override
	public List<ProcessInstanceBean> getSuspendProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		return getSqlMapClientTemplate().queryForList("getSuspendProcessInstance",processInstanceBean);

	}

	@Override
	public Integer getSuspendProcessInstanceCount(
			ProcessInstanceBean processInstanceBean) {
	
		return (Integer) getSqlMapClientTemplate().queryForObject("getSuspendProcessInstanceCount",processInstanceBean);

	}

	@Override
	public ProcessUserDelegationBean getProcessUserDelegationByModelIdAndStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {
		
		return (ProcessUserDelegationBean) getSqlMapClientTemplate().queryForObject("getProcessUserDelegationByModelIdAndStaffNum",processUserDelegationBean);
	}

	@Override
	public void insertProcessUserDelegation(
			ProcessUserDelegationBean processUserDelegationBean) {
	      getSqlMapClientTemplate().insert("insertProcessUserDelegation" ,processUserDelegationBean);
		
	}

	@Override
	public void deleteProcessUserDelegation(
			ProcessUserDelegationBean processUserDelegationBean) {
	    getSqlMapClientTemplate().delete("deleteProcessUserDelegation",processUserDelegationBean);
	}

	@Override
	public List<ProcessUserDelegationBean> getProcessUserDelegationListByStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {
		return getSqlMapClientTemplate().queryForList("getProcessUserDelegationListByStaffNum",processUserDelegationBean);
	}

	@Override
	public Integer getProcessUserDelegationListCountByStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {
		
		return (Integer) getSqlMapClientTemplate().queryForObject("getProcessUserDelegationListCountByStaffNum",processUserDelegationBean);
	}

	@Override
	public void deleteProcessUserDelegationByModelIdAndStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {
		getSqlMapClientTemplate().delete("deleteProcessUserDelegationByModelIdAndStaffNum",processUserDelegationBean);
		
	}

	@Override
	public ProcessUserDelegationBean getProcessUserDelegationById(
			ProcessUserDelegationBean pro) {
		return (ProcessUserDelegationBean) getSqlMapClientTemplate().queryForObject("getProcessUserDelegationById",pro);

	}

	@Override
	public List<String> getUnAssignedTaskIdByProcessDefinitionId(String processDefinitionId) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("processDefinitionId", processDefinitionId);
		return getSqlMapClientTemplate().queryForList("getUnAssignedTaskIdByProcessDefinitionId",map);
	}

	@Override
	public void insertTaskTransfered(TaskTransfered taskTransfered) {	 
		getSqlMapClientTemplate().insert("insertTaskTransfered",taskTransfered);
	}
	
	@Override
	public List<TaskTransfered> getTransmitProcessInstance(ProcessInstanceBean processInstanceBean) {
		
		return getSqlMapClientTemplate().queryForList("getTransmitProcessInstance",processInstanceBean);
	}

	@Override
	public Integer getTransmitProcessCount(ProcessInstanceBean processInstanceBean) {
		
		return (Integer) getSqlMapClientTemplate().queryForObject("getTransmitProcessCount",processInstanceBean);
	}

	@Override
	public void deleteTransferedTask(TaskTransfered taskTransfered) {
		 getSqlMapClientTemplate().delete("deleteTransferedTask",taskTransfered);
		
	}

	@Override
	public ProcessStartTaskDraft getProcessStartTaskDraft(String id) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("id", id);
		return (ProcessStartTaskDraft) getSqlMapClientTemplate().queryForObject("getProcessStartTaskDraft",map);
	}

	@Override
	public void insertProcessStartTaskDraft(
			ProcessStartTaskDraft processStartTaskDraft) {
		 getSqlMapClientTemplate().insert("insertProcessStartTaskDraft",processStartTaskDraft);
		
	}

	@Override
	public void updateProcessStartTaskDraft(
			ProcessStartTaskDraft processStartTaskDraft) {
		getSqlMapClientTemplate().update("updateProcessStartTaskDraft",processStartTaskDraft);
		
	}

	@Override
	public List<ProcessStartTaskDraft> getStartTaskDraft(ProcessStartTaskDraft processStartTaskDraft) {
	
		 return getSqlMapClientTemplate().queryForList("getStartTaskDraft",processStartTaskDraft);
		
	}

	@Override
	public Integer getStartTaskDraftCount(ProcessStartTaskDraft processStartTaskDraft) {
	
		return (Integer) getSqlMapClientTemplate().queryForObject("getStartTaskDraftCount",processStartTaskDraft);
	}

	@Override
	public void deleteProcessStartTaskDraft(String id) {
		 
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", id);
		getSqlMapClientTemplate().delete("deleteProcessStartTaskDraft",map);
	}

	@Override
	public Integer selectProcessHistoryCount(ProcessHistory processHistory) {
		
		return (Integer) getSqlMapClientTemplate().queryForObject("selectProcessHistoryCount",processHistory);
	}

	@Override
	public List<ProcessHistory> selectProcessHistoryList(ProcessHistory processHistory) {
		
		return getSqlMapClientTemplate().queryForList("selectProcessHistoryList",processHistory);
	}

	@Override
	public ProcessHistory selectLatestProcessHistory(String process_instance_id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("process_instance_id", process_instance_id);
		return (ProcessHistory) getSqlMapClientTemplate().queryForObject("selectLatestProcessHistory",map);
	}

	@Override
	public void insertProcessHistory(ProcessHistory processHistory) {
		getSqlMapClientTemplate().insert("insertProcessHistory",processHistory);
		
	}

	@Override
	public void updateProcessInstanceSubmit(
			ProcessInstanceSubmit processInstanceSubmit) {
		getSqlMapClientTemplate().update("updateProcessInstanceSubmit",processInstanceSubmit); 
		
	}

	@Override
	public void insertProcessInstanceSubmit(
			ProcessInstanceSubmit processInstanceSubmit) {
	   getSqlMapClientTemplate().insert("insertProcessInstanceSubmit",processInstanceSubmit);
		
	}

	@Override
	public ProcessInstanceSubmit getProcessInstanceSubmit(String process_instance_id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("process_instance_id", process_instance_id);
	return	 (ProcessInstanceSubmit) getSqlMapClientTemplate().queryForObject("getProcessInstanceSubmit",map);
		
	}

	@Override
	public List<ProcessInstanceBean> getNoCancelFinishedProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		return getSqlMapClientTemplate().queryForList("getNoCancelFinishedProcessInstance", processInstanceBean);
	}

	@Override
	public Integer countNoCancelFinishedProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countNoCancelFinishedProcessInstance", processInstanceBean);
	}

	@Override
	public Integer deleteProcessHistory(String id) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", id);
		return (Integer)getSqlMapClientTemplate().delete("deleteProcessHistory",map);
	}

	@Override
	public ProcessHistory selectProcessHistory(String id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", id);
		return (ProcessHistory) getSqlMapClientTemplate().queryForObject("selectProcessHistory",map);
	}

	@Override
	public ProcessVariable getProcessVariable(String process_instance_id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("process_instance_id", process_instance_id);
		return (ProcessVariable) getSqlMapClientTemplate().queryForObject("getProcessVariable",map);
	}

	@Override
	public void saveProcessVariable(ProcessVariable processVariable) {
		 
		getSqlMapClientTemplate().insert("saveProcessVariable",processVariable);
	}

	@Override
	public void updateProcessVariable(ProcessVariable processVariable) {
	    getSqlMapClientTemplate().update("updateProcessVariable",processVariable);
		
	}

	@Override
	public void deleteHistoryVariable(String process_instance_id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("process_instance_id", process_instance_id);
		getSqlMapClientTemplate().delete("deleteHistoryVariable",map);
		
	}

	@Override
	public List<String> getProcessVariableDeleteCache() {
		 
		return getSqlMapClientTemplate().queryForList("getProcessVariableDeleteCache");
	}

	@Override
	public void deleteProcessVariableDeleteCache(String process_instance_id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("process_instance_id", process_instance_id);
		 getSqlMapClientTemplate().delete("deleteProcessVariableDeleteCache",map);
		
	}

	@Override
	public void insertProcessVariableDeleteCache(String process_instance_id) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("process_instance_id", process_instance_id);
		getSqlMapClientTemplate().insert("insertProcessVariableDeleteCache",map);
	}

	
	
	

}
