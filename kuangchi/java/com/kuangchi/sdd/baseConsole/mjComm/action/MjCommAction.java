package com.kuangchi.sdd.baseConsole.mjComm.action;

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
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("MjCommAction")
public class MjCommAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;
	
	
	 /**
	  * 查询所有门禁通讯服务器信息 
	  * by huixian.pan
	  */
	  public void  getMjCommIpMess(){
		 HttpServletRequest request=getHttpServletRequest();
		 Gson gson=new Gson();
		 HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		 String page=request.getParameter("page");
		 String rows=request.getParameter("rows");
		 map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		 map.put("rows", Integer.parseInt(rows));
		 Grid  grid=mjCommService.getMjCommIpMess(map);
		 printHttpServletResponse(GsonUtil.toJson(grid));
	  }
	  
	  
	  /**
	    * 通过门禁通讯服务器id查询通讯服务器信息  
	    * by huixian.pan
	    */
		 public void getMjCommIpById(){
			 HttpServletRequest request=getHttpServletRequest();
			 String id=request.getParameter("id");
			 Map map=mjCommService.getMjCommIpById(id);
			 printHttpServletResponse(GsonUtil.toJson(map));
		 }
		
		 /**
		  * 新增门禁通讯服务器信息 
		  * by huixian.pan
		  */
		 public void addMjCommIp(){
			HttpServletRequest request=getHttpServletRequest();
			 Gson gson=new Gson();
			 HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			 JsonResult result=new JsonResult();
			 
		     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	         String create_user=loginUser.getYhMc();
	         
			 try {
				 boolean flag=mjCommService.addMjCommIp(map, create_user);
				 if(flag){
					 result.setSuccess(true);
					 result.setMsg("新增成功");
				 }else{
					 result.setSuccess(false);
					 result.setMsg("新增失败");
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
				 result.setSuccess(false);
				 result.setMsg("新增失败");
			 }
			 printHttpServletResponse(GsonUtil.toJson(result));
		 }
		 
		 /**
		  * 删除门禁通讯服务器信息
		  * by huixian.pan 
		  * */
	   public void delMjCommIp(){
		     HttpServletRequest request=getHttpServletRequest();
		     String id=request.getParameter("id");
			 JsonResult result=new JsonResult();
			 
		     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	         String create_user=loginUser.getYhMc();
	         
			 try {
				 boolean flag=mjCommService.delMjCommIp(id, create_user);
				 if(flag){
					 result.setSuccess(true);
					 result.setMsg("删除成功");
				 }else{
					 result.setSuccess(false);
					 result.setMsg("删除失败");
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
				 result.setSuccess(false);
				 result.setMsg("删除失败");
			 }
			 printHttpServletResponse(GsonUtil.toJson(result));
	   }
	   
	   /**
	    * 修改门禁通讯服务器信息
	    * by huixian.pan
	    * */
	   public void updateMjCommIp(){
		    HttpServletRequest request=getHttpServletRequest();
			 Gson gson=new Gson();
			 HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			 JsonResult result=new JsonResult();
			 
		     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	         String create_user=loginUser.getYhMc();
	         
			 try {
				 boolean flag=mjCommService.updateMjCommIp(map, create_user);
				 if(flag){
					 result.setSuccess(true);
					 result.setMsg("修改成功");
				 }else{
					 result.setSuccess(false);
					 result.setMsg("修改失败");
				 }
			 } catch (Exception e) {
				 e.printStackTrace();
				 result.setSuccess(false);
				 result.setMsg("修改失败");
			 }
			 printHttpServletResponse(GsonUtil.toJson(result));
	   }
  
	
		/**
		 * 查询所有门禁通讯服务器Ip
		 * by huixian.pan
		 */
		public void getMjCommIp(){
			List<Map> list=mjCommService.getMjCommIp();
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
			if(mjCommService.getMjCommIpCountByIp(map)){
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
			if(mjCommService.getMjCommIpCountByIpA(map)){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
		/**
		 *  判断是否有设备使用此通讯服务器 
		 *  by huixian.pan
		 *  */
		public void ifMjCommIpUsed(){
			HttpServletRequest request=getHttpServletRequest();
			JsonResult result=new JsonResult(); 
			String id=request.getParameter("id");
			if(mjCommService.ifMjCommIpUsed(id)){
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
