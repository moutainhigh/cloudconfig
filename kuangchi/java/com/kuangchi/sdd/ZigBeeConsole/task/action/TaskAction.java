package com.kuangchi.sdd.ZigBeeConsole.task.action;





import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.ZigBeeConsole.task.service.ITaskService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * 光子锁任务管理- action
 * @author yuman.gao
 */
@Controller("ZigBeeTaskAction")
public class TaskAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "ZigBeeTaskServiceImpl")
	private ITaskService taskService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 根据参数查询任务表
	 * @author yuman.gao
	 */
	public void getZBTaskByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map<String, Object> param = GsonUtil.toBean(data, HashMap.class);
		if(param == null){
			param = new HashMap<String, Object>();
		}
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		param.put("page", (page - 1) * rows);
		param.put("rows", rows);
		
		List<Map> taskList =  taskService.getZBTaskByParamPage(param);
		Integer taskCount = taskService.getZBTaskByParamCount(param);
		
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(taskList);
		grid.setTotal(taskCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	/**
	 * 根据参数查询任务历史表
	 * @author yuman.gao
	 */
	public void getZBTaskHisByParamPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map<String, Object> param = GsonUtil.toBean(data, HashMap.class);
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		param.put("page", (page - 1) * rows);
		param.put("rows", rows);
		
		List<Map> taskHisList =  taskService.getZBTaskHisByParamPage(param);
		Integer taskHisCount = taskService.getZBTaskHisByParamCount(param);
		
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(taskHisList);
		grid.setTotal(taskHisCount);
		printHttpServletResponse(new Gson().toJson(grid));
	}
	/**
	 * 根据参数查询权限表
	 * @author chudan.guo
	 */
	public void getAuthorityPage(){
		
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map<String, Object> param = GsonUtil.toBean(data, HashMap.class);
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		param.put("page", (page - 1) * rows);
		param.put("rows", rows);
		
		List<Map> taskHisList =  taskService.getAuthorityPage(param);
		Integer taskHisCount = taskService.getAuthorityPageCount(param);
		
		Grid<Map> grid = new Grid<Map>();
		grid.setRows(taskHisList);
		grid.setTotal(taskHisCount);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
}
