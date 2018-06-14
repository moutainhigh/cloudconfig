package com.kuangchi.sdd.attendanceConsole.dutyuser.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.aspectj.weaver.ast.Var;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.duty.service.DutyService;
import com.kuangchi.sdd.attendanceConsole.dutyuser.model.DutyUser;
import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.attendanceConsole.dutyuser.util.ExcelExportTemplate;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeeTreeNode;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.exception.ImportException;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;


@Scope("prototype")
@Controller("dutyUserAction")
public class DutyUserAction extends BaseActionSupport {
	public static final Logger LOG = Logger.getLogger(DutyUserAction.class);
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	@Resource(name="dutyUserServiceImpl")
	DutyUserService dutyUserService;
	@Resource(name="dutySerivceImpl")
	DutyService  dutyService;
	@Resource(name="employeeService")
	EmployeeService employeeService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	private File uploadDutyUseFile;
	
	public File getUploadDutyUseFile() {
		return uploadDutyUseFile;
	}

	public void setUploadDutyUseFile(File uploadDutyUseFile) {
		this.uploadDutyUseFile = uploadDutyUseFile;
	}

	//查询全部员工排班信息与模糊查询员工排班信息
	 public void  getDutyUserByParamPage(){
		 HttpServletRequest request=getHttpServletRequest();
	     String data=request.getParameter("data");
	     DutyUser dutyUser=GsonUtil.toBean(data, DutyUser.class);
	     
	     // 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
	     /*boolean isLayer = roleService.isLayer();
		 if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			dutyUser.setJsDm((role.getJsDm()));
			
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			dutyUser.setYhDm(user.getYhDm());
			
		} else {
			dutyUser.setJsDm("0");
		}*/
	     
	    boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		dutyUser.setLayerDeptNum(layerDeptNum);
	     
         String  page=request.getParameter("page");
         String  rows=request.getParameter("rows");
         dutyUser.setPage(Integer.parseInt(page));
         dutyUser.setRows(Integer.parseInt(rows));
	     Grid<DutyUser> grid=dutyUserService.getDutyUserByParamPage(dutyUser);
	     printHttpServletResponse(GsonUtil.toJson(grid));
	     
	 }

	 /**
	  * 判断员工是否已存在排班
	  * by gengji
	  */
	 public void  ifExistDutyUser(){
		 HttpServletRequest request=getHttpServletRequest();
		 String data=request.getParameter("data");
		 DutyUser dutyUser=GsonUtil.toBean(data, DutyUser.class);
		 JsonResult result=new JsonResult(); 
		 if( dutyUserService.getDutyUserByParamCounts(dutyUser)>0){	
			 result.setSuccess(true);
		 }else{
			 result.setSuccess(false);
		 }
		 printHttpServletResponse(GsonUtil.toJson(result));
		 
	 }
	 
	 //查询全部排班名称和排班类型
	 public void  getDutyType(){
		 List<Duty>  dutyType=dutyService.getDutyByParam(null);
		 printHttpServletResponse(GsonUtil.toJson(dutyType));
	 }
	 
	 //查询出排班开始时间和排班结束时间
	 public void  getDutyTime(){
	  HttpServletRequest  request=getHttpServletRequest();
	  String  flag=request.getParameter("flag");
	  String  duty_id=null;
	  if("0".equals(flag)){
      duty_id=request.getParameter("duty_id");
      }else{
      DutyUser dutyUser=null;
      String  id=request.getParameter("id"); 
      dutyUser=dutyUserService.getDutyUserById(id);
      duty_id=dutyUser.getDuty_id().toString();
      }
      Duty duty=new Duty();
      duty=dutyService.getDutyById(duty_id);
      printHttpServletResponse(GsonUtil.toJson(duty));
      
	 }
	 
	 //按id查看单条员工排班信息
 	  public void  getDutyUserById(){
 		  HttpServletRequest  request=getHttpServletRequest();
		  String id=request.getParameter("id");
 		   DutyUser dutyUser=null;
 		  DutyUser dutyUser2=new  DutyUser();
           if(!"".equals(id)){
        	  dutyUser=dutyUserService.getDutyUserById(id);
        	  Integer duty_id=dutyUser.getDuty_id();
              String begin_time=dutyUser.getBegin_time();
              String end_time=dutyUser.getEnd_time();
              String staff_num=dutyUser.getStaff_num();
              String staff_name=dutyUser.getStaff_name();
              String dept_num=dutyUser.getDept_num();
              String  begin_time2=begin_time.substring(0, 10);
              String  end_time2=end_time.substring(0, 10);
              dutyUser2.setBegin_time(begin_time2);
              dutyUser2.setEnd_time(end_time2);
              dutyUser2.setDuty_id(duty_id);
              dutyUser2.setStaff_num(staff_num);
              dutyUser2.setStaff_name(staff_name);
              dutyUser2.setDept_num(dept_num);
              dutyUser2.setId(Integer.parseInt(id));
           }
           
          printHttpServletResponse(GsonUtil.toJson(dutyUser2));
 	  }
	 
