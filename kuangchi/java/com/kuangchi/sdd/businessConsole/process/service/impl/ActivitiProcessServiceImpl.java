package com.kuangchi.sdd.businessConsole.process.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.attendanceConsole.attend.dao.IAttendHandleDao;
import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendHandleService;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessUserTaskService;
import com.kuangchi.sdd.businessConsole.process.dao.ProcessInstanceDao;
import com.kuangchi.sdd.businessConsole.process.model.ForgetCheckModel;
import com.kuangchi.sdd.businessConsole.process.model.LeaveModel;
import com.kuangchi.sdd.businessConsole.process.model.OutWorkModel;
import com.kuangchi.sdd.businessConsole.process.model.ProcessUserDelegationBean;
import com.kuangchi.sdd.businessConsole.process.model.TaskTransfered;
import com.kuangchi.sdd.businessConsole.process.service.ActivitiProcessService;
import com.kuangchi.sdd.businessConsole.process.service.ProcessInstanceService;
import com.kuangchi.sdd.businessConsole.test.service.TestService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;



/*
 * 
 * 该类是activiti  serviceTask调用的类
 * 
 * */
@Service("activitiProcessService")
public class ActivitiProcessServiceImpl  implements ActivitiProcessService{
     
	public static final Logger LOG = Logger.getLogger(ActivitiProcessServiceImpl.class);
	
	@Resource(name = "processEngine")
	ProcessEngine processEngine;

	@Resource(name = "repositoryService")
	RepositoryService repositoryService;

	@Resource(name = "runtimeService")
	RuntimeService runtimeService;

	@Resource(name = "taskService")
	TaskService taskService;

	@Resource(name = "historyService")
	HistoryService historyService;

	@Resource(name = "managementService")
	ManagementService managementService;

	@Resource(name = "identityService")
	IdentityService identityService;
	
	@Resource(name="processInstanceDao")
	ProcessInstanceDao processInstanceDao;
	
	@Resource(name="statisticService")
	StatisticService statisticService;
	
	@Resource(name = "attendHandleServiceImpl")
	private IAttendHandleService attendanceService;
	
	@Resource(name="attendHandleDaoImpl")
	IAttendHandleDao attendHandleDao;
	
	@Autowired
	TestService testService;
	private String addedTime = " 23:59:59";

	@Resource (name="processUserTaskService")
	ProcessUserTaskService processUserTaskService;
	
	@Resource(name="processInstanceService")
	ProcessInstanceService processInstanceService;
	
