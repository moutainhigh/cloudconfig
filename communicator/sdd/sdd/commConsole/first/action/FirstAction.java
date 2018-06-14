package com.kuangchi.sdd.commConsole.first.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.commConsole.fire.model.ResultMsg;
import com.kuangchi.sdd.commConsole.fire.service.IFrieService;
import com.kuangchi.sdd.commConsole.first.service.IFirstService;


@Controller("firstAction")
public class FirstAction extends BaseActionSupport{

	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="firstServiceImpl")
	private IFirstService firstService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setFirst(){
		String mac=getHttpServletRequest().getParameter("mac");
		String sign=getHttpServletRequest().getParameter("device_type");
		String gateId=getHttpServletRequest().getParameter("gateId");
		String first=getHttpServletRequest().getParameter("first");
		
		String resultStr=firstService.setFirst(mac,sign, gateId,first);
		ResultMsg rm=new ResultMsg();
		if(resultStr==null||"null".equals(resultStr)){
			rm.setResult_code("1");
			rm.setResult_msg("设置首卡开门失败");
		}else{
			rm.setResult_code("0");
			rm.setResult_msg("设置首卡开门成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	

}

