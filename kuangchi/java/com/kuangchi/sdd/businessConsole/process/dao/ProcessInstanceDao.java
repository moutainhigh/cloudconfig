package com.kuangchi.sdd.businessConsole.process.dao;

import java.util.List;
import java.util.Map;

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

public interface ProcessInstanceDao {
	List<ProcessInstanceBean> getProcessInstance(ProcessInstanceBean processInstanceBean);
	List<ProcessInstanceBean> getFinishedProcessInstance(ProcessInstanceBean processInstanceBean);
	
	List<ProcessInstanceBean> getNoCancelFinishedProcessInstance(ProcessInstanceBean processInstanceBean);
	Integer countNoCancelFinishedProcessInstance(ProcessInstanceBean processInstanceBean);
	
	Integer getProcessInstanceCount(ProcessInstanceBean processInstanceBean);
	Integer getFinishedProcessInstanceCount(ProcessInstanceBean processInstanceBean);
	
	public void insertLeaveRecord(LeaveModel leaveModel);
	
	public void insertForgetCheck(ForgetCheckModel forgetCheckModel);
	public void outApplication(OutWorkModel outWorkModel);
	
	public List<Map<String, String>> getLeaveCategoryList();
	
	List<ProcessInstanceBean> getSuspendProcessInstance(ProcessInstanceBean processInstanceBean);
	Integer getSuspendProcessInstanceCount(ProcessInstanceBean processInstanceBean);
	
	ProcessUserDelegationBean getProcessUserDelegationByModelIdAndStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	ProcessUserDelegationBean getProcessUserDelegationById(ProcessUserDelegationBean processUserDelegationBean);
	public void insertProcessUserDelegation(ProcessUserDelegationBean processUserDelegationBean);
	public void deleteProcessUserDelegation(ProcessUserDelegationBean processUserDelegationBean);
	public List<ProcessUserDelegationBean> getProcessUserDelegationListByStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	public Integer getProcessUserDelegationListCountByStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	public void deleteProcessUserDelegationByModelIdAndStaffNum(ProcessUserDelegationBean processUserDelegationBean);
	public List<String> getUnAssignedTaskIdByProcessDefinitionId(String processDefinitionId);
	public void insertTaskTransfered(TaskTransfered taskTransfered);
	
	public List<TaskTransfered> getTransmitProcessInstance(ProcessInstanceBean processInstanceBean);
	public Integer getTransmitProcessCount(ProcessInstanceBean processInstanceBean);
	public void deleteTransferedTask(TaskTransfered taskTransfered);
	
	public  ProcessStartTaskDraft getProcessStartTaskDraft(String id);
	
	public void insertProcessStartTaskDraft(ProcessStartTaskDraft processStartTaskDraft);
	public void updateProcessStartTaskDraft(ProcessStartTaskDraft processStartTaskDraft);
	public List<ProcessStartTaskDraft> getStartTaskDraft(ProcessStartTaskDraft processStartTaskDraft);
	public Integer getStartTaskDraftCount(ProcessStartTaskDraft processStartTaskDraft);
	public void deleteProcessStartTaskDraft(String id);

	public Integer selectProcessHistoryCount(ProcessHistory processHistory);
	public List<ProcessHistory> selectProcessHistoryList(ProcessHistory processHistory);
	
	public ProcessHistory selectLatestProcessHistory(String process_instance_id);
	
	public Integer deleteProcessHistory(String process_definition_id);//删除未提交流程
	
	
	public void insertProcessHistory(ProcessHistory processHistory);
	
	public void updateProcessInstanceSubmit(ProcessInstanceSubmit processInstanceSubmit);
	
	public void insertProcessInstanceSubmit(ProcessInstanceSubmit processInstanceSubmit);
	
	public ProcessInstanceSubmit getProcessInstanceSubmit(String process_instance_id);
	
	public ProcessHistory selectProcessHistory(String id);

	public ProcessVariable getProcessVariable(String process_instance_id);
	
	public void saveProcessVariable(ProcessVariable processVariable);
	
	public void updateProcessVariable(ProcessVariable processVariable);
	
	public void deleteHistoryVariable(String process_instance_id);
	
	public List<String>  getProcessVariableDeleteCache();
	
	public void deleteProcessVariableDeleteCache(String process_instance_id);
	
	public void insertProcessVariableDeleteCache(String process_instance_id);
	
}
