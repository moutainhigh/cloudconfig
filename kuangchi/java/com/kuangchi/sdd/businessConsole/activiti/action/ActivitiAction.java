package com.kuangchi.sdd.businessConsole.activiti.action;


import java.io.ByteArrayInputStream;
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
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.activiti.model.ModelMetaInfo;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessDef;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessModelBean;
import com.kuangchi.sdd.businessConsole.activiti.model.ProcessUserTask;
import com.kuangchi.sdd.businessConsole.activiti.model.Template;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessDefService;
import com.kuangchi.sdd.businessConsole.activiti.service.ProcessUserTaskService;
import com.kuangchi.sdd.businessConsole.activiti.service.TemplateService;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.process.model.ProcessUserDelegationBean;
import com.kuangchi.sdd.businessConsole.process.service.ActivitiProcessService;
import com.kuangchi.sdd.businessConsole.process.service.ProcessInstanceService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;


@Controller("activitiAction")
public class ActivitiAction extends BaseActionSupport {
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

	
	@Resource(name="templateService")
	TemplateService templateService;
	
	
	@Resource(name="processDefService")
	ProcessDefService processDefService;
	
	
	@Resource (name="processUserTaskService")
	ProcessUserTaskService processUserTaskService;
	
	@Resource(name="activitiProcessService")
	ActivitiProcessService activitiProcessService;
	
