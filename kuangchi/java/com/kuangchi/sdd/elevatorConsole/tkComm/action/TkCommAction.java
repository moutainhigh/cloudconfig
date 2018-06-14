package com.kuangchi.sdd.elevatorConsole.tkComm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.elevatorConsole.tkComm.service.TkCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("TkCommAction")
public class TkCommAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "tkCommServiceImpl")
	private TkCommService tkCommService;
	
	/**
	 * 查询所有梯控通讯服务器Ip
	 * by huixian.pan
	 */
	public void getTkCommIp(){
		List<Map> list=tkCommService.getTkCommIp();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	 /**
	  *  判断是否已有通讯服务器
	  *  by huixian.pan
	  *  */
	public void ifRepeatIp(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		JsonResult result=new JsonResult(); 
		Map map=gson.fromJson(request.getParameter("data"), HashMap.class);
		if(tkCommService.getTkCommIpCountByIp(map)){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	public void ifRepeatIpA(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		JsonResult result=new JsonResult(); 
		Map map=gson.fromJson(request.getParameter("data"), HashMap.class);
		if(tkCommService.getTkCommIpCountByIpA(map)){
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	  * 判断是否有设备使用此通讯服务器   
	  * by huixian.pan
	  * */
	 public void  ifTkCommIpUsed(){
		 HttpServletRequest request=getHttpServletRequest();
		 String ids = request.getParameter("ids");
		 JsonResult result = new JsonResult();
		 boolean valid = tkCommService.ifTkCommIpUsed(ids);
		 if(valid){
			result.setSuccess(true);
		 }else{
			result.setSuccess(false);
		 }
		 printHttpServletResponse(GsonUtil.toJson(result));
	 }
	

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}
