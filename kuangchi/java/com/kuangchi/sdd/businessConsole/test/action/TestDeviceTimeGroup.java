package com.kuangchi.sdd.businessConsole.test.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;
import com.kuangchi.sdd.baseConsole.deviceGroup.service.DeviceGroupService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
/**
 *修改设备时间组测试使用
 *  在sqlmapConfig-test.xml中添加
 */
/*	<!-- 添加测试设备时间组 -->
	<package name="testDeviceTimeGroup" extends="struts-default">
	        <action name="test/deviceTimeGroup" class="testDeviceTimeGroup" method="modifyDeviceTimeGroup">
	        </action>
	</package>*/

@Controller("testDeviceTimeGroup")
public class TestDeviceTimeGroup {

	@Resource(name="deviceGroupService")
	DeviceGroupService deviceGroupService;

	/* public void modifyDeviceTimeGroup(){
		 DeviceTimeGroup deviceTimeGroup=new DeviceTimeGroup();
		 deviceTimeGroup.setId(798);
		 deviceTimeGroup.setDevice_num("D1234");
		 deviceTimeGroup.setDoor_num("1");
		 deviceTimeGroup.setTime_order(1);
		 deviceTimeGroup.setSunday_time("11:11");
		 deviceTimeGroup.setMonday_time("12:12");
		 deviceTimeGroup.setTuesday_time("13:13");
		 deviceTimeGroup.setWedsday_time("14:14");
		 deviceTimeGroup.setThursday_time("15:15");
		 deviceTimeGroup.setFriday_time("16:16");
		 deviceTimeGroup.setSaturday_time("17:17");
		 deviceTimeGroup.setVacation_time("18:18");
		 deviceTimeGroup.setSunday_action_type("2");
		 deviceTimeGroup.setMonday_action_type("2");
		 deviceTimeGroup.setTuesday_action_type("2");
		 deviceTimeGroup.setWedsday_action_type("2");
		 deviceTimeGroup.setThursday_action_type("2");
		 deviceTimeGroup.setFriday_action_type("2");
		 deviceTimeGroup.setSaturday_action_type("2");
		 deviceTimeGroup.setVacation_action_type("2");
		 deviceTimeGroup.setValidity_flag("1");
		 deviceTimeGroup.setCreate_user("gcd");
		 deviceTimeGroup.setCreate_time("2016-05-27 13:00:00");
		 deviceTimeGroup.setDescription("设备时间组测试");
		 List<DeviceTimeGroup> listDTG=new ArrayList<DeviceTimeGroup>();
		 listDTG.add(deviceTimeGroup);
		 Map<String,List<DeviceTimeGroup>> mapDTG=new HashMap<String, List<DeviceTimeGroup>>();
		 mapDTG.put("deviceGroup0", listDTG);
		boolean result= deviceGroupService.modifyDeviceTimeGroup(mapDTG);
		
		if(result){
			System.out.println("**********修改成功*******");
		}else{
			System.out.println("**************修改失败***");
		}
		
	 }
	 
	 public void selectDeviceTimeGroupinfos(){
		 int device_id=26;
		 List<DeviceTimeGroup> deviceTimeGroup=deviceGroupService.selectDeviceTimeGroupinfos(device_id);
		 //printHttpServletResponse(GsonUtil.toJson(deviceTimeGroup));
		 for(int i=0;i<deviceTimeGroup.size();i++){
			 System.out.println("**********开始************");
			 System.out.println("^^^^^^^"+deviceTimeGroup.get(i).getDevice_num());
			 System.out.println("^^^^^^^"+deviceTimeGroup.get(i).getDoor_name());
		 }
	 }*/
	
	
	
}

