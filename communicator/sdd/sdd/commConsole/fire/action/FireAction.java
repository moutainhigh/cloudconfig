package com.kuangchi.sdd.commConsole.fire.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;
import com.kuangchi.sdd.commConsole.fire.model.ResultMsg;
import com.kuangchi.sdd.commConsole.fire.service.IFrieService;


@Controller("fireAction")
public class FireAction extends BaseActionSupport{

	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="fireServiceImpl")
	private IFrieService fireService;

	@Override
	public Object getModel() { 
		return null;
	}
	
	public void setFire(){	
		String mac=getHttpServletRequest().getParameter("mac");//要设置的设备的mac地址
		String useFireFighting=getHttpServletRequest().getParameter("useFireFighting");//是否启用消防
		String sign=getHttpServletRequest().getParameter("sign");//设备类型
		String resultStr=fireService.setFire(mac, useFireFighting,sign);
		ResultMsg rm=new ResultMsg();
		
		if(resultStr==null||"null".equals(resultStr)){
			rm.setResult_code("1");
			rm.setResult_msg("设置消防失败");
		}else{
			rm.setResult_code("0");
			rm.setResult_msg("设置消防成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	

}

