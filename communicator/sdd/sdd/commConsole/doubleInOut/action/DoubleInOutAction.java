package com.kuangchi.sdd.commConsole.doubleInOut.action;


import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.doubleInOut.model.ResultMsg;
import com.kuangchi.sdd.commConsole.doubleInOut.service.DoubleInOutService;


@Controller("doubleInOutAction")
public class DoubleInOutAction extends BaseActionSupport{

	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="doubleInOutServicempl")
	private DoubleInOutService doubleInOutService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setDoubleInOut(){
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		String inOut=getHttpServletRequest().getParameter("inOut");
		ResultMsg rm=new ResultMsg();
		if("1".equals(deviceType)){//1门设备不下发此指令
			rm.setResult_code("0");
			rm.setResult_msg("设置双向进出控制成功");
		}else{
			String result=doubleInOutService.setDoubleInOut(mac,deviceType,inOut);
			if(result==null||"null".equals(result)){
				rm.setResult_code("1");
				rm.setResult_msg("设置双向进出控制失败");
			}else{
				rm.setResult_code("0");
				rm.setResult_msg("设置双向进出控制成功");
			}
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
	public void getDoubleInOut(){
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		String inOut=doubleInOutService.getDoubleInOut(mac,deviceType);		
		ResultMsg rm=new ResultMsg();
		if(inOut!=null){
			rm.setInOut(inOut);
			rm.setResult_code("0");
			rm.setResult_msg("读取双向进出控制成功");
		}else{
			rm.setResult_code("1");
			rm.setResult_msg("读取双向进出控制失败");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
}

