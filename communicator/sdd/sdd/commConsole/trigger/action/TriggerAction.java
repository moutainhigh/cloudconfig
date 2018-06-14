package com.kuangchi.sdd.commConsole.trigger.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.commConsole.trigger.model.ResultMsg;
import com.kuangchi.sdd.commConsole.trigger.service.ITriggerService;


@Controller("triggerAction")
public class TriggerAction extends BaseActionSupport{

	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="triggerServiceImpl")
	private ITriggerService triggerService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setTrigger(){
		String mac=getHttpServletRequest().getParameter("mac");
		String clientTrigger=getHttpServletRequest().getParameter("clientTrigger");
		
		String resultStr=triggerService.setTrigger(mac, clientTrigger);
		ResultMsg rm=new ResultMsg();
		if(resultStr==null||"null".equals(resultStr)){
			rm.setResult_code("1");
			rm.setResult_msg("设置上位促发开门失败");
		}else{
			rm.setResult_code("0");
			rm.setResult_msg("设置上位促发开门成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	

}

