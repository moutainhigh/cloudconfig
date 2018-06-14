package com.kuangchi.sdd.businessConsole.process.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.businessConsole.process.dao.ProcessInstanceDao;
import com.kuangchi.sdd.businessConsole.process.model.ProcessHistory;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceSubmit;
import com.kuangchi.sdd.businessConsole.process.model.ProcessStartTaskDraft;
import com.kuangchi.sdd.businessConsole.process.model.ProcessUserDelegationBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessVariable;
import com.kuangchi.sdd.businessConsole.process.model.TaskTransfered;
import com.kuangchi.sdd.businessConsole.process.model.UserTaskModel;
import com.kuangchi.sdd.businessConsole.process.service.ProcessInstanceService;

@Service("processInstanceService")
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
    @Resource(name="processInstanceDao")
    ProcessInstanceDao processInstanceDao;

	@Override
	public Grid<ProcessInstanceBean> getProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		
		 Grid<ProcessInstanceBean> grid = new Grid<ProcessInstanceBean>();
	        List<ProcessInstanceBean> resultList = processInstanceDao
	             .getProcessInstance(processInstanceBean);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(processInstanceDao.getProcessInstanceCount(processInstanceBean));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
	}
	@Override
	public Grid<ProcessInstanceBean> getFinishedProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		
		 Grid<ProcessInstanceBean> grid = new Grid<ProcessInstanceBean>();
	        List<ProcessInstanceBean> resultList = processInstanceDao
	             .getFinishedProcessInstance(processInstanceBean);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(processInstanceDao.getFinishedProcessInstanceCount(processInstanceBean));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
	}
	@Override
	public Grid<ProcessInstanceBean> getSuspendProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		
		Grid<ProcessInstanceBean> grid = new Grid<ProcessInstanceBean>();
        List<ProcessInstanceBean> resultList = processInstanceDao
             .getSuspendProcessInstance(processInstanceBean);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(processInstanceDao.getSuspendProcessInstanceCount(processInstanceBean));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}
	@Override
	public ProcessUserDelegationBean getProcessUserDelegationByModelIdAndStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {
	 
		return processInstanceDao.getProcessUserDelegationByModelIdAndStaffNum(processUserDelegationBean);
	}
	@Override
	public void insertProcessUserDelegation(
			ProcessUserDelegationBean processUserDelegationBean) {
		 processInstanceDao.insertProcessUserDelegation(processUserDelegationBean);
		
	}
	@Override
	public void deleteProcessUserDelegation(
			ProcessUserDelegationBean processUserDelegationBean) {
		 processInstanceDao.deleteProcessUserDelegation(processUserDelegationBean);
		
	}
	@Override
	public Grid<ProcessUserDelegationBean> getProcessUserDelegationListByStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {

		Grid<ProcessUserDelegationBean> grid = new Grid<ProcessUserDelegationBean>();
        List<ProcessUserDelegationBean> resultList = processInstanceDao
             .getProcessUserDelegationListByStaffNum(processUserDelegationBean);
        grid.setRows(resultList);
        if (null != resultList) {
            grid.setTotal(processInstanceDao.getProcessUserDelegationListCountByStaffNum(processUserDelegationBean));
        } else {
            grid.setTotal(0);
        }
        return grid;
	}
	@Override
	public void deleteProcessUserDelegationByModelIdAndStaffNum(
			ProcessUserDelegationBean processUserDelegationBean) {
	    processInstanceDao.deleteProcessUserDelegationByModelIdAndStaffNum(processUserDelegationBean);
		
	}
	@Override
	public ProcessUserDelegationBean getProcessUserDelegationById(
			ProcessUserDelegationBean processUserDelegationBean) {
	    return processInstanceDao.getProcessUserDelegationById(processUserDelegationBean);
	}
	@Override
	public List<String> getUnAssignedTaskIdByProcessDefinitionId(
			String processDefinitionId) {
	 
		return processInstanceDao.getUnAssignedTaskIdByProcessDefinitionId(processDefinitionId);
	}
	@Override
	public void insertTaskTransfered(TaskTransfered taskTransfered) {
	   processInstanceDao.insertTaskTransfered(taskTransfered);		
	}

	@Override
	public Grid<TaskTransfered> getTransmitProcessInstance(ProcessInstanceBean processInstanceBean){
		 Grid<TaskTransfered> grid = new Grid<TaskTransfered>();
	     List<TaskTransfered> resultList = processInstanceDao.getTransmitProcessInstance(processInstanceBean);
	     grid.setRows(resultList);
	     if (null != resultList) {
	    	 grid.setTotal(processInstanceDao.getTransmitProcessCount(processInstanceBean));
	     } else {
             grid.setTotal(0);
         }
         return grid;
	}
	@Override
	public void deleteTransferedTask(TaskTransfered taskTransfered) {
		 processInstanceDao.deleteTransferedTask(taskTransfered);
		
	}
	@Override
	public ProcessStartTaskDraft getProcessStartTaskDraft(String id) {
		 
		return processInstanceDao.getProcessStartTaskDraft(id);
	}
	@Override
	public void insertProcessStartTaskDraft(
			ProcessStartTaskDraft processStartTaskDraft) {
	   processInstanceDao.insertProcessStartTaskDraft(processStartTaskDraft);
		
	}
	@Override
	public void updateProcessStartTaskDraft(
			ProcessStartTaskDraft processStartTaskDraft) {
	     processInstanceDao.updateProcessStartTaskDraft(processStartTaskDraft);
		
	}
	@Override
	public Grid<ProcessStartTaskDraft> getProcessStartTaskDraftListByStaffNum(
			ProcessStartTaskDraft processStartTaskDraft) {
		 
		 Grid<ProcessStartTaskDraft> grid = new Grid<ProcessStartTaskDraft>();
	     List<ProcessStartTaskDraft> resultList = processInstanceDao.getStartTaskDraft(processStartTaskDraft);
	     grid.setRows(resultList);
	     if (null != resultList) {
	    	 grid.setTotal(processInstanceDao.getStartTaskDraftCount(processStartTaskDraft));
	     } else {
             grid.setTotal(0);
         }
         return grid;
	}
	@Override
	public void deleteProcessStartTaskDraft(String id) {
		processInstanceDao.deleteProcessStartTaskDraft(id);
		
	}
	@Override
	public Grid<ProcessHistory>  selectProcessHistoryList(ProcessHistory processHistory) {
		 
		 Grid<ProcessHistory> grid = new Grid<ProcessHistory>();
	     List<ProcessHistory> resultList = processInstanceDao.selectProcessHistoryList(processHistory);
	     grid.setRows(resultList);
	     if (null != resultList) {
	    	 grid.setTotal(processInstanceDao.selectProcessHistoryCount(processHistory));
	     } else {
             grid.setTotal(0);
         }
         return grid;
	}
	@Override
	public ProcessHistory selectLatestProcessHistory(String process_instance_id) {
	 
		return processInstanceDao.selectLatestProcessHistory(process_instance_id);
	}
	@Override
	public void insertProcessHistory(ProcessHistory processHistory) {
		 
		processInstanceDao.insertProcessHistory(processHistory);
	}
	@Override
	public void updateProcessInstanceSubmit(
			ProcessInstanceSubmit processInstanceSubmit) {
		 processInstanceDao.updateProcessInstanceSubmit(processInstanceSubmit);
		
	}
	@Override
	public void insertProcessInstanceSubmit(
			ProcessInstanceSubmit processInstanceSubmit) {
		 processInstanceDao.insertProcessInstanceSubmit(processInstanceSubmit);
		
	}
	@Override
	public ProcessInstanceSubmit getProcessInstanceSubmit(
			String process_instance_id) {
		 
		return processInstanceDao.getProcessInstanceSubmit(process_instance_id);
	}
	@Override
	public Grid<ProcessInstanceBean> getNoCancelFinishedProcessInstance(
			ProcessInstanceBean processInstanceBean) {
		 Grid<ProcessInstanceBean> grid = new Grid<ProcessInstanceBean>();
	        List<ProcessInstanceBean> resultList = processInstanceDao.getNoCancelFinishedProcessInstance(processInstanceBean);
	        grid.setRows(resultList);
	        if (null != resultList) {
	            grid.setTotal(processInstanceDao.countNoCancelFinishedProcessInstance(processInstanceBean));
	        } else {
	            grid.setTotal(0);
	        }
	        return grid;
	}
	@Override
	public Integer deleteProcessHistory(String id) {
		return processInstanceDao.deleteProcessHistory(id);
	}
	@Override
	public ProcessHistory selectProcessHistory(String id) {
		return processInstanceDao.selectProcessHistory(id);

	}
	@Override
	public ProcessVariable getProcessVariable(String process_instance_id) {
		 
		return processInstanceDao.getProcessVariable(process_instance_id);
	}
	@Override
	public void saveProcessVariable(ProcessVariable processVariable) {
		 ProcessVariable proV=processInstanceDao.getProcessVariable(processVariable.getProcess_instance_id());
		 if (null==proV) {
			  processInstanceDao.saveProcessVariable(processVariable);
		}else{
			 processInstanceDao.updateProcessVariable(processVariable);	
		}
		
	}
	@Override
	public void deleteHistoryVariable(String process_instance_id) {
		 processInstanceDao.deleteHistoryVariable(process_instance_id);
	}
	
	@Override
	public void deleteProcessVariableDeleteCache(String process_instance_id) {
	    
		processInstanceDao.deleteProcessVariableDeleteCache(process_instance_id);
		
	}
	@Override
	public void insertProcessVariableDeleteCache(String process_instance_id) {
		 processInstanceDao.insertProcessVariableDeleteCache(process_instance_id);
		
	}
	@Override
	public List<String> getProcessVariableDeleteCache() {
		 
		return processInstanceDao.getProcessVariableDeleteCache();
	}


}
