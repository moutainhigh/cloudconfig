package com.kuangchi.sdd.consumeConsole.xfComm.action;

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
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.consumeConsole.xfComm.service.XfCommService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("XfCommAction")
public class XfCommAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "xfCommServiceImpl")
	private XfCommService xfCommService;
	
	
	 /**
	  * 查询所有消费通讯服务器信息 
	  * by huixian.pan
	  */
	  public void  getXfCommIpMess(){
		 HttpServletRequest request=getHttpServletRequest();
		 Gson gson=new Gson();
		 HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		 String page=request.getParameter("page");
		 String rows=request.getParameter("rows");
		 map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		 map.put("rows", Integer.parseInt(rows));
		 Grid  grid=xfCommService.getXfCommIpMess(map);
		 printHttpServletResponse(GsonUtil.toJson(grid));
	  }
	  
	  
	  /**
	    * 通过消费通讯服务器id查询通讯服务器信息  
	    * by huixian.pan
	    */
		 public void getXfCommIpById(){
			 HttpServletRequest request=getHttpServletRequest();
			 String id=request.getParameter("id");
			 Map map=xfCommService.getXfCommIpById(id);
			 printHttpServletResponse(GsonUtil.toJson(map));
		 }
		
		 /**
		  * 新增消费通讯服务器信息 
		  * by huixian.pan
		  */
		 public void addXfCommIp(){
			HttpServletRequest request=getHttpServletRequest();
			 Gson gson=new Gson();
			 HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			 JsonResult result=new JsonResult();
			 
		     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	         String create_user=loginUser.getYhMc();
	         
			 try {
				 boolean flag=xfCommService.addXfCommIp(map, create_user);
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
		  * 删除消费通讯服务器信息
		  * by huixian.pan 
		  * */
	   public void delXfCommIp(){
		     HttpServletRequest request=getHttpServletRequest();
		     String id=request.getParameter("id");
			 JsonResult result=new JsonResult();
			 
		     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	         String create_user=loginUser.getYhMc();
	         
			 try {
				 boolean flag=xfCommService.delXfCommIp(id, create_user);
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
	    * 修改消费通讯服务器信息
	    * by huixian.pan
	    * */
	   public void updateXfCommIp(){
		    HttpServletRequest request=getHttpServletRequest();
			 Gson gson=new Gson();
			 HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			 JsonResult result=new JsonResult();
			 
		     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	         String create_user=loginUser.getYhMc();
	         
			 try {
				 boolean flag=xfCommService.updateXfCommIp(map, create_user);
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
		 * 查询所有消费通讯服务器Ip
		 * by huixian.pan
		 */
		public void getTkCommIp(){
			List<Map> list=xfCommService.getXfCommIp();
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
			if(xfCommService.getXfCommIpCountByIp(map)){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
		/**
		 *  通过消费通讯服务器ip和端口号查询已有通讯服务器条数 
		 *  by huixian.pan
		 *  */
		public void ifXfCommIpUsed(){
			HttpServletRequest request=getHttpServletRequest();
			JsonResult result=new JsonResult(); 
			String id=request.getParameter("id");
			if(xfCommService.ifXfCommIpUsed(id)){
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