	//增加员工排班信息
	 public void  insertDutyUser(){
		 HttpServletRequest  request=getHttpServletRequest();
		 JsonResult result=new  JsonResult();
		 Duty duty=new Duty();
		 String data=request.getParameter("data");
		 DutyUser dutyUser=GsonUtil.toBean(data, DutyUser.class);
		 User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
         String createUser=loginUser.getYhMc();
	        
         Integer duty_id=dutyUser.getDuty_id();
         String begin_time=dutyUser.getBegin_time();
         String end_time=dutyUser.getEnd_time();
         String staff_nums=dutyUser.getStaff_num();
         String dept_nums=dutyUser.getDept_num();
         String[] staff_num=staff_nums.split(",");
         String[] dept_num=dept_nums.split(",");
         
         duty=dutyService.getDutyById(duty_id.toString());
         String  statTime=duty.getDuty_start_check_point();
         String  endTime=duty.getDuty_end_check_point();
         
         //开始时间大于结束时间时，排班结束日期加一天
         /*if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
         if(statTime.compareTo(endTime)>0){
        	 Date date;
        	 try {
        		 date = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
        		 Calendar cal = Calendar.getInstance();
        		 cal.setTime(date);
        		 cal.add(Calendar.DATE, 1);
        		 end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
        	 } catch (ParseException e) {
        		 result.setMsg("增加员工排班信息失败");
        		 e.printStackTrace();
        	 }
        	 
         }
         
         //插入时用的开始和结束时间
         String  begin_time1=begin_time+" "+statTime;
         String  end_time1=end_time+" "+endTime;
         
          
         //插入时传的model
         List<DutyUser> dutyUsers=new  ArrayList<DutyUser>();
         for(int i=0;i<staff_num.length;i++){
        	 DutyUser dutyUser1=new DutyUser();
        	 dutyUser1.setBegin_time(begin_time1);
        	 dutyUser1.setEnd_time(end_time1);
        	 dutyUser1.setDuty_id(duty_id);
        	 dutyUser1.setStaff_num(staff_num[i]);
        	 dutyUser1.setDept_num(dept_num[i]);
        	 dutyUsers.add(dutyUser1);
         }
      
         
         
    	 Date date=new Date();//创建当前系统时间
         DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String today=format.format(date);
		 boolean result1=dutyUserService.insertDutyUser(dutyUsers, today,createUser);
		 if(result1){
			 result.setMsg("增加员工排班信息成功");
			 result.setSuccess(true);
		 }else{
			 result.setMsg("增加员工排班信息失败");
			 result.setSuccess(false);
		 }
	
		
         printHttpServletResponse(GsonUtil.toJson(result));
	 }
	 
		//增加部门排班信息
	 public void  insertDutyDept(){
		 HttpServletRequest  request=getHttpServletRequest();
		 List<Map> staffNumList=null;
		 JsonResult result=new  JsonResult();
		 String data=request.getParameter("data");
	 	 Date date=new Date();//创建当前系统时间
         DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String today2=format.format(date);
		 DutyUser dutyUser=GsonUtil.toBean(data, DutyUser.class);
	     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	     String createUser=loginUser.getYhMc();
	     
         //根据部门排班（即批量对员工排班）
         Date date1=new Date();//创建当前系统时间
         DateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
         String today=format1.format(date1);
        	 
        	 Duty duty2=new Duty();
        	 
        	 Integer duty_id=dutyUser.getDuty_id();
             String begin_time=dutyUser.getBegin_time();
             String end_time=dutyUser.getEnd_time();
             
             duty2=dutyService.getDutyById(duty_id.toString());
             String  statTime=duty2.getDuty_start_check_point();
             String  endTime=duty2.getDuty_end_check_point();
             
             //开始时间大于结束时间时，排班结束日期加一天
             /*if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
             if(statTime.compareTo(endTime)>0){
            	 Date date2;
            	 try {
            		 date2 = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
            		 Calendar cal = Calendar.getInstance();
            		 cal.setTime(date2);
            		 cal.add(Calendar.DATE, 1);
            		 end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
            	 } catch (ParseException e) {
            		 result.setMsg("增加员工排班信息失败");
            		 e.printStackTrace();
            	 }
            	 
             }
             
             //插入时用的开始和结束时间
             String  begin_time1=begin_time+" "+statTime;
             String  end_time1=end_time+" "+endTime;
             //用于部门排班 
             DutyUser dutyUser2=new DutyUser();
             dutyUser2.setBegin_time(begin_time1);
             dutyUser2.setEnd_time(end_time1);
             dutyUser2.setDuty_id(duty_id);
             dutyUser2.setDept_num(dutyUser.getDept_num());//此处的dept_num 字段是拼装过的
             
	     
	     //这里进行部门排班，即将部门排班的信息保存到部门排班表（kc_attend_duty_dept）
	     boolean flag=dutyUserService.insertDutyDept(dutyUser2, today2,createUser);
	     if(! flag){
	    	 result.setMsg("增加部门排班信息失败");
        	 result.setSuccess(false);
        	 printHttpServletResponse(GsonUtil.toJson(result));
			 return;
	     }
	     if(dutyUser.getBegin_time()==null||dutyUser.getEnd_time()==null||dutyUser.getDept_num()==null||dutyUser.getDuty_id()==null){
        	 result.setMsg("请检查是否有未填项");
			  result.setSuccess(false);
			  printHttpServletResponse(GsonUtil.toJson(result));
			  return;
        }

         //根据部门代码筛选出选中部门的所有员工
         staffNumList=dutyUserService.getAllUserByDept_DM(dutyUser.getDept_num());//此处的dutyUser.getStaff_num()拿到的实际上是部门代码
         if(staffNumList.size()<=0){
        	 result.setMsg("增加部门排班信息成功");
			  result.setSuccess(true);
			  printHttpServletResponse(GsonUtil.toJson(result));
			  return;
		}
         
       //插入时传的model
         List<DutyUser> dutyUsers=new  ArrayList<DutyUser>();
         for(Map map:staffNumList){
             DutyUser dutyUser1=new DutyUser();
             dutyUser1.setBegin_time(begin_time1);
             dutyUser1.setEnd_time(end_time1);
             dutyUser1.setDuty_id(duty_id);
             dutyUser1.setStaff_num(map.get("staff_num").toString());
             dutyUser1.setDept_num(map.get("staff_dept").toString());
             dutyUsers.add(dutyUser1);
         }
         
         boolean result1=dutyUserService.insertDutyUser(dutyUsers, today2, createUser);
         if(result1 && flag){
        	 result.setMsg("新增成功，请到“排班查询”查看排班详情");
        	 result.setSuccess(true);
         }else{
        	 result.setMsg("增加部门排班信息失败");
        	 result.setSuccess(false);
         }
         
         printHttpServletResponse(GsonUtil.toJson(result));

	 }
	 
	 /**
	  * 判断部门排班是否有冲突
	  * by gengji.yang
	  */
	 public void isExistDutyUserByDept(){

		 HttpServletRequest  request=getHttpServletRequest();
		 List<Map> staffNumList=null;
		 JsonResult result=new  JsonResult();
		 String data=request.getParameter("data");

		 DutyUser dutyUser=GsonUtil.toBean(data, DutyUser.class);
	     User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	     String createUser=loginUser.getYhMc();
	     
	     if(dutyUser.getBegin_time()==null||dutyUser.getEnd_time()==null||dutyUser.getStaff_num()==null||dutyUser.getDuty_id()==null){
        	 result.setMsg("请检查是否有未填项");
			  result.setSuccess(false);
			  printHttpServletResponse(GsonUtil.toJson(result));
			  return;
        }
         //根据部门代码筛选出选中部门的所有员工
         staffNumList=dutyUserService.getAllUserByDept_DM(dutyUser.getStaff_num());//此处的dutyUser.getStaff_num()拿到的实际上是部门代码
         if(staffNumList.size()<=0){
        	 result.setMsg("此部门下没有员工");
			  result.setSuccess(false);
			  printHttpServletResponse(GsonUtil.toJson(result));
			  return;
		}
         //根据部门排班（即批量对员工排班）
         Date date1=new Date();//创建当前系统时间
         DateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
         String today=format1.format(date1);
        	 
        	 Duty duty2=new Duty();
        	 
        	 Integer duty_id=dutyUser.getDuty_id();
             String begin_time=dutyUser.getBegin_time();
             String end_time=dutyUser.getEnd_time();
             
             duty2=dutyService.getDutyById(duty_id.toString());
             String  statTime=duty2.getDuty_start_check_point();
             String  endTime=duty2.getDuty_end_check_point();
             
             //若排班开始日期与排班结束日期相等并且开始时间大于结束时间，排班结束日期加一天
            /* if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
            if(statTime.compareTo(endTime)>0){
            	 Date date2;
            	 try {
            		 date2 = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
            		 Calendar cal = Calendar.getInstance();
            		 cal.setTime(date2);
            		 cal.add(Calendar.DATE, 1);
            		 end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
            	 } catch (ParseException e) {
            		 result.setMsg("增加部门排班信息失败");
            		 e.printStackTrace();
            	 }
            	 
             }
             
             //插入时用的开始和结束时间
             String  begin_time1=begin_time+" "+statTime;
             String  end_time1=end_time+" "+endTime;
       
             for(Map map:staffNumList){
	             DutyUser dutyUser1=new DutyUser();
	             dutyUser1.setBegin_time(begin_time1);
	             dutyUser1.setEnd_time(end_time1);
	             dutyUser1.setDuty_id(duty_id);
	             dutyUser1.setStaff_num(map.get("staff_num").toString());
	             if( dutyUserService.getDutyUserByParamCounts(dutyUser1)>0){	
	    			 result.setSuccess(true);
	    			 break;
	    		 }
             }
         printHttpServletResponse(GsonUtil.toJson(result));
	 }
	 
	 
	//修改员工信息
	/*public  void  updateDutyUser(){
		HttpServletRequest  request=getHttpServletRequest();
		JsonResult  result=new JsonResult();
		Duty duty=new Duty();
		DutyUser  originDutyUser=new DutyUser();
		String data=request.getParameter("data");
		DutyUser  dutyUser=GsonUtil.toBean(data,DutyUser.class);
		
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
        String createUser=loginUser.getYhMc();
       
        //获取页面填写的修改排班信息
        Integer  id=dutyUser.getId();
        Integer duty_id=dutyUser.getDuty_id();
        String begin_time=dutyUser.getBegin_time();
        String end_time=dutyUser.getEnd_time();
        String staff_num=dutyUser.getStaff_num();
        String flag=dutyUser.getFlag();
        
        //获取排班类型的开始时间和结束时间
        duty=dutyService.getDutyById(duty_id.toString());
        String  statTime=duty.getDuty_start_check_point();
        String  endTime=duty.getDuty_end_check_point();
        
        //获取原来该员工的排班信息
        originDutyUser=dutyUserService.getDutyUserById(id.toString());

        //若排班开始日期与排班结束日期相等并且开始时间大于结束时间，排班结束日期加一天
        if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){
       	 Date date;
			try {
				date = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
			} catch (ParseException e) {
				 result.setMsg("修改员工排班信息失败");
				e.printStackTrace();
			}
       	
        }
        
        
        //新增新的排班时用的开始和结束时间
        String  begin_time1=begin_time+" "+statTime;
        String  end_time1=end_time+" "+endTime;
   
        //修改原来排班结束日期时用的结束时间
        Date date1=new Date();//获取当天日期
        DateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
        String today1="";
        //如果排班类型为跨天的情况，结束日期为当天日期
        if(statTime.compareTo(endTime)>0){
        	today1=format2.format(date1);
        }else{//否则为昨天日期
        	Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date1);
			cal2.set(Calendar.DATE, cal2.get(Calendar.DATE) - 1);
			today1=format2.format(cal2.getTime());
			
        }
        String  end_time2=today1+" "+endTime;
        
        
        //新增新排班时传的model(排班不跨了今天时用)
        DutyUser dutyUser1=new DutyUser();
        dutyUser1.setBegin_time(begin_time1);
        dutyUser1.setEnd_time(end_time1);
        dutyUser1.setDuty_id(duty_id);
        dutyUser1.setStaff_num(staff_num);
        dutyUser1.setDept_num(dutyUser.getDept_num());
        dutyUser1.setFlag(flag);
        dutyUser1.setId(id);
        
        //若排班日期跨了今天，但页面修改的排班开始日期为今天时
        if(begin_time.equals(format2.format(date1))){//如果排班开始日期等于今天
        	dutyUser1.setBegin_time(originDutyUser.getBegin_time());
    		flag="2";
    	}
        
        
        
        
        //修改原来排班结束日期时传的model(排班跨了今天时用)
        DutyUser dutyUser2=new DutyUser();
        dutyUser2.setEnd_time(end_time2);
	    dutyUser2.setId(id);
    
       try {
        	 Date date=new Date();//创建当前系统时间
             DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String today2=format.format(date);
             if("0".equals(flag)){//修改的排班不跨天的情况
            	 dutyUserService.updDutyUser(dutyUser1, createUser, today2);
             }else if("1".equals(flag)){
            	 dutyUserService.updDutyUserAmountToday(dutyUser2, dutyUser1, createUser, today2);
             }else{
            	 dutyUserService.updDutyUser(dutyUser1, createUser, today2);
             }
    		 result.setMsg("修改员工排班信息成功");
			 result.setSuccess(true);
		} catch (Exception e) {
			  e.printStackTrace();
         	 result.setMsg("修改员工排班信息失败");
			  result.setSuccess(false);
		}
		
        printHttpServletResponse(GsonUtil.toJson(result));
	}*/
	
	 
	 //修改员工排班信息
	 public  void  updateDutyUser(){
		 HttpServletRequest  request=getHttpServletRequest();
			JsonResult  result=new JsonResult();
			Duty duty=new Duty();
			DutyUser  originDutyUser=new DutyUser();
			String data=request.getParameter("data");
			DutyUser  dutyUser=GsonUtil.toBean(data,DutyUser.class);
			
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	        String createUser=loginUser.getYhMc();
	       
	        //获取页面填写的修改排班信息
	        Integer  id=dutyUser.getId();
	        Integer duty_id=dutyUser.getDuty_id();
	        String begin_time=dutyUser.getBegin_time();
	        String end_time=dutyUser.getEnd_time();
	        String staff_num=dutyUser.getStaff_num();
	        String flag=dutyUser.getFlag();
	        
	        //获取排班类型的开始时间和结束时间
	        duty=dutyService.getDutyById(duty_id.toString());
	        String  statTime=duty.getDuty_start_check_point();
	        String  endTime=duty.getDuty_end_check_point();
	        
	        //获取原来该员工的排班信息
	       /* originDutyUser=dutyUserService.getDutyUserById(id.toString());*/

	        //开始时间大于结束时间时，排班结束日期加一天
/*	        if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
	        if(statTime.compareTo(endTime)>0){
	       	 Date date;
				try {
					date = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DATE, 1);
					end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
				} catch (ParseException e) {
					 result.setMsg("修改员工排班信息失败");
					e.printStackTrace();
				}
	       	
	        }
	        
	        
	        //新增新的排班时用的开始和结束时间
	        String  begin_time1=begin_time+" "+statTime;
	        String  end_time1=end_time+" "+endTime;
	   
	        
	        
	        //新增新排班时传的model
	        DutyUser dutyUser1=new DutyUser();
	        dutyUser1.setBegin_time(begin_time1);
	        dutyUser1.setEnd_time(end_time1);
	        dutyUser1.setDuty_id(duty_id);
	        dutyUser1.setStaff_num(staff_num);
	        dutyUser1.setDept_num(dutyUser.getDept_num());
	        dutyUser1.setFlag(flag);
	        dutyUser1.setId(id);
	        
	    
	       try {
	        	 Date date=new Date();//创建当前系统时间
	             DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	             String today2=format.format(date);
	             dutyUserService.updDutyUser(dutyUser1, createUser, today2);
	    		 result.setMsg("修改员工排班信息成功");
				 result.setSuccess(true);
			} catch (Exception e) {
				  e.printStackTrace();
	         	 result.setMsg("修改员工排班信息失败");
				  result.setSuccess(false);
			}
			
	        printHttpServletResponse(GsonUtil.toJson(result));
	 }
	 
	 
	 
	
	//取消员工排班
		public void cancelDutyUser(){
			HttpServletRequest  request=getHttpServletRequest();
			JsonResult  result=new JsonResult();
			Duty duty=new Duty();
			DutyUser dutyUsers=new DutyUser();
			List<DutyUser> dutyUserList=new ArrayList<DutyUser>();
			String data=request.getParameter("data");
			Map  dutyUser=GsonUtil.toBean(data,HashMap.class);
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
	        String createUser=loginUser.getYhMc();
			
			String[]  ids=dutyUser.get("id").toString().split(",");
			String[] duty_id=dutyUser.get("duty_id").toString().split(",");
			String deleteIds=dutyUser.get("delete_ids").toString();
	        
			for(int i=0;i<ids.length;i++){
			   duty=dutyService.getDutyById(duty_id[i]);
		       String  statTime=duty.getDuty_start_check_point();
		       String  endTime=duty.getDuty_end_check_point();

		       dutyUsers=dutyUserService.getDutyUserById(ids[i]);
		       String  begin_time=dutyUsers.getBegin_time().substring(0, 10);
		       //String  end_time=dutyUsers.getBegin_time();
		        
		       //若排班开始日期与排班结束日期相等并且开始时间大于结束时间，排班结束日期加一天
		       Date date1=new Date();//获取当天日期
		       DateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
		       String today1="";
		       if(begin_time.equals(format2.format(date1)) && statTime.compareTo(endTime)>0){
		        	Calendar cal2 = Calendar.getInstance();
		        	cal2.setTime(date1);
		        	cal2.add(Calendar.DATE, 1);
		        	today1=format2.format(cal2.getTime());
		        }else{//否则为当天日期
		        	today1=format2.format(date1);
		        }
		        
		        String  end_time2=today1+" "+endTime;
		        
		        DutyUser dutyUser2=new DutyUser();
		        dutyUser2.setEnd_time(end_time2);
		        dutyUser2.setId(Integer.parseInt(ids[i]));
		        dutyUserList.add(dutyUser2);
	        }
	     
	        
	        try {
	           	 dutyUserService.updateDutyUserEndDate(dutyUserList,deleteIds,createUser);
	   		     result.setMsg("取消员工排班信息成功");
				 result.setSuccess(true);
			} catch (Exception e) {
				  e.printStackTrace();
	        	  result.setMsg("取消员工排班信息失败");
				  result.setSuccess(false);
			}
			
	       printHttpServletResponse(GsonUtil.toJson(result));
	        
		}
	
	
	//删除员工排班信息
	public void deleteDutyUser(){
		HttpServletRequest  request=getHttpServletRequest();
		String ids=request.getParameter("id");
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
        String createUser=loginUser.getYhMc();
        JsonResult  result=new JsonResult();
        result.setSuccess(true);
        
        try {
          dutyUserService.deleteDutyUserById(ids, createUser);	
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
        printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	//按筛选条件导出员工排班
	public  void exportDutyUser(){
		HttpServletRequest  request=getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		String data=request.getParameter("data");
		DutyUser  dutyUser=GsonUtil.toBean(data, DutyUser.class);
		
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		dutyUser.setLayerDeptNum(layerDeptNum);
		
		List dutyUse=dutyUserService.getDutyUserByParam(dutyUser);
		String jsonList=GsonUtil.toJson(dutyUse);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("员工工号");
		cloTitleList.add("员工名称");
		cloTitleList.add("班次名称");
		cloTitleList.add("排班开始时间");
		cloTitleList.add("排班结束时间");
		
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("duty_name");
		colList.add("begin_time");
		colList.add("end_time");
		
		String[] colTitles=new String[colList.size()];
		String[] cols=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=cloTitleList.get(i);
		}
		
	    OutputStream  out=null;
	    try {
			out=response.getOutputStream();
			response.setContentType("application/x-msecxel");
			String fileName="员工排班表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("员工排班表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
	  }  
	
	/*//下载员工排班表模版
	public void downloadDutyUseTemplate(){
		   HttpServletRequest request=getHttpServletRequest();
		   HttpServletResponse response=getHttpServletResponse();
		   response.setContentType("application/x-msexcel");
	       response.setHeader("Content-Disposition", "attachment;filename=dutyUse_template.xls");
	       //maven的下载路径
	       //String path =  request.getSession().getServletContext().getRealPath("/"+File.separator+".."+File.separator+".."+File.separator+".."+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"conf"+File.separator+"template");
		   //Tomcat的下载路径
	       String path=  request.getSession().getServletContext().getRealPath("WEB-INF"+File.separator+"classes"+File.separator+"conf"+File.separator+"template"+File.separator);
	       String fileName="DutyUse.xls";
		   File file=new File(path+File.separator+fileName);
		   FileInputStream fis=null;
		   OutputStream out=null; 
		   try {
			fis=new FileInputStream(file);
			out=response.getOutputStream();
			byte[] buffer=new byte[1024];
			while (fis.read(buffer)!=-1) {
				out.write(buffer);
			}
			out.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null!=fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null!=out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	   } */
	
	/**
	 * 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-24 下午7:17:19
	 * @功能描述: 下载部门排班
	 * @参数描述:
	 */
	
	public void downloadDeptDutyUseTemplate(){
		   HttpServletResponse response = getHttpServletResponse();
		   OutputStream out = null;
		   ExcelExportTemplate excelExport = new ExcelExportTemplate();
		   
		   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		   List<String> list = dutyUserService.getAllDutyName();//获取全部排班类型
		   
		   boolean isLayer = roleService.isLayer();
	       String layerDeptNum = null;
		   if(isLayer){
	 			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			layerDeptNum = departmentService.deptGetLayerDeptNum(user.getYhDm(), role.getJsDm());
		   } 
		   List<String> deptNumlist = employeeService.getAllStaffDept(layerDeptNum);// 获取全部部门编号
		  
		   TitleRowCell t1t = new TitleRowCell("部门名称",true);
		   for(String deptNum:deptNumlist){
			   t1t.addSuggest(deptNum);
		   }
		   TitleRowCell t2t = new TitleRowCell("排班开始时间",true);
		   TitleRowCell t3t = new TitleRowCell("排班结束时间",true);
		   TitleRowCell t4t = new TitleRowCell("班次名称",true);
	
		   for(String staff_name:list){
			   t4t.addSuggest(staff_name);
		   }
		  
		   titleRowCells.add(t1t);
		   titleRowCells.add(t2t);
		   titleRowCells.add(t3t);
		   titleRowCells.add(t4t);
		   excelExport.createDeptLongTitleRow(titleRowCells);
		   try {
				out = response.getOutputStream();
				response.setContentType("application/x-msexcel");
				String fileName="部门排班模版.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				excelExport.getWorkbook().write(out);
		        out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	
	
	
	 /**
     * 下载员工导入模板
     */
	public void downloadDutyUseTemplate(){
		   HttpServletResponse response = getHttpServletResponse();
		   OutputStream out = null;
		   ExcelExportTemplate excelExport = new ExcelExportTemplate();
		   
		   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		   List<String> list = dutyUserService.getAllDutyName();//获取全部排班类型
		   //List<TitleRowCell> tRCs = new ArrayList<TitleRowCell>();
		  // List<String> list = employeeService.getAllStaffDept();//获取全部部门代码
		   
		   boolean isLayer = roleService.isLayer();
	       String layerDeptNum = null;
		   if(isLayer){
	 			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
		   } 
			
		   List<String> staffNoList = dutyUserService.getAllStaffNo(layerDeptNum);//获取全部员工工号
		   TitleRowCell t1t = new TitleRowCell("员工工号",true);
		   for(String staffNo:staffNoList){
			   t1t.addSuggest(staffNo);
		   }
		   TitleRowCell t2t = new TitleRowCell("排班开始时间",true);
		   TitleRowCell t3t = new TitleRowCell("排班结束时间",true);
		   TitleRowCell t4t = new TitleRowCell("班次名称",true);
		   TitleRowCell t5t = new TitleRowCell("员工名称",false);
		   TitleRowCell t6t = new TitleRowCell("部门编号",false);
		   TitleRowCell t7t = new TitleRowCell("部门名称",false);
		   for(String duty_name:list){
			   t4t.addSuggest(duty_name);
		   }
		  
		   titleRowCells.add(t1t);
		   titleRowCells.add(t2t);
		   titleRowCells.add(t3t);
		   titleRowCells.add(t4t);
		   titleRowCells.add(t5t);
		   titleRowCells.add(t6t);
		   titleRowCells.add(t7t);
		   excelExport.createLongTitleRow(titleRowCells);
		   try {
				out = response.getOutputStream();
				response.setContentType("application/x-msexcel");
				String fileName="员工排班模版.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				excelExport.getWorkbook().write(out);
		        out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	

	
	
	
	
	//导入部门员工排班信息
		public String  downloadDutyUser(){
			HttpServletRequest  request=getHttpServletRequest();
			Duty duty=new Duty();
			String value = request.getParameter("value");
			FileInputStream fis = null;
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
		    String createUser=loginUser.getYhMc();
				try {
					if (uploadDutyUseFile == null) {
						request.setAttribute("message", "请选择需要上传的文件!");
						return "success";
					}
					
						fis=new FileInputStream(uploadDutyUseFile);
						HSSFWorkbook workbook=new HSSFWorkbook(fis);
						HSSFSheet sheet=workbook.getSheetAt(0);
						HSSFRow  titleRow=sheet.getRow(0);
						Cell  titleCell=titleRow.getCell(0);
						int totalRow = sheet.getLastRowNum();
						//request.setAttribute("zhi", value);
						
						if ("部门排班表".equals(titleCell.getStringCellValue().trim())
								&& "0".equals(value)) {
							if (totalRow < 5) {
								request.setAttribute("message", "导入失败，文件内容为空!");
								return "success";
							}
							List<DutyUser> dutyList = new ArrayList<DutyUser>();
							for(int i = 5; i <= totalRow; i++){
								DutyUser dutyUser=new DutyUser();
								String bmNos = null;// 主属部门编号(为解析) 必填
								String[] bmNoCode = null;// 主属部门编号
								String bmDm = null;//部门代码
								String    begin_time=null; //排班开始时间
								String    end_time=null;   //排班结束时间
								Integer    duty_id=null;    //排班类型
								String    duty_name=null;  //排班名称
								String	  dutyName[] = null;//用于解析选中的排班名称
								String    duty_names=null;
								HSSFRow  row=sheet.getRow(i);
								if(null==row){
									continue;
								}
								
								Cell bmNoCell = row.getCell(0);
								Cell beginTimeCell=row.getCell(1);
							    Cell endTimeCell=row.getCell(2);
							    Cell dutyNameCell=row.getCell(3);
							    
								if (null != bmNoCell) {
									bmNoCell.setCellType(Cell.CELL_TYPE_STRING);
									bmNos = bmNoCell.getStringCellValue();
									if (null == bmNos || "".equals(bmNos)) {
										request.setAttribute("message", "导入部门排班信息失败，请检查第"
												+ (i + 1) + "行的“部门名称”是否为空！");
										return "success";
									}
									bmNoCode = bmNos.split(" :");
									bmDm = employeeService.getBmdmByBmNo(bmNoCode[1]);

								} else {
									request.setAttribute("message", "导入部门排班信息失败，请检查第" + (i + 1)
											+ "行的“部门名称”是否为空！");
									return "success";
								}
								
							    if(null==beginTimeCell){
							    	throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“排班开始时间”是否为空!");
							    }else{
							    	//SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
							    /*	Date dt = HSSFDateUtil.getJavaDate(beginTimeCell.getNumericCellValue());//获取成DATE类型     
							    	begin_time = dateformat.format(dt);  */
							    	beginTimeCell.setCellType(Cell.CELL_TYPE_STRING);
							    	begin_time=beginTimeCell.getStringCellValue().trim();//处理相应的时间
							    }
						    	
							    if(null==endTimeCell){
							    	throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“排班结束时间”是否为空!");
							    }else{
							    	//SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");  
							    	/*Date dt1 = HSSFDateUtil.getJavaDate(endTimeCell.getNumericCellValue());//获取成DATE类型     
							    	end_time = dateformat1.format(dt1);*/
							    	endTimeCell.setCellType(Cell.CELL_TYPE_STRING);
							    	end_time=endTimeCell.getStringCellValue().trim();
							    }
							    
							    if(null==dutyNameCell){
							    	throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“班次名称”是否为空!");
							    }else{
							    	dutyNameCell.setCellType(Cell.CELL_TYPE_STRING);
							    	duty_names=dutyNameCell.getStringCellValue().trim();//需解析
							    	dutyName = duty_names.split(":");
							    	duty_name = dutyName[0];
							    }
								
							    
							    //判断排班类型
						    	duty_id=dutyUserService.getDutyIdByDutyName(duty_name);
						    	if(null==duty_name||"".equals(duty_name)){
						    		throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“班次名称”是否为空!");
						    	}else if(null==duty_id){
						    		throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“班次名称”是否存在!");
						    	}
							    
								duty=dutyService.getDutyById(duty_id.toString());
						    	String  statTime=duty.getDuty_start_check_point();//本班次开始统计打卡时间点
						    	String  endTime=duty.getDuty_end_check_point();//本班次结束统计打卡时间点
						    	 //开始时间大于结束时间时，排班结束日期加一天
						       /* if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
						        if(statTime.compareTo(endTime)>0){
						        	 Date date2;
									try {
										date2 = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
										Calendar cal = Calendar.getInstance();
										cal.setTime(date2);
										cal.add(Calendar.DATE, 1);
										end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
									} catch (ParseException e) {
										e.printStackTrace();
									}
						        	
						         }
						        
						    	Date date=new Date();//创建当前系统时间
				                DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
				                String today1=format1.format(date);
				                
								//判断排班开始时间
						        if(null==begin_time||"".equals(begin_time)){
						        	throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“排班开始时间”是否为空!");
						        
						        }else if(!begin_time.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")){
						        	throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“排班开始时间”格式是否正确!");
						        }else if(today1.compareTo(begin_time)>0){
						        	throw new ImportException("导入部门排班信息失败，第"+(i+1)+"行的“排班开始时间”不能早于当天日期 !");
						        }
							    
							    //判断排班结束时间
						    	if(null==end_time||"".equals(end_time)){
						    		throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“排班结束时间”是否为空!");
						         }else if(!end_time.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")){
								     throw new ImportException("导入部门排班信息失败，请检查第"+(i+1)+"行的“排班结束时间”格式是否正确!");
							     }
								  if(!("".equals(begin_time))||null!=begin_time){
									
									if(begin_time.compareTo(end_time)>0){
			    					   throw new ImportException("导入部门排班信息失败，第"+(i+1)+"行的“排班开始时间”不能晚于“排班结束时间”!");
									}
								}
								  
								   //导入时用的开始和结束时间
							        String  begin_time1=begin_time+" "+statTime;
							        String  end_time1=end_time+" "+endTime;
							        
							        //给部门下的所有员工添加一样的排班
					                dutyUser.setBegin_time(begin_time1);
					                dutyUser.setEnd_time(end_time1);
					                dutyUser.setDuty_id(duty_id);
					                dutyUser.setDept_num(bmDm);
					                dutyList.add(dutyUser);
					                
							  }   
									Date date3=new Date();//创建当前系统时间
					                DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					                String today2=format2.format(date3);
					                //boolean result = false;
					                boolean result1 = false; 
					                for(int i=0;i<dutyList.size();i++){
					                	  /* result = dutyUserService.insertDutyDept(dutyList.get(i), today2,createUser);
					                	   if(!result){
					                		   break;
					                	   }*/
					                	//用于部门排班
					                	DutyUser dutyUser2=new DutyUser();
					                    dutyUser2.setBegin_time(dutyList.get(i).getBegin_time());
					                    dutyUser2.setEnd_time(dutyList.get(i).getEnd_time());
					                    dutyUser2.setDuty_id(dutyList.get(i).getDuty_id());
					                    dutyUser2.setDept_num(dutyList.get(i).getDept_num());//此处的dept_num 字段是拼装过的
					                	
					                	dutyUserService.insertNewDeptDutyWithoutConflict(dutyList.get(i).getDept_num(), today2,dutyUser2);
					                	   //根据部门代码筛选出选中部门的所有员工
					                	 List<Map> staffNumList = dutyUserService.getAllUserByDept_DM("'"+dutyList.get(i).getDept_num()+"'");//此处的dutyUser.getStaff_num()拿到的实际上是部门代码
							             if(null!=staffNumList){
							            	 List<DutyUser> dutyUsers=new  ArrayList<DutyUser>();
								             for(Map map:staffNumList){
								                 DutyUser dutyUser1=new DutyUser();
								                 dutyUser1.setBegin_time(dutyList.get(i).getBegin_time());
								                 dutyUser1.setEnd_time(dutyList.get(i).getEnd_time());
								                 dutyUser1.setDuty_id(dutyList.get(i).getDuty_id());
								                 dutyUser1.setStaff_num(map.get("staff_num").toString());
								                 dutyUser1.setDept_num(map.get("staff_dept").toString());
								                 dutyUsers.add(dutyUser1);
								             }
								             result1 = dutyUserService.insertDutyUser(dutyUsers, today2, createUser);//新增部门下员工的排班
								             if(!result1){
								            	 break;
								            	 }
							             }
					                }
				             if(result1){
				            	 request.setAttribute("message", "导入成功!");
				             }else{
				            	 request.setAttribute("message", "导入部门排班失败！");
				             }
				             return  "success";
				}else if ("员工排班表".equals(titleCell.getStringCellValue().trim())
						&& "1".equals(value)) {
					List<DutyUser> list = new ArrayList<DutyUser>();
	                  if(totalRow>4){
						for(int i=5;i<=totalRow;i++){
							DutyUser dutyUser = new DutyUser();
							HSSFRow  row=sheet.getRow(i);
						    //DutyUser  dutyUser=new DutyUser();
							//String    staff_name=null; //员工名称
							String    staff_no=null; //员工工号
							String    begin_time=null; //排班开始时间
							String    end_time=null;   //排班结束时间
							Integer    duty_id=null;    //排班类型
							String    duty_name=null;  //排班名称
							String	  dutyName[] = null;//用于解析选中的排班名称
							String    duty_names=null;
					    
						//判断有无空行,若有空行就跳出本次循环
						if(null==row){
							continue;
						}
						
						//获取员工工号，排班开始时间，排班结束时间，排班类型
						Cell  staffNoCell=row.getCell(0);
						Cell beginTimeCell=row.getCell(1);
					    Cell endTimeCell=row.getCell(2);
					    Cell dutyNameCell=row.getCell(3);
					    
					    if(null==staffNoCell){
					    	throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“员工工号”是否为空!");
					    }else{
					    	staffNoCell.setCellType(Cell.CELL_TYPE_STRING);
					    	staff_no=staffNoCell.getStringCellValue().trim();
					    }
						
					    if(null==beginTimeCell){
					    	throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“排班开始时间”是否为空!");
					    }else{
					    	/*SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");  
					    	Date dt = HSSFDateUtil.getJavaDate(beginTimeCell.getNumericCellValue());//获取成DATE类型     
					    	begin_time = dateformat.format(dt);  */
					    	beginTimeCell.setCellType(Cell.CELL_TYPE_STRING);
					    	begin_time=beginTimeCell.getStringCellValue().trim();//处理相应的时间
					    }
				    	
					    if(null==endTimeCell){
					    	throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“排班结束时间”是否为空!");
					    }else{
					    	/*SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");  
					    	Date dt1 = HSSFDateUtil.getJavaDate(endTimeCell.getNumericCellValue());//获取成DATE类型     
					    	end_time = dateformat1.format(dt1);*/
					    	endTimeCell.setCellType(Cell.CELL_TYPE_STRING);
					    	end_time=endTimeCell.getStringCellValue().trim();
					    }
				    	
					    if(null==dutyNameCell){
					    	throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“班次名称”是否为空!");
					    }else{
					    	dutyNameCell.setCellType(Cell.CELL_TYPE_STRING);
					    	duty_names=dutyNameCell.getStringCellValue().trim();//需解析
					    	dutyName = duty_names.split(":");
					    	duty_name = dutyName[0];
					    }
				    	
				    	Date date=new Date();//创建当前系统时间
		                DateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
		                String today1=format1.format(date);
		                
				    	//判断是否有空行，若有空行就跳出本次循环
	                    /*if((null==staff_no || "".equals(staff_no)) && (null==begin_time ||"".equals(begin_time)) 
	                    && (null==end_time ||"".equals(end_time)) && (null==duty_name||"".equals(duty_name))){
	                    	continue;
	                    }*/
					    //判断员工工号
						Integer count=dutyUserService.getCountByStaffNum(staff_no);
						if(null==staff_no||"".equals(staff_no)){
					    throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“员工工号”是否为空!");
						}else if(count==0){
							throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“员工工号”是否存在! ");
					
						}
						
						
						//判断排班开始时间
				        if(null==begin_time||"".equals(begin_time)){
				        	throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“排班开始时间”是否为空!");
				        
				        }else if(!begin_time.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")){
				        	throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“排班开始时间”格式是否正确!");
				        }else if(today1.compareTo(begin_time)>0){
				        	throw new ImportException("导入员工排班信息失败，第"+(i+1)+"行的“排班开始时间”不能早于当天日期!");
				        }
					    
					    
					    //判断排班结束时间
				    	if(null==end_time||"".equals(end_time)){
				    		throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“排班结束时间”是否为空!");
				         }else if(!end_time.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")){
						     throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“排班结束时间”格式是否正确!");
					     }
						  if(!("".equals(begin_time))||null!=begin_time){
							
							if(begin_time.compareTo(end_time)>0){
	    					   throw new ImportException("导入员工排班信息失败，第"+(i+1)+"行的“排班开始时间”不能晚于“排班结束时间”!");
							}
						}
					
	    			
					    
					    //判断排班类型
				    	duty_id=dutyUserService.getDutyIdByDutyName(duty_name);
				    	if(null==duty_name||"".equals(duty_name)){
				    		throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“班次名称”是否为空!");
				    	}else if(null==duty_id){
				    		throw new ImportException("导入员工排班信息失败，请检查第"+(i+1)+"行的“班次名称”是否存在!");
				    	}
					    
				    	duty=dutyService.getDutyById(duty_id.toString());
				    	String  statTime=duty.getDuty_start_check_point();
				    	String  endTime=duty.getDuty_end_check_point();
				    	//开始时间大于结束时间时，排班结束日期加一天
				        /*if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
				        if(statTime.compareTo(endTime)>0){
				        	 Date date2;
							try {
								date2 = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
								Calendar cal = Calendar.getInstance();
								cal.setTime(date2);
								cal.add(Calendar.DATE, 1);
								end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
							} catch (ParseException e) {
								e.printStackTrace();
							}
				        	
				         }
				        //导入时用的开始和结束时间
				        String  begin_time1=begin_time+" "+statTime;
				        String  end_time1=end_time+" "+endTime;
				        
				        dutyUser.setBegin_time(begin_time1);
				        dutyUser.setEnd_time(end_time1);
				        dutyUser.setDuty_id(duty_id);
				        dutyUser.setStaff_no(staff_no);
				        String staff_num = employeeService.selectStaffNum(staff_no);
				        dutyUser.setStaff_num(staff_num);
				        EmployeeTreeNode empNode = employeeService.getEmployeeByStaffNo(staff_no);
				        dutyUser.setDept_num(empNode.getBmDm());
				        list.add(dutyUser);
					}
				        
				        //导入时传的model
				     /*   DutyUser dutyUser1=new DutyUser();
				        dutyUser1.setBegin_time(begin_time1);
				        dutyUser1.setEnd_time(end_time1);
				        dutyUser1.setDuty_id(duty_id);
				        dutyUser1.setStaff_no(staff_no);*/
						 Date date3=new Date();//创建当前系统时间
			             DateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			             String today2=format2.format(date3);
						for(int i=0;i<list.size();i++){
							dutyUserService.insertNewDutyWithoutConflict(list.get(i).getStaff_num(), today2, list.get(i));
						}
						request.setAttribute("message", "导入成功!");
	                 }else{
	                	  request.setAttribute("message", "导入员工排班信息失败，请检查导入内容是否为空！");
	                 	}
					}else{
						  request.setAttribute("message", "导入排班信息失败！请检查导入模板或导入数据是否正确！");
					}
						
				}catch(ImportException  e2){
					request.setAttribute("message", e2.getMessage());
				}catch (Exception e) {
					e.printStackTrace();
	                request.setAttribute("message", "导入排班信息失败！请检查导入模板或导入数据是否正确！");
				}finally{
					if(null!=fis){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
			return  "success";
		}
	
	//获取当前服务器时间
	public void getSystemTime(){
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
		String str = dateFormat.format( now ); 
		printHttpServletResponse(GsonUtil.toJson(str));
		
		/*Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int date = c.get(Calendar.DATE); 
		String str=""+year+"-";
		if(month+1<10){
			str+="0"+(month+1)+"-";
		}else{
			str+=(month+1)+"-";
		}
		
		if(date<10){
			str+="0"+date;
		}else{
			str+=date;
		}
		LOG.info(str); */
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-31
	 * @功能描述: 通过员工编号获取排班类型ID
	 * @参数描述:
	 */
    public void  getDutyIdByStaffNum2(){
    	HttpServletRequest  request=getHttpServletRequest();
		String staffNum=request.getParameter("staffNum");
		String every_date=request.getParameter("everydate");
		Map<String, Object> map=new HashMap<String, Object>();
		LOG.info("staffNum:"+staffNum+" ,"+"every_date:"+every_date);
		map.put("staffNum", staffNum);
		map.put("every_date", every_date);
		DutyUser duty=dutyUserService.getDutyIdByStaffNum(map);
		printHttpServletResponse(GsonUtil.toJson(duty));
    }
	
    /**
	 * @创建人　: 陈桂波
	 * @创建时间: 2016-11-22
	 * @功能描述: （门户）通过员工编号获取排班类型ID
	 * @参数描述:
	 */
    public void  getPortalDutyIdByStaffNum2(){
    	HttpServletRequest  request=getHttpServletRequest();
		String staffNum=request.getParameter("staffNum");
		String every_date=request.getParameter("everydate");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("staffNum", staffNum);
		map.put("every_date", every_date);
		DutyUser duty=dutyUserService.getDutyIdByStaffNum(map);
		printHttpServletResponse(GsonUtil.toJson(duty));
    }
	

    //查询部门排班信息
  	 public void  getDutyDeptByParamPage(){
  		 HttpServletRequest request=getHttpServletRequest();
  	     HashMap map=GsonUtil.toBean(request.getParameter("data"), HashMap.class);
  	     String page=request.getParameter("page");
		 String rows=request.getParameter("rows");
		 map.put("skip", (Integer.parseInt(page)-1)*Integer.parseInt(rows));
		 map.put("rows", Integer.parseInt(rows));
		 
		 boolean isLayer = roleService.isLayer();
         String layerDeptNum = null;
		 if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		 } 
		 map.put("layerDeptNum", layerDeptNum);
			
		 Grid grid=dutyUserService.getDutyDeptByParamPage(map); 
  	     printHttpServletResponse(GsonUtil.toJson(grid));
  	     
  	 }
	
  	 //按id查看单条部门排班信息
	  public void  getDutyDeptById(){
		  HttpServletRequest  request=getHttpServletRequest();
		  String id=request.getParameter("id");
		  Map map=null;
          if(!"".equals(id)){
       	     map=dutyUserService.getDutyDeptById(id);
          }
         printHttpServletResponse(GsonUtil.toJson(map));
	  }
	  
	  //按筛选条件导出部门排班信息
	  public  void exportDutyDept(){
			HttpServletRequest  request=getHttpServletRequest();
			HttpServletResponse  response=getHttpServletResponse();
			HashMap map=GsonUtil.toBean(request.getParameter("data"), HashMap.class);
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			map.put("layerDeptNum", layerDeptNum);
			
			List dutyDept=dutyUserService.getDutyDeptByParam(map);
			String jsonList=GsonUtil.toJson(dutyDept);
			List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
			//设置 列表头和列数据键
			List<String> colList=new ArrayList<String>();
			List<String> cloTitleList=new ArrayList<String>();
			
			cloTitleList.add("部门编号");
			cloTitleList.add("部门名称");
			cloTitleList.add("班次名称");
			cloTitleList.add("排班开始时间");
			cloTitleList.add("排班结束时间");
			
			colList.add("dept_no");
			colList.add("dept_name");
			colList.add("duty_name");
			colList.add("begin_time");
			colList.add("end_time");
			
			String[] colTitles=new String[colList.size()];
			String[] cols=new String[colList.size()];
			for(int i=0;i<colList.size();i++){
				cols[i]=colList.get(i);
				colTitles[i]=cloTitleList.get(i);
			}
			
		    OutputStream  out=null;
		    try {
				out=response.getOutputStream();
				response.setContentType("application/x-msecxel");
				String fileName="部门排班表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
	            Workbook  workbook=ExcelUtilSpecial.exportExcel("部门排班表", colTitles, cols, list);
		        workbook.write(out);
		        out.flush();
		    } catch (Exception e) {
				e.printStackTrace();
			}
		  } 
	  
	   
	//修改部门排班
	public  void  updateDutyDept(){
		HttpServletRequest  request=getHttpServletRequest();
		JsonResult  result=new JsonResult();
		Duty duty=new Duty();
		String data=request.getParameter("data");
		List<Map> staffNumList=null;
		
		//获取页面数据
		DutyUser  dutyUser=GsonUtil.toBean(data,DutyUser.class);
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
        String createUser=loginUser.getYhMc();
        Integer  id=dutyUser.getId();
        Integer duty_id=dutyUser.getDuty_id();
        String begin_time=dutyUser.getBegin_time();
        String end_time=dutyUser.getEnd_time();
        
        
        duty=dutyService.getDutyById(duty_id.toString());
        String  statTime=duty.getDuty_start_check_point();
        String  endTime=duty.getDuty_end_check_point();
        
        
        //开始时间大于结束时间时，排班结束日期加一天
       /* if(begin_time.equals(end_time) && statTime.compareTo(endTime)>0){*/
        if(statTime.compareTo(endTime)>0){
       	 Date date;
			try {
				date = (new SimpleDateFormat("yyyy-MM-dd")).parse(end_time);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, 1);
				end_time=(new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime());
			} catch (ParseException e) {
				 result.setMsg("修改员工排班信息失败");
				e.printStackTrace();
			}
       	
        }
        
        
        //修改时用的开始和结束时间
        String  begin_time1=begin_time+" "+statTime;
        String  end_time1=end_time+" "+endTime;
        
        //用于修改部门排班 
        DutyUser dutyDept=new DutyUser();
        dutyDept.setBegin_time(begin_time1);
        dutyDept.setEnd_time(end_time1);
        dutyDept.setDuty_id(duty_id);
        dutyDept.setDept_num(dutyUser.getDept_num());
        dutyDept.setId(id);
        
      
      //根据部门代码筛选出选中部门的所有员工
      staffNumList=dutyUserService.getStaffMessByDept_DM("'"+dutyUser.getDept_num()+"'");
     
        
      //修改时传的model(这一步已经没用，但是下面的方法封装时要传这个值过去，但在那个封装的方法里实际没用上)
        List<DutyUser> dutyUsers=new  ArrayList<DutyUser>();
        if(staffNumList.size()<=0){
        	    dutyUsers=null;
		}else{
			for(Map map:staffNumList){
				DutyUser dutyUser1=new DutyUser();
				dutyUser1.setBegin_time(begin_time1);
				dutyUser1.setEnd_time(end_time1);
				dutyUser1.setDuty_id(duty_id);
				dutyUser1.setStaff_num(map.get("staff_num").toString());
				dutyUser1.setDept_num(map.get("staff_dept").toString());
				dutyUser1.setId((Integer)map.get("id"));
				dutyUsers.add(dutyUser1);
			}
		}
        
    
       Map<String,String> log = new HashMap<String,String>();
       try {
        	 Date date=new Date();//创建当前系统时间
             DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             String today2=format.format(date);
    		 dutyUserService.updDutyDept(dutyDept, dutyUsers, createUser, today2);
	    	 log.put("V_OP_TYPE", "业务");
			 log.put("V_OP_NAME", "部门排班维护");
			 log.put("V_OP_FUNCTION", "修改");
			 log.put("V_OP_ID", createUser);
			 log.put("V_OP_MSG", "修改排班信息成功");
			 logDao.addLog(log);
    		 result.setMsg("修改部门排班信息成功");
			 result.setSuccess(true);
		} catch (Exception e) {
		     e.printStackTrace();
		     log.put("V_OP_TYPE", "异常");
			 log.put("V_OP_NAME", "部门排班维护");
			 log.put("V_OP_FUNCTION", "修改");
			 log.put("V_OP_ID", createUser);
			 log.put("V_OP_MSG", "修改排班信息失败");
			 logDao.addLog(log);
         	 result.setMsg("修改部门排班信息失败");
			 result.setSuccess(false);
		}
		
        printHttpServletResponse(GsonUtil.toJson(result));
	}  
	  

	//删除部门排班信息
	public void deleteDutyDept(){
		HttpServletRequest  request=getHttpServletRequest();
	    Gson gson=new Gson();
		Map map=gson.fromJson(request.getParameter("data"), HashMap.class);
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
        String createUser=loginUser.getYhMc();
        JsonResult  result=new JsonResult();
      
        try {
        	//根据部门代码筛选出选中部门的所有员工
        	List<Map>  staffNumList=dutyUserService.getStaffMessByDept_DM(map.get("dept_num").toString());
        	
        	//获取该部门下所有员工的排班信息id
        	String staffIds="";
        	if(null != staffNumList && staffNumList.size()>0){
        		for(Map m:staffNumList){
        			staffIds+="'"+m.get("id")+"',";
        		}
        		staffIds=staffIds.substring(0, staffIds.length()-1);
        	}else{
        		staffIds="''";
        	}
    		
    		//获取部门排班信息id
        	String deptIds=map.get("ids").toString();
        	
        	boolean  flag=dutyUserService.deleteDutyDept(staffIds, deptIds, createUser);
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
	
	//取消部门排班
	public void cancelDutyDept(){
		HttpServletRequest  request=getHttpServletRequest();
		JsonResult  result=new JsonResult();
		Duty duty=new Duty();
		Map dutyUsers=new HashMap();
		List<DutyUser> dutyUserList=new ArrayList<DutyUser>();
		String data=request.getParameter("data");
		Map  dutyUser=GsonUtil.toBean(data,HashMap.class);
		User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);        
        String createUser=loginUser.getYhMc();
		
		String[]  ids=dutyUser.get("id").toString().split(",");
		String[] duty_id=dutyUser.get("duty_id").toString().split(",");
		String deleteIds=dutyUser.get("delete_ids").toString();
        
		for(int i=0;i<ids.length;i++){
		   duty=dutyService.getDutyById(duty_id[i]);
	       String  statTime=duty.getDuty_start_check_point();
	       String  endTime=duty.getDuty_end_check_point();
	        
	       dutyUsers=dutyUserService.getDutyDeptById(ids[i]);
	       String  begin_time=dutyUsers.get("begin_time").toString().substring(0, 10);
	       //String  end_time=dutyUsers.get("end_time").toString();
	       
	       //若排班开始日期与排班结束日期相等并且开始时间大于结束时间，排班结束日期加一天
	       Date date1=new Date();//获取当天日期
	       DateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
	       String today1="";
	       if(begin_time.equals(format2.format(date1)) && statTime.compareTo(endTime)>0){
	        	Calendar cal2 = Calendar.getInstance();
	        	cal2.setTime(date1);
	        	cal2.add(Calendar.DATE, 1);
	        	today1=format2.format(cal2.getTime());
	        }else{//否则为当天日期
	        	today1=format2.format(date1);
	        }
	        
	        String  end_time2=today1+" "+endTime;
	        
	        DutyUser dutyUser2=new DutyUser();
	        dutyUser2.setEnd_time(end_time2);
	        dutyUser2.setId(Integer.parseInt(ids[i]));
	        dutyUserList.add(dutyUser2);
        }
     
        
        try {
           	 dutyUserService.updateDutyDeptEndDate(dutyUserList, deleteIds, createUser);
   		     result.setMsg("取消部门排班信息成功");
			 result.setSuccess(true);
		} catch (Exception e) {
			  e.printStackTrace();
        	  result.setMsg("取消部门排班信息失败");
			  result.setSuccess(false);
		}
		
       printHttpServletResponse(GsonUtil.toJson(result));
        
	}
	
	
	@Override
	public Object getModel() {
		return null;
	}
	
	
}
