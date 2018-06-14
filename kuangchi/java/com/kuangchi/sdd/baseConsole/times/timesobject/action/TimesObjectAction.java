package com.kuangchi.sdd.baseConsole.times.timesobject.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.timesobject.model.TimesObject;
import com.kuangchi.sdd.baseConsole.times.timesobject.service.TimesObjectService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.sun.mail.iap.ConnectionException;

/**
 * @创建人　: 梁豆豆
 * @创建时间: 2016-4-6 下午1:34:13
 * @功能描述: 对象时间组Action
 */
@Controller("timesObjectAction")
public class TimesObjectAction extends BaseActionSupport {
	
	private static final long serialVersionUID = -6309002797333809114L;
	private static final Logger LOG = Logger.getLogger(TimesObjectAction.class);
	
	private TimesObject timesObject;
	
	public TimesObjectAction(){
		timesObject = new TimesObject();
	}
	
	@Resource(name = "timesObjectServiceImpl")
	private TimesObjectService timesObjectService;
	
	@Resource(name = "deviceService")
	DeviceService deviceService;
	
	@Override
	public Object getModel() {
		return timesObject;
	}
	
	//分页查询对象时间组
	public void getTimesObjectByParamPage(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");// 获取前台序列化的数据
		String page = request.getParameter("page");
		String size = request.getParameter("rows");
	
		TimesObject timesObject = GsonUtil.toBean(beanObject, TimesObject.class);
	
		timesObject.setValidity_flag("0");
		
		List<TimesObject> toList = timesObjectService.getTimesObjectByParamPage(timesObject, 
				Integer.valueOf(page), Integer.valueOf(size));

		int allCount = timesObjectService.getTimesObjectByParamCount(timesObject);
		Grid<TimesObject> grid = new Grid<TimesObject>();
		grid.setTotal(allCount);
		grid.setRows(toList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-2 下午2:54:10
	 * @功能描述: 根据设备编号查询节假日时段（分页）
	 * @参数描述:
	 */
	public void getHolidayTimeByDevicePage(){
		
		HttpServletRequest request = getHttpServletRequest();
		String page = request.getParameter("page");
		String size = request.getParameter("rows");
		String device_num = request.getParameter("device_num");
		
		// 当前已下发数据重新下发到服务器
		reAddTimesObject(device_num, "2");
		
		List<HolidayTimesModel> holidayTimeList = timesObjectService.getHolidayTimeByDevicePage(device_num, 
				Integer.valueOf(page), Integer.valueOf(size));
		int allCount = timesObjectService.getHolidayTimeByDeviceCount(device_num);
		Grid<HolidayTimesModel> grid = new Grid<HolidayTimesModel>();
		grid.setTotal(allCount);
		grid.setRows(holidayTimeList);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	}
	
	/**
	 * 查询前重新下发对象时段组，防止与服务器数据不一致
	 * by yuman.gao
	 */
	public void reAddTimesObject(String device_num, String object_type){
		try {
			HttpServletRequest request = getHttpServletRequest();
			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
			
			List<HolidayTimesModel> allHolidayTime = timesObjectService.getHolidayTimeByDevice(device_num);
			StringBuilder group_nums = new StringBuilder();
			if(allHolidayTime != null && allHolidayTime.size()>0){
				for (HolidayTimesModel holidayTimes : allHolidayTime) {
					group_nums.append(holidayTimes.getHoliday_time_num());
					group_nums.append(",");
				}
				group_nums.setLength(group_nums.length()-1);
				
			} 

			DeviceInfo device = deviceService.getDeviceInfoByNum(device_num);
			String device_mac = device.getDevice_mac();
			String device_type = device.getDevice_type();
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("group_nums", group_nums.toString());
			map.put("object_type", object_type);
			map.put("device_num", device_num);
			map.put("device_mac", device_mac);
			map.put("device_type", device_type);
			map.put("loginUserName", loginUser.getYhMc());
			
			timesObjectService.addTimesObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-2 下午2:44:16
	 * @功能描述: 添加对象时段组
	 * @参数描述:
	 */
	public void addTimesObject(){
		JsonResult result = new JsonResult();
		try {
			HttpServletRequest request = getHttpServletRequest();
			
			User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String device_num  = request.getParameter("device_num");
			String group_nums = request.getParameter("group_nums");
			String device_mac = request.getParameter("device_mac");
			String object_type = request.getParameter("object_type");
			String device_type = request.getParameter("device_type");
			
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("device_num", device_num);
			map.put("group_nums", group_nums);
			map.put("device_mac", device_mac);
			map.put("object_type", object_type);
			map.put("device_type", device_type);
			map.put("loginUserName", loginUser.getYhMc());
			
			boolean addResult = timesObjectService.addTimesObject(map);
			if(addResult){
				result.setMsg("下发成功");
				result.setSuccess(true);
			} else {
				result.setMsg("下发失败");
				result.setSuccess(false);
			}
		
		} catch (ConnectionException e) {
			result.setMsg("连接异常");
			result.setSuccess(false);
			
		} catch (Exception e) {
			result.setMsg("下发失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-1 下午6:33:35
	 * @功能描述: 根据时段组编号查询用户时段组名称
	 * @参数描述:
	 */
	public void getGroupNameByNum(){
		HttpServletRequest request = getHttpServletRequest();
		String group_num = request.getParameter("data");
		String group_name = timesObjectService.getGroupNameByNum(group_num);
		printHttpServletResponse(GsonUtil.toJson(group_name));
	}
	
	/** 根据时段组编号查询节假日时段信息*/
	public void getHolidayByGroup(){
		HttpServletRequest request = getHttpServletRequest();
		String holiday_time_num = request.getParameter("data");
		HolidayTimesModel holiday = timesObjectService.getHolidayByNum(holiday_time_num);
		printHttpServletResponse(GsonUtil.toJson(holiday));
	}
   
	/** 查询所有对象时段组下的用户时段组*/
	public void getUserTimesGroup(){
		HttpServletRequest request = getHttpServletRequest();
		String group_name = request.getParameter("group_name");
		String page = request.getParameter("page");
		String size = request.getParameter("rows");
		TimesObject timesObject=new TimesObject();
		if(null!=group_name && !"".equals(group_name)){
	       timesObject.setGroup_name(group_name);
		}
	    
		List<TimesObject> toList = timesObjectService.getUserTimesGroup(timesObject, Integer.valueOf(page), Integer.valueOf(size));

		int allCount = timesObjectService.getUserTimesGroupCount(timesObject);
		Grid<TimesObject> grid = new Grid<TimesObject>();
		grid.setTotal(allCount);
		grid.setRows(toList);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
		
	}
	
	/** 查询所有对象时段组下的节假日时段组*/
	public void getHolidayTimesGroup(){
		HttpServletRequest request = getHttpServletRequest();
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer size = Integer.parseInt(request.getParameter("rows"));
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("page", (page - 1) * size);
		map.put("size", size);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<TimesObject> toList = timesObjectService.getHolidayTimesGroup(map);

		int allCount = timesObjectService.getHolidayTimesGroupCount(map);
		Grid<TimesObject> grid = new Grid<TimesObject>();
		grid.setTotal(allCount);
		grid.setRows(toList);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	}
	
}