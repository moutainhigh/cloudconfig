package com.kuangchi.sdd.consumeConsole.terminalTask.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.device.service.IDeviceService;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskHistoryModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.CardTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.model.TerminalTaskModel;
import com.kuangchi.sdd.consumeConsole.terminalTask.service.TerminalTaskService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("terminalTaskAction")
public class TerminalTaskAction extends BaseActionSupport {
	@Resource(name = "terminalTaskServiceImpl")
	private TerminalTaskService terminalTaskService;
	
	@Resource(name = "cDeviceService")
	IDeviceService deviceService;

	
	@Override
	public Object getModel() {
		return null;
	}
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-25 下午3:23:37
	 * @功能描述:获取终端任务 
	 * @参数描述:
	 *//*
	public void getAllTerminalTask(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<TerminalTaskModel> grid = new Grid<TerminalTaskModel>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("rows", rows);
		map.put("skips", skips);
		grid = terminalTaskService.getAllTerminalTask(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}*/
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-25 下午3:54:57
	 * @功能描述: 获取正在执行（未完成）的任务
	 * @参数描述:
	 */
	public void getAllCardTask(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("rows", rows);
		map.put("skips", skips);
		Grid<CardTaskModel> grid = terminalTaskService.getAllCardTask(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-29 下午4:27:17
	 * @功能描述: 获取已完成的任务
	 * @参数描述:
	 */
	public void getAllCardTaskHistory(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<CardTaskHistoryModel> grid = new Grid<CardTaskHistoryModel>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("rows", rows);
		map.put("skips", skips);
		grid = terminalTaskService.getAllCardTaskHistory(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	public void reDistributes(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_num;
		String card_num;
		String id;
		String flag;
		String validity_flag;
		String trigger_flag;
		
		String deviceNums = request.getParameter("device_nums");
		String cardNums = request.getParameter("card_nums");
		String ids = request.getParameter("ids");
		String flags = request.getParameter("flags");
		
		String[] id_s = ids.split(",");
		String[] device_nums = deviceNums.split(",");
		String[] card_nums = cardNums.split(",");
		String[] f = flags.split(",");
		for(int i=0;i<device_nums.length; i++){
			device_num = device_nums[i];
			card_num = card_nums[i];
			id = id_s[i];
			flag = f[i];
			if("0".equals(flag)){
				validity_flag = "0";
				trigger_flag = "8";
			}else {
				validity_flag = "1";
				trigger_flag = "9";
			}
			try{
				deviceService.insertNameTask(device_num, card_num, validity_flag, trigger_flag);
			    terminalTaskService.delCardTaskHistory(id);
				result.setMsg("重新下发名单成功");
				result.setSuccess(true);
				printHttpServletResponse(GsonUtil.toJson(result));
			}catch(Exception e){
				e.printStackTrace();
				result.setMsg("重新下发名单失败");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}
	}
	
	
	public void cancelDistributes(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String device_num;
		String card_num;
		String id;
		String flag;
		String validity_flag = "1";
		String trigger_flag = "9";
		
		String deviceNums = request.getParameter("device_nums");
		String cardNums = request.getParameter("card_nums");
		String ids = request.getParameter("ids");
		String flags = request.getParameter("flags");
		
		String[] id_s = ids.split(",");
		String[] device_nums = deviceNums.split(",");
		String[] card_nums = cardNums.split(",");
		String[] f = flags.split(",");
		for(int i=0;i<device_nums.length; i++){
			device_num = device_nums[i];
			card_num = card_nums[i];
			id = id_s[i];
			flag = f[i];
			
			try{
			/*	deviceService.insertNameTask(device_num, card_num, validity_flag, trigger_flag);	*/
			    boolean res = terminalTaskService.cancelDistributes(id);
			    if(res){
			    	result.setMsg("取消下发名单成功");
					result.setSuccess(true);
			    }else {
			    	result.setMsg("取消下发名单失败");
					result.setSuccess(false);
			    }
				printHttpServletResponse(GsonUtil.toJson(result));
			}catch(Exception e){
				e.printStackTrace();
				result.setMsg("取消下发名单失败");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		}
	}
}
