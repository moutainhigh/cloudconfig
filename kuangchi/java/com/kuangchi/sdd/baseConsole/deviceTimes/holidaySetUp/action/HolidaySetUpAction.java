package com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.model.HolidaySetUp;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.service.HolidaySetUpService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.sun.mail.iap.ConnectionException;

@Controller("holidaySetUpAction")
public class HolidaySetUpAction extends BaseActionSupport implements Serializable{
	private static final long serialVersionUID = 1L;

	@Resource(name = "holidaySetUpServiceImpl")
	transient private HolidaySetUpService holidaySetUpService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-9 
	 * @功能描述: 获取设备设置的节假日信息-Action
	 */
	public void getHolidayTimesOfDevice(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<HolidaySetUp> grid = new Grid<HolidaySetUp>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = holidaySetUpService.getHolidayTimesOfDevice(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-9 
	 * @功能描述: 添加设备节假日时间-Action
	 */
	public void addHolidayTimes(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String holiday_date=request.getParameter("holiday_date");
		String day_of_week=request.getParameter("day_of_week");
		String device_num=request.getParameter("device_num");
		
		// 后台判断，不能为空
		if (EmptyUtil.atLeastOneIsEmpty(holiday_date,day_of_week)||day_of_week=="-1") {
			result.setMsg("必输参数不能为空");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		HolidaySetUp holidaySetUp=new HolidaySetUp();
		holidaySetUp.setHoliday_date(holiday_date);
		holidaySetUp.setDay_of_week(day_of_week);
		holidaySetUp.setDevice_num(device_num);
		boolean getMaxHolidayTimesNum=holidaySetUpService.getMaxHolidayTimesNum(holidaySetUp);
		if(getMaxHolidayTimesNum){
			int num=holidaySetUpService.getHolidayDateOfDevice(holidaySetUp);
			if(num>0){
				result.setMsg("该日期已被使用，请修改日期");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return;
			}
		}else{
			result.setMsg("新增失败,节假日时段已达到128个");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		boolean addresult=false;
			try {
				addresult = holidaySetUpService.addHolidayTimes(holidaySetUp, login_User);
				if(addresult){
					result.setMsg("新增节假日时段成功");
					result.setSuccess(true);
				}else{
					result.setMsg("新增失败,节假日时段已达到128个");
					result.setSuccess(false);
				}
			} catch (Exception e) {
				result.setMsg("新增节假日时段失败");
				result.setSuccess(false);
			} finally {
				printHttpServletResponse(GsonUtil.toJson(result));
			}
		
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-10 
	 * @功能描述: 修改设备节假日时段-Action
	 */
	public void modifyHolidayTimes(){
		//根据id修改
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String id=request.getParameter("id");
		String holiday_date=request.getParameter("holiday_date");
		String day_of_week=request.getParameter("day_of_week");
		String device_num=request.getParameter("device_num");
		
		if (EmptyUtil.atLeastOneIsEmpty(holiday_date,day_of_week)||day_of_week=="-1") {
			result.setMsg("必输参数不能为空");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		HolidaySetUp holidaySetUp=new HolidaySetUp();
		holidaySetUp.setId(Integer.valueOf(id));
		holidaySetUp.setHoliday_date(holiday_date);
		holidaySetUp.setDay_of_week(day_of_week);
		holidaySetUp.setDevice_num(device_num);
		int num=holidaySetUpService.getHolidayDateOfDevice(holidaySetUp);
		if(num>0){
			result.setMsg("该日期已被使用，请修改日期");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		boolean modifyResult=false;
		try {
			modifyResult = holidaySetUpService.updateHolidayTimes(holidaySetUp, login_User);
			
			if(modifyResult){
				result.setMsg("修改节假日时段成功");
				result.setSuccess(true);
			}else{
				result.setMsg("修改节假日时段失败");
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setMsg("修改节假日时段失败");
			result.setSuccess(false);
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-10 
	 * @功能描述: 下发设备节假日时段-Action
	 */
	public void addTimesObject(){
		JsonResult result = new JsonResult();
		try {
			HttpServletRequest request = getHttpServletRequest();
			String device_num  = request.getParameter("device_num");
			String device_mac = request.getParameter("device_mac");
			String device_type = request.getParameter("device_type");
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("device_num", device_num);
			map.put("device_mac", device_mac);
			map.put("device_type", device_type);
			
			holidaySetUpService.addTimesObject(map);
			result.setMsg("下发成功");
			result.setSuccess(true);
		} catch (ConnectionException e) {
			result.setMsg("服务器连接异常");
			result.setSuccess(false);
		} catch (Exception e) {
			result.setMsg("下发失败,检查设备是否在线");
			result.setSuccess(false);
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-14 
	 * @功能描述: 判断服务器是否连接或设备是否在线
	 */
	public void isNormalState(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		
		String device_mac=request.getParameter("device_mac");
		String resultMessage=holidaySetUpService.watchHolidayTimes(device_mac);
		if("".equals(resultMessage)){
			result.setMsg("服务器连接异常");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
		}
		if(null==resultMessage || "null".equals(resultMessage)){
			result.setMsg("读取失败，检查设备是否在线");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
		}
		result.setMsg("正常状态");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-10 
	 * @功能描述: 查看设备已下发节假日时段-Action
	 */
	public void watchHolidayTimes(){
		HttpServletRequest request = getHttpServletRequest();
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String device_mac=request.getParameter("device_mac");
		String resultMessage=holidaySetUpService.watchHolidayTimes(device_mac);
		Map getParamResult=GsonUtil.toBean(resultMessage, HashMap.class);
		List<Map> groups=(List) getParamResult.get("groups");
		Grid<HolidaySetUp> grid = new Grid<HolidaySetUp>();
		List<HolidaySetUp> outMapData = new ArrayList<HolidaySetUp>();
		int num=0;
		if(groups!=null && groups.size()!=0){
			for(Map groupsMap : groups){
				Map getParamStart= (Map) groupsMap.get("start");
				String getYear=(getParamStart.get("year").toString()).split("\\.")[0];
				if("0".equals(getYear)) break;
				String getMonth=(getParamStart.get("month").toString()).split("\\.")[0];
				String getDay=(getParamStart.get("day").toString()).split("\\.")[0];
				String year=Integer.toHexString(Integer.valueOf(getYear));
				String month=Integer.toHexString(Integer.valueOf(getMonth));
				String day=Integer.toHexString(Integer.valueOf(getDay));
				if(Integer.valueOf(year)<10){
					year="0"+year;
				}
				if(Integer.valueOf(month)<10){
					month="0"+month;
				}
				if(Integer.valueOf(day)<10){
					day="0"+day;
				}
				String ymd="20"+year+"-"+month+"-"+day;
				String day_of_week=(getParamStart.get("dayOfWeek").toString()).split("\\.")[0];
				String holiday_times_num=String.valueOf(num++);
				
				HolidaySetUp holidaySetUp=new HolidaySetUp();
				holidaySetUp.setHoliday_times_num(holiday_times_num);
				holidaySetUp.setHoliday_date(ymd);
				holidaySetUp.setDay_of_week(day_of_week);
				outMapData.add(holidaySetUp);
			}
		}
		List<HolidaySetUp> newList=null;
		int total=outMapData.size();
        newList=outMapData.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
		grid.setRows(newList);
		grid.setTotal(num);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-12 
	 * @功能描述: 复制节假日时段-Action
	 */
	public void copyHolidayTimes(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String device_name=request.getParameter("device_name"); 
		String device_num=request.getParameter("device_num"); //源设备
		String deviceNums=request.getParameter("deviceNums"); //目标设备
		String [] deviceNum = deviceNums.split(",");
	    for(int i=0;i<deviceNum.length;i++){
	    	boolean msg=holidaySetUpService.copyHolidayTimes(device_num, deviceNum[i]);
	    	if(msg==false){
	    		result.setMsg("复制失败，检查设备"+device_name+"是否有节假日时段");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result)); 
				return;
			}
	    }
	    result.setMsg("复制成功");
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-12 
	 * @功能描述: 复制并下发节假日时段-Action
	 */
	public void copyAndIssuedHolidayTimes(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String device_name=request.getParameter("device_name"); 
		String device_num=request.getParameter("device_num"); //源设备
		String deviceNums=request.getParameter("deviceNums"); //目标设备

		StringBuffer deviceNames=new StringBuffer();
		String [] deviceNum = deviceNums.split(",");
	    for(int i=0;i<deviceNum.length;i++){
	    	boolean msg;
			try {
				msg = holidaySetUpService.copyAndIssuedHolidayTimes(device_num,deviceNum[i]);
				if(msg==false){
					result.setMsg("复制失败，检查设备"+device_name+"是否有节假日时段");
					result.setSuccess(false);
					printHttpServletResponse(GsonUtil.toJson(result)); 
					return;
				}
			} catch (ConnectionException e) {
				result.setMsg("服务器连接异常");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result)); 
				return;
			} catch (Exception e) {
				Map deviceMap=holidaySetUpService.getDeviceMacAndType(deviceNum[i]);
				String deviceName =(String) deviceMap.get("device_name");
				deviceNames.append(deviceName+" ");
			} 
	    }
	    int length=deviceNames.length();
	    if(length>0){
	    	result.setMsg("以下设备无法连接,下发失败:"+deviceNames);
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
	    };
	    result.setMsg("复制并下发成功");
		result.setSuccess(true);
	    printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除节假日时段
	 * @author minting.he
	 */
	public void delHolidayById(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String ids = request.getParameter("ids"); 
		if (EmptyUtil.atLeastOneIsEmpty(ids)) {
			result.setSuccess(false);
			result.setMsg("删除失败，数据不合法");
		}else {
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			String login_user = user.getYhMc();
			if (EmptyUtil.atLeastOneIsEmpty(login_user)) {
				result.setSuccess(false);
				result.setMsg("删除失败，请先登录");
			}else {
				boolean r = holidaySetUpService.delHolidayById(ids, login_user);
				if(r){
					result.setSuccess(true);
					result.setMsg("删除成功");
				}else {
					result.setSuccess(false);
					result.setMsg("删除失败");
				}
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	

}
