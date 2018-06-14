package com.kuangchi.sdd.commConsole.actualTime.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.actualTime.model.ActualTimeBean;
import com.kuangchi.sdd.commConsole.actualTime.model.ResultMsg;
import com.kuangchi.sdd.commConsole.actualTime.service.IActualTimeService;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;


@Controller("actualTimeAction")
public class ActualTimeAction extends BaseActionSupport{

	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="actualTimeServicempl")
	private IActualTimeService actualTimeService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setActualTime(){
		String mac=getHttpServletRequest().getParameter("mac");
		String sign=getHttpServletRequest().getParameter("device_type");
		
		String actual_Time=getHttpServletRequest().getParameter("actual_time");//7
		String intervaltime=getHttpServletRequest().getParameter("intervaltime");//10
//		int interval_Time=(int) ((Double.valueOf(intervaltime))*1000/100);
		int interval_Time=Integer.valueOf(intervaltime);
		
		int actual_time=Integer.valueOf(actual_Time);
		
		int actualTime=actual_time<<8|interval_Time;//实时上传数据
		
		String result=actualTimeService.setActualTime(mac,sign,actualTime);
		ResultMsg rm=new ResultMsg();
		if(result==null||"null".equals(result)){
			rm.setResult_code("1");
			rm.setResult_msg("设置读取实时上传参数失败");
		}else{
			rm.setResult_code("0");
			rm.setResult_msg("设置读取实时上传参数成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
	public void getActualTime(){
		String mac=getHttpServletRequest().getParameter("mac");
		String device_type=getHttpServletRequest().getParameter("device_type");
		ActualTimeBean allActualTime=actualTimeService.getActualTime(mac, device_type);		
		ResultMsg rm=new ResultMsg();
		if(allActualTime==null||"null".equals(allActualTime)){
			rm.setResult_code("1");
			rm.setResult_msg("读取实时上传参数失败");
		}else{
			rm.setAllActualTime(allActualTime);
			rm.setResult_code("0");
			rm.setResult_msg("读取实时上传参数成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
}