	@Resource(name="processInstanceService")
	ProcessInstanceService processInstanceService;
	@Resource(name="employeeService")
    EmployeeService	employeeService;
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}



	

	// ============================================================

	/**
	 * 模型列表
	 * 流程维护-->>流程定义页面进入后的列表
	 * 
	 * 
	 */
    
	public void processDefinitionList() {

		HttpServletRequest request = getHttpServletRequest();
		String beanObject = getHttpServletRequest().getParameter("data"); // 获取前台序列化的数据
		ProcessModelBean processModel = GsonUtil.toBean(beanObject,
				ProcessModelBean.class); // 将数据转化为javabean

		int page = 1;
		int rows = 10;
		if (null != request.getParameter("page")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		if (null != request.getParameter("rows")) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}

		int start = (page - 1) * rows;

		List<Model> list = null;
		int count = 0;
		if (processModel.getName() == null) {
			list = repositoryService.createModelQuery().orderByModelName()
					.desc().listPage(start, rows);
			count = Long.valueOf(repositoryService.createModelQuery().count())
					.intValue();

		} else {
			list = repositoryService.createModelQuery()
					.modelNameLike("%" + processModel.getName() + "%")
					.orderByModelName().desc().listPage(start, rows);
			count = Long.valueOf(
					repositoryService
							.createModelQuery()
							.modelNameLike(
									processModel.getName() == null ? "%%" : "%"
											+ processModel.getName() + "%")
							.count()).intValue();

		}

		
		
		
		
		Grid<ProcessModelBean> grid = new Grid<ProcessModelBean>();
		List<Model> resultList = list;
		List<ProcessModelBean> processModelList=new ArrayList<ProcessModelBean>();
		for (int i = 0; i < resultList.size(); i++) {
			Model model=resultList.get(i);
	        ModelMetaInfo modelMetaInfo = GsonUtil.toBean(model.getMetaInfo(), ModelMetaInfo.class);	//将数据转化为javabean
	        ProcessModelBean pm=new ProcessModelBean();
	         pm.setDescription(modelMetaInfo.getDescription());
	         pm.setId(model.getId());
	         pm.setKey(model.getKey());
	         pm.setName(modelMetaInfo.getName());
	         pm.setVersion(model.getVersion());
	         processModelList.add(pm);
		}
		
		grid.setRows(processModelList);
		if (null != resultList) {
			grid.setTotal(count);
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}

	
	
	

	
	
	
	
	
	
	
	/**
	 * 创建模型
	 */

	public void createActiviti() {
		try {
			HttpServletRequest request = getHttpServletRequest();
			HttpServletResponse response = getHttpServletResponse();

			String id = request.getParameter("id");
			String flag = request.getParameter("flag");
			String data = request.getParameter("data");
			if (data!=null) {
				data=new String(data.getBytes("ISO-8859-1"),"UTF-8");
			}
			ProcessModelBean processModel=GsonUtil.toBean(data,ProcessModelBean.class);



			if ("add".equals(flag)) {
				
				String name = processModel.getName();
				String key=processModel.getKey();
				String description=processModel.getDescription();
				
				ObjectMapper objectMapper = new ObjectMapper();
				ObjectNode editorNode = objectMapper.createObjectNode();
				editorNode.put("id", "canvas");
				editorNode.put("resourceId", "canvas");
				ObjectNode stencilSetNode = objectMapper.createObjectNode();
				stencilSetNode.put("namespace",
						"http://b3mn.org/stencilset/bpmn2.0#");
				editorNode.put("stencilset", stencilSetNode);
				Model modelData = repositoryService.newModel();

				ObjectNode modelObjectNode = objectMapper.createObjectNode();
				modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
				modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
				description = StringUtils.defaultString(description);
				modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,
						description);
				modelData.setMetaInfo(modelObjectNode.toString());
				modelData.setName(name);
				modelData.setKey(StringUtils.defaultString(key));
				

				// 保存模型
				repositoryService.saveModel(modelData);
				repositoryService.addModelEditorSource(modelData.getId(),
						editorNode.toString().getBytes("utf-8"));

				response.sendRedirect(request.getContextPath()
						+ "/service/editor?id=" + modelData.getId());

			} else {
				response.sendRedirect(request.getContextPath()
						+ "/service/editor?id=" + id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据Model部署流程
	 */

	public void deploy() {
		HttpServletRequest request = getHttpServletRequest();
		String modelId = request.getParameter("modelId");
		 String data=request.getParameter("templateFormData");
		 String data2=request.getParameter("processUserFormData");
		 
		 Gson gson=new Gson();
		 List<ProcessUserTask> processUserTaskList= gson.fromJson(data2, new TypeToken<List<ProcessUserTask>>(){}.getType());
	      
		 
		 
  
		 JsonResult result = new JsonResult();
		 Template template=GsonUtil.toBean(data,Template.class);	
		try {
			
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
						       ProcessUserDelegationBean processUserDelegationBean=processInstanceService.getProcessUserDelegationByModelIdAndStaffNum(proUserDelegationBean);
							   if (null==processUserDelegationBean) {
									  userTask.setAssignee(processUserTask.getAssigneeDm());
							  }else{
								  userTask.setAssignee(processUserDelegationBean.getDelegator());

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
			
			
			result.setMsg("部署成功");
		    result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("部署失败");
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
	
	
	

	/**
	 * 获取表单模板
	 */

	public String addTemplateCode() {	
		HttpServletRequest request = getHttpServletRequest();
        String id=request.getParameter("id");
        Template template=templateService.getTemplateById(id);
        //第一次直接生成一个空的模板
        if (null==template) {
			template=new Template();
			template.setId(id);
		}
        //获取当前使用的流程定义
        ProcessDef processDef=processDefService.getProcessDefByModelId(id);
        //获取bpmnmodel解析的用户任务
        List<ProcessUserTask> bpmnProcessUserTaskList=getProcessNodes(id);
        
        for(ProcessUserTask put:bpmnProcessUserTaskList){
        	//找到退回处理任务，然后把它从设置任务表中移除掉
        	if ("backToStarterTask".equals(put.getTaskId())) {
        		bpmnProcessUserTaskList.remove(put);
        		break;
			}
        }
        //设置指派的用户名称
        for (int i = 0; i < bpmnProcessUserTaskList.size(); i++) {
        	ProcessUserTask pt=bpmnProcessUserTaskList.get(i);
        	if (null!=pt.getAssigneeDm()) {
				Employee employee=employeeService.getEmployeeDetail(pt.getAssigneeDm());
				pt.setAssigneeMc(employee.getYhMc());	
			}
		}
        
        List<ProcessUserTask> processUserTaskList=new ArrayList<ProcessUserTask>();

        //如果processDef不为空值，说明不是第一次部署，如果为空值说明是第一次部署
        if(null!=processDef){
        	boolean changed=false;
        	processUserTaskList =processUserTaskService.getProcessUserTaskByProcessDefId(processDef.getProdefinitionId());      
            
        	
        	//设置指派的用户名称
            for (int i = 0; i < processUserTaskList.size(); i++) {
            	ProcessUserTask pt=processUserTaskList.get(i);
            	if (null!=pt.getAssigneeDm()) {
    				Employee employee=employeeService.getEmployeeDetail(pt.getAssigneeDm());
    				pt.setAssigneeMc(employee.getYhMc());	
    			}
    		}
        	
        	
        	
        	
        	
           //粗略比较我们数据库里面存的节点和解析bpmnmodel得到的结点Id是否匹配，有一个不匹配，说明用户在界面上做了编辑，这时候界面无法返选，
           //给用户呈现初始界面，如果全部匹配，则用记在界面可以看到历史返选值
        	if (null!=processUserTaskList&&processUserTaskList.size()>0) { 
        		if(processUserTaskList.size()!=bpmnProcessUserTaskList.size()){
        			changed=true;
        		}else{
        			for (int i = 0; i < processUserTaskList.size(); i++) {
        				ProcessUserTask processUserTask=processUserTaskList.get(i);
	    		    boolean find=false;
	    		   for (int j = 0; j < bpmnProcessUserTaskList.size(); j++) {
    			   ProcessUserTask bpmnProcessUserTask=bpmnProcessUserTaskList.get(j);
    			   if(bpmnProcessUserTask.getTaskId().equals(processUserTask.getTaskId())){
    				   find=true;
    				   break;
    			   }		   
			}
    		   if(!find){
    			   changed=true;
    		   }
    		   
		   }	
    	 }
	    }else{
	    	changed=true;
	    }
        	

        	//如果改变了放解析后的新值，未改变，则放我们存的上一次的值 
        	if(changed){
        		request.setAttribute("processUserTaskList", bpmnProcessUserTaskList);
        	}else{
        		request.setAttribute("processUserTaskList", processUserTaskList);
        	}
       
        }else{
        	 request.setAttribute("processUserTaskList", bpmnProcessUserTaskList);
        }
        

        request.setAttribute("template", template); 
        return "success";
	}
	
	
	
	
	
	/**
	 * 删除模型
	 */

	public void deleteActiviti() {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();
		 JsonResult result = new JsonResult();
        String dataIds=request.getParameter("data_ids").replace("'", "");
       
		try {
			
			
			repositoryService.deleteModel(dataIds);
		   
			result.setMsg("删除成功");
		    result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("删除失败");
	        result.setSuccess(false);
			e.printStackTrace();
		}
        printHttpServletResponse(GsonUtil.toJson(result));

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 导出model的xml文件
	 */
	 public   void exportBpmn() {
		 
		 HttpServletRequest request = getHttpServletRequest();
		 HttpServletResponse response=getHttpServletResponse();
	        String id=request.getParameter("id");
	 try {
	 Model modelData = repositoryService.getModel(id);
	 BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
	 JsonNode editorNode = new
	 ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
	 BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
	 BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
	 byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
	
	 ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
	 IOUtils.copy(in, response.getOutputStream());
	 String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
	 response.setHeader("Content-Disposition", "attachment; filename=" +
	 filename);
	 response.flushBuffer();
	 } catch (Exception e) {
	     e.printStackTrace();
	 }
	 }
	
	
	public void  getLeaveCategoryList(){
		List<Map<String, String>> list=activitiProcessService.getLeaveCategoryList();

        printHttpServletResponse(GsonUtil.toJson(list));

	}
	
	
	

	
	
	

}
