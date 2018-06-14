package com.kuangchi.sdd.elevator.authority.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.elevator.authority.service.ElevatorAuthorityService;
import com.kuangchi.sdd.elevator.model.Result;

@Controller("elevatorAuthorityAction")
public class ElevatorAuthorityAction extends BaseActionSupport {
	

	@Override
	public Object getModel() {
		return null;
	}

	@Autowired
	private ElevatorAuthorityService elevatorAuthorityService;
	
	/**
	 * 梯控系统
	 * 下发权限
	 * 通讯接口
	 * by gengji.yang
	 */
	public void setEleAuth(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Result result=elevatorAuthorityService.setAuth(map);
		printHttpServletResponse(GsonUtil.toJson(result));//resultCode=0，成功
	}
	
	/**
	 * 梯控系统
	 * 删除权限
	 * 通讯接口
	 * by gengji.yang
	 */
	public void delEleAuth(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Result result=elevatorAuthorityService.delAuth(map);
		printHttpServletResponse(GsonUtil.toJson(result));//resultCode=0，成功
	}

}
