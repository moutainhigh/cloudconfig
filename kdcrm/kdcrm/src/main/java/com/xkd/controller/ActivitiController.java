package com.xkd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.vo.QingJiaBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/11/28.
 */
@Api(description = "工作流相关接口")
@Controller
@RequestMapping("/workflow")
@Transactional
public class ActivitiController extends BaseController {
    @Autowired
    IdentityService identityService;
    @Autowired
    ProcessEngine processEngine;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;


    @ApiOperation(value = "部署")
    @ResponseBody
    @RequestMapping(value = "/deploy", method = {RequestMethod.GET})
    public ResponseDbCenter deploy(@ApiParam(value = "部署Id", required = true) @RequestParam(required = true) String modelId) {   //从数据库部署,
        Model modelData = repositoryService.getModel(modelId);
        String myForm = "姓名:<input name=\"name\"></input>";
        try {
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model, "ISO-8859-1");
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
                    .addString(processName, new String(bpmnBytes, "UTF-8")).addString("myForm", myForm).deploy();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            Map<String, Object> map = new HashMap<>();
            map.put("id", processDefinition.getId());
            map.put("category", processDefinition.getCategory());
            map.put("name", processDefinition.getName());
            map.put("key", processDefinition.getKey());
            map.put("description", processDefinition.getDescription());
            map.put("deploymentId", processDefinition.getDeploymentId());
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(map);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "获取流程执行状态图片")
    @ResponseBody
    @RequestMapping(value = "/getCurrentDiagram", method = {RequestMethod.GET})
    public void getCurrentDiagram(HttpServletResponse resp, @ApiParam(value = "流程实例Id", required = true) @RequestParam(required = true) String processInstanceId) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            List<String> activitiIds = runtimeService.getActiveActivityIds(processInstanceId);
            InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
                    .generateDiagram(bpmnModel, "png", activitiIds, new ArrayList<String>(), processEngine.
                                    getProcessEngineConfiguration().getActivityFontName(),
                            processEngine.getProcessEngineConfiguration().getLabelFontName(), null,
                            processEngine.getProcessEngineConfiguration().getClassLoader(), 1.0);
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = imageStream.read(bytes)) > 0) {
                httpServletResponse.getOutputStream().write(bytes, 0, len);
            }
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @ApiOperation(value = "获取流程图片")
    @RequestMapping(value = "/getDiagram", method = {RequestMethod.GET})
    public void getDiagram(HttpServletResponse resp, @ApiParam(value = "流程定义Id", required = true) @RequestParam(required = true) String processDefinitionId) {
        try {
            InputStream imageStream = repositoryService.getProcessDiagram(processDefinitionId);
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = imageStream.read(bytes)) > 0) {
                httpServletResponse.getOutputStream().write(bytes, 0, len);
            }
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "启动请假流程")
    @ResponseBody
    @RequestMapping(value = "/startQingJiaProcess", method = {RequestMethod.POST})
    public ResponseDbCenter startProcess(@ApiParam(value = "流程定义Id", required = true) @RequestParam(required = true) String processDefinitionId,
                                         @ApiParam(value = "启动人Id", required = true) @RequestParam String starter,
                                         @ApiParam(value = "下一个流程处理人Id", required = true) @RequestParam String asignee) {
        try {
            Map map = new HashMap<String, Object>();


            QingJiaBean qingJiaBean = new QingJiaBean();
            qingJiaBean.setStarter(starter);
            qingJiaBean.setAsignee(asignee);
            map.put("qingjia", qingJiaBean);
            identityService.setAuthenticatedUserId(qingJiaBean.getStarter());//流程发起人
            // ProcessInstance processInstance = processEngine.getFormService().submitStartFormData(processDefinitionId,map);
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, map);//普通启动
            Map resultMap = new HashMap<>();
            resultMap.put("id", processInstance.getId());
            resultMap.put("processDefinitionId", processInstance.getProcessDefinitionId());
            resultMap.put("processDefinitionName", processInstance.getProcessDefinitionName());
            resultMap.put("deploymentId", processInstance.getDeploymentId());
            resultMap.put("bussinessKey", processInstance.getBusinessKey());
            resultMap.put("name", processInstance.getName());

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultMap);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "获取我的任务")
    @ResponseBody
    @RequestMapping(value = "/getMyTask", method = {RequestMethod.GET})
    public ResponseDbCenter getMyTask(@ApiParam(value = "用户Id", required = true) @RequestParam(required = true) String userId) {
        try {
            List<Task> taskList = taskService.createTaskQuery().taskAssignee(userId).list();
            List<Map> list = new ArrayList<>();
            for (Task task : taskList) {
                Map map = new HashMap<>();
                map.put("name", task.getName());
                map.put("id", task.getId());
                map.put("category", task.getCategory());
                map.put("description", task.getDescription());
                map.put("processDefinitionId", task.getProcessDefinitionId());
                map.put("processInstanceId", task.getProcessInstanceId());
                map.put("owner", task.getOwner());
                map.put("createTime", task.getCreateTime());
                list.add(map);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "获取任务变量")
    @ResponseBody
    @RequestMapping(value = "/getProcessVariable", method = {RequestMethod.GET})
    public ResponseDbCenter getProcessVariable(@ApiParam(value = "任务ID", required = true) @RequestParam(required = true) String taskId) {
        try {
            Map<String, Object> map = taskService.getVariables(taskId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(map);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "填写请假表单")
    @ResponseBody
    @RequestMapping(value = "/commitAbsentForm", method = {RequestMethod.POST})
    public ResponseDbCenter commitAbsentForm(@ApiParam(value = "任务Id", required = true) @RequestParam(required = true) String taskId,
                                         @ApiParam(value = "请假原因", required = true) @RequestParam String reason,
                                         @ApiParam(value = "请假天数", required = true) @RequestParam Integer days,
                                         @ApiParam(value = "开始时间 2012-10-11 08:00:00", required = true) @RequestParam String startTime,
                                         @ApiParam(value = "结束时间 2012-10-15 18:00:00", required = true) @RequestParam String endTime,
                                         @ApiParam(value = "下一个处理人", required = true) @RequestParam String asignee) {
        try {
            Map<String,Object> variables =   taskService.getVariables(taskId);
            QingJiaBean qingJiaBean= (QingJiaBean) variables.get("qingjia");
            qingJiaBean.setReason(reason);
            qingJiaBean.setDays(days);
            qingJiaBean.setStartTime(startTime);
            qingJiaBean.setEndTime(endTime);
            qingJiaBean.setAsignee(asignee);
            Map<String, Object> map = new HashMap<>();
            map.put("qingjia", qingJiaBean);
            Task task=  taskService.createTaskQuery().taskId(taskId).singleResult();
            List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(task.getProcessInstanceId()).messageEventSubscriptionName("message1").list();

            for (int i = 0; i <executions.size() ; i++) {
                runtimeService.messageEventReceived("message1",executions.get(i).getId());
            }

            taskService.complete(taskId, map);


            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }




    @ApiOperation(value = "部门领导审批")
    @ResponseBody
    @RequestMapping(value = "/departmentVerify", method = {RequestMethod.POST})
    public ResponseDbCenter departmentVerify(@ApiParam(value = "任务Id", required = true) @RequestParam(required = true) String taskId,
                                         @ApiParam(value = "部门领导意见", required = true) @RequestParam String departmentLeaderComment,
                                         @ApiParam(value = "下一个处理人", required = true) @RequestParam String asignee,
                                         @ApiParam(value = "部门领导审批", required = true) @RequestParam Integer departmentLeaderPass) {
        try {
            Map<String,Object> variables =   taskService.getVariables(taskId);
            QingJiaBean qingJiaBean= (QingJiaBean) variables.get("qingjia");
            qingJiaBean.setDepartmentLeaderComment(departmentLeaderComment);
            qingJiaBean.setDepartmentLeaderPass(departmentLeaderPass);
            qingJiaBean.setAsignee(asignee);
            Map<String, Object> map = new HashMap<>();
            map.put("qingjia", qingJiaBean);
            taskService.complete(taskId, map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }
    @ApiOperation(value = "副总审批")
    @ResponseBody
    @RequestMapping(value = "/superviserVerify", method = {RequestMethod.POST})
    public ResponseDbCenter superviserVerify(@ApiParam(value = "任务Id", required = true) @RequestParam(required = true) String taskId,
                                             @ApiParam(value = "副总意见", required = true) @RequestParam String superviserComment,
                                             @ApiParam(value = "下一个处理人", required = true) @RequestParam String asignee,
                                             @ApiParam(value = "superviserPass", required = true) @RequestParam Integer superviserPass) {
        try {
            Map<String,Object> variables =   taskService.getVariables(taskId);
            QingJiaBean qingJiaBean= (QingJiaBean) variables.get("qingjia");
            qingJiaBean.setSuperviserComment(superviserComment);
            qingJiaBean.setSuperviserPass(superviserPass);
            qingJiaBean.setAsignee(asignee);
            Map<String, Object> map = new HashMap<>();
            map.put("qingjia", qingJiaBean);
            taskService.complete(taskId, map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }






    @ApiOperation(value = "考勤人员核实")
    @ResponseBody
    @RequestMapping(value = "/clerkVerify", method = {RequestMethod.POST})
    public ResponseDbCenter clerkVerify(@ApiParam(value = "任务Id", required = true) @RequestParam(required = true) String taskId,
                                             @ApiParam(value = "考勤人员核实结果", required = true) @RequestParam String clerkComment) {
        try {
            Map<String,Object> variables =   taskService.getVariables(taskId);
            QingJiaBean qingJiaBean= (QingJiaBean) variables.get("qingjia");
            qingJiaBean.setClerkComment(clerkComment);
             Map<String, Object> map = new HashMap<>();
            map.put("qingjia", qingJiaBean);
            taskService.complete(taskId, map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }




    @ApiOperation(value = "已完成的流程")
    @ResponseBody
    @RequestMapping(value = "/getHistoryProcessList", method = {RequestMethod.POST})
    public ResponseDbCenter getHistoryProcessList(@ApiParam(value = "用户Id", required = true) @RequestParam(required = true) String userId) {
        try {
            HistoricProcessInstanceQuery query = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery();
            List<HistoricProcessInstance> list = query.startedBy(userId).finished().list();
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (HistoricProcessInstance hi : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", hi.getId());
                map.put("startTime", hi.getStartTime());
                map.put("endTime", hi.getEndTime());
                map.put("name", hi.getName());
                map.put("processDefinitionName", hi.getProcessDefinitionName());
                map.put("description", hi.getDescription());
                map.put("processDefinitionKey", hi.getProcessDefinitionKey());
                resultList.add(map);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultList);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "执行中的流程")
    @ResponseBody
    @RequestMapping(value = "/getActiveProcessList", method = {RequestMethod.POST})
    public ResponseDbCenter getActiveProcessList(@ApiParam(value = "用户Id", required = true) @RequestParam(required = true) String userId) {
        try {
            HistoricProcessInstanceQuery query = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery();
            List<HistoricProcessInstance> list = query.startedBy(userId).unfinished().list();
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (HistoricProcessInstance hi : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", hi.getId());
                map.put("startTime", hi.getStartTime());
                map.put("endTime", hi.getEndTime());
                map.put("name", hi.getName());
                map.put("processDefinitionName", hi.getProcessDefinitionName());
                map.put("description", hi.getDescription());
                map.put("processDefinitionKey", hi.getProcessDefinitionKey());
                resultList.add(map);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultList);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    @ApiOperation(value = "启动消息事件")
    @ResponseBody
    @RequestMapping(value = "/startmsg", method = {RequestMethod.POST})
    public ResponseDbCenter startmsg() {
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("qingjia",new QingJiaBean());
            runtimeService.startProcessInstanceByMessage("startmsg",map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();



             return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    @ApiOperation(value = "启动信号事件")
    @ResponseBody
    @RequestMapping(value = "/startsignal", method = {RequestMethod.POST})
    public ResponseDbCenter startsignal() {
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("qingjia",new QingJiaBean());
            runtimeService.signalEventReceived("signal",map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "启动流程")
    @ResponseBody
    @RequestMapping(value = "/start", method = {RequestMethod.POST})
    public ResponseDbCenter start(@ApiParam(value = "流程定义Id", required = true) @RequestParam(required = true) String processDefinitionId) {
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("qingjia",new QingJiaBean());
            runtimeService.startProcessInstanceById(processDefinitionId,map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


}
