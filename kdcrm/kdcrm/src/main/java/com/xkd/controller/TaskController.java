package com.xkd.controller;


import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.Task;
import com.xkd.service.*;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/task")
public class TaskController {
	
	@Autowired
	TaskInfoService taskService;

	@Autowired
	UserService userService;

	@Autowired
	UserDataPermissionService userDataPermissionService;

	
	@ResponseBody
	@RequestMapping("/saveTask")
	@Transactional
	public ResponseDbCenter saveTask(@RequestBody Task task, String token, HttpServletRequest req) throws Exception{
		if(task == null || StringUtils.isBlank(task.getTaskName()) || StringUtils.isBlank(task.getTaskUserIds())){
			return ResponseConstants.MISSING_PARAMTER;
		}
		Map<String, Object> user = userService.selectUserById(req.getSession().getAttribute(token).toString());
		task.setPcCompanyId(user.get("pcCompanyId")+"");
		task.setDepartmentId(user.get("departmentId")+"");
		task.setCreatedBy(user.get("id").toString());
		taskService.saveTask(task);



		return ResponseConstants.SUCCESS;
	}

	@ResponseBody
	@RequestMapping("/changeTask")
	@Transactional
	public ResponseDbCenter changeTask(@RequestBody Task task, String token, HttpServletRequest req) throws Exception{
		if(task == null || StringUtils.isBlank(task.getId()) || StringUtils.isBlank(task.getTaskName()) || StringUtils.isBlank(task.getTaskUserIds())){
			return ResponseConstants.MISSING_PARAMTER;
		}
		String userId = req.getSession().getAttribute(token).toString();
		task.setUpdatedBy(userId);
		taskService.changeTask(task);
		return ResponseConstants.SUCCESS;
	}

	@ResponseBody
	@RequestMapping("/deleteTask")
	@Transactional
	public ResponseDbCenter deleteTask(String taskIds, String token, HttpServletRequest req) throws Exception{
		if(taskIds == null || StringUtils.isBlank(taskIds)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		String userId = req.getSession().getAttribute(token).toString();
		taskService.deleteTask(taskIds,userId);

		return ResponseConstants.SUCCESS;
	}

	@ResponseBody
	@RequestMapping("/getTaskList")
	public ResponseDbCenter getKaquanList(String currentPage,
										  String pageSize,
										  String taskName,
										  String queryStatus,String token, HttpServletRequest req) throws Exception{
		
		if(StringUtils.isBlank(pageSize) || StringUtils.isBlank(currentPage)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		int  pageSizeInt = Integer.parseInt(pageSize);
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		Map<String, Object> map = new HashMap<>();
		map.put("pageSize", pageSizeInt);
		map.put("pageNo", currentPageInt);
		map.put("taskName", taskName);
		map.put("queryStatus", queryStatus);//1我创建的，2我参与的
		map.put("userId",req.getSession().getAttribute(token).toString());

		/*Map<String, Object> user = userService.selectUserById(req.getSession().getAttribute(token).toString());

		if(!user.get("roleId").equals("1") && user.get("isAdmin").equals("0")){
			map.put("departmentIds", userDataPermissionService.getDataPermissionDepartmentIdList(user.get("departmentId").toString(),user.get("id").toString()));
		}*/
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(taskService.getTaskList(map));
		responseDbCenter.setTotalRows(taskService.getTaskListTotal(map)+"");
		return responseDbCenter;
	}

	@ResponseBody
	@RequestMapping("/getTaskById")
	public ResponseDbCenter getTaskById(String taskId, String token, HttpServletRequest req) throws Exception{
		if(StringUtils.isBlank(taskId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		String userId = req.getSession().getAttribute(token).toString();
		taskService.getTaskById(taskId);
		return ResponseConstants.SUCCESS;
	}
}
