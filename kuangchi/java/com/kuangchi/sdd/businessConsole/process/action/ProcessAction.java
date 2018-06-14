package com.kuangchi.sdd.businessConsole.process.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.apache.struts2.json.JSONUtil;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDefinitionBean;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;
import com.kuangchi.sdd.businessConsole.activiti.model.Template;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessDefService;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessUserTaskService;
import com.kuangchi.sdd.businessConsole.activiti.service.TemplateService;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.process.model.JsonResults;
import com.kuangchi.sdd.businessConsole.process.model.ProcessHistory;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessInstanceSubmit;
import com.kuangchi.sdd.businessConsole.process.model.ProcessStartTaskDraft;
import com.kuangchi.sdd.businessConsole.process.model.ProcessUserDelegationBean;
import com.kuangchi.sdd.businessConsole.process.model.ProcessVariable;
import com.kuangchi.sdd.businessConsole.process.model.TaskTransfered;
import com.kuangchi.sdd.businessConsole.process.model.UserTaskModel;
import com.kuangchi.sdd.businessConsole.process.service.ProcessInstanceService;
import com.kuangchi.sdd.businessConsole.process.service.UserTaskService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;


@Controller("processAction")
public class ProcessAction extends BaseActionSupport  {
	
	public static final Logger LOG = Logger.getLogger(ProcessAction.class);
	
	public final static String processRootId="1";
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

	@Resource(name = "objectMapper")
	ObjectMapper objectMapper;

	
	@Resource(name = "employeeService")
	EmployeeService employeeService;
	
    @Resource(name = "departmentServiceImpl")
    private IDepartmentService departmentService;
	
    
	@Resource(name="templateService")
	TemplateService templateService;
    
    
	@Resource(name="processDefService")
	ProcessDefService processDefService;
    
	@Resource (name="processUserTaskService")
	ProcessUserTaskService processUserTaskService;
	
	
	@Resource(name="processInstanceService")
	ProcessInstanceService processInstanceService;
	
