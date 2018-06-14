package com.kuangchi.sdd.commConsole.deviceGroup.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.util.Util;

import com.kuangchi.sdd.commConsole.actualTime.service.impl.ActualTimeServicempl;
import com.kuangchi.sdd.commConsole.deviceGroup.service.DeviceGroupService;
import com.kuangchi.sdd.commConsole.fire.model.ResultMsg;


@Controller("deviceGroupAction")
public class DeviceGroupAction extends BaseActionSupport{

	public static final Logger LOG = Logger.getLogger(DeviceGroupAction.class);
	
	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="deviceGroupServiceImpl")
	private DeviceGroupService deviceGroupService;

	@Override
	public Object getModel() { 
		return null;
	}
	
	public void setDeviceGroup(){	
		String mac=getHttpServletRequest().getParameter("mac");
		//String mac="85000a";
		String data=getHttpServletRequest().getParameter("data");
		String sign=getHttpServletRequest().getParameter("sign");//设备类型
		LOG.info("设备类型："+sign);
		Gson gson=new Gson();
		List<DeviceTimeBlock> deviceBlockList=new ArrayList<DeviceTimeBlock>();//存放两个块的list
		DeviceTimeBlock deviceTimeBolock0=new DeviceTimeBlock();//0块的数据（块号和数据）
		DeviceTimeBlock deviceTimeBolock1=new DeviceTimeBlock();//1块的数据（块号和数据）
		List<DeviceTimeData> deviceTimeDateList0=new ArrayList<DeviceTimeData>();//0块的数据
		List<DeviceTimeData> deviceTimeDateList1=new ArrayList<DeviceTimeData>();//1块的数据
		
		
		Map<String,List<Map<String,String>>> deviceGroupMap=gson.fromJson(data,new HashMap<String,List<Map<String,String>>>().getClass());//将接收到的数据转换成Map=[sunday={hour,minute,actionType,id},monday={hour,minute,actionType,id},...]
		List<Map<String,String >>  sunday=deviceGroupMap.get("sunday");
		List<Map<String,String >>  monday=deviceGroupMap.get("monday");
		List<Map<String,String >>  vocation=deviceGroupMap.get("vocation");
		List<Map<String, String>> tuesday= deviceGroupMap.get("tuesday");
		List<Map<String, String>> wedsday= deviceGroupMap.get("wedsday");
		List<Map<String, String>> thursday= deviceGroupMap.get("thursday");
		List<Map<String, String>> friday= deviceGroupMap.get("friday");
		List<Map<String, String>> saturday= deviceGroupMap.get("saturday");

			for (int i = 0; i < sunday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subSundayElementMap= sunday.get(i);
				LOG.info("actionType="+subSundayElementMap.get("actionType")+",hour="+subSundayElementMap.get("hour")+",minute="+subSundayElementMap.get("minute")+",retain="+subSundayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subSundayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subSundayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subSundayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList0.add(deviceTimeData0);
			} 
			for (int i = 0; i < monday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subMondayElementMap= monday.get(i);
				LOG.info("actionType="+subMondayElementMap.get("actionType")+",hour="+subMondayElementMap.get("hour")+",minute="+subMondayElementMap.get("minute")+",retain="+subMondayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subMondayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subMondayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subMondayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList0.add(deviceTimeData0);
			} 
			for (int i = 0; i < tuesday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subTuesdayElementMap= tuesday.get(i);
				LOG.info("actionType="+subTuesdayElementMap.get("actionType")+",hour="+subTuesdayElementMap.get("hour")+",minute="+subTuesdayElementMap.get("minute")+",retain="+subTuesdayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subTuesdayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subTuesdayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subTuesdayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList0.add(deviceTimeData0);
			} 
			for (int i = 0; i < wedsday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subWedsdayElementMap= wedsday.get(i);
				LOG.info("actionType="+subWedsdayElementMap.get("actionType")+",hour="+subWedsdayElementMap.get("hour")+",minute="+subWedsdayElementMap.get("minute")+",retain="+subWedsdayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subWedsdayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subWedsdayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subWedsdayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList0.add(deviceTimeData0);
			} 
			
			deviceTimeBolock0.setBlock(0x00);
			deviceTimeBolock0.setDeviceTimes(deviceTimeDateList0);
			deviceBlockList.add(deviceTimeBolock0);//将封装好的块0放进数组里
			
			
			for (int i = 0; i < thursday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subThursdayElementMap= thursday.get(i);
				LOG.info("actionType="+subThursdayElementMap.get("actionType")+",hour="+subThursdayElementMap.get("hour")+",minute="+subThursdayElementMap.get("minute")+",retain="+subThursdayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subThursdayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subThursdayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subThursdayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList1.add(deviceTimeData0);
			} 
			
			for (int i = 0; i < friday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subFridayElementMap= friday.get(i);
				LOG.info("actionType="+subFridayElementMap.get("actionType")+",hour="+subFridayElementMap.get("hour")+",minute="+subFridayElementMap.get("minute")+",retain="+subFridayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subFridayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subFridayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subFridayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList1.add(deviceTimeData0);
			} 
			
			for (int i = 0; i < saturday.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subSaturdayElementMap= saturday.get(i);
				LOG.info("actionType="+subSaturdayElementMap.get("actionType")+",hour="+subSaturdayElementMap.get("hour")+",minute="+subSaturdayElementMap.get("minute")+",retain="+subSaturdayElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subSaturdayElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subSaturdayElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subSaturdayElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList1.add(deviceTimeData0);
			} 
			
			for (int i = 0; i < vocation.size(); i++) {
				DeviceTimeData deviceTimeData0=new DeviceTimeData();
				Map<String, String> subVocationElementMap= vocation.get(i);
				LOG.info("actionType="+subVocationElementMap.get("actionType")+",hour="+subVocationElementMap.get("hour")+",minute="+subVocationElementMap.get("minute")+",retain="+subVocationElementMap.get("retain"));
				Integer actionTypeStr=Util.getIntHex((subVocationElementMap.get("actionType").toString()).split("\\.")[0]);
				deviceTimeData0.setActionType(actionTypeStr);
				deviceTimeData0.setHour(Integer.valueOf(String.valueOf(subVocationElementMap.get("hour").toString()).split("\\.")[0]));
				deviceTimeData0.setMinute(Integer.valueOf(String.valueOf(subVocationElementMap.get("minute").toString()).split("\\.")[0]));
				deviceTimeDateList1.add(deviceTimeData0);
			}
			
			deviceTimeBolock1.setBlock(0x01);
			deviceTimeBolock1.setDeviceTimes(deviceTimeDateList1);
			deviceBlockList.add(deviceTimeBolock1);//将封装好的块0放进数组里
			
		String resultStr=deviceGroupService.setDeviceGroup(mac, deviceBlockList,sign);
		ResultMsg rm=new ResultMsg();
		
		if(resultStr==null||"null".equals(resultStr)){
			rm.setResult_code("1");
			rm.setResult_msg("设置设备时段组失败");
		}else{
			rm.setResult_code("0");
			rm.setResult_msg("设置设备时段组成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	

}

