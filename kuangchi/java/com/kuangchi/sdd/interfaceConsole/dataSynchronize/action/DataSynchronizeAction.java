package com.kuangchi.sdd.interfaceConsole.dataSynchronize.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("dataSynchronizeAction")
public class DataSynchronizeAction extends BaseActionSupport {
	
	public static final Logger LOG = Logger.getLogger(DataSynchronizeAction.class);
	
	//添加员工信息
    public void addEmployee(){
    	HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		LOG.info("........"+data);
    	JsonResult result = new JsonResult();
    	result.setMsg("调用成功");
    	result.setSuccess(true);
    	printHttpServletResponse(GsonUtil.toJson(result)); 
    }

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
