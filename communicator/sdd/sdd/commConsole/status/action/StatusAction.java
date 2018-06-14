package com.kuangchi.sdd.commConsole.status.action;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.status.model.GateParamData;
import com.kuangchi.sdd.commConsole.status.model.ResultMsg;
import com.kuangchi.sdd.commConsole.status.service.IStatusService;

@Controller("statusAction")
public class StatusAction extends BaseActionSupport{

	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name="statusServicempl")
	private IStatusService statusService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void getStatus(){
		String mac=getHttpServletRequest().getParameter("mac");
		List<GateParamData> allStatus=statusService.getStatus(mac);		
		ResultMsg rm=new ResultMsg();
		if(allStatus==null||"null".equals(allStatus)){
			rm.setResult_code("1");
			rm.setResult_msg("获取设备状态失败");
		}else{
			rm.setAllStatus(allStatus);
			rm.setResult_code("0");
			rm.setResult_msg("获取设备状态成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
}

