package com.kuangchi.sdd.commConsole.userGroup.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.group.model.ResultMsg;
import com.kuangchi.sdd.commConsole.userGroup.service.IUserGroupService;

@Controller("userGroupAction")
public class UserGroupAction extends BaseActionSupport{
	public static final Logger LOG = Logger.getLogger(UserGroupAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 293788929553302791L;
	
	@Resource(name="userGroupServiceImpl")
	
	private IUserGroupService userGroupService;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void setUserGroup()  {
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		String data=getHttpServletRequest().getParameter("data");
		LOG.info("传过来的mac地址"+mac);
		Gson gson=new Gson();
		java.lang.reflect.Type type = new TypeToken<List<String>>() {}.getType(); 
		List<String> userGroup=gson.fromJson(data, type);
		String result=userGroupService.setUserGroup(mac,deviceType, userGroup);
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
	
	
	public void getUserGroup()  {
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		String block=getHttpServletRequest().getParameter("block");
		String result=userGroupService.getUserGroup(mac,deviceType, block);
		printHttpServletResponse(result);
	}
}
