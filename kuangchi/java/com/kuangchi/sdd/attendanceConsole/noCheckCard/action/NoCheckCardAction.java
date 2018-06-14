package com.kuangchi.sdd.attendanceConsole.noCheckCard.action;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.noCheckCard.model.NoCheckCard;
import com.kuangchi.sdd.attendanceConsole.noCheckCard.service.INoCheckCardService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("noCheckCardAction")
public class NoCheckCardAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(NoCheckCardAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	@Resource(name = "NoCheckCardServiceImpl")
	private INoCheckCardService noCheckCardService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	private NoCheckCard model;
	public NoCheckCardAction(){
		model=new NoCheckCard();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	
	public String toMyNoCheckCard(){
		return "success";
	}
	
	//查询全部员工免打卡信息
	public void getAllNoCheckCardByStaff(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		NoCheckCard noCheck=GsonUtil.toBean(data,NoCheckCard.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*boolean isLayer = roleService.isLayer();
		if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			noCheck.setJsDm(role.getJsDm());
			
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			noCheck.setYhDm(user.getYhDm());
		} else {
			noCheck.setJsDm("0");
		}*/
		
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		noCheck.setLayerDeptNum(layerDeptNum);
		
		Grid allDiscount=noCheckCardService.getAllNoCheckCardByStaff(noCheck, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	
	}
	//新增员工免打卡信息
	public void addNoCheckCardByStaff(){ 
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String data= request.getParameter("data");
		NoCheckCard noCheck_info=GsonUtil.toBean(data,NoCheckCard.class);
		
		//字符串转化为十进制
		String s=noCheck_info.getNocheck4()+noCheck_info.getNocheck3()+
				noCheck_info.getNocheck2()+noCheck_info.getNocheck1();
	    int x = 0;
	    int mul = 1;
	    for (int i = s.length() - 1; i >= 0; i--) {
	     x += mul * (s.charAt(i) == '1' ? 1 : 0);
	     mul *= 2;
	    }
	    noCheck_info.setCheck_point(x);
	    

	    // 如果设置了一直免打卡，则默认给时间区间100年
	    if("1".equals(noCheck_info.getIs_always())){
	    	noCheck_info.setFrom_time("1970-01-01");
	    	
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    Calendar canlandar = Calendar.getInstance();
		    canlandar.setTime(new Date());
		    canlandar.add(canlandar.YEAR,100);
		    String to_time = df.format(canlandar.getTime()).toString();
		    
		    noCheck_info.setTo_time(to_time);
	    }
	    
	    User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		noCheck_info.setCreate_user(loginUser.getYhMc());
		JsonResult result = new JsonResult();
	    String staff_nums=noCheck_info.getStaff_num();
		String[] sourceStrArray = staff_nums.split(",");//对人员编号进行截取
		 for (int i = 0; i < sourceStrArray.length; i++) {
			// List<NoCheckCard> staffNumList=noCheckCardService.selectNoCheckCardByStaffNum(sourceStrArray[i]);
			    noCheck_info.setStaff_num(sourceStrArray[i]);
				boolean obj=noCheckCardService.insertNoCheckCard(noCheck_info);
				 if(obj){
						result.setMsg("添加成功");
			  			result.setSuccess(true);
					}else{
						result.setMsg("添加失败");
			  			result.setSuccess(false);
					}
			 
		 }
	printHttpServletResponse(GsonUtil.toJson(result));
}
	//修改员工免打卡信息
	public void updateNoCheckCardByStaff(){ 
		HttpServletRequest request = getHttpServletRequest();
		String data= request.getParameter("data");
		NoCheckCard noCheck_info=GsonUtil.toBean(data,NoCheckCard.class);
		
		//字符串转化为十进制
		String s=noCheck_info.getNocheck4()+noCheck_info.getNocheck3()+
				noCheck_info.getNocheck2()+noCheck_info.getNocheck1();
		int x = 0;
		int mul = 1;
		for (int i = s.length() - 1; i >= 0; i--) {
			x += mul * (s.charAt(i) == '1' ? 1 : 0);
			mul *= 2;
		}
		noCheck_info.setCheck_point(x);
		
		

	    // 如果设置了一直免打卡，则默认给时间区间100年
	    if("1".equals(noCheck_info.getIs_always())){
	    	noCheck_info.setFrom_time("1970-01-01");
	    	
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    Calendar canlandar = Calendar.getInstance();
		    canlandar.setTime(new Date());
		    canlandar.add(canlandar.YEAR,100);
		    String to_time = df.format(canlandar.getTime()).toString();
		    
		    noCheck_info.setTo_time(to_time);
	    }
	    
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		noCheck_info.setCreate_user(loginUser.getYhMc());
		JsonResult result = new JsonResult();
	    Boolean obj=noCheckCardService.updateNoCheckCardByStaff(noCheck_info);
			if(obj){
				result.setMsg("修改成功");
		        result.setSuccess(true);
			}else{
				result.setMsg("修改失败");
		        result.setSuccess(false);
			}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//删除员工免打卡信息
	public void deleNoCheckCardByStaff(){
		HttpServletRequest request = getHttpServletRequest();
		String id=request.getParameter("deleteID");//多个值传入进行批量删除
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
		JsonResult result =null;
		String [] str= id.split(",");
		for (int i = 0; i < str.length; i=i+1) {
				Integer obj=noCheckCardService.deleNoCheckCardByStaff(str[i],loginUser.getYhMc());
				 result = new JsonResult();
				if(obj==1){
					 result.setMsg("删除成功");
				     result.setSuccess(true);
				   
				}else{
					 result.setMsg("删除失败");
				    result.setSuccess(false);
				}
			}
		
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	//根据员工编号查询已存在的免打卡信息
	public void getNoCheckCardByStaffNum(){
		HttpServletRequest request = getHttpServletRequest();
		String data= request.getParameter("data");
		NoCheckCard noCheck_info=GsonUtil.toBean(data,NoCheckCard.class);
		boolean rt=noCheckCardService.getNoCheckCardByStaffNum(noCheck_info);
		JsonResult result = new JsonResult();
			if(rt){
			     result.setSuccess(true);  //免打卡时间有重复部分
			}else{
			    result.setSuccess(false);
			}
		printHttpServletResponse(GsonUtil.toJson(result));
		
	}
	//根据部门编号查询已存在的免打卡信息
		public void getNoCheckCardByDeptNum(){
			HttpServletRequest request = getHttpServletRequest();
			String data= request.getParameter("data");
			NoCheckCard noCheck_info=GsonUtil.toBean(data,NoCheckCard.class);
			boolean rt=noCheckCardService.getNoCheckCardByDeptNum(noCheck_info);
			JsonResult result = new JsonResult();
				if(rt){
				     result.setSuccess(true);  //免打卡时间有重复部分
				}else{
				    result.setSuccess(false);
				}
			printHttpServletResponse(GsonUtil.toJson(result));
			
		}
	
	

	//查询部门免打卡信息
		public void getAllNoCheckCardByDept(){
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			NoCheckCard noCheck=GsonUtil.toBean(data,NoCheckCard.class);
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				noCheck.setJsDm(role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				noCheck.setYhDm(user.getYhDm());
			} else {
				noCheck.setJsDm("0");
			}*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			noCheck.setLayerDeptNum(layerDeptNum);
			
			Grid allDiscount=noCheckCardService.getAllNoCheckCardByDept(noCheck, page, rows);
			printHttpServletResponse(GsonUtil.toJson(allDiscount));
		
		}
		
		//新增部门免打卡信息
		public void addNoCheckCardByDept(){ 
			HttpServletRequest request = getHttpServletRequest();
			String flag = request.getParameter("flag");
			String data= request.getParameter("data");
			NoCheckCard noCheck_info=GsonUtil.toBean(data,NoCheckCard.class);
			
			//字符串转化为十进制
			String s=noCheck_info.getNocheck4()+noCheck_info.getNocheck3()+
					noCheck_info.getNocheck2()+noCheck_info.getNocheck1();
		    int x = 0;
		    int mul = 1;
		    for (int i = s.length() - 1; i >= 0; i--) {
		     x += mul * (s.charAt(i) == '1' ? 1 : 0);
		     mul *= 2;
		    }
		    noCheck_info.setCheck_point(x);
		    
		    
		    // 如果设置了一直免打卡，则默认给时间区间100年
		    if("1".equals(noCheck_info.getIs_always())){
		    	noCheck_info.setFrom_time("1970-01-01");
		    	
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    Calendar canlandar = Calendar.getInstance();
			    canlandar.setTime(new Date());
			    canlandar.add(canlandar.YEAR,100);
			    String to_time = df.format(canlandar.getTime()).toString();
			    
			    noCheck_info.setTo_time(to_time);
		    }
		    
		    
		    User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			noCheck_info.setCreate_user(loginUser.getYhMc());
			JsonResult result = new JsonResult();
			Boolean obj;
			obj=noCheckCardService.insertNoCheckCardByDept(noCheck_info);
			 if(obj){
					result.setMsg("添加成功");
		  			result.setSuccess(true);
				}else{
					result.setMsg("添加失败");
		  			result.setSuccess(false);
			}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
		//修改部门免打卡信息
		public void updateNoCheckCardByDept(){ 
			HttpServletRequest request = getHttpServletRequest();
			String data= request.getParameter("data");
			NoCheckCard noCheck_info=GsonUtil.toBean(data,NoCheckCard.class);
			
			//字符串转化为十进制
			String s=noCheck_info.getNocheck4()+noCheck_info.getNocheck3()+
					noCheck_info.getNocheck2()+noCheck_info.getNocheck1();
			int x = 0;
			int mul = 1;
			for (int i = s.length() - 1; i >= 0; i--) {
				x += mul * (s.charAt(i) == '1' ? 1 : 0);
				mul *= 2;
			}
			noCheck_info.setCheck_point(x);
			

		    // 如果设置了一直免打卡，则默认给时间区间100年
		    if("1".equals(noCheck_info.getIs_always())){
		    	noCheck_info.setFrom_time("1970-01-01");
		    	
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    Calendar canlandar = Calendar.getInstance();
			    canlandar.setTime(new Date());
			    canlandar.add(canlandar.YEAR,100);
			    String to_time = df.format(canlandar.getTime()).toString();
			    
			    noCheck_info.setTo_time(to_time);
		    }
		    
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			noCheck_info.setCreate_user(loginUser.getYhMc());
			JsonResult result = new JsonResult();
			Boolean obj=noCheckCardService.updateNoCheckCardByDept(noCheck_info);
			if(obj){
				result.setMsg("修改成功");
				result.setSuccess(true);
			}else{
				result.setMsg("修改失败");
				result.setSuccess(false);
			}
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	//删除部门免打卡信息
		public void deleNoCheckCardByDept(){
			HttpServletRequest request = getHttpServletRequest();
			String id=request.getParameter("deleteID");//多个值传入进行批量删除
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			JsonResult result =null;
			String [] str= id.split(",");
			for (int i = 0; i < str.length; i=i+1) {
				Integer obj=noCheckCardService.deleNoCheckCardByDept(str[i],loginUser.getYhMc());
				result = new JsonResult();
				if(obj==1){
					result.setMsg("删除成功");
					result.setSuccess(true);
					
				}else{
					result.setMsg("删除失败");
					result.setSuccess(false);
				}
			}
			
			printHttpServletResponse(GsonUtil.toJson(result));
			
		}
	
	// 分发页面
		public String toOperatePage() {
			HttpServletRequest request = getHttpServletRequest();
			String flag = request.getParameter("flag");
			 if("editStaff".equals(flag)){
				Integer id=Integer.valueOf(request.getParameter("id"));
				 List<NoCheckCard> noCheckCard=noCheckCardService.selectNoCheckCardByStaff(id);
				 for (NoCheckCard noCheckCard2 : noCheckCard) {
					String a=Integer.toBinaryString(noCheckCard2.getCheck_point());
					if(a.length()==1){
						noCheckCard2.setNocheck4("0");
						noCheckCard2.setNocheck3("0");
						noCheckCard2.setNocheck2("0");
						noCheckCard2.setNocheck1(a.substring(0, 1));
					}else if(a.length()==2){
						noCheckCard2.setNocheck4("0");
						noCheckCard2.setNocheck3("0");
						noCheckCard2.setNocheck2(a.substring(1, 2));
						noCheckCard2.setNocheck1(a.substring(0, 1));
					}else if(a.length()==3){
						noCheckCard2.setNocheck4("0");
						noCheckCard2.setNocheck3(a.substring(2, 3));
						noCheckCard2.setNocheck2(a.substring(1, 2));
						noCheckCard2.setNocheck1(a.substring(0, 1));
					}else{
						noCheckCard2.setNocheck4(a.substring(0, 1));
						noCheckCard2.setNocheck3(a.substring(1, 2));
						noCheckCard2.setNocheck2(a.substring(2, 3));
						noCheckCard2.setNocheck1(a.substring(3, 4));
					}
				request.setAttribute("noCheckCard", noCheckCard2);
				}
				return "editStaff";
			}else if("editDept".equals(flag)){
				Integer id=Integer.valueOf(request.getParameter("id"));
				 List<NoCheckCard> noCheckCard=noCheckCardService.selectNoCheckCardByDept(id);
				 for (NoCheckCard noCheckCard2 : noCheckCard) {
					String a=Integer.toBinaryString(noCheckCard2.getCheck_point());
					if(a.length()==1){
						noCheckCard2.setNocheck4("0");
						noCheckCard2.setNocheck3("0");
						noCheckCard2.setNocheck2("0");
						noCheckCard2.setNocheck1(a.substring(0, 1));
					}else if(a.length()==2){
						noCheckCard2.setNocheck4("0");
						noCheckCard2.setNocheck3("0");
						noCheckCard2.setNocheck2(a.substring(1, 2));
						noCheckCard2.setNocheck1(a.substring(0, 1));
					}else if(a.length()==3){
						noCheckCard2.setNocheck4("0");
						noCheckCard2.setNocheck3(a.substring(2, 3));
						noCheckCard2.setNocheck2(a.substring(1, 2));
						noCheckCard2.setNocheck1(a.substring(0, 1));
					}else{
						noCheckCard2.setNocheck4(a.substring(0, 1));
						noCheckCard2.setNocheck3(a.substring(1, 2));
						noCheckCard2.setNocheck2(a.substring(2, 3));
						noCheckCard2.setNocheck1(a.substring(3, 4));
					}
				request.setAttribute("noCheckCard", noCheckCard2);
				}
				return "editDept";
			}
			return "success";
		}	
		
}