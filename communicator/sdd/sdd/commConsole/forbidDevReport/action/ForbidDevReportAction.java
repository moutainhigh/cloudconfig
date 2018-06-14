package com.kuangchi.sdd.commConsole.forbidDevReport.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.device.model.ResultMsg;
import com.kuangchi.sdd.commConsole.forbidDevReport.service.ForbidDevReportService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("forbidDevReportAction")
public class ForbidDevReportAction extends BaseActionSupport {
	
	
	private static final long serialVersionUID = 6424360506377453404L;
	@Resource(name="forbidDevReportServiceImpl")
	private ForbidDevReportService forbidDevReportService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-11 下午4:13:21
	 * @功能描述:禁止上报设备action 
	 * @参数描述:
	 */
	public void setForbidDevReport(){
		HttpServletRequest request = getHttpServletRequest();
		String mac = request.getParameter("mac");
		String device_type = request.getParameter("device_type");
		String data = request.getParameter("data");
		String isStatu = request.getParameter("isStatu");
		String result = forbidDevReportService.setForbidDevReport(mac, device_type,isStatu,data);
		ResultMsg msg = new ResultMsg();
		
		if(result==null){
			msg.setResult_code("1");
			msg.setResult_msg("禁止设备上报失败");
		}else{
			msg.setResult_code("0");
			msg.setResult_msg("禁止设备上报成功");
		}
		printHttpServletResponse(GsonUtil.toJson(msg));
	}	 
	
	public void getForbidDevReport(){
		HttpServletRequest request = getHttpServletRequest();
		String mac = request.getParameter("mac");
		String device_type = request.getParameter("device_type");
		String result = forbidDevReportService.getForbidDevReport(mac, device_type);
	
		printHttpServletResponse(result);
		
	}
	
}
