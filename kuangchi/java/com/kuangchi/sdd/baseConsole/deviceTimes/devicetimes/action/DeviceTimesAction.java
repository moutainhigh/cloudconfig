package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceAttriModel;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;
import com.kuangchi.sdd.baseConsole.deviceGroup.service.DeviceGroupService;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.service.DeviceTimesService;
import com.kuangchi.sdd.baseConsole.deviceTimes.holidaySetUp.service.HolidaySetUpService;
import com.kuangchi.sdd.baseConsole.deviceTimes.timesGroup.service.TimesGroupService;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;
import com.sun.mail.iap.ConnectionException;

/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-9 下午4:53:42
 * @功能描述: 时段管理模块Action
 */
@Controller("DevicetimesAction")
public class DeviceTimesAction extends BaseActionSupport {
	
	private static final long serialVersionUID = -6309002797333809114L;
	private static final Logger LOG = Logger.getLogger(DeviceTimesAction.class);

	@Resource(name = "DevicetimesServiceImpl")
	private DeviceTimesService devicetimesService;
	private DeviceTimes times;
	
	@Resource(name="deviceGroupService")
	DeviceGroupService deviceGroupService;

	@Resource(name = "deviceTimesGroupService")
	private TimesGroupService timesGroupService;
	
	@Resource(name = "holidaySetUpServiceImpl")
	private HolidaySetUpService holidaySetUpService;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	@Autowired
	DeviceService deviceService;  
	
	@Override
	public Object getModel() {
		return times;
	}
	
