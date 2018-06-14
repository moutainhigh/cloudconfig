package com.kuangchi.sdd.visitorConsole.paramConfiguration.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.visitorConsole.paramConfiguration.service.ParamConfigurationService;

@Controller("paramConfigurationAction")
public class ParamConfigurationAction extends BaseActionSupport{
	
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "paramConfigurationServiceImpl")
	private ParamConfigurationService paramConfigurationService;

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询来访事宜到主页面  by huixian.pan
	 */
	public  void getVisitMatter(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
 		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=paramConfigurationService.getVisitMatterInfo(map); 
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 新增来访事宜  by huixian.pan
	 */
	public  void addVisitMatter(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
	        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String today=format.format(date);
	        
	        User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	        String create_user=loginUser.getYhMc();
	        /*if(paramConfigurationService.getRepeatVisitMatter(map)==0){*/
				boolean flag=paramConfigurationService.addVisitMatter(map, today, create_user);
				if(flag){
					result.setSuccess(true);
					result.setMsg("新增成功");
				}else{
					result.setSuccess(false);
					result.setMsg("新增失败");
				}
	       /* }else{
	        	    result.setSuccess(false);
				    result.setMsg("已有此来访事宜，请重新输入");
	        }*/
		} catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg("新增失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 修改来访事宜  by huixian.pan
	 */
	public  void editVisitMatter(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
		/*	if(paramConfigurationService.getRepeatVisitMatter(map)==0){*/
				boolean flag=paramConfigurationService.editVisitMatter(map, today, create_user);
				if(flag){
					result.setSuccess(true);
					result.setMsg("修改成功");
				}else{
					result.setSuccess(false);
					result.setMsg("修改失败");
				}
			/*}else{
				    result.setSuccess(false);
				    result.setMsg("已有此来访事宜，请重新输入");
			}	*/
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("修改失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除来访事宜  by huixian.pan
	 */
	public  void delVisitMatter(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		try {
			String ids=request.getParameter("data");
			Map map=new HashMap();
			map.put("id", ids);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.delVisitMatter(map, today, create_user);
			if(flag){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 判断是否有重复的来访事宜  by  huixian.pan
	 */
	public  void ifRepeateVisitMatter(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			
			if(paramConfigurationService.getRepeatVisitMatter(map)==0){
					result.setSuccess(true);
			}else{
				    result.setSuccess(false);
				    result.setMsg("已有此来访事宜，请重新输入");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 查询所有来访事宜（接口） by huixian.pan
	 */
	public void  getVisitMatterReg(){
		List<Map> list=paramConfigurationService.getVisitMatterReg();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	
	/**
	 * 查询携带物品到主页面  by huixian.pan
	 */
	public  void getCarryGoods(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=paramConfigurationService.getCarryGoodsInfo(map); 
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 新增携带物品  by huixian.pan
	 */
	public  void addCarryGoods(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			/*if(paramConfigurationService.getRepeatCarryGoods(map)==0){*/
				boolean flag=paramConfigurationService.addCarryGoods(map, today, create_user);
				if(flag){
					result.setSuccess(true);
					result.setMsg("新增成功");
				}else{
					result.setSuccess(false);
					result.setMsg("新增失败");
				}
			/*}else{
				    result.setSuccess(false);
				    result.setMsg("已有此携带物品，请重新输入");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("新增失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 修改携带物品  by huixian.pan
	 */
	public  void editCarryGoods(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
		/*	if(paramConfigurationService.getRepeatCarryGoods(map)==0){*/
				boolean flag=paramConfigurationService.editCarryGoods(map, today, create_user);
				if(flag){
					result.setSuccess(true);
					result.setMsg("修改成功");
				}else{
					result.setSuccess(false);
					result.setMsg("修改失败");
				}
			/*}else{
			    result.setSuccess(false);
			    result.setMsg("已有此携带物品，请重新输入");
		    }*/
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("修改失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 删除携带物品  by huixian.pan
	 */
	public  void delCarryGoods(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		try {
			String ids=request.getParameter("data");
			HashMap map=new HashMap();
			map.put("id", ids);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.delCarryGoods(map, today, create_user);
			if(flag){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	
	/**
	 * 判断是否有重复的携带物品  by  huixian.pan
	 */
	public  void ifRepeateCarryGoods(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			
			if(paramConfigurationService.getRepeatCarryGoods(map)==0){
					result.setSuccess(true);
			}else{
				    result.setSuccess(false);
				    result.setMsg("已有此携带物品，请重新输入");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 查询所有携带物品（接口） by huixian.pan
	 */
	public void  getCarryGoodsReg(){
		List<Map> list=paramConfigurationService.getCarryGoodsReg();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	
	/**
	 * 查询黑名单到主页面  by huixian.pan
	 */
	public  void getBlackList(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=paramConfigurationService.getBlackListInfo(map); 
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 通过ID查看黑名单  by huixian.pan
	 */
	public void getBlackListById(){
		HttpServletRequest request=getHttpServletRequest();
		String id=request.getParameter("data");
		HashMap map=new HashMap();
		map.put("id", id);
		Map map2=paramConfigurationService.getBlackListById(map);
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	
	/**
	 * 新增黑名单  by huixian.pan
	 */
	public  void addBlackList(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.addBlackList(map, today, create_user);
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
	 * 新增黑名单（接口）  by huixian.pan
	 */
	public  void addFkBlackListReg(){
		HttpServletRequest request=getHttpServletRequest();
		//JsonResult result=new JsonResult();
		Map map2=new HashMap();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			//User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user="visitor_sys";
			boolean flag=paramConfigurationService.addBlackList(map, today, create_user);
			if(flag){
				  map2.put("result", "0"); //成功
				  map2.put("msg", "新增成功"); 
			}else{
				  map2.put("result", "1"); //失败
				  map2.put("msg", "新增失败"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			map2.put("result", "1"); //失败
			map2.put("msg", "新增失败"); 
		}
		
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	/**
	 * 修改黑名单  by huixian.pan
	 */
	public  void editBlackList(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.editBlackList(map, today, create_user);
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
	 * 修改黑名单（接口）  by huixian.pan
	 */
	public  void modifyFkBlackListReg(){
		HttpServletRequest request=getHttpServletRequest();
		Map map2=new HashMap();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			//User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user="visitor_sys";
			boolean flag=paramConfigurationService.editBlackList(map, today, create_user);
			if(flag){
				  map2.put("result", "0"); //成功
				  map2.put("msg", "修改成功"); 
			}else{
				map2.put("result", "1"); //失败
				map2.put("msg", "修改失败"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			map2.put("result", "1"); //失败
			map2.put("msg", "修改失败"); 
		}
		
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	/**
	 * 删除黑名单  by huixian.pan
	 */
	public  void delBlackList(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		try {
			String visitorNums=request.getParameter("data");
			HashMap map=new HashMap();
			map.put("visitorNum", visitorNums);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.delBlackList(map, today, create_user);
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
	 * 删除黑名单（接口）  by huixian.pan
	 */
	public  void deleteFkBlackListReg(){
		HttpServletRequest request=getHttpServletRequest();
		Map map2=new HashMap();
		try {
			String visitorNums=request.getParameter("visitorNum");
			HashMap map=new HashMap();
			map.put("visitorNum", visitorNums);
			Date date=new Date();//创建当前系统时间
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String today=format.format(date);
			
			//User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user="visitor_sys";
			boolean flag=paramConfigurationService.delBlackList(map, today, create_user);
			if(flag){
				map2.put("result", "0"); //成功
				map2.put("msg", "删除失败"); 
			}else{
				map2.put("result", "1"); //失败
				map2.put("msg", "删除失败"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			map2.put("result", "1"); //失败
			map2.put("msg", "删除失败"); 
		}
		
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	
	/**
	 * 判断是否有重复的黑名单  by  huixian.pan
	 */
	public  void getRepeatBlackList(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			
			if(paramConfigurationService.getRepeatBlackList(map)==0){
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
				result.setMsg("此访客已经在黑名单中");
			}	
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("操作失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
	 * 查询所有黑名单（接口） by huixian.pan
	 */
	public void  getFkBlackListReg(){
		List<Map> list=paramConfigurationService.getBlackListReg();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	/**
	 * 查询通知名单到主页面  by huixian.pan
	 */
	public  void getNotifyListInfo(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		Grid grid=paramConfigurationService.getNotifyListInfo(map); 
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	/**
	 * 新增通知名单   by huixian.pan
	 */
	public  void addNotifyList(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		Gson gson=new Gson();
		try {
			List<String> staffNums=gson.fromJson(request.getParameter("data"), ArrayList.class);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.addNotifyList(staffNums, create_user);
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
	 * 删除通知名单   by huixian.pan
	 */
	public  void delNotifyList(){
		HttpServletRequest request=getHttpServletRequest();
		JsonResult result=new JsonResult();
		try {
			String  ids=request.getParameter("data");
			Map map=new HashMap();
			map.put("id", ids);
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
			String create_user=loginUser.getYhMc();
			boolean flag=paramConfigurationService.delNotifyList(map, create_user);
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
	
	/*-------------------------------接口---------------------------------*/
	/**
	 * 查询权限组  by huixian.pan
	 */
	public void  queryAuthGroup(){
		List<Map> list=paramConfigurationService.queryAuthGroup();
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	/**
	 * 查询权限信息  by huixian.pan
	 */
	public void  queryAuthInfo(){
		HttpServletRequest request=getHttpServletRequest();
		String groupNum=request.getParameter("groupNum");
		Map map=paramConfigurationService.queryAuthInfo(groupNum);
		printHttpServletResponse(GsonUtil.toJson(map));
	}
	/**
	 * 验证卡号   by huixian.pan
	 */
	public  void validateCardNum(){
		HttpServletRequest request=getHttpServletRequest();
		String cardNum=request.getParameter("cardNum");
		Map map2=new HashMap();
		if(paramConfigurationService.validateCardNum(cardNum)){
		    map2.put("result", "0"); //合法
		}else{
			map2.put("result", "1"); //非法
		}
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	/**
	 * 查询历史被访人员  by huixian.pan
	 */
	public void  getHisVisitedPerson(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		Map map2=paramConfigurationService.getHisVisitedPerson(map);
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	/**
	 * 来访查询（查询今日访客（正在访问，已离开，预约））  by huixian.pan
	 */
	public void  queryTodayVisitor(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		Map map2=paramConfigurationService.queryTodayVisitor(map);
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	/**
	 * 离开登记（修改访客记录状态）  by huixian.pan
	 */
	public  void visitorLeave(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		Map map2=new HashMap();
		if(paramConfigurationService.visitorLeave(map)){
			map2.put("result", "0"); //登记成功
		}else{
			map2.put("result", "1"); //登记失败
		}
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	/**
	 * 查询来访次数   by huixian.pan
	 */
	public void  getHisVisitedCount(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		Map map2=paramConfigurationService.getHisVisitedCount(map);
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	
	
	/**
	 * 访客登记（点击登记按钮）  by huixian.pan
	 */
	public  void mainVisitorReg(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		Map m=new HashMap();
		try {
 			HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
			boolean flag=paramConfigurationService.mainVisitorReg(map);
			if(flag){
				m.put("msg", "登记成功");
				m.put("result", "0");
			}else{
				m.put("msg", "登记失败");
				m.put("result", "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			m.put("msg", "登记失败");
			m.put("result", "1");
		}
		printHttpServletResponse(GsonUtil.toJson(m));
	}
	
	/**
	 * 来访查询  by huixian.pan
	 */
	public  void  queryMainVisitorList(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		List<Map> list=paramConfigurationService.queryMainVisitorLis(map);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	/**
	 * 被访人管理 by huixian.pan
	 */
	public  void  queryPassiveVisitorByParam(){
		HttpServletRequest request=getHttpServletRequest();
		Map map=new HashMap();
		String param=request.getParameter("param");
		map.put("param", param);
		List<Map> list=paramConfigurationService.queryPassiveVisitorByParam(map);
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	/**
	 * 被访人总条数 by huixian.pan
	 */
	public  void  countPassiveVisitor(){
		HttpServletRequest request=getHttpServletRequest();
		String param=request.getParameter("param");
		Map map=new HashMap();
		String page=request.getParameter("page");
		String rows=request.getParameter("rows");
		map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		map.put("rows", Integer.parseInt(rows));
		map.put("param", param);
		Integer  count=paramConfigurationService.countPassiveVisitor(map);
		printHttpServletResponse(GsonUtil.toJson(count));
	}
	
	/**
	 * 取消预约  by huixian.pan
	 */
	public  void cancelBooking(){
		HttpServletRequest request=getHttpServletRequest();
		Gson gson=new Gson();
		HashMap map=gson.fromJson(request.getParameter("data"), HashMap.class);
		Map map2=new HashMap();
		if(paramConfigurationService.cancelBooking(map)){
			map2.put("result", "0"); //取消成功
		}else{
			map2.put("result", "1"); //取消失败
		}
		printHttpServletResponse(GsonUtil.toJson(map2));
	}
	/*-------------------------------接口---------------------------------*/
}
