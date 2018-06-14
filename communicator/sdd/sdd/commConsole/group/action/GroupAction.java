package com.kuangchi.sdd.commConsole.group.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.common.EquipmentDataForServer;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;
import com.kuangchi.sdd.comm.equipment.gate.holiday.SetHolidayConnector;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.group.model.ResultMsg;
import com.kuangchi.sdd.commConsole.group.model.TimeGroupBlock;
import com.kuangchi.sdd.commConsole.group.service.IGroupService;
import com.kuangchi.sdd.commConsole.holiday.model.HolidayBean;
import com.kuangchi.sdd.commConsole.holiday.service.IHolidayService;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;

import com.kuangchi.sdd.commConsole.search.service.ISearchEquipment;

@Controller("groupAction")
public class GroupAction extends BaseActionSupport{

	/**
	 * 
	 */
	
	public static final Logger LOG = Logger.getLogger(GroupAction.class);
	private static final long serialVersionUID = 293788929553302791L;
	
	@Resource(name="groupServiceImpl")
	
	private IGroupService groupService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setGroup(){
		String mac=getHttpServletRequest().getParameter("mac");
		String device_type=getHttpServletRequest().getParameter("device_type");
		LOG.info("传过来的mac："+mac);
		String data=getHttpServletRequest().getParameter("data");
		Gson gson=new Gson();
		java.lang.reflect.Type type = new TypeToken<List<TimeBlock>>() {}.getType(); 
		List<TimeBlock> group=gson.fromJson(data, type);
		try {
		String result=groupService.setGroup(mac, device_type,group);
		if(result!=null){
			ResultMsg rm=new ResultMsg();
			rm.setResult_code("0");
			rm.setResult_msg("设置成功");
			printHttpServletResponse(new Gson().toJson(rm));
		}else{
			ResultMsg rm=new ResultMsg();
		rm.setResult_code("1");
		rm.setResult_msg("设置失败");
		printHttpServletResponse(new Gson().toJson(rm));
		}
		} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	//获取时段组
		public void getGroup(){
			String mac=getHttpServletRequest().getParameter("mac");
			String block=getHttpServletRequest().getParameter("block");
			String device_type=getHttpServletRequest().getParameter("device_type");
			String getTimegroup=null;
			try {
				getTimegroup=groupService.getGroup(mac,block,device_type);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ResultMsg rm=new ResultMsg();
			if(getTimegroup != null && !"null".equals(getTimegroup)){
				rm.setResult_code("0");
				rm.setResult_msg("获取时段组权限成功");
				rm.setResult_value(getTimegroup);
				/*Gson gson=new Gson();
				TimeBlock tt=gson.fromJson(rm.getResult_value(), TimeBlock.class);
				System.out.println(tt);*/
			}else{
				rm.setResult_code("1");
				rm.setResult_msg("获取时段组权限失败");
			}
			printHttpServletResponse(new Gson().toJson(rm));
		}
	
}