	//获取日期  yyyy-MM-dd HH:mm
	public static Date getSimpleDate(String dateString){
		try {
	        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 请假申请调用的方法
	 */
	@Override
	public String insertLeaveRecord(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	     String startUserId=historicProcessInstance.getStartUserId();
		 List<HistoricVariableInstance>  historicVariables= historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		 Map<String,String> map=new HashMap<String, String>();
		 map.put("staffNum", startUserId);
         for(HistoricVariableInstance hv:historicVariables){
        	 map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
         }
         String staffNum=map.get("staffNum");
         Date startDate=getSimpleDate(map.get("startDate"));
         Date endDate=getSimpleDate(map.get("endDate"));
           LeaveModel leaveModel=new LeaveModel();
           leaveModel.setStaffNum(staffNum);
           leaveModel.setProcessInstanceId(processInstanceId);
           leaveModel.setFromTime(DateUtil.getDateString(startDate));
           leaveModel.setToTime(DateUtil.getDateString(endDate));
           leaveModel.setReason(map.get("reason"));
           leaveModel.setLeaveCategory(map.get("leaveCategory"));
           attendanceService.toDelExceptionAfterApply(staffNum,leaveModel.getFromTime(),leaveModel.getToTime());
           processInstanceDao.insertLeaveRecord(leaveModel);
           statisticService.reStatisticByStaff(startDate, endDate, staffNum);
           processInstanceService.insertProcessVariableDeleteCache(processInstanceId);
		return null;
	}

	/**
	 * 外出申请调用的方法
	 */
	@Override
	public String outApplication(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	     String startUserId=historicProcessInstance.getStartUserId();
		 List<HistoricVariableInstance>  historicVariables= historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		 Map<String,String> map=new HashMap<String, String>();
		 map.put("staffNum", startUserId);
       for(HistoricVariableInstance hv:historicVariables){
      	 map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
       }
       
       OutWorkModel outWorkModel=new OutWorkModel();
       String staffNum=map.get("staffNum");
       Date startDate=getSimpleDate(map.get("startDate"));
       Date endDate=getSimpleDate(map.get("endDate"));
       outWorkModel.setStaffNum(staffNum);
       outWorkModel.setProcessInstanceId(processInstanceId);
       outWorkModel.setFromTime(DateUtil.getDateString(startDate));
       outWorkModel.setToTime(DateUtil.getDateString(endDate));//2016-07-08 15:03:00
       outWorkModel.setReason(map.get("reason"));
       outWorkModel.setDestination(map.get("destination"));//目的地
       outWorkModel.setContactName(map.get("contact_name"));//接洽人姓名
       outWorkModel.setContactPhone(map.get("contact_phone"));//接洽人电话
       outWorkModel.setTransportCosts(Double.valueOf(map.get("transport_costs")));//预计交通费
       outWorkModel.setEatLiveCosts(Double.valueOf(map.get("eat_live_costs")));// 预计餐费（住宿费用
       outWorkModel.setTransport(map.get("transport")); //乘坐的交通工具
       attendanceService.toDelExceptionAfterApply(staffNum, outWorkModel.getFromTime(),outWorkModel.getToTime());
       processInstanceDao.outApplication(outWorkModel);
       statisticService.reStatisticByStaff(startDate, endDate, staffNum);
       processInstanceService.insertProcessVariableDeleteCache(processInstanceId);

		return null;
	}
	
	/**
	 * 忘打卡记录
	 */
	@Override
	public String insertForgetCheck(String processInstanceId){
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String startUserId = historicProcessInstance.getStartUserId();
		List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		Map<String, String> map = new HashMap<String, String>();
		map.put("staffNum", startUserId);
		for(HistoricVariableInstance hv : historicVariables){
			map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
		}
		String staffNum=map.get("staffNum");
		String startDateTime=map.get("time");
		String endDateTime=map.get("time");
		String forgetPoint=map.get("forgetPoint");
		String forgetPoint_elastic=map.get("forgetPoint_elastic");
		String elasticFlag=map.get("isElasticFlag");
		if ("1".equals(elasticFlag)) {
			forgetPoint=forgetPoint_elastic;
		}
	    Date startDate=DateUtil.getDate(startDateTime);
	    Date endDate=DateUtil.getDate(endDateTime);  //忘记打卡的开始和结束是同一天
		ForgetCheckModel forgetCheckModel = new ForgetCheckModel();
		forgetCheckModel.setStaffNum(staffNum);
		forgetCheckModel.setReason(map.get("reason"));
		forgetCheckModel.setTime(startDateTime); //日期格式：yyyy-MM-dd
		
		forgetCheckModel.setProcessInstanceId(processInstanceId);
		forgetCheckModel.setForgetPoint(forgetPoint);
		processInstanceDao.insertForgetCheck(forgetCheckModel);
		
		AttendModel attendModel=new AttendModel();
		attendModel.setStaff_num(staffNum);
		attendModel.setChecktime(startDateTime+" "+forgetPoint+":00");
		attendModel.setFrom_time(startDateTime);
		attendModel.setTo_time(endDateTime+addedTime);
		attendModel.setFlag_status("1");
		attendHandleDao.addAttendInfo(attendModel);
		
		statisticService.reStatisticByStaff(startDate, endDate, staffNum);
        processInstanceService.insertProcessVariableDeleteCache(processInstanceId);

		//添加忘打卡申请后，同时删除相应的考勤异常数据
		try {
			Duty duty = attendanceService.getDutyInfo(attendModel);
			String isElastic=duty.getIs_elastic();
			String makeUpTimeStr=startDateTime+" "+forgetPoint+":00";//2016-05-20 12:00:00
			
			if("0".equals(isElastic)){//坐班
				attendanceService.toDelExcepAfterMakeUp(staffNum,duty,startDateTime,makeUpTimeStr,false);
			}else{//弹性班
				attendanceService.toDelExcepAfterMakeUp(staffNum,duty,startDateTime,makeUpTimeStr,true);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Map<String, String>> getLeaveCategoryList() {
		return processInstanceDao.getLeaveCategoryList();
	}

	
	
	public void recordTransfered(String processInstanceId){
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
		List<ActivityImpl> activitiList = def.getActivities(); //rs是指RepositoryService的实例
		
		//获取当前执行对象，即当前执行到哪一步
		List<Execution> executionList= runtimeService.createExecutionQuery().processInstanceId(historicProcessInstance.getId()).list();
		//由于我们流程图我们定义了当前serviceTask下一节点只有一个分支，所以我们取一个
		Execution execution=executionList.get(0);
	    String activitiId =execution.getActivityId();
		
		//下一个任务的定义键
		String nextTaskDefinitionKey=null;
		for(ActivityImpl activityImpl:activitiList){
				String id = activityImpl.getId();
				if(activitiId.equals(id)){
					LOG.info("当前任务："+activityImpl.getProperty("name")); //输出某个节点的某种属性
					List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();//获取从某个节点出来的所有线路
				for(PvmTransition tr:outTransitions){
					PvmActivity ac = tr.getDestination(); //获取线路的终点节点
					nextTaskDefinitionKey=ac.getId();
				}
				break;
				}
			}

		//获取我们当前的过程定义，这个存在自定义表中,这个定义定义了任务是逐级审批还是指定办理人，指定办理人是谁等
	    ProcessUserTask processUserTask=processUserTaskService.getProcessUserTaskByProcessDefIdAndTaskId(def.getId(), nextTaskDefinitionKey);
	
	    String shouldUserId=	(String) runtimeService.getVariable(execution.getId(), "shouldUserId");
		String userId=(String) runtimeService.getVariable(execution.getId(), "userId");
		LOG.info("shouldUserId:"+shouldUserId+"  ,   userId:"+userId );
		
		
		 //如果下一个任务是指定办理人的的任务
		   if("1".equals(processUserTask.getIsDesignated())){
			   ProcessUserDelegationBean proUserDelegationBean=new ProcessUserDelegationBean();
		       proUserDelegationBean.setStaff_num(processUserTask.getAssigneeDm()); 
		       proUserDelegationBean.setModel_Id(processUserTask.getModelId());
		       //查询出指定办理人是否有转交
		       ProcessUserDelegationBean processUserDelegationBean=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);  
			   
		       //如果不等于null，则说明下一步会转交到代理人
			   if (processUserDelegationBean!=null) {
				   TaskTransfered taskTransfered=new TaskTransfered();
				   taskTransfered.setProcess_definition_id(def.getId());
				   taskTransfered.setProcess_instance_id(historicProcessInstance.getId());
				   taskTransfered.setStaff_num(processUserTask.getAssigneeDm());
				   taskTransfered.setTask_definition_key(nextTaskDefinitionKey);
				   taskTransfered.setTransfered_staff_num(processUserDelegationBean.getDelegator());
				   processInstanceService.deleteTransferedTask(taskTransfered);
				   processInstanceService.insertTaskTransfered(taskTransfered);
			   }
			   
		   }else{
			   //如果处理人和应该处理人不一样，则说明是代理人在处理
			   if (!userId.equals(shouldUserId)) {
				   TaskTransfered taskTransfered=new TaskTransfered();
				   taskTransfered.setProcess_definition_id(def.getId());
				   taskTransfered.setProcess_instance_id(historicProcessInstance.getId());
				   taskTransfered.setStaff_num(shouldUserId);
				   taskTransfered.setTask_definition_key(nextTaskDefinitionKey);
				   taskTransfered.setTransfered_staff_num(userId);
				   processInstanceService.deleteTransferedTask(taskTransfered);
				   processInstanceService.insertTaskTransfered(taskTransfered);
		     	} 
		   }
		
	}
	
	
	public void recordOverTimeApplication(String processInstanceId){
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String startUserId = historicProcessInstance.getStartUserId();
		List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		Map<String, String> map = new HashMap<String, String>();
		map.put("staffNum", startUserId);
		for(HistoricVariableInstance hv : historicVariables){
			map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
		}
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String staffNum=map.get("staffNum");
		String otBegin=map.get("otBegin");
		String otEnd=map.get("otEnd");
		String otTime=map.get("otTime");
		String otTimeCal=map.get("otTimeCal");
		String otType=map.get("otType");
		String otReason=map.get("otReason");
		String otApplyDate=sdf.format(d);
		
		Map oTMap=new HashMap();
		
		oTMap.put("staffNum", staffNum);
		oTMap.put("otBegin", otBegin);
		oTMap.put("otEnd", otEnd);
		oTMap.put("otTime", otTime);
		oTMap.put("otType", otType);
		oTMap.put("otReason", otReason);
		oTMap.put("otApplyDate", otApplyDate);
		oTMap.put("processInstanceId", processInstanceId);
		oTMap.put("otTimeCal", Integer.parseInt(otTimeCal)*1.0/60/1000/60.0);
		
		Date startDate=DateUtil.getDate(otBegin);
		Date endDate=DateUtil.getDate(otEnd);
//		try{
			statisticService.addStaffOt(oTMap);
			statisticService.reStatisticByStaff(startDate, endDate, staffNum);
			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	           processInstanceService.insertProcessVariableDeleteCache(processInstanceId);

	}
	
	/**
		销假申请通过审批，后删除请假记录，重新统计考勤
		by gengji.yang
	 */
	public void cancelLeaveTimeRecord(String processInstanceId){
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String startUserId = historicProcessInstance.getStartUserId();
		List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		Map<String, String> map = new HashMap<String, String>();
		map.put("staffNum", startUserId);
		for(HistoricVariableInstance hv : historicVariables){
			map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
		}
		Date d=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String oldId=map.get("oldId");
		String staffNum=map.get("staffNum");
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String leaveCategory=map.get("leaveCategory");
		String reason=map.get("reason");
		String createTime=sdf.format(d);
		
		Map oTMap=new HashMap();
		
		oTMap.put("staffNum", staffNum);
		oTMap.put("fromTime", startDate);
		oTMap.put("toTime", endDate);
		oTMap.put("leaveCategory", leaveCategory);
		oTMap.put("reason", reason);
		oTMap.put("createTime", createTime);
		oTMap.put("processInstanceId", processInstanceId);
		oTMap.put("oldId", oldId);
		
		
		Date startDate1=DateUtil.getDate(startDate);
		Date endDate1=DateUtil.getDate(endDate);
		//try{
			statisticService.delStaffLeaveRecord(oldId);
			statisticService.insertCancelRecord(oTMap);
			statisticService.reStatisticByStaff(startDate1, endDate1, staffNum);
	           processInstanceService.insertProcessVariableDeleteCache(processInstanceId);

//		}catch(Exception e){
//			e.printStackTrace();
//		}
	
	}
	
}
