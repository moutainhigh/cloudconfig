package com.kuangchi.sdd.interfaceConsole.checkHandle.action;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.event.service.EventService;
import com.kuangchi.sdd.interfaceConsole.checkHandle.model.DeviceStateModel;
import com.kuangchi.sdd.interfaceConsole.checkHandle.service.ICheckHandleService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * @创建时间: 2016-5-19 下午2:17:54
 * @功能描述: 对外接口Action
 */
@Controller("checkHandleAction")
public class CheckHandleAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	@Resource(name = "deviceService")
	private DeviceService deviceService;
	@Resource(name = "eventService")
	private EventService eventService;
	@Resource(name = "checkHandleServiceImpl")
	private ICheckHandleService checkHandleService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-17 上午10:38:52
	 * @功能描述: 增加打卡信息
	 * @参数描述:
	 */
	public void addCheckRecord(){
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		
		String door_num = request.getParameter("door_num");
		String card_num = request.getParameter("card_num");
		String checktime = request.getParameter("checktime");
		String device_mac = request.getParameter("device_mac");
		String record_id = request.getParameter("record_id");
		String record_type = request.getParameter("record_type");
		String event_type =  request.getParameter("event_type");
		
		try {
			
			if(null==card_num||"".equals(card_num.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("卡号card_num不能为空");		
			}
			if(null==device_mac||"".equals(device_mac.trim())){
       			throw new com.kuangchi.sdd.base.exception.MyException("设备mac不能为空");		
       		}
			if(null==checktime||"".equals(checktime.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("刷卡时间checktime不能为空");		
			}
			if(null==record_id||"".equals(record_id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("record_id不能为空");		
			}
			try {
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				simpleDateFormat.setLenient(false);
				simpleDateFormat.parseObject(checktime);
			} catch (Exception e) {
				throw new com.kuangchi.sdd.base.exception.MyException("checktime日期格式不正确");	
			}	
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setSuccess(false);
			result.setMsg(myException.getMessage());
			printHttpServletResponse(GsonUtil.toJson(result)); 
			return;
		}	
	
		try {
			boolean reportResult = checkHandleService.checkRecordReport(record_id, record_type, device_mac, 
					door_num, card_num, event_type, checktime);
			if(reportResult){
				result.setSuccess(true);
				result.setMsg("操作成功");
			} else {
				result.setSuccess(false);
				result.setMsg("操作失败");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("操作失败");
			e.printStackTrace();
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result)); 
		}
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-17 上午10:39:57
	 * @功能描述: 设备状态上报
	 * @参数描述:
	 */
	public void updateDeviceState(){
		
		JsonResult result = new JsonResult();
		
		String loginUser = (String) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String device_mac = getHttpServletRequest().getParameter("device_mac");
		String data = getHttpServletRequest().getParameter("data");
		
		DeviceStateModel deviceStateModel = GsonUtil.toBean(data,DeviceStateModel.class);
		DeviceInfo device = checkHandleService.getInfoByMac(device_mac);
		if(device == null){
			result.setSuccess(false);
			result.setMsg("设备不存在");
			printHttpServletResponse(GsonUtil.toJson(result)); 
		} else if (deviceStateModel == null){
			result.setSuccess(false);
			result.setMsg("传入参数错误");
			printHttpServletResponse(GsonUtil.toJson(result)); 
		} else {
			//将状态定义为Integer,进行&运算解析各门号状态，消防状态不需解析
			Integer lockState = Integer.parseInt(deviceStateModel.getLock_state());
			Integer doorState = Integer.parseInt(deviceStateModel.getDoor_state());
			Integer keyState = Integer.parseInt(deviceStateModel.getKey_state());
			Integer skidState = Integer.parseInt(deviceStateModel.getSkid_state());
			String fireState = deviceStateModel.getFire_state();
			
			Integer[] doors = {1,2,4,8};
			DeviceInfo d = deviceService.getDeviceIpByDeviceMac(device_mac);
			for(int i=0; i<Integer.valueOf(d.getDevice_type()); i++){
				String doorLockState = "0";
				String doorDoorState = "0";
				String doorKeyState = "0";
				String doorSkidState = "0";
				if((doors[i] & lockState) > 0){
					doorLockState = "1";
				} 
				if((doors[i] & doorState) > 0){
					doorDoorState = "1";
				}
				if((doors[i] & keyState) > 0){
					doorKeyState = "1";
				}
				if((doors[i] & skidState) > 0){
					doorSkidState = "1";
				}
				
				
				//将门号转为字符串，更新设备状态
				String doorNum = (i+1)+"";
				//		deviceService.updateDeviceState(device_mac, doorNum,doorLockState, doorDoorState, doorKeyState, doorSkidState, fireState, loginUser);
				
				//上报设备状态
				checkHandleService.reportDeviceState(device_mac, doorNum, doorLockState, doorDoorState, doorKeyState, doorSkidState, fireState, loginUser);
				
				//对状态进行解析，处理相应的告警事件
				StringBuilder event_dms = new StringBuilder();
				if("0".equals(fireState)){
					event_dms.append("81,82,");
				}
				if("0".equals(doorLockState)){
					event_dms.append("83,");
				}
				if("0".equals(doorDoorState)){
					event_dms.append("84,");
				}
				if("0".equals(doorSkidState)){
					event_dms.append("85,");
				}
				if("0".equals(doorKeyState)){
//					event_dms.append("86,87,88,89,");
					event_dms.append("86,87,88,");//89：胁迫事件暂不出队
				}
				event_dms.setLength(event_dms.length()-1);
				
				//		checkHandleService.handleEventWarning(device_mac, doorNum, event_dms.toString());
				
				//上报状态，等待处理告警事件
				checkHandleService.handleWarningEvent(device_mac, doorNum, event_dms.toString());
			}
			result.setSuccess(true);
			result.setMsg("设置成功");
			printHttpServletResponse(GsonUtil.toJson(result)); 
		}
	}
	
	
	
	
	
	
	/**
	 * 读取缓存最新读头事件
	 * @author minting.he
	 */
	public void readCacheEvent(){
		HttpServletRequest request = getHttpServletRequest();
		String devices = request.getParameter("devices");
		String doors = request.getParameter("doors");
		List<Object> list = checkHandleService.readCacheEvent(devices, doors);
	//	List<Object> list = checkHandleService.readAllEvent();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 读取缓存告警事件
	 * @author minting.he
	 */
	public void readCacheWarningEvent(){
		HttpServletRequest request = getHttpServletRequest();
		String devices = request.getParameter("devices");
		String doors = request.getParameter("doors");
		List<Object> list = checkHandleService.readCacheWarningEvent(devices, doors);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 读取缓存设备状态
	 * @author minting.he
	 */
	public void readCacheDeviceState(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String door_num = request.getParameter("door_num");
		Map<String, Object> map = checkHandleService.readCacheDeviceState(device_num, door_num);
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	
	/**
	 * 读取缓存中所有有效设备的状态
	 * @author minting.he
	 */
	public void readCacheAllState(){
		HttpServletRequest request = getHttpServletRequest();
		String devices = request.getParameter("devices");
		String doors = request.getParameter("doors");
		List<Map<String, Object>> list = checkHandleService.readCacheAllState(devices, doors);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
}
