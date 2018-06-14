package com.kuangchi.sdd.businessConsole.process.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.process.model.ProcessHistory;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceSubmit;
import com.kuangchi.sdd.businessConsole.process.model.ProcessStartTaskDraft;
import com.kuangchi.sdd.businessConsole.process.model.ProcessUserDelegationBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessVariable;
import com.kuangchi.sdd.businessConsole.process.model.TaskTransfered;


public interface ProcessInstanceService {
	Grid<ProcessInstanceBean> getProcessInstance(ProcessInstanceBean processInstanceBean);
	Grid<ProcessInstanceBean> getFinishedProcessInstance(ProcessInstanceBean processInstanceBean);
	Grid<ProcessInstanceBean> getNoCancelFinishedProcessInstance(ProcessInstanceBean processInstanceBean);
	Grid<ProcessInstanceBean> getSuspendProcessInstance(ProcessInstanceBean processInstanceBean);
	ProcessUserDelegationBean getProcessUserDelegationByModelIdAndStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	public void insertProcessUserDelegation(ProcessUserDelegationBean processUserDelegationBean);
	public void deleteProcessUserDelegation(ProcessUserDelegationBean processUserDelegationBean);
	public Grid<ProcessUserDelegationBean> getProcessUserDelegationListByStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	public void deleteProcessUserDelegationByModelIdAndStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	ProcessUserDelegationBean getProcessUserDelegationById(ProcessUserDelegationBean processUserDelegationBean);
	public List<String> getUnAssignedTaskIdByProcessDefinitionId(String processDefinitionId);
	public void insertTaskTransfered(TaskTransfered taskTransfered);
	public Grid<TaskTransfered> getTransmitProcessInstance(ProcessInstanceBean processInstanceBean);
	public void deleteTransferedTask(TaskTransfered taskTransfered);

    public  ProcessStartTaskDraft getProcessStartTaskDraft(String id);
	
	public void insertProcessStartTaskDraft(ProcessStartTaskDraft processStartTaskDraft);
	public void updateProcessStartTaskDraft(ProcessStartTaskDraft processStartTaskDraft);
	public Grid<ProcessStartTaskDraft> getProcessStartTaskDraftListByStaffNum(ProcessStartTaskDraft processStartTaskDraft);
	public void deleteProcessStartTaskDraft(String id);
	
	
	public Grid<ProcessHistory> selectProcessHistoryList(ProcessHistory processHistory);
	
	public ProcessHistory selectLatestProcessHistory(String process_instance_id);
	
	public Integer deleteProcessHistory(String process_definition_id);//删除未提交流程
	public void insertProcessHistory(ProcessHistory processHistory);
	
	public void updateProcessInstanceSubmit(ProcessInstanceSubmit processInstanceSubmit);
	
	public void insertProcessInstanceSubmit(ProcessInstanceSubmit processInstanceSubmit);
	
	public ProcessInstanceSubmit getProcessInstanceSubmit(String process_instance_id);

	
	public ProcessHistory selectProcessHistory(String id);
    public ProcessVariable getProcessVariable(String process_instance_id);
	
	public void saveProcessVariable(ProcessVariable processVariable);
	
	public List<String>  getProcessVariableDeleteCache();

	public void deleteHistoryVariable(String process_instance_id);
	public void deleteProcessVariableDeleteCache(String process_instance_id);
	public void insertProcessVariableDeleteCache(String process_instance_id);

}
