package com.kuangchi.sdd.commConsole.holiday.action;

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
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;
import com.kuangchi.sdd.comm.equipment.gate.holiday.SetHolidayConnector;
import com.kuangchi.sdd.commConsole.group.model.TimeGroupBlock;
import com.kuangchi.sdd.commConsole.group.service.impl.GroupServiceImpl;
import com.kuangchi.sdd.commConsole.holiday.model.HolidayBean;
import com.kuangchi.sdd.commConsole.holiday.model.TimeJson;
import com.kuangchi.sdd.commConsole.holiday.model.holidayData;
import com.kuangchi.sdd.commConsole.holiday.model.holidayTime;
import com.kuangchi.sdd.commConsole.holiday.service.IHolidayService;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;
import com.kuangchi.sdd.commConsole.search.model.ResultMsg;
import com.kuangchi.sdd.commConsole.search.service.ISearchEquipment;

@Controller("holidayAction")
public class HolidayAction extends BaseActionSupport{
	public static final Logger LOG = Logger.getLogger(HolidayAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 293788929553302791L;
	
	@Resource(name="holidayServiceImpl")
	
	private IHolidayService holidayService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setHoliday(){
		String mac=getHttpServletRequest().getParameter("mac");
		String device_type=getHttpServletRequest().getParameter("device_type");
		LOG.info("传过来的值:"+device_type);
		String data=getHttpServletRequest().getParameter("data");
		Gson gson=new Gson();
		java.lang.reflect.Type type = new TypeToken<List<holidayData>>() {}.getType(); 
		List<holidayData> holiday=gson.fromJson(data, type);
		String result=holidayService.setHoliday(mac, device_type,holiday);
		ResultMsg rm=new ResultMsg();
		if(result!=null){
			rm.setResult_code("0");
			rm.setResult_msg("设置成功");
			printHttpServletResponse(new Gson().toJson(rm));
		}else
		rm.setResult_code("1");
		rm.setResult_msg("设置失败");
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
	//获取时段组
	public void getHoliday(){
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		String result = holidayService.getHoliday(mac,deviceType);
		printHttpServletResponse(result);
		
	}
	
}
