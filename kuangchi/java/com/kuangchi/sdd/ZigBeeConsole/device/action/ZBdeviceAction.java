package com.kuangchi.sdd.ZigBeeConsole.device.action;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.device.service.IZBdeviceService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.PropertyUtils;

/**
 * 光子锁记录 - action
 * @author guibo.chen
 */
@Controller("zBdeviceAction")
public class ZBdeviceAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "zBdeviceServiceImpl")
	private IZBdeviceService deviceService;
	
	@Resource(name="startQuertz")
    Scheduler scheduler;
	
	@Override
	public Object getModel() {
		return null;
	}

	//跳转到门信息主页面
		public String toMyZBdevice(){
			return "success";
		}
	
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-18 上午9:50:27
	 * @功能描述: 根据参数查询光子锁记录[分页]
	 * @参数描述:
	 */
	public void getDeviceByParamPage(){
		HttpServletRequest request = getHttpServletRequest();
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String data=request.getParameter("data");
		ZBdeviceModel device=GsonUtil.toBean(data, ZBdeviceModel.class);
		Grid grid=deviceService.getRecordByParamPage(device, page, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//查询网关信息
	public void getZBgateways(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		ZBdeviceModel device=GsonUtil.toBean(data, ZBdeviceModel.class);
		List<ZBdeviceModel> list=deviceService.getZBgatewayByGateway(device);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	
	public void deleteZBDeviceInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String device_id=request.getParameter("device_id");
		//ZBdeviceModel device=GsonUtil.toBean(data, ZBdeviceModel.class);
		JsonResult result = new JsonResult();
		Boolean obj=deviceService.deleteZBdevice(device_id);
		if(obj){
			result.setMsg("删除成功");
  			result.setSuccess(true);
		}else{
			result.setMsg("删除失败");
  			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	
	//查看设备信息
	
	public String viewDeviceInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String device_id=request.getParameter("device_id");
		List<ZBdeviceModel> device=deviceService.getZBdeviceByIp(device_id);
		if(device.size()!=0){
			for (ZBdeviceModel zBdeviceModel : device) {
				if("0".equals(zBdeviceModel.getDevice_state())){
					zBdeviceModel.setDevice_state("在线");
				}else{
					zBdeviceModel.setDevice_state("不在线");
				}
				
				Double device_signal=Double.valueOf(zBdeviceModel.getDevice_signal());
				if(device_signal>65){
					zBdeviceModel.setDevice_signal("弱("+device_signal+")");
				}
				else if(device_signal>55 && device_signal<=65){
					zBdeviceModel.setDevice_signal("中("+device_signal+")");
				}else{
					zBdeviceModel.setDevice_signal("强("+device_signal+")");
				}
				request.setAttribute("device", zBdeviceModel);
			}
		}
		return "view";
	}
	
	//设置panid
	public void editZBPanId(){
		HttpServletRequest request = getHttpServletRequest();
		String data=request.getParameter("data");
		ZBdeviceModel device=GsonUtil.toBean(data, ZBdeviceModel.class);
		
		StringBuffer failDev = new StringBuffer();
		String device_ids = device.getDevice_id();
		for (String device_id : device_ids.split(",")) {
			device.setDevice_id(device_id);
			Boolean obj=deviceService.updateZbPanId(device);
			if(!obj){
				failDev.append("'" + device_id + "',");
			}
		}
		
		JsonResult result = new JsonResult();
		if("".equals(failDev.toString())){
			result.setMsg("设置成功，可能由于与网关Pan_id不一致而无法搜到设备");
			result.setSuccess(true);
		} else {
			failDev.setLength(failDev.length() - 1);
			result.setMsg(failDev);
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//搜索全部设备
	public void serachZBDevice(){
		List<ZBdeviceModel> list=deviceService.getZBgateway(null);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(list.size()!=0){
			resultMap.put("count", list.size());
		}else{
			resultMap.put("count", 0);
		}
		printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
	
	//搜索勾选设备
	public void getserachZBDevice(){
		HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("id");	
		List<ZBdeviceModel> list=deviceService.getZBgateway(id);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(list.size()!=0){
			resultMap.put("count", list.size());
		}else{
			resultMap.put("count", 0);
		}
		printHttpServletResponse(GsonUtil.toJson(resultMap));
		
	}
	
	
	
	/**
	 * 更新系统时间
	 * @author yuman.gao
	 */
	public void updateSystemTime(){
		
		HttpServletRequest request = getHttpServletRequest();
		String device_ids = request.getParameter("device_id");
		
		StringBuffer failDev = new StringBuffer();
		for (String device_id : device_ids.split(",")) {
			boolean setResult = deviceService.setSystemTime(device_id);
			if(!setResult){
				failDev.append("'" + device_id + "',");
			} 
		}
		
		JsonResult result = new JsonResult();
		if("".equals(failDev.toString())){
			result.setMsg("校时成功");
			result.setSuccess(true);
		} else {
			failDev.setLength(failDev.length() - 1);
			result.setMsg(failDev);
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	
	/**
	 * 更新密钥
	 * @author yuman.gao
	 */
	public void setPassword(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		String device_ids = request.getParameter("device_ids");
//		String password = Integer.toHexString(Integer.parseInt(request.getParameter("password")));
		String password = request.getParameter("password");
		
		// 将password转化为8个字节
//		String pasParam = "0000000000";
//		password = pasParam.substring(0, 8-password.length()) + password;
		  
		StringBuffer failDev = new StringBuffer();
		for (String device_id : device_ids.split(",")) {   // 分批设置密钥，若设置失败，则将设备具体信息存入List，返回到前台
			boolean setResult = deviceService.setPassword(device_id, password);
			if(!setResult){
				failDev.append("'" + device_id + "',");
			}
		}
		if("".equals(failDev.toString())){
			result.setMsg("设置成功");
			result.setSuccess(true);
		} else {
			failDev.setLength(failDev.length() - 1);
			result.setMsg(failDev);
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * 查询设备当前时间
	 * @author yuman.gao
	 */
	public void getDeviceTime(){
		HttpServletRequest request = getHttpServletRequest();
		String device_id = request.getParameter("device_id");
		String time = deviceService.getDeviceTime(device_id);
		printHttpServletResponse(GsonUtil.toJson(time)); 
	}
	
	
	//设置小区号
	public void setHouseNumber(){
		JsonResult result = new JsonResult();
		String data=getHttpServletRequest().getParameter("data");
		ZBdeviceModel device = GsonUtil.toBean(data, ZBdeviceModel.class);
		
		String device_ids = device.getDevice_id();
		if(device_ids != null){
			StringBuffer failDev = new StringBuffer();
			for (String device_id : device_ids.split(",")) {
				device.setDevice_id(device_id);
				Boolean obj=deviceService.setHouseNumber(device);  // 分批设置小区号，若设置失败，则将设备具体信息存入List，返回到前台
				if(!obj){
					failDev.append("'" + device_id + "',");
				} 
			}
			if("".equals(failDev.toString())){
				result.setMsg("设置成功");
				result.setSuccess(true);
			} else {
				failDev.setLength(failDev.length() - 1);
				result.setMsg(failDev);
				result.setSuccess(false);
			}
		}
		
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-11-09 下午3:43:57
	 * @功能描述: 设置设备更新在线状态
	 * @参数描述:
	 */
	public void changeDeviceTime() {
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String upMinute = request.getParameter("upMinute");
		//String cronExpression="*/10 * * * * ?";
		String cronExpression=null;
		
		 if(!"".equals(upMinute)|| upMinute!=null){
			 cronExpression = "0 */"+upMinute+" * * * ?";
		 }else{
			 cronExpression = "0 */5 * * * ?";
		 }
		
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "zigbeeDeviceTime", cronExpression,null);
		
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey("updateZBDeviceStateTimer");
				
				CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
				trigger.setCronExpression(cronExpression);
				scheduler.rescheduleJob(triggerKey, trigger);
				result.setSuccess(true);
				result.setMsg("设置成功");
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("设置失败");
				e.printStackTrace();
			}
		} else {
			result.setSuccess(false);
			result.setMsg("设置失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-11-09 下午4:28:39
	 * @功能描述: 查询初始化设备的分秒
	 * @参数描述:
	 */
	public void getInitMinute(){
    	HttpServletRequest request=getHttpServletRequest();
    	String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
    	Properties  property=PropertyUtils.readProperties(propertyFile);
		String cron=property.getProperty("zigbeeDeviceTime");
		String sec=null;
		String minute=null;
		String[] str=cron.split(" ");
		if(str[0].length()>1){
			 sec=str[0].substring(2, str[0].length());
		}else if("*".equals(str[0])){
			sec="0";
		}else{
			sec=str[0];
		}
		if(str[1].length()>1){
			minute=str[1].substring(2, str[1].length());
		}else if("*".equals(str[1])){
			minute="0";
		}else{
			minute=str[1];
		}	
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("minuteTime", minute);
		map.put("secTime", sec);
		printHttpServletResponse(GsonUtil.toJson(map));
    }
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-11-09 下午3:43:57
	 * @功能描述: 设置任务下发时间
	 * @参数描述:
	 */
	public void changeTaskTime() {
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String upMinute=null;
		String upSec =null;
		 upMinute = request.getParameter("upMinutes");
		 upSec = request.getParameter("upSecs");
		 if("".equals(upMinute)|| upMinute==null){
				upMinute="0";
			}
		if("".equals(upSec)|| upSec==null){
				upSec="0";
			}
		String cronExpression=null;
		if("0".equals(upMinute)&&"0".equals(upSec)){
			result.setSuccess(false);
			result.setMsg("请选择合理的设置时间");
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		if("0".equals(upSec)){
			 cronExpression = "0 */"+upMinute+" * * * ?";
		}else if("0".equals(upMinute)){
			 cronExpression = "*/"+upSec+" * * * * ?";
		}else{
			 cronExpression = "0 */5 * * * ?";
		}
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "zigbeeDeviceTaskTime", cronExpression,null);
		
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey("autoFindUnIssuedTaskTimer");
				
				CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
				trigger.setCronExpression(cronExpression);
				scheduler.rescheduleJob(triggerKey, trigger);
				result.setSuccess(true);
				result.setMsg("设置成功");
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("设置失败");
				e.printStackTrace();
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-11-09 下午4:28:39
	 * @功能描述: 查询初始化设备任务的分秒
	 * @参数描述:
	 */
	public void getInitTask(){
    	HttpServletRequest request=getHttpServletRequest();
    	String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
    	Properties  property=PropertyUtils.readProperties(propertyFile);
		String cron=property.getProperty("zigbeeDeviceTaskTime");
		String sec=null;
		String minute=null;
		String[] str=cron.split(" ");
		if(str[0].length()>1){
			 sec=str[0].substring(2, str[0].length());
		}else if("*".equals(str[0])){
			sec="0";
		}else{
			sec=str[0];
		}
		if(str[1].length()>1){
			minute=str[1].substring(2, str[1].length());
		}else if("*".equals(str[1])){
			minute="0";
		}else{
			minute=str[1];
		}	
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("minuteTime", minute);
		map.put("secTime", sec);
		printHttpServletResponse(GsonUtil.toJson(map));
    }
	
	//点击刷新
	public void reLoaddeviceTime(){
		
		HttpServletRequest request = getHttpServletRequest();
		String device_ids=request.getParameter("device_id");
		//ZBdeviceModel device=GsonUtil.toBean(data, ZBdeviceModel.class);
		
		for (String device_id : device_ids.split(",")) {
			Boolean obj=deviceService.reloadDevice(device_id);
		}
		JsonResult result = new JsonResult();
		result.setMsg("刷新成功");
		result.setSuccess(true);
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
}	
	
	//查询设备信号强度  by huixian.pan
	public void getDeviceSignalRange(){
		List<Map> list=deviceService.getDeviceSignalRange();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	//设置设备信号强度  by huixian.pan
	public void setSignalDegree(){
		Gson gson=new Gson();
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		try {
			Map map1 = gson.fromJson(request.getParameter("range1"), HashMap.class);
			Map map2 = gson.fromJson(request.getParameter("range2"), HashMap.class);
			Map map3 = gson.fromJson(request.getParameter("range3"), HashMap.class);
			List<Map> list=new ArrayList<Map>();
			list.add(map1);
			list.add(map2);
			list.add(map3);
			Boolean obj=deviceService.updDeviceSignalRange(list);
			result.setSuccess(obj);
		} catch (Exception e) {
			e.printStackTrace();	
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//查询失败设备信息  by huixian.pan
	public void  getFailureZBdeviceInfo(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		String id = request.getParameter("id");
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		
		Map map = new HashMap();
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		map.put("id", id);
		Grid grid=deviceService.getFailureZBdeviceInfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
}