	public String showOpera(){
		return "success";
	}
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-10上午9:35:21
	 * @功能描述: 根据条件查询(页面分页显示)
	 * @参数描述:
	 */
	public void getTimesByParamPage() {

		String page = getHttpServletRequest().getParameter("page");
		String size = getHttpServletRequest().getParameter("rows");
		String begin_time = getHttpServletRequest().getParameter("begin_time");
		String end_time = getHttpServletRequest().getParameter("end_time");
		String beanObject = getHttpServletRequest().getParameter("data"); 
		
		DeviceTimes times = GsonUtil.toBean(beanObject, DeviceTimes.class);
		times.setBegin_time(begin_time);
		times.setEnd_time(end_time);
		
		List<DeviceTimes> timesList = devicetimesService.getTimesByParamPage(times, Integer.valueOf(page), Integer.valueOf(size));
		
		for(int i=0;i<timesList.size();i++){
			String begin_time_str = timesList.get(i).getBegin_time();
			String end_time_str = timesList.get(i).getEnd_time();
			timesList.get(i).setBegin_time(begin_time_str.substring(0,2)+":"+begin_time_str.substring(2, 4));
			timesList.get(i).setEnd_time(end_time_str.substring(0,2)+":"+end_time_str.substring(2, 4));
		}
		int allCount = devicetimesService.getTimesByParamCount(times);
		Grid<DeviceTimes> grid = new Grid<DeviceTimes>();
		grid.setTotal(allCount);
		grid.setRows(timesList);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-10 下午3:46:45
	 * @功能描述: 增加时段组
	 * @参数描述:
	 */
	public void addTimes(){
		
		JsonResult result = new JsonResult();
		
		try {
			User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String login_User = loginUser.getYhMc();
			String begin_time = getHttpServletRequest().getParameter("begin_time");
			String end_time = getHttpServletRequest().getParameter("end_time");
			String device_num=getHttpServletRequest().getParameter("device_num");
			DeviceTimes times = new DeviceTimes();
			times.setBegin_time(begin_time);
			times.setEnd_time(end_time);
			times.setDevice_num(device_num);
			boolean addResult = devicetimesService.addDeviceTimes(times,login_User);
			
			if(addResult){
				result.setMsg("增加时间段成功");
				result.setSuccess(true);
			}else{
				result.setMsg("编号已达最大值，不可添加");
				result.setSuccess(false);
			}
			
		} catch (Exception e) {
			result.setMsg("增加失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * @创建人　:陈桂波
	 * @创建时间: 2016-10-10 下午5:12:14
	 * @功能描述: 修改时间段
	 * @参数描述:
	 */
	public void modifyTimes(){
		
		JsonResult result = new JsonResult();
		try {
			User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String login_User = loginUser.getYhMc();
			String begin_time = getHttpServletRequest().getParameter("begin_time");
			String end_time = getHttpServletRequest().getParameter("end_time");
			Integer times_id = Integer.valueOf(getHttpServletRequest().getParameter("times_id"));
			
			DeviceTimes times = new DeviceTimes();
			times.setBegin_time(begin_time);
			times.setEnd_time(end_time);
			times.setId(times_id);
			boolean modifyResult = devicetimesService.modifyTimes(times,login_User);
			
			if(modifyResult){
				result.setMsg("修改时间段成功");
				result.setSuccess(true);
			} else {
				result.setMsg("修改时间段失败");
				result.setSuccess(false);
			}
		}  catch (Exception e) {
			result.setMsg("修改失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * 时段下发
	 * @author yuman.gao
	 */
	public void issuedTime(){
		JsonResult result = new JsonResult();
		
		try {
			String device_num = getHttpServletRequest().getParameter("device_num");
			devicetimesService.issuedTime(device_num);
			result.setMsg("下发成功");
			result.setSuccess(true);
			
		} catch (ConnectionException e) {
			result.setMsg("连接异常");
			result.setSuccess(false);
			
		} catch (Exception e) {
			result.setMsg("设备未连接,下发失败");
			result.setSuccess(false);
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-10-14 
	 * @功能描述: 判断服务器是否连接或设备是否能在线
	 */
	public void isNormalState(){
		JsonResult result = new JsonResult();
		String device_num = getHttpServletRequest().getParameter("device_num");
		String resultMessage=devicetimesService.isNormalState(device_num);
		if("".equals(resultMessage)){
			result.setMsg("服务器连接异常");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
		}
		if(null==resultMessage){
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
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-11 下午9:46:45
	 * @功能描述: 查看已下发的时段
	 * @参数描述:
	 */
	
	public void getissuedTime(){
		String pages = getHttpServletRequest().getParameter("page");
		String size = getHttpServletRequest().getParameter("rows");
			//int page = 1;
			//int rows = 10;
			//if(page!=null&&size!=null){
				int page=Integer.valueOf(pages);
				int rows=Integer.valueOf(size);
				
			//}
			List<DeviceTimes> newList=null;
			Grid grid=new Grid();
			String device_num = getHttpServletRequest().getParameter("device_num");
			/*List<DeviceTimes> newLists=(List<DeviceTimes>) getHttpServletRequest().getSession().getAttribute("TimeGroups");
			if(newLists!=null){
				int total=newLists.size();
				newList=newLists.subList(rows*(page-1), (rows*page)>total?total:(rows*page));
				  grid.setRows(newLists);
			      grid.setTotal(total);
				printHttpServletResponse(GsonUtil.toJson(newLists));
				return;
			}*/
			
			List<DeviceTimes> list=devicetimesService.getDeviceMacByNums2(device_num);
			getHttpServletRequest().getSession().setAttribute("TimeGroups", list);
			
			if(list!=null){
				int total=0;
					total=list.size();
				newList=list.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
				for(int i=0; i<newList.size(); i++){
					if("0".equals(newList.get(i).getTimes_num())){
						newList.get(i).setTimes_num(null);
						newList.get(i).setBegin_time(null);
						newList.get(i).setEnd_time(null);
						//newList.remove(i);
						break;
					}
				}
		        grid.setRows(newList);
		        grid.setTotal(total);
			}
			printHttpServletResponse(GsonUtil.toJson(grid));
			/*DeviceTimes times =new DeviceTimes();
			times.setDevice_num(device_num);
			List<DeviceTimes> timesList = devicetimesService.getTimesByParamPage(times, Integer.valueOf(page), Integer.valueOf(size));
			
			for(int i=0;i<timesList.size();i++){
				String begin_time_str = timesList.get(i).getBegin_time();
				String end_time_str = timesList.get(i).getEnd_time();
				timesList.get(i).setBegin_time(begin_time_str.substring(0,2)+":"+begin_time_str.substring(2, 4));
				timesList.get(i).setEnd_time(end_time_str.substring(0,2)+":"+end_time_str.substring(2, 4));
			}
			int allCount = devicetimesService.getTimesByParamCount(times);
			Grid<DeviceTimes> grid = new Grid<DeviceTimes>();
			grid.setTotal(allCount);
			grid.setRows(timesList);
			printHttpServletResponse(GsonUtil.toJson(grid));*/
	} 
	
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-12 下午10:46:45
	 * @功能描述: 保存复制时段
	 * @参数描述:
	 */
	public void copeissuedTime(){
		JsonResult result = new JsonResult();
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String login_User = loginUser.getYhMc();
		String device_num = getHttpServletRequest().getParameter("device_num");
		String deviceNums = getHttpServletRequest().getParameter("deviceNums");
		List<DeviceTimes> list=devicetimesService.getDeviceTimesByDeviceNum(device_num);
		Boolean addResult=false;
		try {
			if(list.size()!=0){
				String[] str=deviceNums.split(",");
				for (int i = 0; i < str.length; i++) {
					devicetimesService.deleteDeviceTimesByDeviceNum(str[i]);
					for (int j = 0; j < list.size(); j++) {
						DeviceTimes times=new DeviceTimes();
						times.setTimes_num(list.get(j).getTimes_num());
						times.setBegin_time(list.get(j).getBegin_time());
						times.setEnd_time(list.get(j).getEnd_time());
						times.setDevice_num(str[i]);
						times.setEare_num(list.get(j).getEare_num());
						addResult=devicetimesService.copeAddDeviceTimes(times, login_User);
					}
				}
				
			}else{
				String[] str=deviceNums.split(",");
				for (int i = 0; i < str.length; i++) {
					 addResult=devicetimesService.deleteDeviceTimesByDeviceNum(str[i]);
					}
			}
			
			if(addResult){
				result.setMsg("复制时间段成功");
				result.setSuccess(true);
			}else{
				result.setMsg("复制时间段失败");
				result.setSuccess(false);
			}
		} catch (Exception e) {
			result.setMsg("复制时间段失败");
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	/**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-10-12 下午10:46:45
	 * @功能描述: 保存复制并下发时段
	 * @参数描述:
	 */
	public void copeissuedTimeAndSend(){
		JsonResult result = new JsonResult();
		//User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		//String login_User = loginUser.getYhMc();
		String device_num = getHttpServletRequest().getParameter("device_num");
		String deviceNums = getHttpServletRequest().getParameter("deviceNums");
		Map<String,Object> strs=new HashMap<String, Object>();
		List<DeviceInfo> device=null;
		
			String[] str=deviceNums.split(",");
			for (int i = 0; i < str.length; i++) {
				 device=devicetimesService.getDeviceByNums(str[i]);
				try {
					devicetimesService.getDeviceTimesByDeviceNums(device_num,str[i]);
				} catch (ConnectionException e) {
					result.setMsg("连接异常");
					result.setSuccess(false);
				}catch (Exception e) {
					for (DeviceInfo deviceInfo : device) {
						strs.put("device_name",deviceInfo.getDevice_name()+",");
					}
					//throw new RuntimeException();
				}
			}
				result.setMsg("保存并下发成功");
				result.setSuccess(true);
		if(strs.size()!=0){
			result.setMsg("以下设备无法连接,下发失败:"+strs.get("device_name"));
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-17 下午5:07:59
	 * @功能描述: 复制门禁设置
	 * @参数描述:
	 */
	public void copyDoorParamAndSave(){
		User loginUser = (User)getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		String device_num = getHttpServletRequest().getParameter("device_num");//被复制的设备
		
		String deviceNums = getHttpServletRequest().getParameter("deviceNums");//复制的多个设备
		String flag = getHttpServletRequest().getParameter("flag");
		String[] flagValid = flag.split(",");
		String[] str=deviceNums.split(",");
		//Map<String,Object> strs=new HashMap<String, Object>();
	/*	StringBuffer failDeviceNum = new StringBuffer();
	    String device_name = getHttpServletRequest().getParameter("device_name");
		StringBuffer deviceNames=new StringBuffer();*/
		DeviceAttriModel deviceAttri =  deviceService.getDeviceAttributeByDeviceNum(device_num);

		String resultSucc = "";
		String resultFail = "";
		JsonResult jsonResult = new JsonResult();
		for(String target_num:str){//选中的多个设备
			boolean result0 = true;
			boolean result1 = true;
			boolean result2 = true;
			boolean result3 = true;
			boolean result4 = true;
			boolean result5 = true;
			boolean result6 = true;
			Map mapTarget = deviceService.getMacByDeviceNum(target_num);
			String targetMac = (String) mapTarget.get("deviceMac");
			String targetDeviceType = (String) mapTarget.get("deviceType");
			String targetDeviceName = (String) mapTarget.get("deviceName");
		for(int i=0;i<flagValid.length;i++){//选中多个要复制的选项
				if("0".equals(flagValid[i])){//复制时段
					//List<DeviceInfo> device=devicetimesService.getDeviceByNums(target_num);
					try {
						result0 = devicetimesService.getDeviceTimesByDeviceNums(device_num,target_num);
					} catch (ConnectionException e) {
						result0 = false;
					}catch (Exception e) {
						result0 = false;
						e.printStackTrace();
					}
				}else if("1".equals(flagValid[i])){//消防联动
					
					  /*Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
					  String fireUrl=mjCommService.getMjCommUrl(target_num)+"fireAction/setFire.do?";
//					  String setInOutUrl=mjCommService.getMjCommUrl(target_num)+"doubleInOut/setDoubleInOut.do?";
					  String useFireFighting = deviceAttri.getFireFlag();
//					  StringBuilder build=new StringBuilder();
//					  
//					  build.append(deviceAttri.getFourLockControlFlag());
//					  build.append(deviceAttri.getThreeLockControlFlag());
//					  build.append(deviceAttri.getTwoLockControlFlag());
//					  build.append(deviceAttri.getOneLockControlFlag());
//					  build.append(deviceAttri.getTwoOutControlFlag());
//					  build.append(deviceAttri.getOneOutControlFlag());
//					  
//					  String inOutParam=tranBinStrToDeciStr(build.toString());
					  String  fireRespStr=HttpRequest.sendPost(fireUrl, "useFireFighting="+useFireFighting+"&mac="+targetMac+"&sign="+targetDeviceType);
//					  String  setInOutRespStr=HttpRequest.sendPost(setInOutUrl, "inOut="+inOutParam+"&mac="+targetMac+"&deviceType="+targetDeviceType);
					
					  Map fireMap=GsonUtil.toBean(fireRespStr,HashMap.class);
//					  Map inOutMap=GsonUtil.toBean(setInOutRespStr,HashMap.class);
					  
					  if(fireMap!=null/*&&inOutMap!=null*/){
						  if("0".equals(fireMap.get("result_code")) /*&& "0".equals(inOutMap.get("result_code"))*/ ){
							  deviceService.setDeviceAttribute(target_num, deviceAttri.getHeaderCardFlag(), deviceAttri.getOneOutControlFlag(), deviceAttri.getTwoOutControlFlag(), deviceAttri.getOneLockControlFlag(), deviceAttri.getTwoLockControlFlag(), deviceAttri.getThreeLockControlFlag(), deviceAttri.getFourLockControlFlag(), deviceAttri.getDelayOpenDoorTime(), deviceAttri.getFireFlag(),loginUser.getYhMc());
						  }else{
							  result1 = false;
						  }
					  }else{
						  result1 = false;
					  }
					  
				}else if("2".equals(flagValid[i])){//实时上传参数
					String mac = deviceAttri.getDeviceMac();
					String device_type = deviceAttri.getDeviceType();
				/*	Map<String, String> map0 = PropertiesToMap
							.propertyToMap("comm_interface.properties");*/
					String actualtimeUrl0 = mjCommService.getMjCommUrl(device_num)
							+ "actualTimeAction/getActualTime.do";
					String actualtimeStr0 = HttpRequest.sendPost(actualtimeUrl0, "mac=" + mac
							+ "&device_type=" + device_type);
					Map actualtimeResult0 = GsonUtil.toBean(actualtimeStr0, HashMap.class);
					if(actualtimeResult0!=null && "0".equals(actualtimeResult0.get("result_code"))){
						Map data = (Map) actualtimeResult0.get("allActualTime");
						double acTime1 = (Double)data.get("actualTime");
						int acTime = (int) acTime1;
						double intervaltime = ((double)(acTime&255))/10;//上传间隔时间
						Integer actualStatus = null;
						Integer doorEvent = null;
						Integer cardRecord = null;

						if(acTime>>8==1){	
							 actualStatus = 1;
							 doorEvent = 0;
							 cardRecord = 0;
						}else if(acTime>>8==2){
							 actualStatus =0;
							 doorEvent = 2;
							 cardRecord = 0;
						}else if(acTime>>8==3){
							 actualStatus =1;
							 doorEvent = 2;
							 cardRecord = 0;
						}else if(acTime>>8==4){
							 actualStatus =0;
							 doorEvent = 0;
							 cardRecord = 4;
						}else if(acTime>>8==5){
							 actualStatus =1;
							 doorEvent = 0;
							 cardRecord = 4;
						}else if(acTime>>8==6){
							 actualStatus = 0;
							 doorEvent = 2;
							 cardRecord = 4;
						}else if(acTime>>8==7){
							 actualStatus = 1;
							 doorEvent = 2;
							 cardRecord = 4;
						}
						Integer actualtime = actualStatus | doorEvent | cardRecord;// 做按位或运算
					/*	Map<String, String> map = PropertiesToMap
								.propertyToMap("comm_interface.properties");*/
						String actualtimeUrl = mjCommService.getMjCommUrl(target_num)
								+ "actualTimeAction/setActualTime.do";
						String actualtimeStr = HttpRequest.sendPost(actualtimeUrl, "mac=" + targetMac
								+ "&device_type=" + targetDeviceType + "&actual_time=" + actualtime
								+ "&intervaltime=" + intervaltime);
						Map actualtimeResult = GsonUtil.toBean(actualtimeStr, HashMap.class);
						if(actualtimeResult!=null){
							if ("0".equals(actualtimeResult.get("result_code"))) {
								result2 = true;
							} else {
								result2 = false;
							}
						}else {
							result1 = false;
						}
					}else {
						result2 = false;
					}
				}else if("3".equals(flagValid[i])){//禁止设备上报
					/*List list = new ArrayList();*/
					String mac = deviceAttri.getDeviceMac();
					String device_type = deviceAttri.getDeviceType();
					/*Map<String, String> map = PropertiesToMap
							.propertyToMap("comm_interface.properties");*/
					String forbidDevReportUrl = mjCommService.getMjCommUrl(device_num)
							+ "forbidDevReportAction/getForbidDevReport.do";
					String forbidDevStr = HttpRequest.sendPost(forbidDevReportUrl, "mac="
							+ mac + "&device_type=" + device_type);
					Map forbidResult = GsonUtil.toBean(forbidDevStr, HashMap.class);
					if(forbidResult!=null){
						Object str1 = forbidResult.get("timeForbids");
						Double enableDouble = (Double) forbidResult.get("enable");
						String enable = Integer.toHexString(enableDouble.intValue());
						ArrayList arrays = GsonUtil.toBean(str1.toString(), ArrayList.class);
						Map maps1 = GsonUtil.toBean(arrays.get(0).toString(), HashMap.class);
						Map maps2 = GsonUtil.toBean(arrays.get(1).toString(), HashMap.class);
						Map maps3 = GsonUtil.toBean(arrays.get(2).toString(), HashMap.class);
						Map maps4 = GsonUtil.toBean(arrays.get(3).toString(), HashMap.class);
						
						String begin_time_hour = Integer.toHexString(((Double) maps1
								.get("startHour")).intValue());
						String begin_time_minute = Integer.toHexString(((Double) maps1
								.get("startMinute")).intValue());
						String end_time_hour = Integer.toHexString(((Double) maps1
								.get("endHour")).intValue());
						String end_time_minute = Integer.toHexString(((Double) maps1
								.get("endMinute")).intValue());
						
						String begin_time_hour1 = Integer.toHexString(((Double) maps2
								.get("startHour")).intValue());
						String begin_time_minute1 = Integer.toHexString(((Double) maps2
								.get("startMinute")).intValue());
						String end_time_hour1 = Integer.toHexString(((Double) maps2
								.get("endHour")).intValue());
						String end_time_minute1 = Integer.toHexString(((Double) maps2
								.get("endMinute")).intValue());
						
						String begin_time_hour2 = Integer.toHexString(((Double) maps3
								.get("startHour")).intValue());
						String begin_time_minute2 = Integer.toHexString(((Double) maps3
								.get("startMinute")).intValue());
						String end_time_hour2 = Integer.toHexString(((Double) maps3
								.get("endHour")).intValue());
						String end_time_minute2 = Integer.toHexString(((Double) maps3
								.get("endMinute")).intValue());
						
						
						String begin_time_hour3 = Integer.toHexString(((Double) maps4
								.get("startHour")).intValue());
						String begin_time_minute3 = Integer.toHexString(((Double) maps4
								.get("startMinute")).intValue());
						String end_time_hour3 = Integer.toHexString(((Double) maps4
								.get("endHour")).intValue());
						String end_time_minute3 = Integer.toHexString(((Double) maps4
								.get("endMinute")).intValue());
						
						
						/*List<Map> list = new ArrayList();*/
						
							if(Integer.parseInt(begin_time_hour)<10){
		                		begin_time_hour="0"+begin_time_hour;
		                	}
		                	if(Integer.parseInt(end_time_hour)<10){
		                		end_time_hour="0"+end_time_hour;
		                	}
		                	if(Integer.parseInt(begin_time_minute)<10){
		                		begin_time_minute="0"+begin_time_minute;
		                	}
		                	if(Integer.parseInt(end_time_minute)<10){
		                		end_time_minute="0"+end_time_minute;
		                	}
						
							if(Integer.parseInt(begin_time_hour1)<10){
		                		begin_time_hour1="0"+begin_time_hour1;
		                	}
		                	if(Integer.parseInt(end_time_hour1)<10){
		                		end_time_hour1="0"+end_time_hour1;
		                	}
		                	if(Integer.parseInt(begin_time_minute1)<10){
		                		begin_time_minute1="0"+begin_time_minute1;
		                	}
		                	if(Integer.parseInt(end_time_minute1)<10){
		                		end_time_minute1="0"+end_time_minute1;
		                	}

		                	if(Integer.parseInt(begin_time_hour2)<10){
		                		begin_time_hour2="0"+begin_time_hour2;
		                	}
		                	if(Integer.parseInt(end_time_hour2)<10){
		                		end_time_hour2="0"+end_time_hour2;
		                	}
		                	if(Integer.parseInt(begin_time_minute2)<10){
		                		begin_time_minute2="0"+begin_time_minute2;
		                	}
		                	if(Integer.parseInt(end_time_minute2)<10){
		                		end_time_minute2="0"+end_time_minute2;
		                	}
		                	
		                	if(Integer.parseInt(begin_time_hour3)<10){
		                		begin_time_hour3="0"+begin_time_hour3;
		                	}
		                	if(Integer.parseInt(end_time_hour3)<10){
		                		end_time_hour3="0"+end_time_hour1;
		                	}
		                	if(Integer.parseInt(begin_time_minute3)<10){
		                		begin_time_minute3="0"+begin_time_minute3;
		                	}
		                	if(Integer.parseInt(end_time_minute3)<10){
		                		end_time_minute3="0"+end_time_minute3;
		                	}

		                	
		                	
		                	Map mapTime = new HashMap();
		                	mapTime.put("begin_time_hour1", begin_time_hour1);
		                	mapTime.put("end_time_hour1", end_time_hour1);
		                	mapTime.put("begin_time_minute1", begin_time_minute1);
		                	mapTime.put("end_time_minute1", end_time_minute1);
		                	
		                	mapTime.put("begin_time_hour2", begin_time_hour2);
		                	mapTime.put("end_time_hour2", end_time_hour2);
		                	mapTime.put("begin_time_minute2", begin_time_minute2);
		                	mapTime.put("end_time_minute2", end_time_minute2);
		                	
		                	mapTime.put("begin_time_hour3", begin_time_hour3);
		                	mapTime.put("end_time_hour3", end_time_hour3);
		                	mapTime.put("begin_time_minute3", begin_time_minute3);
		                	mapTime.put("end_time_minute3", end_time_minute3);
		                	
		                	mapTime.put("begin_time_hour", begin_time_hour);
		                	mapTime.put("end_time_hour", end_time_hour);
		                	mapTime.put("begin_time_minute", begin_time_minute);
		                	mapTime.put("end_time_minute", end_time_minute);
		                	
		                	String data = GsonUtil.toJson(mapTime);
		                	
		                	/*Map<String, String> forbidMap = PropertiesToMap
		            				.propertyToMap("comm_interface.properties");*/
		            		String forbidDevReportUrl1 = mjCommService.getMjCommUrl(target_num)
		            				+ "forbidDevReportAction/setForbidDevReport.do";
		            		String forbidDevStr1 = HttpRequest.sendPost(forbidDevReportUrl1, "mac="
		            				+ targetMac + "&device_type=" + targetDeviceType + "&isStatu=" + enable
		            				+ "&data=" + data);
		            		Map forbidResult1 = GsonUtil.toBean(forbidDevStr1, HashMap.class);
		            		if ("0".equals(forbidResult1.get("result_code"))) {
		            			result3 = true;
		            		} else {
		            			result3 = false;
		            		}
					}else {
						result3 = false;
					}
					
				}else if("4".equals(flagValid[i])){//用户时段
					target_num = target_num.replace("'", "");
					Map<String, Object> device = timesGroupService.getDeviceInfoByNum(target_num);
					if(device!=null){
						String target_mac = (String)device.get("device_mac");
						String target_type = (String)device.get("device_type");
						//String target_name = (String)device.get("device_name");
						try {
							result4 = timesGroupService.copyIssuedTimesGroup(device_num, target_num, target_mac, target_type, loginUser.getYhMc());
						} catch (ConnectionException e) {
							result4 = false;
						} catch (Exception e) {
							result4 = false;
							e.printStackTrace();
						}
					}
					
				}else if("5".equals(flagValid[i])){
						try {
							result5 = holidaySetUpService.copyAndIssuedHolidayTimes(device_num,target_num);
						} catch (ConnectionException e) {
							result5 = false;
						} catch (Exception e) {
							result5 = false;
						} 
				 
			   }else if("6".equals(flagValid[i])){//设备时间段
				    List<DeviceTimeGroup>  list=deviceGroupService.getDeviceTimeGroupById(device_num);
				    List<DeviceTimeGroup>  targetList=deviceGroupService.getDeviceTimeGroupById(target_num);
					Map commonMap=deviceGroupService.downLoadDeviceTimeGroupFirst(list);
				/*	Map<String,String> map=PropertiesToMap.propertyToMap("comm_interface.properties");*/
				    String dataString= GsonUtil.toJson(commonMap);
				    String deviceTimeGroupUrl=mjCommService.getMjCommUrl(target_num)+"deviceGroupAction/setDeviceGroup.do?";
				    String  deviceTimeGroupRespStr=HttpRequest.sendPost(deviceTimeGroupUrl, "data="+dataString+"&mac="+targetMac+"&sign="+targetDeviceType);
					Map deviceTimeMap=GsonUtil.toBean(deviceTimeGroupRespStr,HashMap.class);
					  
					try {
						if(deviceTimeMap!=null){
							if("0".equals(deviceTimeMap.get("result_code"))){
								for(int k=0;k<list.size();k++){
									targetList.get(k).setSunday_time(list.get(k).getSunday_time());
									targetList.get(k).setMonday_time((list.get(k).getMonday_time()));
									targetList.get(k).setThursday_time((list.get(k).getThursday_time()));
									targetList.get(k).setWedsday_time((list.get(k).getWedsday_time()));
									targetList.get(k).setTuesday_time((list.get(k).getTuesday_time()));
									targetList.get(k).setFriday_time((list.get(k).getFriday_time()));
									targetList.get(k).setSaturday_time((list.get(k).getSaturday_time()));
									targetList.get(k).setVacation_time(list.get(k).getVacation_time());
									
									targetList.get(k).setSunday_action_type((list.get(k).getSunday_action_type()));
									targetList.get(k).setMonday_action_type((list.get(k).getMonday_action_type()));
									targetList.get(k).setThursday_action_type((list.get(k).getThursday_action_type()));
									targetList.get(k).setWedsday_action_type((list.get(k).getWedsday_action_type()));
									targetList.get(k).setTuesday_action_type((list.get(k).getTuesday_action_type()));
									targetList.get(k).setFriday_action_type((list.get(k).getFriday_action_type()));
									targetList.get(k).setSaturday_action_type((list.get(k).getSaturday_action_type()));
									targetList.get(k).setVacation_action_type((list.get(k).getVacation_action_type()));
								}
								result6 = deviceGroupService.modifyTargetDeviceTimeGroup(targetList,loginUser.getYhMc());
							}else{
								result6 = false;
							}
						}else {
							result6 = false;
						}
					} catch (Exception e) {
							result6 = false;
			                e.printStackTrace();
					}  
			   }
		}
				//若果复制设置中有一个为false
				if(result0==false||result1==false||result2==false||result3==false||result4==false||result5==false||result6==false){
					resultFail += targetDeviceName+" ";
				}else{
					resultSucc +=targetDeviceName+" ";
				}
				
		}
		jsonResult.setSuccess(true);
		if("".equals(resultSucc)){
			jsonResult.setMsg("复制 "+"<font color='red'>"+resultFail+"</font>"+" 设备失败 ");
			printHttpServletResponse(GsonUtil.toJson(jsonResult)); 
			return;
		}
		if("".equals(resultFail)){
			jsonResult.setMsg("复制 "+"<font color='green'>"+resultSucc+"</font>"+" 设备成功 ");
			printHttpServletResponse(GsonUtil.toJson(jsonResult)); 
			return;
		}
		
		jsonResult.setMsg("复制 "+"<font color='green'>"+resultSucc+"</font>"+"设备成功，复制 "+"<font color='red'>"+resultFail+"</font>"+"设备失败");
		printHttpServletResponse(GsonUtil.toJson(jsonResult)); 
	}
	
	private static String tranBinStrToDeciStr(String binStr){
		char[] c=binStr.toCharArray();
		String rt="";
		for(int i=binStr.length()-1;i>=0;i--){
			rt+=c[i];
		}
		Double sum=0.0;
		for(int i=0;i<binStr.length();i++){
			sum+=Double.parseDouble(Character.valueOf(rt.charAt(i)).toString())*Math.pow(2, i);
		}
		return (int)sum.doubleValue()+"";
	}
	
	/**
	 * 设备上报时段，并更新数据库
	 * by gengji.yang
	 */
	public void reportDeviceTime(){
		String device_num = getHttpServletRequest().getParameter("device_num");
		//查询之前先让设备上报时段并更新库记录 by gengji.yang
		devicetimesService.getDeviceMacByNums(device_num);
		JsonResult result=new JsonResult();
		result.setSuccess(true);
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * 删除时段
	 * @author minting.he
	 */
	public void delTimesById(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		String ids = request.getParameter("ids"); 
		String device_num = request.getParameter("device_num"); 
		if (EmptyUtil.atLeastOneIsEmpty(ids, device_num)) {
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
				boolean r = devicetimesService.delTimesById(ids, device_num, login_user);
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
	
	/**
	 * 删除前判断时段是否在被使用
	 * @author minting.he
	 */
	public void ifUsedTimes(){
		HttpServletRequest request = getHttpServletRequest();
		String times_num = request.getParameter("times_num"); 
		String device_num = request.getParameter("device_num"); 
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", device_num);
		map.put("times_num", times_num);
		Integer count = devicetimesService.ifUsedTimes(map);
		printHttpServletResponse(GsonUtil.toJson(count));
	}
	
}	



			
			

		