	@Resource(name="userTaskService")
	UserTaskService userTaskService;
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-24 上午10:29:43
	 * @功能描述:获取全部流程总数接口 
	 * @参数描述:
	 */
	public void getFlowCounts(){
		  HttpServletRequest request=getHttpServletRequest();
		  JsonResults result = new JsonResults(); 
		  ProcessInstanceBean processInstanceBeanPage  = new ProcessInstanceBean(); // 将数据转化为javabean
		  ProcessStartTaskDraft processStartTaskDraft  = new ProcessStartTaskDraft(); // 将数据转化为javabean
		  UserTaskModel userTaskPage  = new UserTaskModel(); // 将数据转化为javabean
		  Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		  String yhDm=staff.getStaff_num();
		  userTaskPage.setYhDm(yhDm); 
		  processStartTaskDraft.setStaff_num(yhDm);
		  processInstanceBeanPage.setYhDm(yhDm);
			int page = 1;
			int rows = 10;
			if (null != request.getParameter("page")) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			if (null != request.getParameter("rows")) {
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			processInstanceBeanPage.setRows(rows);
			processInstanceBeanPage.setPage(page);
			userTaskPage.setRows(rows);
			userTaskPage.setPage(page);
			processStartTaskDraft.setPage(page);
			processStartTaskDraft.setRows(rows);
		  
			try{
				Integer userTaskCounts = userTaskService.getUserTask(userTaskPage).getTotal();//获取待办任务总数
				Integer finishCounts =	processInstanceService.getFinishedProcessInstance(processInstanceBeanPage).getTotal();//获取已完成的申请总数
				Integer suspendCounts = processInstanceService.getSuspendProcessInstance(processInstanceBeanPage).getTotal();//已挂起的流程总数
				Integer processCounts = processInstanceService.getProcessInstance(processInstanceBeanPage).getTotal();//执行中的流程总数
				Integer transmitProcessCounts = processInstanceService.getTransmitProcessInstance(processInstanceBeanPage).getTotal();//获取已转交的流程总数
				Integer processStartTaskCounts = processInstanceService.getProcessStartTaskDraftListByStaffNum(processStartTaskDraft).getTotal();//未提交流程总数
				Integer myAllCounts = finishCounts+suspendCounts+processCounts+transmitProcessCounts+processStartTaskCounts;//我的流程总数
				result.setSuccess(true);
				result.setMsg("获取成功");
				result.setUserTaskCounts(userTaskCounts);
				result.setFinishCounts(finishCounts);
				result.setSuspendCounts(suspendCounts);
				result.setProcessCounts(processCounts);
				result.setTransmitProcessCounts(transmitProcessCounts);
				result.setMyAllCounts(myAllCounts);
				result.setProcessStartTaskCounts(processStartTaskCounts);
			}catch(Exception e){
				result.setMsg("获取失败");
				result.setSuccess(false);
				e.printStackTrace();
			}
		  printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * 部署的流程定义列表
	 */

	/**流程维护-->>流程列表
	 * 
	 */
	public void deployedProcessDefinitionList() {
		List<ProcessDef> list=null;

		list  = processDefService.getAllProcessDefs();
	    
		List<Tree> resultList = new ArrayList<Tree>();
		for (int i = 0; i < list.size(); i++) {
			Tree tree=new Tree();
			tree.setId(list.get(i).getProdefinitionId());
			tree.setText(list.get(i).getName());
			tree.setChecked(false);
	        tree.setIconCls("icon-script");
	        tree.setState("open");	       
			resultList.add(tree);
		}
//根节点id设为1,这个是定死的
	       Tree tree =new Tree();
	        tree.setText("工作流");
	        tree.setChecked(true);
	        tree.setId(processRootId);
	        tree.setState("open");
	        tree.setIconCls("icon-flow");
	        tree.setChildren(resultList);
	        StringBuilder builder = new StringBuilder();

	        builder.append("[");
	        builder.append(new Gson().toJson(tree));
	        builder.append("]");
	        printHttpServletResponse(builder.toString());
		
	}	
	
	
	/**
	 * 获得所有 的Model
	 */

	/**
	 * 
	 */
	public void deployedModelList() {
		List<ProcessDef> list=null;
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String yhDm=staff.getStaff_num();
		list  = processDefService.getUserModelsExcludeDelegated(yhDm);
	    
		List<Tree> resultList = new ArrayList<Tree>();
		for (int i = 0; i < list.size(); i++) {
			Tree tree=new Tree();
			tree.setId(list.get(i).getModelId());
			tree.setText(list.get(i).getName());
			tree.setChecked(false);
	        tree.setIconCls("icon-script");
	        tree.setState("open");	       
			resultList.add(tree);
		}
//根节点id设为1,这个是定死的
	       Tree tree =new Tree();
	        tree.setText("工作流");
	        tree.setChecked(true);
	        tree.setId(processRootId);
	        tree.setState("open");
	        tree.setIconCls("icon-flow");
	        tree.setChildren(resultList);
	        StringBuilder builder = new StringBuilder();

	        builder.append("[");
	        builder.append(new Gson().toJson(tree));
	        builder.append("]");
	        printHttpServletResponse(builder.toString());
		
	}	
	
	/**
	 * 获取流程的总体图片
	 */
	public void getProcessImg(){
		HttpServletRequest request=getHttpServletRequest();
		String processDefinitionId = request.getParameter("id");
		if (processRootId.equals(processDefinitionId)) {
			return;
		}
		BpmnModel bpmnModel = repositoryService
				.getBpmnModel(processDefinitionId);
		InputStream imageStream =processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "png",  processEngine.getProcessEngineConfiguration().getActivityFontName(),
                processEngine.getProcessEngineConfiguration().getLabelFontName(), processEngine.getProcessEngineConfiguration().getClassLoader(),1.0);
		HttpServletResponse response=getHttpServletResponse();
		java.io.OutputStream out=null;
		try {
			 out = response.getOutputStream();
			byte[] bytes=new byte[1024];
			while (imageStream.read(bytes, 0, 1024)!=-1) {
				 out.write(bytes);			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageStream!=null) {
				try {
					imageStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * 获取流程执行状态图片，当前执行点高亮
	 */
	public void getProcessTaskImg(){
		HttpServletRequest request=getHttpServletRequest();
		String processInstanceId = request.getParameter("id");
		HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
       BpmnModel bpmnModel=repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
       	List<String> activitIds=runtimeService.getActiveActivityIds(processInstanceId);  	
		InputStream imageStream =processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activitIds, new ArrayList<String>(), processEngine.getProcessEngineConfiguration().getActivityFontName(),
             processEngine.getProcessEngineConfiguration().getLabelFontName(), processEngine.getProcessEngineConfiguration().getClassLoader(),1.0);	
		HttpServletResponse response=getHttpServletResponse();
		java.io.OutputStream out=null;
		try {
			 out = response.getOutputStream();
			byte[] bytes=new byte[1024];
			while (imageStream.read(bytes, 0, 1024)!=-1) {
				 out.write(bytes);			
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (imageStream!=null) {
				try {
					imageStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	/**
	 * 获取流程的详情
	 */
	
	public void processDefinitionDetail(){
		HttpServletRequest request=getHttpServletRequest();
		String processDefinitionId = request.getParameter("id");
		ProcessDefinitionBean processDefinitionBean=new ProcessDefinitionBean();
		//工作流菜单根目录不是流程,因此在此给一些必要显示信息
		if (processRootId.equals(processDefinitionId)) {
	        processDefinitionBean.setId(processRootId);
	        processDefinitionBean.setKey("无");
	        processDefinitionBean.setName("工作流根目录");
	        processDefinitionBean.setResourceName("无");
	        processDefinitionBean.setVersion(0);
	        processDefinitionBean.setDeploymentId("无");
	        processDefinitionBean.setDescription("工作流根目录");
		}else{
		ProcessDefinition processDefinition=repositoryService.getProcessDefinition(processDefinitionId);        
        processDefinitionBean.setId(processDefinition.getId());
        processDefinitionBean.setKey(processDefinition.getKey());
        processDefinitionBean.setName(processDefinition.getName());
        processDefinitionBean.setResourceName(processDefinition.getResourceName());
        processDefinitionBean.setVersion(processDefinition.getVersion());
        processDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
        processDefinitionBean.setDescription(processDefinition.getDescription());
		}
        printHttpServletResponse(GsonUtil.toJson(processDefinitionBean));
	}
	
	
	/**
	 * 删除部署的流程
	 */
	public void deleteDeployedProcess(){
		HttpServletRequest request=getHttpServletRequest();
		String processDefinitionId = request.getParameter("id");
		 JsonResult result = new JsonResult();
			try {
				//删除部署的流程，同一个模板点击部署按钮两次，则会产生两条流程定义记录，其中最近的一次部署的记录为当前流程义，其它的为历史流程定义
				//如果有历史流程实例未完成模板又被重新部署时，新启动的流程实例按新的流程定义，历史未完成的流程实例照样 按历史的流程定义执行，相互不影响
				//删除流程定义不是delete,而是update表中的标志位
				   processDefService.deleteProcessDef(processDefinitionId);
			      result.setMsg("删除成功");
			      result.setSuccess(true);
			} catch (Exception e) {
			      result.setMsg("删除失败");
			      result.setSuccess(false);
			}
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 到开始流程界面
	 */
	public String startProcess(){
		HttpServletRequest request=getHttpServletRequest();
		String processDefinitionId = request.getParameter("id");
		String draftId=request.getParameter("draftId");
	    //String yhDm=	(String) request.getSession().getAttribute("yhDm");//改
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String yhDm=staff.getStaff_num();
	    Employee employee=employeeService.getEmployeeDetail(yhDm);
	    
	    if (null!=draftId&&!"".equals(draftId)) {
	        ProcessStartTaskDraft draft=    processInstanceService.getProcessStartTaskDraft(draftId);
	        request.setAttribute("draft", draft.getProcess_variables());
	        request.setAttribute("draftId",draftId);
		} 
	    
		
		 Object startForm=processEngine.getFormService().getRenderedStartForm(processDefinitionId).toString();
		 //将申请人信息放入request中，界面上显示
		 if (null!=employee) {
			 Department  department=departmentService.getDepartmentDet(employee.getBmDm());
			 request.setAttribute("yhDm", employee.getYhDm());
			 request.setAttribute("yhNo", employee.getYhNo());
			 request.setAttribute("yhMc", employee.getYhMc());
			 request.setAttribute("bmMc", department.getBmMc());
			 request.setAttribute("bmDm", department.getBmDm());
			 request.setAttribute("bmNo", department.getBmNo());
		}
		 request.setAttribute("processForm", startForm);
		 //说明是开始事件
		 request.setAttribute("isStart", true);
		 request.setAttribute("processDefinitionId", processDefinitionId);
		return "success";
	}
	
	
	
	
	/**
	 * 到用户任务界面
	 */
	public String startUserTask(){
		HttpServletRequest request=getHttpServletRequest();
		String taskId = request.getParameter("id");
		org.activiti.engine.task.Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
		//获取用户表单的模板代码，以便在前台界面进行展示
		 Object processForm=processEngine.getFormService().getRenderedTaskForm(taskId);
		 //流程变量
		 Map<String, Object> variables= taskService.getVariables(taskId);
		 Gson gson=new Gson();
		 String variableJson=gson.toJson(variables);
		 request.setAttribute("processForm", processForm);
		 //向前面传入当前任务定义Id（和主键ID不同，流程定义Id是流程图上输入的），其作用是：界面根据该ID对比表单 中输入框的taskId属性，如果两值相同，
		 //表示输入框为当前任务需要输入的，如果不同，则表示输入框是其它任务中需要输入的,如请假申请人只能填写请假时间 ，原因等，审批输入框只有审批人才能输入
		 request.setAttribute("currentTaskId", task.getTaskDefinitionKey());
		 //任务对象主键
		 request.setAttribute("taskId", taskId);
		 request.setAttribute("isStart", true);
		 request.setAttribute("variableJson", variableJson); 
		return "success";
	}
	
	
	
	/**
	 * 到用户任务界面
	 */
	public String revokeToStartUserTask(){
		HttpServletRequest request=getHttpServletRequest();
		String task_definition_key = request.getParameter("task_definition_key");
		String process_instance_id= request.getParameter("process_instance_id");
		String process_definition_id= request.getParameter("process_definition_id");

		 ProcessDef processDef=processDefService.getProcessDef(process_definition_id);
		//获取用户表单的模板代码，以便在前台界面进行展示
		 Object processForm=templateService.getTemplateById(processDef.getModelId()).getTemplateCode();
		 
				 
		 
		 //流程变量
		 List<HistoricVariableInstance>  historicVariables= historyService.createHistoricVariableInstanceQuery().processInstanceId(process_instance_id).list();
		 Map<String,String> map=new HashMap<String, String>();
		 for(HistoricVariableInstance hv:historicVariables){
	      	 map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
	       }
		 Gson gson=new Gson();
		 String variableJson=gson.toJson(map);
		 request.setAttribute("processForm", processForm);
		 //向前面传入当前任务定义Id（和主键ID不同，流程定义Id是流程图上输入的），其作用是：界面根据该ID对比表单 中输入框的taskId属性，如果两值相同，
		 //表示输入框为当前任务需要输入的，如果不同，则表示输入框是其它任务中需要输入的,如请假申请人只能填写请假时间 ，原因等，审批输入框只有审批人才能输入
		 request.setAttribute("currentTaskId", task_definition_key);
		 //任务对象主键
		 request.setAttribute("process_instance_id", process_instance_id);
		 if("start".equals(task_definition_key)){
			 request.setAttribute("isStart", true);
		 }
		
		 request.setAttribute("variableJson", variableJson); 
		return "success";
	}
	
	
	
	public void saveUserTaskRevoke(){
		HttpServletRequest request=getHttpServletRequest();
		String process_instance_id= request.getParameter("process_instance_id");
		String data = request.getParameter("data");
		JsonResult result = new JsonResult();

		 try {
			 	ProcessInstanceSubmit processInstanceSubmit=processInstanceService.getProcessInstanceSubmit(process_instance_id);
				if (processInstanceSubmit.getLock_status()==0) {
					 result.setMsg("请先撤回流程再进行本操作");
					 result.setSuccess(false);	   
				}else{
				 	Gson gson=new Gson();
				 	Map<String, Object> map = gson.fromJson(data, new TypeToken<Map<String, Object>>() { }.getType());   
				
				 
			//获取当前执行对象，即当前执行到哪一步
					List<Execution> executionList= runtimeService.createExecutionQuery().processInstanceId(process_instance_id).list();
					//由于我们流程图我们定义了当前serviceTask下一节点只有一个分支，所以我们取一个
					Execution execution=executionList.get(0);
					runtimeService.setVariables(execution.getId(), map );
					//修改提交状态为0，表示下一个节点可以正常处理了
					processInstanceSubmit.setLock_status(0);
					processInstanceService.updateProcessInstanceSubmit(processInstanceSubmit);
					
					result.setSuccess(true);
					result.setMsg("保存成功");
				}	

		 } catch (Exception e) {
			    result.setSuccess(false);
				result.setMsg("保存失败");
				e.printStackTrace(); 
			}
		 printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
    /**
     * 获取流程变量,全局的
     */
    public void getTaskVariables() {
        String taskId = getHttpServletRequest().getParameter("id");//部门代码
		 Map<String, Object> variables= taskService.getVariables(taskId);
		 Gson gson=new Gson();
		 String variableJson=gson.toJson(variables);
        printHttpServletResponse(GsonUtil.toJson(variableJson));
    }
	
	
	/**
	 * 提交某个用户任务
	 */
    
    //代码中有转交功能，即自己如果指定了转交人，则任务会流向转交人，但不能多级转交，转交人不能把被指定的任务再转交到另一个人
	public void submitUserTask(){
		HttpServletRequest request=getHttpServletRequest();
		   Gson gson=new Gson();
			JsonResult result = new JsonResult();
			   String taskId=request.getParameter("taskId");
			   String data = request.getParameter("data");
			//登录用户信息
			   Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			   String loginUserDm=staff.getStaff_num();
			try {
				//获取登录用户的详细信息
				   Employee employee=employeeService.getEmployeeDetail(loginUserDm);  
				   
				   
				   //通过工作流内置taskService获取task对象
				    org.activiti.engine.task.Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
				   
				   //获取之前的流程变量
					 List<HistoricVariableInstance>  historicVariables= historyService.createHistoricVariableInstanceQuery().processInstanceId(task.getProcessInstanceId()).list();
					 Map<String,Object> map=new HashMap<String, Object>();
					 for(HistoricVariableInstance hv:historicVariables){
				      	 map.put(hv.getVariableName(), hv.getValue()==null?null:hv.getValue().toString());
				     }
				   
				   //从界面获取的流程变量
			       Map<String, Object> mapSubmited = gson.fromJson(data, new TypeToken<Map<String, Object>>() { }.getType()); 
			       
			       //将新的流程变量覆盖之前的流程变量
			       map.putAll(mapSubmited);
			       
			       
			      

				    //该流程定义中逐级审批的任务定义Key列表,这个列表中只包含逐级审批的任务节点定义Key,对于指定办理人的任务节点不在该列表中
					   List<String> unAssignedTaskIds=processInstanceService.getUnAssignedTaskIdByProcessDefinitionId(task.getProcessDefinitionId());
					   //已经走完了流程中的哪些任务节点,这个字符串中保存了流程执行到当前节点时之前执行过的任务定义keys, key间用“,”分隔
				       String executedTaskIds=(String)taskService.getVariable(taskId, "executedTaskIds");
                       
				    
					   
				    //任务处理人的属性键,其变量的命名规则是定死的为  : 任务定义key+"Operator",界面上通过这个变量来显示
				    String taskOperator=task.getTaskDefinitionKey()+"Operator";
				    //获取我们当前的过程定义，这个存在自定义表中,这个定义定义了任务是逐级审批还是指定办理人，指定办理人是谁等
				    ProcessUserTask processUserTask=processUserTaskService.getProcessUserTaskByProcessDefIdAndTaskId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
				    
				       //shouldUserId保存的是：如果该任务是逐级审批的话，那么当前任务未被转交时应由谁办理，如A的领导是B，A提交了流程，如果B没有指定转交人，那么下一个逐级审批任务就应该流向B
				       String shouldUserId=(String)taskService.getVariable(taskId, "shouldUserId");
				       String userId=(String)taskService.getVariable(taskId, "userId");
				       Employee shouldUser=employeeService.getEmployeeDetail(shouldUserId);

				    //如果当前任务为逐级审批，则指定userId属性，目的是让自己处理完后，流程回归到发起者的逐级审批中 ,如果流程为退回发起人的任务,则重新处理则放入userId和其它初始值
				    if ("backToStarterTask".equals(task.getTaskDefinitionKey())||!"1".equals(processUserTask.getIsDesignated())) {
				    	
				    	 //如果到当前任务这一步之后还有逐级审批的任务节点，则需要判断当前任务处理人是否有领导 ，如果没有 领导 则不能让其提交流程,否则后续任务就被提交给了null导致这个流程实例不能走完，因为没有代码为Null的用户
							       if ("backToStarterTask".equals(task.getTaskDefinitionKey())) {
							    	   //如果用户的领导为空
									   if(employee.getSjldyhDm()==null){
										   //如果当前的员工有领导，之后 的任务中还有逐级审批任务，则不能提交.
										   if (unAssignedTaskIds.size()>0) {
											   throw new com.kuangchi.sdd.base.exception.MyException("当前登录用户没有审批人，请联系管理员设置审批人后再提交");	   	
										   }
									   }
								    }else{
								    	
										   for (int i = 0; i < unAssignedTaskIds.size(); i++) {
											   //如果任务等于当前任务我们不需要验证当前用户是否有领导，否则我们需要验证当前任务节点以后是否还有逐级审批的任务，如果有则需要验证审批领导，如果没有则不管
										       if(!task.getTaskDefinitionKey().equals(unAssignedTaskIds.get(i))){
										    	   //如果逐级审批任务列表中还有未执行的任务，并且流程应当处理人也没有领导，这里为什么是shouldUser而不是staff，原因是如果当前任务被转交了，如A转交给了B，A就相当于shouldUser,那么B在处理完流程后还要将任务返回到A的领导链中去，如果A本身没有领导 ，而且后面还有逐级审批的话，流程返回到A的领导链中就走不下去了，全部到了用户Null那里
												   if (!executedTaskIds.contains(unAssignedTaskIds.get(i))&&shouldUser.getSjldyhDm()==null) {
													   //逐级审批,loginUserDm与shouldUserId不一样说明是代理人处理
													   if(!loginUserDm.equals(shouldUserId)){
														   throw new com.kuangchi.sdd.base.exception.MyException("当前用户正在处理转交任务，但转交人"+shouldUser.getYhMc()+"没有审批领导，请联系管理员为转交人设置审批领导后,代理人才能办理");														   
													   }else{
														   throw new com.kuangchi.sdd.base.exception.MyException("当前登录用户没有审批人，请联系管理员设置审批人后再提交");														   
													   }
												    }
									       }
								    }

						           }
				    	
						ProcessDef processDef=processDefService.getProcessDef(task.getProcessDefinitionId());  
						
				    	//如果是回退任务,则相当于重新从起点开始，userId,shouldUserId要重新设
					     if ("backToStarterTask".equals(task.getTaskDefinitionKey())) {
					    	 ProcessUserDelegationBean proUserDelegationBean=new ProcessUserDelegationBean();
						       proUserDelegationBean.setStaff_num(employee.getSjldyhDm());
						       proUserDelegationBean.setModel_Id(processDef.getModelId()); 
						       ProcessUserDelegationBean processUserDelegationBean=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);
						       if (null==processUserDelegationBean) {
							       //放入非指定处理用户的上级用户代码属性，以便流向其领导
							       map.put("userId", employee.getSjldyhDm());
							   }else{
								   //如果不为空，则交给其代理人处理
								   map.put("userId", processUserDelegationBean.getDelegator());
							   }
					            
						       //shouldUserId表示如果下一个任务是逐级审批的话，本应该由谁办理
						       map.put("shouldUserId", employee.getSjldyhDm());
						       


					    	  
						 }else{
							if (null!=shouldUser) {
							       ProcessUserDelegationBean proUserDelegationBean=new ProcessUserDelegationBean();
							       proUserDelegationBean.setStaff_num(shouldUser.getSjldyhDm()); 
							       proUserDelegationBean.setModel_Id(processDef.getModelId());
							       ProcessUserDelegationBean processUserDelegationBean=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);    	
							       //如果当前任务没有 被转交
							       if (null==processUserDelegationBean) {
								       //放入非指定处理用户的上级用户代码属性，以便流向其领导
									       map.put("userId", shouldUser.getSjldyhDm());
								   }else{
									   //如果不为空，则交给其代理人处理
									   map.put("userId", processUserDelegationBean.getDelegator());
								   }

							       
							   }	
							map.put("shouldUserId", shouldUser.getSjldyhDm());
						}
					       
					}
				    
				    
				 
				    
				       //放入流程处理人属性，为了前端、显示处理人
				       map.put(taskOperator,employee.getYhMc());
				       
				       
                     if("backToStarterTask".equals(task.getTaskDefinitionKey())){
					       //执行过的任务重新清掉,即将executedTaskIds设为start
					   	  map.put("executedTaskIds", "start");
                     }else{
	  					   //如果当前任务提交了，则将当前任务的定义键放入，来记录已经执行了哪些流程节点,目的是在后面的用户完成流程时可以判断后面的流程是否还有逐级审批的任务，如果有则需要判断处理用户是否设置了领导，如果处理用户没有领导，则流程就被提交给了null
	   					   map.put("executedTaskIds", executedTaskIds+","+task.getTaskDefinitionKey());
	   					  
                     }

                     
                     //退回任务由任务发起人处理，如果不是退回任务,则任务可被转交，则需要作以下记录转交记录
                    /* if (!"backToStarterTask".equals(task.getTaskDefinitionKey())) {
                         //如果是指定的任务
        				   if("1".equals(processUserTask.getIsDesignated())){
        					   //如果当前处理人和指定人不一样，则说明是该任务由代理人在处理
        					   if (!loginUserDm.equals(processUserTask.getAssigneeDm())) {
        						   TaskTransfered taskTransfered=new TaskTransfered();
        						   taskTransfered.setProcess_instance_id(task.getProcessInstanceId());
        						   taskTransfered.setStaff_num(processUserTask.getAssigneeDm());
        						   taskTransfered.setTask_definition_key(task.getTaskDefinitionKey());
        						   taskTransfered.setTransfered_staff_num(loginUserDm);
        						   processInstanceService.insertTaskTransfered(taskTransfered);
        				     	}
        				   }else{
        					   //如果处理人和应该处理人不一样，则说明是代理人在处理
        					   if (!loginUserDm.equals(shouldUserId)) {
        						   TaskTransfered taskTransfered=new TaskTransfered();
        						   taskTransfered.setProcess_instance_id(task.getProcessInstanceId());
        						   taskTransfered.setStaff_num(shouldUserId);
        						   taskTransfered.setTask_definition_key(task.getTaskDefinitionKey());
        						   taskTransfered.setTransfered_staff_num(loginUserDm);
        						   processInstanceService.insertTaskTransfered(taskTransfered);
        				     	} 
        				   }
					}*/
                     
                
                     //保存提交记录到自定表
 			        ProcessHistory processHistory=new ProcessHistory();
 			        processHistory.setProcess_definition_id(task.getProcessDefinitionId());
 			        processHistory.setProcess_instance_id(task.getProcessInstanceId());
 			        processHistory.setStaff_num(loginUserDm);
 			        processHistory.setTask_definition_key(task.getTaskDefinitionKey());
 			        processHistory.setVariable_grasp(GsonUtil.toJson(map));//保存当时的流程变量
 			        processInstanceService.insertProcessHistory(processHistory);
                     
                     
 			       //保存流程变量到自定义表
 				    ProcessVariable processVariable=new ProcessVariable();
 				    processVariable.setProcess_instance_id(task.getProcessInstanceId());
 				    processVariable.setProcess_variables(GsonUtil.toJson(map));
 				    processInstanceService.saveProcessVariable(processVariable);
 			        
 			        
                     
				   taskService.complete(taskId, map);

				 
				   result.setSuccess(true);
				   result.setMsg("提交成功");  
			        
				   
			
				  
			} catch(com.kuangchi.sdd.base.exception.MyException me){
				result.setMsg(me.getMessage());
				result.setSuccess(false);
			}catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("提交失败");
				e.printStackTrace();
			}
			   printHttpServletResponse(GsonUtil.toJson(result));

	}
	
	/**
	 * 提交启动任务
	 */
	public void submitStartTask(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result = new JsonResult();
		//String loginUserDm=(String) getHttpServletRequest().getSession().getAttribute("yhDm"); //改
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
	    String loginUserDm=staff.getStaff_num();
		try {
			  
			  Gson gson=new Gson();
				Employee employee=employeeService.getEmployeeDetail(loginUserDm);
				//获取草稿ID
				String draftId=request.getParameter("draftId");

			   String processDefinitionId=request.getParameter("processDefinitionId");
			   List<String> unAssignedTaskIds=processInstanceService.getUnAssignedTaskIdByProcessDefinitionId(processDefinitionId);
			   //启动流程时如果流程中有逐级审批任务节点，则判断彷是否有审批领导，如果没有则不能提交
			   if (unAssignedTaskIds.size()>0&&employee.getSjldyhDm()==null) {
				throw new com.kuangchi.sdd.base.exception.MyException("当前登录用户没有审批人，请联系管理员设置审批人后再提交");
			}
			   String data = request.getParameter("data");
		       Map<String, Object> map = gson.fromJson(data, new TypeToken<Map<String, Object>>() { }.getType());
		       ProcessDef processDef=processDefService.getProcessDef(processDefinitionId);
		       
		       ProcessUserDelegationBean proUserDelegationBean=new ProcessUserDelegationBean();
		       proUserDelegationBean.setStaff_num(employee.getSjldyhDm());
		       proUserDelegationBean.setModel_Id(processDef.getModelId());
		       
		       
		       
		       ProcessUserDelegationBean processUserDelegationBean=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);
		       if (null==processUserDelegationBean) {
			       //放入非指定处理用户的上级用户代码属性，以便流向其领导
			       map.put("userId", employee.getSjldyhDm());
			   }else{
				   //如果不为空，则交给其代理人处理
				   map.put("userId", processUserDelegationBean.getDelegator());
			   }
	
		       //shouldUserId表示如果下一个任务是逐级审批的话，本应该由谁办理
		       map.put("shouldUserId", employee.getSjldyhDm());
		       //放入流程发起人属性，为了前端的显示处理人
		       map.put("startOperator", employee.getYhMc());
		       //流程退回变量，用来表示流程退回给谁
		       map.put("startUserId", employee.getYhDm());
		            //启动任务的时候放入启动任务Id
			   map.put("executedTaskIds", "start");

		       
		       identityService.setAuthenticatedUserId(loginUserDm);
		       ProcessInstance processInstance=  runtimeService.startProcessInstanceById(processDefinitionId,map);
		       //向流程变量里添加一个processInstanceId变量，其作用如：如请假成功后要将请假信息插入另外一张表的数据库中，这时候就要
		        List<Execution> executionList= runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
		        if (executionList.size()>0) {
					runtimeService.setVariable(executionList.get(0).getId(), "processInstanceId", processInstance.getId());
				}
		        runtimeService.signalEventReceived("transferRecord");
		        
		        
		        
		        
		        
		        //如果不为空，说明是从草稿页面提交的
		        if(null!=draftId&&!"".equals(draftId)){
		        	processInstanceService.deleteProcessStartTaskDraft(draftId);
		        }
		        
		        //保存提交的流程实例到自定义表
		        ProcessInstanceSubmit processInstanceSubmit=new ProcessInstanceSubmit();
		        processInstanceSubmit.setLock_status(0);
		        processInstanceSubmit.setProcess_definition_id(processDefinitionId);
		        processInstanceSubmit.setProcess_instance_id(processInstance.getId());
		        processInstanceSubmit.setStaff_num(loginUserDm);
		        processInstanceService.insertProcessInstanceSubmit(processInstanceSubmit);
		        
		        //保存提交记录到自定表
		        ProcessHistory processHistory=new ProcessHistory();
		        processHistory.setProcess_definition_id(processDefinitionId);
		        processHistory.setProcess_instance_id(processInstance.getId());
		        processHistory.setStaff_num(loginUserDm);
		        processHistory.setTask_definition_key("start");
			    processHistory.setVariable_grasp(GsonUtil.toJson(map));//保存当时的流程变量
		        processInstanceService.insertProcessHistory(processHistory);

			    
			    //保存流程变量到自定义表
			    ProcessVariable processVariable=new ProcessVariable();
			    processVariable.setProcess_instance_id(processInstance.getId());
			    processVariable.setProcess_variables(GsonUtil.toJson(map));
			    processInstanceService.saveProcessVariable(processVariable);
                
		        
		        
		        
		        
				result.setSuccess(true);
				result.setMsg("提交成功");
		} catch(com.kuangchi.sdd.base.exception.MyException me){
			result.setSuccess(false);
			result.setMsg(me.getMessage());
		}catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("提交失败");
			e.printStackTrace();
		}
		   printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	/**
	 * 获取某个人的待办任务
	 */
	public  void getTaskList(){
		HttpServletRequest request=getHttpServletRequest();
		
		String beanObject = getHttpServletRequest().getParameter("data"); // 获取前台序列化的数据
		UserTaskModel userTaskPage  = GsonUtil.toBean(beanObject,
				UserTaskModel.class); // 将数据转化为javabean
        
		ProcessDef processDef=processDefService.getProcessDef(userTaskPage.getProcessDefinitionId());

		//TODO
		//这个地方要对接从Session中获取登录用户代码的逻辑
		//String yhDm=(String) getHttpServletRequest().getSession().getAttribute("yhDm"); //改
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String yhDm=staff.getStaff_num();
		int page = 1;
		int rows = 10;
		if (null != request.getParameter("page")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		if (null != request.getParameter("rows")) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		userTaskPage.setYhDm(yhDm); 
		userTaskPage.setRows(rows);
		userTaskPage.setPage(page);
		
		
		if(null!=processDef){
			//因为同一个模板Model会布署多次，每一次会产生一个新的流程定义，因此在模板被重新布置后，同一个模板旧有的流程定义下的任务仍然需要查出来，所以本处先
			//将同一个模板的所有 流程 义ID查出来,拼接成字符串，以便在dao层进行条件 in 查询
			List<ProcessDef> processDefList=processDefService.getAllProcessDefsByModelId(processDef.getModelId());
			StringBuffer processDefs=new StringBuffer("");
			String processDefString="";
			for (int i = 0; i < processDefList.size(); i++) {
				processDefs.append("'").append(processDefList.get(i).getProdefinitionId()).append("'").append(",");
			}
			processDefString=processDefs.substring(0,processDefs.length()-1);
			userTaskPage.setProcessDefinitionIds(processDefString);
		}

		
		Grid<UserTaskModel> grid=userTaskService.getUserTask(userTaskPage);
		
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	

	
	
	/**
	 * 获取某个人发起的流程,完成的和未完成的
	 */
	public  void getHistoryProcessInstanceList(){
		HttpServletRequest request=getHttpServletRequest();
		
		
		String state=request.getParameter("state");
		
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		ProcessInstanceBean processInstanceBeanPage  = GsonUtil.toBean(beanObject,
				ProcessInstanceBean.class); // 将数据转化为javabean
		ProcessDef processDef=processDefService.getProcessDef(processInstanceBeanPage.getProcessDefinitionId());
	
		int page = 1;
		int rows = 10;
		if (null != request.getParameter("page")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		if (null != request.getParameter("rows")) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		//TODO
		//这个地方要对接从Session中获取登录用户代码的逻辑
		//String yhDm=(String) getHttpServletRequest().getSession().getAttribute("yhDm");; //改
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String yhDm=staff.getStaff_num();
		processInstanceBeanPage.setRows(rows);
		processInstanceBeanPage.setPage(page);
		processInstanceBeanPage.setYhDm(yhDm); 
		
		if (null!=processDef) {
			//因为同一个模板Model会布署多次，每一次会产生一个新的流程定义，因此在模板被重新布置后，同一个模板旧有的流程定义下的流程实例仍然需要查出来，所以本处先
			//将同一个模板的所有 流程 义ID查出来,拼接成字符串，以便在dao层进行条件 in 查询
			List<ProcessDef> processDefList=processDefService.getAllProcessDefsByModelId(processDef.getModelId());
			StringBuffer processDefs=new StringBuffer("");
			String processDefString="";
			for (int i = 0; i < processDefList.size(); i++) {
				processDefs.append("'").append(processDefList.get(i).getProdefinitionId()).append("'").append(",");
			}
			processDefString =processDefs.substring(0,processDefs.length()-1);
			processInstanceBeanPage.setProcessDefinitionIds(processDefString);
		}
		
		 Grid<ProcessInstanceBean> grid=null;
		//获取已完成的流程或正在处理的流程
		if ("finished".equals(state)) {
		     grid=	processInstanceService.getFinishedProcessInstance(processInstanceBeanPage);
		}else if("suspended".equals(state)){
		     grid=	processInstanceService.getSuspendProcessInstance(processInstanceBeanPage);
		}else{
		     grid=	processInstanceService.getProcessInstance(processInstanceBeanPage);
		}
		
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-13 下午5:13:52
	 * @功能描述: 获取转交流程实例
	 * @参数描述:
	 */
	public void getTransmitProcessInstanceList(){
		
		HttpServletRequest request=getHttpServletRequest();
		
		String beanObject = request.getParameter("data"); 
		ProcessInstanceBean processInstanceBeanPage  = GsonUtil.toBean(beanObject,ProcessInstanceBean.class); 
		ProcessDef processDef = processDefService.getProcessDef(processInstanceBeanPage.getProcessDefinitionId());
	
		int page = 1;
		int rows = 10;
		if (null != request.getParameter("page")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		if (null != request.getParameter("rows")) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		
		//这个地方要对接从Session中获取登录用户代码的逻辑
		Staff staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String yhDm = staff.getStaff_num();
		processInstanceBeanPage.setRows(rows);
		processInstanceBeanPage.setPage(page);
		processInstanceBeanPage.setYhDm(yhDm); 
		
		if (null != processDef) {
			//因为同一个模板Model会布署多次，每一次会产生一个新的流程定义，因此在模板被重新布置后，同一个模板旧有的流程定义下的流程实例仍然需要查出来，所以本处先
			//将同一个模板的所有 流程 义ID查出来,拼接成字符串，以便在dao层进行条件 in 查询
			List<ProcessDef> processDefList = processDefService.getAllProcessDefsByModelId(processDef.getModelId());
			StringBuffer processDefs = new StringBuffer("");
			String processDefString = "";
			for (int i = 0; i < processDefList.size(); i++) {
				processDefs.append("'").append(processDefList.get(i).getProdefinitionId()).append("'").append(",");
			}
			processDefString = processDefs.substring(0,processDefs.length()-1);
			processInstanceBeanPage.setProcessDefinitionIds(processDefString);
		}
		
		//获取已转交的流程
		Grid<TaskTransfered> grid = processInstanceService.getTransmitProcessInstance(processInstanceBeanPage);
		
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	/**
	 * 获取某个流程的详情
	 */
	public String toProcessDetail(){
		HttpServletRequest request=getHttpServletRequest();
		 String processInstanceId = request.getParameter("id");
		// HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		 //这个地方是根据流程实例所对应的流程定义Id获取界面的表单代码
		// Object form=processEngine.getFormService().getRenderedStartForm(historicProcessInstance.getProcessDefinitionId()).toString();
		  ProcessInstanceSubmit processInstanceSubmit=processInstanceService.getProcessInstanceSubmit(processInstanceId);
			 Object form=processEngine.getFormService().getRenderedStartForm(processInstanceSubmit.getProcess_definition_id()).toString();

		 request.setAttribute("processForm", form);
		 //获取流程运行到某一点之前所产生的所有流程变量的值，并将共传到界面，由界面表单进行返选展示
		// List<HistoricVariableInstance>  historicVariables= historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		 Map<String, Object> map=new HashMap<String, Object>();
//		 for (int i = 0; i < historicVariables.size(); i++) {
//			 HistoricVariableInstance variable=historicVariables.get(i);
//			 map.put(variable.getVariableName(), variable.getValue());
//		}
		 ProcessVariable processVariable=processInstanceService.getProcessVariable(processInstanceId);
		 if (null!=processVariable) {
			 Gson gson=new Gson();
		       map = gson.fromJson(processVariable.getProcess_variables(), new TypeToken<Map<String, Object>>() { }.getType());   
		   }
		 
		 Gson gson=new Gson();
		 String variableJson=gson.toJson(map);
		 request.setAttribute("processInstanceId", processInstanceId);
		 request.setAttribute("variableJson", variableJson);
		return "success";
	}
	
	
	
	/**
	 * 获取某个历史点的流程详情
	 */
	public String toProcessHistoryDetail(){
		HttpServletRequest request=getHttpServletRequest();
		 String processHistoryId = request.getParameter("id");
		 
		 
		 ProcessHistory processHistory=processInstanceService.selectProcessHistory(processHistoryId);
		 
		 
		 
		 HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery().processInstanceId(processHistory.getProcess_instance_id()).singleResult();
		 //这个地方是根据流程实例所对应的流程定义Id获取界面的表单代码
		 Object form=processEngine.getFormService().getRenderedStartForm(historicProcessInstance.getProcessDefinitionId()).toString();
		 request.setAttribute("processForm", form);
		 request.setAttribute("processInstanceId", processHistory.getProcess_instance_id());
		 request.setAttribute("variableJson", processHistory.getVariable_grasp());//某一时刻的流程参数快照
		return "success";
	}
	
	
	
	
	/**
	 * 删除某个流程
	 */
	public void deleteProcess(){ 
		HttpServletRequest request = getHttpServletRequest();
		 JsonResult result = new JsonResult();
         String dataIds=request.getParameter("data_ids").replace("'", "");
         String[] ids=dataIds.split(",");
		try {
			//删除历史流程实例
			for (int i = 0; i < ids.length; i++) {
				historyService.deleteHistoricProcessInstance(ids[i]);
			} 
			result.setMsg("删除成功");
		    result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("删除失败");
	        result.setSuccess(false);
			e.printStackTrace();
		}
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/***暂存流程
	**/
	
	public void saveUserTask(){
		HttpServletRequest request=getHttpServletRequest();
		   Gson gson=new Gson();
			JsonResult result = new JsonResult();
			   String taskId=request.getParameter("taskId");
			   String data = request.getParameter("data");
               try {
				
		
			   Map<String, Object> map = gson.fromJson(data, new TypeToken<Map<String, Object>>() { }.getType());   
			       //通过工作流内置taskService获取task对象
			   org.activiti.engine.task.Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
			   
			   runtimeService.setVariables(task.getExecutionId(), map);
			   result.setMsg("保存成功");
			    result.setSuccess(true);
           	} catch (Exception e) {
           		result.setMsg("保存失败");
    		    result.setSuccess(false);
			}
			   printHttpServletResponse(GsonUtil.toJson(result));
		
		
		
		
		
	}
	
	/***暂存流程
	**/
	
	public void saveStartTask(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result = new JsonResult();
		   String draftId = request.getParameter("draftId");
           
		//String loginUserDm=(String) getHttpServletRequest().getSession().getAttribute("yhDm"); //改
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
	    String loginUserDm=staff.getStaff_num();
		try {
			  String data = request.getParameter("data");
               if (null!=draftId&&!"".equals(draftId)) {
                   ProcessStartTaskDraft processStartTaskDraft=processInstanceService.getProcessStartTaskDraft(draftId);
                   processStartTaskDraft.setProcess_variables(data);
                   processInstanceService.updateProcessStartTaskDraft(processStartTaskDraft);
			}else{
	              ProcessStartTaskDraft processStartTaskDraft=new ProcessStartTaskDraft();
				   String processDefinitionId=request.getParameter("processDefinitionId");
	               processStartTaskDraft.setProcess_definition_id(processDefinitionId);
	               processStartTaskDraft.setProcess_variables(data);
	               processStartTaskDraft.setStaff_num(loginUserDm);
	               processInstanceService.insertProcessStartTaskDraft(processStartTaskDraft);
			}

				result.setSuccess(true);
				result.setMsg("保存成功");
		}catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("保存失败");
			e.printStackTrace();
		}
		   printHttpServletResponse(GsonUtil.toJson(result));
		
		
		
		
	}
	/**
	 * 挂起某个流程
	 */
	public void suspendProcess(){
		HttpServletRequest request = getHttpServletRequest();
		 JsonResult result = new JsonResult();
         String processInstanceId=request.getParameter("processInstanceId");
		try {
			//删除历史流程实例
				runtimeService.suspendProcessInstanceById(processInstanceId);
			
			result.setMsg("挂起成功");
		    result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("挂起失败");
	        result.setSuccess(false);
			e.printStackTrace();
		}
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	//恢复流程
	public void restoreProcess(){
		HttpServletRequest request = getHttpServletRequest();
		 JsonResult result = new JsonResult();
        String processInstanceId=request.getParameter("processInstanceId");
		try {
			//删除历史流程实例
				runtimeService.activateProcessInstanceById(processInstanceId);
			
			result.setMsg("恢复成功");
		    result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("恢复失败");
	        result.setSuccess(false);
			e.printStackTrace();
		}
       printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * 委派流程
	 */
	public void delegateTask(){
		HttpServletRequest request = getHttpServletRequest();
		 JsonResult result = new JsonResult();
         String modelId=request.getParameter("modelId");
		 String delegatedStaffNum=request.getParameter("delegator");
		 Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		 String yhDm=staff.getStaff_num();
		 
		 
		 ProcessUserDelegationBean processUserDelegationBean=new ProcessUserDelegationBean();
		 processUserDelegationBean.setModel_Id(modelId);
		 processUserDelegationBean.setStaff_num(yhDm);
		 processUserDelegationBean.setDelegator(delegatedStaffNum);
		try {
			
			
			 processInstanceService.deleteProcessUserDelegationByModelIdAndStaffNum(processUserDelegationBean);
			 processInstanceService.insertProcessUserDelegation(processUserDelegationBean);
			   
	        	ProcessDef processDef0=processDefService.getProcessDefByModelId(modelId);
			    List<ProcessUserTask> processUserTaskList =processUserTaskService.getProcessUserTaskByProcessDefId(processDef0.getProdefinitionId());      
			    
		           Template template=templateService.getTemplateById(modelId);

					Model modelData = repositoryService.getModel(modelId);
					ObjectNode modelNode = (ObjectNode) new ObjectMapper()
							.readTree(repositoryService.getModelEditorSource(modelData
									.getId()));
					byte[] bpmnBytes = null;

					BpmnModel model = new BpmnJsonConverter()
							.convertToBpmnModel(modelNode);
					
					
					//将界面配置的指派人员，以代码的形式赋到bpmnmodel中,并部署
					
					
					List<UserTask> userTaskList=getUserTaskInBpmnModel(model);
					
					for (int i = 0; i < userTaskList.size(); i++) {
						UserTask userTask=userTaskList.get(i);
						//首先将用户任务都默认的设为上级领导审批 
						userTask.setAssignee("${userId}");
						//找到回退到发起人处理的任务，然后设置其指派人表达式
						if("backToStarterTask".equals(userTask.getId())){
							
							userTask.setAssignee("${startUserId}");					
						}
						
						//将用户任务表单formKey设为界面 上我们输入的值
						userTask.setFormKey(template.getName());
						
						for (int j = 0; j <processUserTaskList.size(); j++) {
							ProcessUserTask processUserTask=processUserTaskList.get(j);
							 if (userTask.getId().equals(processUserTask.getTaskId())) {
								 //如果界面指定由某一个具体的人审批，则这里要赋值具体的人
								 if ("1".equals(processUserTask.getIsDesignated())) {
									 ProcessUserDelegationBean proUserDelegationBean=new ProcessUserDelegationBean();
								       proUserDelegationBean.setStaff_num(processUserTask.getAssigneeDm());
								       proUserDelegationBean.setModel_Id(modelId);
								       
								       
								       //指定人是否有委托给其它人处理
								       ProcessUserDelegationBean processUserDelegationB=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);
									   if (null==processUserDelegationB) {
											  userTask.setAssignee(processUserTask.getAssigneeDm());
									  }else{
										  userTask.setAssignee(processUserDelegationB.getDelegator());

									  }
								}
							}
						}
					}
					
					
					
					//获取流程中起点的值，并将其formKey设为我们界面输入的值
					List<StartEvent> startEventList=getStartEventInBpmnModel(model);
					for (int i = 0; i < startEventList.size(); i++) {
						StartEvent startEvent=startEventList.get(i);
						startEvent.setFormKey(template.getName());
					}
					

					bpmnBytes = new BpmnXMLConverter().convertToXML(model,"ISO-8859-1");

					String processName = modelData.getName() + ".bpmn20.xml";
					Deployment deployment = repositoryService.createDeployment()
							.name(modelData.getName())
							.addString(processName, new String(bpmnBytes,"UTF-8")).addString(template.getName(), template.getTemplateCode()).deploy();
					
					templateService.updateTemplate(template);
				    
					
					ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
					
					
					//保存部署的流程信息
					processDefService.deleteProcessDefByModelId(modelId);

					ProcessDef processDef=new ProcessDef();
					processDef.setName(deployment.getName());
					processDef.setModelId(modelId);
					processDef.setDeploymentId(deployment.getId());
					processDef.setIsCurrent("1");
					processDef.setProdefinitionId(processDefinition.getId());
					processDefService.addProcessDef(processDef);

					
					//保存部署的用户任务处理人信息
					

					for (int i = 0; i < processUserTaskList.size(); i++) {
						ProcessUserTask processUserTask=processUserTaskList.get(i);
						processUserTask.setProcessDefinitionId(processDefinition.getId());
						if ("1".equals(processUserTask.getIsDesignated())) {
							processUserTaskService.insertProcessUserTask(processUserTask);
						}else{
							processUserTask.setAssigneeDm(null);
							processUserTaskService.insertProcessUserTask(processUserTask);
						}
					}
					

					
					
					result.setMsg("保存成功");
				    result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("保存失败");
	        result.setSuccess(false);
			e.printStackTrace();
		}	
		 printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	
	
	
	
	
	
	//解析Bpmnmodel，获取被定义为用户任务的节点
	
		public List<UserTask> getUserTaskInBpmnModel(BpmnModel model){
		    List<UserTask > userTaskList=new ArrayList<UserTask>();
			try {
			    List<org.activiti.bpmn.model.Process> processList=	model.getProcesses();
			    
			    for (int i = 0; i < processList.size(); i++) {
					Process process=processList.get(i);
					Collection<FlowElement> flowElementList=process.getFlowElements();
			    Iterator<FlowElement> iterator=		flowElementList.iterator();
			       while(iterator.hasNext()){
			    	   FlowElement flowElement=iterator.next();
			    	   	if (flowElement instanceof UserTask) {
			    	   		UserTask userTask=(UserTask)flowElement;
			    	   		userTaskList.add(userTask);
						}
			       }
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    return userTaskList;			

		}
		
		
		
		//解析Bpmnmodel，获取起点
		
			public List<StartEvent> getStartEventInBpmnModel(BpmnModel model){
				
			     List<StartEvent> startEventList=new ArrayList<StartEvent>();
			     
				try {
				    List<org.activiti.bpmn.model.Process> processList=	model.getProcesses();
				    
				    for (int i = 0; i < processList.size(); i++) {
						Process process=processList.get(i);
						Collection<FlowElement> flowElementList=process.getFlowElements();
				    Iterator<FlowElement> iterator=		flowElementList.iterator();
				       while(iterator.hasNext()){
				    	   FlowElement flowElement=iterator.next();
				    	   	if (flowElement instanceof StartEvent) {
				    	   		StartEvent startEvent=(StartEvent)flowElement;
				    	   		startEventList.add(startEvent);
							}
				       }
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    return startEventList;			

			}
		
		
		
		
		public List<ProcessUserTask> getProcessNodes(String modelId){
		    List<ProcessUserTask > userTaskList=new ArrayList<ProcessUserTask>();
			try {
				Model modelData = repositoryService.getModel(modelId);
				ObjectNode modelNode = (ObjectNode) new ObjectMapper()
						.readTree(repositoryService.getModelEditorSource(modelData
								.getId()));

				BpmnModel model = new BpmnJsonConverter()
						.convertToBpmnModel(modelNode);
				
			    List<org.activiti.bpmn.model.Process> processList=	model.getProcesses();
			    
			    for (int i = 0; i < processList.size(); i++) {
					Process process=processList.get(i);
					Collection<FlowElement> flowElementList=process.getFlowElements();
			    Iterator<FlowElement> iterator=		flowElementList.iterator();
			       while(iterator.hasNext()){
			    	   FlowElement flowElement=iterator.next();
			    	   	if (flowElement instanceof UserTask) {
			    	   		UserTask userTask=(UserTask)flowElement;
			    	   		ProcessUserTask u=new ProcessUserTask();
			    	   		u.setAssigneeDm(userTask.getAssignee());
			    	   		u.setModelId(modelId);
			    	   		u.setName(userTask.getName());
			    	   		u.setTaskId(userTask.getId());
			    	   		userTaskList.add(u);
						}
			       }
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    return userTaskList;			

		}
		
	
	
	
	
	
	
	
	public void getProcessUserDelegation(){
		HttpServletRequest request = getHttpServletRequest();
         String modelId=request.getParameter("modelId");
		 Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		 String yhDm=staff.getStaff_num();
		 ProcessUserDelegationBean processUserDelegationBean=new ProcessUserDelegationBean();
		 processUserDelegationBean.setModel_Id(modelId);
		 processUserDelegationBean.setStaff_num(yhDm);
		 ProcessUserDelegationBean existingProcessUserDelegationBean=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(processUserDelegationBean);
		 printHttpServletResponse(GsonUtil.toJson(existingProcessUserDelegationBean));
	}
	
	
	public void deleteProcessUSerDelegation(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
        String id=request.getParameter("id");
        
        ProcessUserDelegationBean processUserDelegationBean=new ProcessUserDelegationBean();
		 processUserDelegationBean.setId(id);
		 try {
			 

			 ProcessUserDelegationBean proDelegationBean=processInstanceService.getProcessUserDelegationById(processUserDelegationBean);
			 String modelId=proDelegationBean.getModel_Id();
			 
			 
             //删除数据库中保留的值 
		        processInstanceService.deleteProcessUserDelegation(processUserDelegationBean);
			 
			 ProcessDef processDef0=processDefService.getProcessDefByModelId(modelId);
			    List<ProcessUserTask> processUserTaskList =processUserTaskService.getProcessUserTaskByProcessDefId(processDef0.getProdefinitionId());      
			    
		           Template template=templateService.getTemplateById(modelId);

					Model modelData = repositoryService.getModel(modelId);
					ObjectNode modelNode = (ObjectNode) new ObjectMapper()
							.readTree(repositoryService.getModelEditorSource(modelData
									.getId()));
					byte[] bpmnBytes = null;

					BpmnModel model = new BpmnJsonConverter()
							.convertToBpmnModel(modelNode);
					
					
					//将界面配置的指派人员，以代码的形式赋到bpmnmodel中,并部署
					
					
					List<UserTask> userTaskList=getUserTaskInBpmnModel(model);
					
					for (int i = 0; i < userTaskList.size(); i++) {
						UserTask userTask=userTaskList.get(i);
						//首先将用户任务都默认的设为上级领导审批 
						userTask.setAssignee("${userId}");
						//找到回退到发起人处理的任务，然后设置其指派人表达式
						if("backToStarterTask".equals(userTask.getId())){
							
							userTask.setAssignee("${startUserId}");					
						}
						
						//将用户任务表单formKey设为界面 上我们输入的值
						userTask.setFormKey(template.getName());
						
						for (int j = 0; j <processUserTaskList.size(); j++) {
							ProcessUserTask processUserTask=processUserTaskList.get(j);
							 if (userTask.getId().equals(processUserTask.getTaskId())) {
								 //如果界面指定由某一个具体的人审批，则这里要赋值具体的人
								 if ("1".equals(processUserTask.getIsDesignated())) {
									 ProcessUserDelegationBean proUserDelegationBean=new ProcessUserDelegationBean();
								       proUserDelegationBean.setStaff_num(processUserTask.getAssigneeDm());
								       proUserDelegationBean.setModel_Id(modelId);
								       
								       
								       //指定人是否有委托给其它人处理
								       ProcessUserDelegationBean processUserDelegationB=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);
									   if (null==processUserDelegationB) {
											  userTask.setAssignee(processUserTask.getAssigneeDm());
									  }else{
										  userTask.setAssignee(processUserDelegationB.getDelegator());

									  }
								}
							}
						}
					}
					
					
					
					//获取流程中起点的值，并将其formKey设为我们界面输入的值
					List<StartEvent> startEventList=getStartEventInBpmnModel(model);
					for (int i = 0; i < startEventList.size(); i++) {
						StartEvent startEvent=startEventList.get(i);
						startEvent.setFormKey(template.getName());
					}
					

					bpmnBytes = new BpmnXMLConverter().convertToXML(model,"ISO-8859-1");

					String processName = modelData.getName() + ".bpmn20.xml";
					Deployment deployment = repositoryService.createDeployment()
							.name(modelData.getName())
							.addString(processName, new String(bpmnBytes,"UTF-8")).addString(template.getName(), template.getTemplateCode()).deploy();
					
					templateService.updateTemplate(template);
				    
					
					ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
					
					
					//保存部署的流程信息
					processDefService.deleteProcessDefByModelId(modelId);

					ProcessDef processDef=new ProcessDef();
					processDef.setName(deployment.getName());
					processDef.setModelId(modelId);
					processDef.setDeploymentId(deployment.getId());
					processDef.setIsCurrent("1");
					processDef.setProdefinitionId(processDefinition.getId());
					processDefService.addProcessDef(processDef);

					
					//保存部署的用户任务处理人信息
					

					for (int i = 0; i < processUserTaskList.size(); i++) {
						ProcessUserTask processUserTask=processUserTaskList.get(i);
						processUserTask.setProcessDefinitionId(processDefinition.getId());
						if ("1".equals(processUserTask.getIsDesignated())) {
							processUserTaskService.insertProcessUserTask(processUserTask);
						}else{
							processUserTask.setAssigneeDm(null);
							processUserTaskService.insertProcessUserTask(processUserTask);
						}
					}

		        result.setMsg("删除成功");
		        result.setSuccess(true);
			} catch (Exception e) {
				result.setMsg("删除失败");
		        result.setSuccess(false);
				e.printStackTrace();
			}
	     printHttpServletResponse(GsonUtil.toJson(result));

	}
	
	
	
	
	public void getProcessUserDelegationList(){
		 Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		 String yhDm=staff.getStaff_num();
		 ProcessUserDelegationBean processUserDelegationBean=new ProcessUserDelegationBean();
		 processUserDelegationBean.setStaff_num(yhDm);
		 Grid<ProcessUserDelegationBean> grid=null;
	     grid=processInstanceService.getProcessUserDelegationListByStaffNum(processUserDelegationBean);
	     printHttpServletResponse(GsonUtil.toJson(grid));
		 
	}
	
	
	public void getStartTaskDraftList(){
		 HttpServletRequest request = getHttpServletRequest();
		 Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		 String yhDm=staff.getStaff_num();
		 String beanObject = request.getParameter("data"); 
			ProcessStartTaskDraft processStartTaskDraft  = GsonUtil.toBean(beanObject,ProcessStartTaskDraft.class); 
			ProcessDef processDef = processDefService.getProcessDef(processStartTaskDraft.getProcess_definition_id());

		 Grid<ProcessStartTaskDraft> grid=null;
		 int page=0;
		 int rows=10;
		 processStartTaskDraft.setStaff_num(yhDm);
			if (null != request.getParameter("page")) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			if (null != request.getParameter("rows")) {
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			processStartTaskDraft.setPage(page);
			processStartTaskDraft.setRows(rows);
			
			if (null != processDef) {
				//因为同一个模板Model会布署多次，每一次会产生一个新的流程定义，因此在模板被重新布置后，同一个模板旧有的流程定义下的流程实例仍然需要查出来，所以本处先
				//将同一个模板的所有 流程 义ID查出来,拼接成字符串，以便在dao层进行条件 in 查询
				List<ProcessDef> processDefList = processDefService.getAllProcessDefsByModelId(processDef.getModelId());
				StringBuffer processDefs = new StringBuffer("");
				String processDefString = "";
				for (int i = 0; i < processDefList.size(); i++) {
					processDefs.append("'").append(processDefList.get(i).getProdefinitionId()).append("'").append(",");
				}
				processDefString = processDefs.substring(0,processDefs.length()-1);
				processStartTaskDraft.setProcessDefinitionIds(processDefString);
			}
			
		 grid=processInstanceService.getProcessStartTaskDraftListByStaffNum(processStartTaskDraft);
	     printHttpServletResponse(GsonUtil.toJson(grid));
 
	}
	 
	
	//获取我的处理历史
	public void getMyAlreadyDoneList(){
		 HttpServletRequest request = getHttpServletRequest();
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		 String yhDm=staff.getStaff_num();
		 String beanObject = request.getParameter("data"); 
			ProcessHistory processHistory  = GsonUtil.toBean(beanObject,ProcessHistory.class); 
			ProcessDef processDef = processDefService.getProcessDef(processHistory.getProcess_definition_id()); 
		 int page=1;
		 int rows=10;
		 if (null != request.getParameter("page")) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			if (null != request.getParameter("rows")) {
				rows = Integer.parseInt(request.getParameter("rows"));
			}
			processHistory.setStaff_num(yhDm);
			processHistory.setPage(page);
			processHistory.setRows(rows);
			if (null != processDef) {
				//因为同一个模板Model会布署多次，每一次会产生一个新的流程定义，因此在模板被重新布置后，同一个模板旧有的流程定义下的流程实例仍然需要查出来，所以本处先
				//将同一个模板的所有 流程 义ID查出来,拼接成字符串，以便在dao层进行条件 in 查询
				List<ProcessDef> processDefList = processDefService.getAllProcessDefsByModelId(processDef.getModelId());
				StringBuffer processDefs = new StringBuffer("");
				String processDefString = "";
				for (int i = 0; i < processDefList.size(); i++) {
					processDefs.append("'").append(processDefList.get(i).getProdefinitionId()).append("'").append(",");
				}
				processDefString = processDefs.substring(0,processDefs.length()-1);
				processHistory.setProcessDefinitionIds(processDefString);
			}
		 Grid<ProcessHistory> grid= processInstanceService.selectProcessHistoryList(processHistory);
		 printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	//撤回任务 
	public void revokeTask(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result = new JsonResult();
		String id=request.getParameter("id");
		String process_instance_id=request.getParameter("process_instance_id");
		 Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		 String yhDm=staff.getStaff_num();
		 
		try {
			
			ProcessInstanceSubmit processInstanceSubmit=processInstanceService.getProcessInstanceSubmit(process_instance_id);
			if (processInstanceSubmit.getLock_status()==1) {
				 result.setMsg("已经为撤回状态");
				 result.setSuccess(false);	   
			}else{
				
				ProcessHistory latestProcessHistory=processInstanceService.selectLatestProcessHistory(process_instance_id);
				//如果要处理的任务记录是最新的
				if (id.equals(latestProcessHistory.getId())) {
/*					processInstanceSubmit.setLock_status(1);
					processInstanceSubmit.setProcess_instance_id(process_instance_id);
					processInstanceService.updateProcessInstanceSubmit(processInstanceSubmit);*/
					result.setMsg("撤回成功");
					 result.setSuccess(true);	   
				}else{
					result.setMsg("该流程已经被下一个节点用户处理，已经不能撤回");
					result.setSuccess(false);	
				}
			}
		       
		} catch (Exception e) {
			result.setMsg("撤回失败");
	        result.setSuccess(false); 
	        e.printStackTrace();
		}
	     printHttpServletResponse(GsonUtil.toJson(result));
			   
	}
	
	
	
	
	
	/**
	 * 会签测试用的
	 */
	public void counterSign(){
       try {
   		HttpServletRequest request = getHttpServletRequest();
       	String file="D:/my workspace/zzz/src/main/resources/testggg.bpmn";

    	   InputStream in=new FileInputStream(new File(file));
    	   Deployment deployment=repositoryService.createDeployment().addInputStream("testggg.bpmn", in).deploy();
	    ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
	    Map<String, Object>  map=new HashMap<String, Object>();
	    List<String>  list=new ArrayList<String>();
	    list.add("bob1");
	    list.add("bob2");
	    map.put("assigneeList", list);
	    map.put("pass", 0);
	   
	    
	     ProcessInstance processInstance=  runtimeService.startProcessInstanceById(pd.getId(),map);
	     
	     List<Task> jimList= taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("jim1").list();
	     for (int i = 0; i < jimList.size(); i++) {
	    	 LOG.info("==========jim1============"+jimList.get(i).getName());
	    
	    	 //taskService.complete(jimList.get(i).getId());
		}
	     

	     
	     
	     List<Task>  bob1List=taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("bob1").list();
	     for (int i = 0; i <bob1List.size(); i++) {
	    	 LOG.info(".........bob1.........."+bob1List.get(i).getName());
	    	 map.put("pass", 1);
	    	 taskService.complete(bob1List.get(i).getId(),map);

		}
	     
	     List<Task> lucyList=taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("bob2").list();
	     for (int i = 0; i < lucyList.size(); i++) {
	    	 LOG.info("......bob2......"+lucyList.get(i).getName());
	    	// taskService.complete(bob1List.get(i).getId());

		}
	     
	     
	     List<Task> jim2List=taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee("jim2").list();
	     for (int i = 0; i < jim2List.size(); i++) {
	    	 LOG.info("......jim2......"+jim2List.get(i).getName());

		}
	     
	     

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			
	}
	
	
	/**
	 * 获取历史表单信息，用于销假申请中重现已经审批的表单信息
	 * by gengji.yang
	 */
	public void getLeaveForm(){
		HttpServletRequest request=getHttpServletRequest();
		 String processInstanceId = request.getParameter("id");
	//	 List<HistoricVariableInstance>  historicVariables= historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
		 
		 
		 Map<String, Object> map=new HashMap<String, Object>();
//		 for (int i = 0; i < historicVariables.size(); i++) {
//			 HistoricVariableInstance variable=historicVariables.get(i);
//			 map.put(variable.getVariableName(), variable.getValue());
//		}
		 ProcessVariable processVariable=processInstanceService.getProcessVariable(processInstanceId);
		 Gson gson=new Gson();
		 if(processVariable!=null){
		       map = gson.fromJson(processVariable.getProcess_variables(), new TypeToken<Map<String, Object>>() { }.getType());
		 }
		 String variableJson=gson.toJson(map);
		 printHttpServletResponse(variableJson);
	}
	
	/**
	 * 获取 没有撤销过的请假流程
	 * by gengji.yang	
	 */
	public void getNoCancelLeaveProcess(){

		HttpServletRequest request=getHttpServletRequest();
		
		
		String state=request.getParameter("state");
		
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		ProcessInstanceBean processInstanceBeanPage  = GsonUtil.toBean(beanObject,
				ProcessInstanceBean.class); // 将数据转化为javabean
		ProcessDef processDef=processDefService.getProcessDefByName(processInstanceBeanPage.getName());
	
		int page = 1;
		int rows = 10;
		if (null != request.getParameter("page")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		if (null != request.getParameter("rows")) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		//TODO
		//这个地方要对接从Session中获取登录用户代码的逻辑
		//String yhDm=(String) getHttpServletRequest().getSession().getAttribute("yhDm");; //改
		Staff staff=(Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		String yhDm=staff.getStaff_num();
		processInstanceBeanPage.setRows(rows);
		processInstanceBeanPage.setPage(page);
		processInstanceBeanPage.setYhDm(yhDm); 
		
		if (null!=processDef) {
			//因为同一个模板Model会布署多次，每一次会产生一个新的流程定义，因此在模板被重新布置后，同一个模板旧有的流程定义下的流程实例仍然需要查出来，所以本处先
			//将同一个模板的所有 流程 义ID查出来,拼接成字符串，以便在dao层进行条件 in 查询
			List<ProcessDef> processDefList=processDefService.getAllProcessDefsByModelId(processDef.getModelId());
			StringBuffer processDefs=new StringBuffer("");
			String processDefString="";
			for (int i = 0; i < processDefList.size(); i++) {
				processDefs.append("'").append(processDefList.get(i).getProdefinitionId()).append("'").append(",");
			}
			processDefString =processDefs.substring(0,processDefs.length()-1);
			processInstanceBeanPage.setProcessDefinitionIds(processDefString);
		}
		
		 Grid<ProcessInstanceBean> grid=null;
		//获取已完成的流程或正在处理的流程
		if ("finished".equals(state)) {
		     grid=	processInstanceService.getNoCancelFinishedProcessInstance(processInstanceBeanPage);
		}else if("suspended".equals(state)){
		     grid=	processInstanceService.getSuspendProcessInstance(processInstanceBeanPage);
		}else{
		     grid=	processInstanceService.getProcessInstance(processInstanceBeanPage);
		}
		
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 删除未提交流程
	 */
	public void deletePortalProcess(){ 
		HttpServletRequest request = getHttpServletRequest();
		 JsonResult result = new JsonResult();
         String dataIds=request.getParameter("draftId");
		try {
			Integer obj=processInstanceService.deleteProcessHistory(dataIds);
			if(obj!=0){
				result.setMsg("删除成功");
			    result.setSuccess(true);
			}else{
			result.setMsg("删除失败");
		    result.setSuccess(false);
		    }
		} catch (Exception e) {
			result.setMsg("删除失败");
	        result.setSuccess(false);
			e.printStackTrace();
		}
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	

}
