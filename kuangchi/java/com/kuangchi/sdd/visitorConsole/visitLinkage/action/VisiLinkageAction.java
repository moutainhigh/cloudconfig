package com.kuangchi.sdd.visitorConsole.visitLinkage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.visitorConsole.visitLinkage.service.VisitLinkageService;
@Controller("visiLinkageAction")
public class VisiLinkageAction extends BaseActionSupport{

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	@Autowired
	private VisitLinkageService visitLinkageService;
	
	/**
	 * 获取设备门记录
	 * by gengji.yang
	 */
	public void getFkDevDors(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson g=new Gson();
		List<Map> devDorList=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
		Map map =new HashMap();
		map.put("devDorList", devDorList);
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getFkDevDors(map)));
	}
	
	/**
	 * 获取访客系统中的门禁权限
	 * by gengji.yang
	 */
	public void getFkDoorSysAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		map.put("deviceType","0");
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getFkDoorSysAuths(map)));
	}
	
	/**
	 * 获取访客系统中的门禁权限
	 * 不带分页
	 * by gengji.yang
	 */
	public void getFkDoorSysAuthsNoPage(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		map.put("deviceType","0");
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getFkDoorSysAuthsNoPage(map)));
	}
	/**
	 * 新增访客系统中的门禁权限
	 * by gengji.yang
	 */
	public void addFkDoorSysAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson g=new Gson();
		JsonResult result=new JsonResult();
		try{
			List<Map> devDorList=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
			visitLinkageService.addFkDoorSysAuth(devDorList);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除访客系统中的门禁权限
	 * by gengji.yang
	 */
	public void delFkDoorSysAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		JsonResult result=new JsonResult();
		Map map=new HashMap();
		map.put("powerNums", data);
		try{
			visitLinkageService.delFkDoorSysAuth(map);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 查询访客系统中的梯控权限
	 * by gengji.yang
	 */
	public void getFkTkAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		map.put("deviceType","1");
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getFkTkSysAuths(map)));
	}
	
	/**
	 * 查询访客系统中的梯控权限
	 * 不带分页
	 * by gengji.yang
	 */
	public void getFkTkAuthsNoPage(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		map.put("deviceType","1");
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getFkTkSysAuthsNoPage(map)));
	}
	
	/**
	 * 获取设备门记录
	 * by gengji.yang
	 */
	public void getFkTkDevs(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Map map =new HashMap();
		map.put("deviceNums", data);
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getFkTkDevs(map)));
	}
	
	/**
	 * 新增访客系统中的梯控权限信息
	 * by gengji.yang
	 */
	public void addFkTkAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson g=new Gson();
		JsonResult result=new JsonResult();
		try{
			List<Map> list=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
			visitLinkageService.addFkTkSysAuth(list);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 保存访客系统中的梯控权限信息
	 * by gengji.yang
	 */
	public void saveFkTkAuths(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson g=new Gson();
		JsonResult result=new JsonResult();
		try{
			List<Map> list=g.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
			visitLinkageService.saveFkTkAuths(list);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 新增权限组信息
	 * by gengji.yang
	 */
	public void addAuthGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		List<Map> list=gson.fromJson(data, new ArrayList<LinkedTreeMap>().getClass());
		JsonResult result=new JsonResult();
		try{
			visitLinkageService.addAuthGroup(list);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 查询权限组信息
	 * by gengji.yang
	 */
	public void getAuthGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		printHttpServletResponse(GsonUtil.toJson(visitLinkageService.getAuthGroupInfoByGroupNum(map)));
	}
	
	/**
	 * 删除权限组信息
	 * by gengji.yang
	 */
	public void delAuthGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		JsonResult result=new JsonResult();
		Map map=new HashMap();
		map.put("groupNums", data);
		try{
			visitLinkageService.delAuthGroup(map);
			result.setSuccess(true);
		}catch(Exception e){
			e.printStackTrace();
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 判断权限是否已被分配到权限组
	 * by huixian.pan
	 */
	public void  ifAuthExitInGroup(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		JsonResult result=new JsonResult();
		if(visitLinkageService.ifAuthExitInGroup(map)){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
}
