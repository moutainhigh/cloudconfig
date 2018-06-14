
package com.kuangchi.sdd.attendanceConsole.attend.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.PunchCardModel;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendHandleService;
import com.kuangchi.sdd.attendanceConsole.attend.util.ExcelExportTemplate;
import com.kuangchi.sdd.attendanceConsole.attend.util.ExcelUtilSpecial;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDuty;
import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDutyUser;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticCalService;
import com.kuangchi.sdd.attendanceConsole.statistic.service.StatisticService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("attendHandleAction")
public class AttendHandleAction extends BaseActionSupport {
	
	public final static Semaphore semaphore=new Semaphore(1);
	static int waitedCount=0;
	static int finishedCount=0;
	private static final  Logger LOG = Logger.getLogger(AttendHandleAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	private String addedTime = " 23:59:59";
	
	@Resource(name = "attendHandleServiceImpl")
	private IAttendHandleService attendanceService;
	
	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	
	@Resource(name = "statisticService")
	private StatisticService statisticService; 
	
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Resource(name="statisticCalService")
	private StatisticCalService statisticCalService; 
	
	 private File uploadPunchCardFile;
	 
	
	public File getUploadPunchCardFile() {
		return uploadPunchCardFile;
	}
	public void setUploadPunchCardFile(File uploadPunchCardFile) {
		this.uploadPunchCardFile = uploadPunchCardFile;
	}
	@Override
	public Object getModel() {
		return null;
	}
	//批量补打卡跳转页面
	public String openUploadDialog(){
		return "success";
	}
	
	//跳转到主页面
	public String toMyAttend(){
		return "success";
	}
	public String toStaffAttend(){
		return "success";
	}
	//补打卡设置
	public String toPunchCardPage(){
		return "success";
	}
	
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-28 上午10:19:40
	 * @功能描述: 查询个人所有考勤信息
	 * @参数描述:
	 */
	public void getMyAttend(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		Staff login_staff = (Staff)request.getSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		if(login_staff == null){
			return;
		}
		
		String loginStaff = login_staff.getStaff_num();
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		String data = request.getParameter("data");
		
		AttendModel attendModel = GsonUtil.toBean(data,AttendModel.class);
		attendModel.setStaff_num(loginStaff);
		attendModel.setTo_time(attendModel.getTo_time() + addedTime);
		attendModel.setPage(page);
		attendModel.setRows(rows);
		
		List<AttendModel> myAttend = attendanceService.getMyAttend(attendModel);
		int allCount = attendanceService.getMyAttendCount(attendModel);
		
		Grid<AttendModel> grid = new Grid<AttendModel>();
		grid.setTotal(allCount);
		grid.setRows(myAttend);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-28 下午3:58:54
	 * @功能描述: 查看指定员工考勤信息
	 * @参数描述:
	 */
	public void getAllAttend(){
		HttpServletRequest request = getHttpServletRequest();
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		String data = request.getParameter("data");
		
		AttendModel attendModel = GsonUtil.toBean(data,AttendModel.class);
		attendModel.setPage(page);
		attendModel.setRows(rows);
		if(attendModel.getTo_time() != null){
			attendModel.setTo_time(attendModel.getTo_time() + addedTime);
		}
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		attendModel.setLayerDeptNum(layerDeptNum);
		
				
		List<AttendModel> allAttend = attendanceService.getAllAttend(attendModel);
		int allCount = attendanceService.getAllAttendCount(attendModel);
		
		Grid<AttendModel> grid = new Grid<AttendModel>();
		grid.setTotal(allCount);
		grid.setRows(allAttend);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * @创建人　: guibo.chen
	 * @创建时间: 2016-12-07 下午4:01:32
	 * @功能描述: 增加考勤信息
	 * @参数描述:
	 */
	public void addAttendInfo(){
		JsonResult result = new JsonResult();
		try {
			HttpServletRequest request = getHttpServletRequest();
			String checkdate = request.getParameter("checkdate");
			String staff_num = request.getParameter("staff_num");
			String staff_name = request.getParameter("staff_name");
			//PunchCardModel punchCards = GsonUtil.toBean(data,PunchCardModel.class);
			
			String[] str=staff_num.substring(0, staff_num.length()).split(",");
			String[] strs=staff_name.substring(0, staff_name.length()).split(",");
			for (int i = 0; i < strs.length; i++) {
				PunchCardModel punchCard =new PunchCardModel();
				punchCard.setStaff_num(str[i]);
				punchCard.setStaff_name(strs[i]);
				punchCard.setChecktime(checkdate+":00");
				punchCard.setFlag_status("1");
				attendanceService.addDutyAttendInfo(punchCard);
			}
			
			result.setSuccess(true);
			result.setMsg("补打卡成功");
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("补打卡失败");
			
		} finally {
			printHttpServletResponse(GsonUtil.toJson(result));
		}
		
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-29 上午11:38:48
	 * @功能描述: 删除补打卡信息
	 * @参数描述:
	 */
	public void deleteAttendInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String ids = request.getParameter("ids");
		attendanceService.deleteAttendInfoById(ids);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setMsg("删除成功");
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-29 上午10:11:25
	 * @功能描述: 查询员工班次信息
	 * @参数描述:
	 */
	public void getDutyInfo(){
		try {
			HttpServletRequest request = getHttpServletRequest();
			String staff_num = request.getParameter("staff_num");
			String from_time = request.getParameter("checkdate")+" 00:00";
			String to_time = request.getParameter("checkdate") + " 23:59";
			AttendModel attendModel = new AttendModel();
			attendModel.setStaff_num(staff_num);
			attendModel.setFrom_time(from_time);
			attendModel.setTo_time(to_time);
			Duty duty = attendanceService.getDutyInfo(attendModel);
			
			if(duty == null){
				printHttpServletResponse(GsonUtil.toJson(null));
				return;
			}
			List<String> list = new ArrayList<String>();
			list.add(duty.getDuty_time1());
			list.add(duty.getDuty_time2());
			list.add(duty.getDuty_time3());
			list.add(duty.getDuty_time4());
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("duty_time", list);
			map.put("duty_name", duty.getDuty_name());
			map.put("is_elastic", duty.getIs_elastic());
			printHttpServletResponse(GsonUtil.toJson(map));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @创建人　: 连郭飞
	 * @创建时间: 2016-6-12 下午13:23
	 * @功能描述: 手动统计考勤
	 * @参数描述:
	 */
	public void statisticAttendanceByHand(){
		boolean getResource=false;
		try {
			getResource=semaphore.tryAcquire(1,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return;
		}
		if(getResource){
			try {
				HttpServletRequest request = getHttpServletRequest();
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				String staff_num=request.getParameter("staff_num");
				String dept_num=request.getParameter("dept_num");
				String isLeave=request.getParameter("isLeave");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
				Date startTime=null;
				Date endTime=null;
				try {
					startTime = sdf.parse(startDate);
					endTime=sdf.parse(endDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				DepartmentPage departmentPage=new DepartmentPage();
				if(!"".equals(dept_num)){
					departmentPage.setDepId(dept_num);
				}
				if(!"".equals(staff_num)){
					departmentPage.setStaffNum(staff_num);
				}
				if("1".equals(isLeave)){
					departmentPage.setHireState("0"); // 若不统计离职人员，则只查询在职的员工
				}
				List<Employee> employeeList=employeeService.getEmployeeBybmDmAndstaffNum(departmentPage);
				waitedCount=employeeList.size();
				finishedCount=0;
				if(employeeList.size()!=0){
					for(Employee employee:employeeList){
						statisticService.reStatisticByStaff(startTime, endTime, employee.getYhDm());
						finishedCount++;
						waitedCount--;
					}
				}
				
				waitedCount=0;
				
				JsonResult result = new JsonResult();
				result.setSuccess(true);
				result.setMsg("统计成功");
				
				printHttpServletResponse(GsonUtil.toJson(result));
			}catch (Exception e) {
				  e.printStackTrace();
				  JsonResult result = new JsonResult();
				 result.setSuccess(false);
				 result.setMsg("统计失败，请联系系统管理员");
				 printHttpServletResponse(GsonUtil.toJson(result));
			} finally {
				semaphore.release();	 
			}
		} else {
			JsonResult result = new JsonResult();
			result.setSuccess(false);
			result.setMsg("服务器正忙，请稍候");
			printHttpServletResponse(GsonUtil.toJson(result));
		}
	}
	
	
	
	//获取前一天的时间
			public static Date getPreviousDayTime(Date date){
							if(date==null){
								return null;
							}
							 Calendar calendar =Calendar.getInstance();
							 calendar.setTime(date);
							 calendar.add(Calendar.DAY_OF_MONTH, -1);
							 return calendar.getTime();
					}	
	
	
			
			//获取日期  yyyy-MM-dd HH:mm
			public static Date getSimpleDate(String dateString){
				try {
			        return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateString);

				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			
			//获取后一天的时间
			public static Date getNextDayTime(Date date){
							if(date==null){
								return null;
							}
							 Calendar calendar =Calendar.getInstance();
							 calendar.setTime(date);
							 calendar.add(Calendar.DAY_OF_MONTH,  1);
							 return calendar.getTime();
					}
			
			
			public static String getyyyyMMddHHmm(Date date){
				return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
			}
			
			
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-01
	 * @功能描述: 获取某员工在其排班类型时间段内的打卡记录
	 * @参数描述:
	 */
	public void getMyAttendDuringDutyTime(){
		
		HttpServletRequest request = getHttpServletRequest();
		String to_time=null;
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		String staff_num = request.getParameter("staff_num");
		to_time = request.getParameter("to_time");
		String from_time = request.getParameter("from_time");
		
		  Map<String, String> attendanceDutyUserMap=new HashMap<String, String>();
		  attendanceDutyUserMap.put("staffNum", staff_num);
		  attendanceDutyUserMap.put("startTime", from_time);
		  attendanceDutyUserMap.put("endTime",to_time+" 23:59:59" );
		  
		  AttendModel attendModel = new AttendModel();
		  //查找某一天的排班
		  List<AttendanceDutyUser >  attendanceDutyUserList= statisticCalService.getAttendanceDutyUserListByStaffAndTime(attendanceDutyUserMap);
		  //如果有排班,注意每天最多一条排班记录
		  if (attendanceDutyUserList.size()>0) {
			  //attendanceDutyUserList最多只有一条记录
			  AttendanceDutyUser adu=attendanceDutyUserList.get(0);
			  Map<String,Integer> attendanceDutyMap=new HashMap<String, Integer>();
			  attendanceDutyMap.put("id", adu.getDuty_id());
			  //获取班次信息
			  AttendanceDuty attendanceDuty=statisticCalService.getAttendanceDutyById(attendanceDutyMap); 
			  
			  Date startDate=getSimpleDate(to_time+" "+attendanceDuty.getDutyStartCheckPoint());
			  Date endDate=getSimpleDate(to_time+" "+attendanceDuty.getDutyEndCheckPoint());
			  
			  if (attendanceDuty.getIsElastic()==1) { //弹性班
				  Date flagDate=getSimpleDate(to_time+" "+attendanceDuty.getDutyStartCheckPoint());
				   if (endDate.compareTo(flagDate)<0) {
					   endDate=getNextDayTime(endDate);
				   }
			}else{ //非弹性班
				    Date flagDate=getSimpleDate(to_time+" "+attendanceDuty.getDutyTime1());   

				   if (startDate.compareTo(flagDate)>=0) {
					startDate=getPreviousDayTime(startDate);
				  }
				   
				   if (endDate.compareTo(flagDate)<0) {
					endDate=getNextDayTime(endDate);
				}
			}

			  attendModel.setFrom_time(getyyyyMMddHHmm(startDate));
			  attendModel.setTo_time(getyyyyMMddHHmm(endDate));
			  attendModel.setPage(page);
			  attendModel.setRows(rows);
		  }
		  
		if(staff_num == null || "".equals(staff_num)){
			Staff login_staff = (Staff)request.getSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			if(login_staff == null){
				return;
			}
			String loginStaff = login_staff.getStaff_num();
			attendModel.setStaff_num(loginStaff);
		}else{
			attendModel.setStaff_num(staff_num);
		}
		
		
		List<AttendModel> myAttend = attendanceService.getMyAttend(attendModel);
		int allCount = attendanceService.getMyAttendCount(attendModel);
		
		Grid<AttendModel> grid = new Grid<AttendModel>();
		grid.setTotal(allCount);
		grid.setRows(myAttend);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}	
	
	/**
	 * @创建人　: chenguibo
	 * @创建时间: 2016-11-24
	 * @功能描述: 获取某员工在其排班类型时间段内的打卡记录(门户)
	 * @参数描述:
	 */
	public void getPortalMyAttendDuringDutyTime(){
		
		HttpServletRequest request = getHttpServletRequest();
		
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		String to_time = request.getParameter("to_time");
		String from_time = request.getParameter("from_time");
		 String to_times=null;
		if(from_time.equals(to_time)){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = format.parse(to_time);//取时间 
				Calendar calendar =new GregorianCalendar(); 
			     calendar.setTime(date); 
			     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			     date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
			     to_times=format.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		     
		}
		String staff_num = request.getParameter("staff_num");
		AttendModel attendModel = new AttendModel();
		attendModel.setTo_time(to_times);
		attendModel.setFrom_time(from_time);
		attendModel.setPage(page);
		attendModel.setRows(rows);
		
		if(staff_num == null || "".equals(staff_num)){
			Staff login_staff = (Staff)request.getSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			if(login_staff == null){
				return;
			}
			String loginStaff = login_staff.getStaff_num();
			attendModel.setStaff_num(loginStaff);
		}else{
			attendModel.setStaff_num(staff_num);
		}
		
		
		List<AttendModel> myAttend = attendanceService.getMyAttend(attendModel);
		int allCount = attendanceService.getMyAttendCount(attendModel);
		
		Grid<AttendModel> grid = new Grid<AttendModel>();
		grid.setTotal(allCount);
		grid.setRows(myAttend);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}	
	/**
	 * 删除数据库分区数据
	 * @author yuman.gao
	 */
	public void removeAreaRecord(){
		JsonResult result = new JsonResult();
		
		HttpServletRequest request = getHttpServletRequest();
		User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
		int beginYear = Integer.parseInt(request.getParameter("beginYear"));
		int beginMonth = Integer.parseInt(request.getParameter("beginMonth"));
		int endYear = Integer.parseInt(request.getParameter("endYear"));
		int endMonth = Integer.parseInt(request.getParameter("endMonth"));
		
		boolean removeResult = false;
		if(beginYear == endYear){
			for(int month = beginMonth; month <= endMonth; month++){
				String partition_name = "p"+beginYear+"_"+month;
				removeResult = attendanceService.removeAreaRecord(partition_name, loginUser.getYhMc());
			}
		} else {
			for(int year=beginYear; year<=endYear; year++){
				if(year == beginYear){
					for(int month = beginMonth; month <= 12; month++){
						String partition_name = "p"+year+"_"+month;
						removeResult = attendanceService.removeAreaRecord(partition_name, loginUser.getYhMc());
					}
				} else if(year != beginYear && year != endYear){
					for(int month = 1; month <= 12; month++){
						String partition_name = "p"+year+"_"+month;
						removeResult = attendanceService.removeAreaRecord(partition_name, loginUser.getYhMc());
					}
				} else {
					for(int month = 1; month <= endMonth; month++){
						String partition_name = "p"+year+"_"+month;
						removeResult = attendanceService.removeAreaRecord(partition_name, loginUser.getYhMc());
					}
				}
			}
		}
	
		if(removeResult){
			result.setSuccess(true);
			result.setMsg("清除成功");
		} else {
			result.setSuccess(false);
			result.setMsg("清除失败");
		}
		
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	 /**
     * 班次tree
     * guibo.chen
     */
    public void getClassesTree() {
        HttpServletRequest request = getHttpServletRequest();
        String id = request.getParameter("id");
        List<Map> list = new ArrayList<Map>();
        List<Duty> lists= attendanceService.getClasses();
        for (Duty duty : lists) {
        	Map<String, Object> map = new HashMap<String, Object>();

			map.put("TEXT", duty.getDuty_name());// 绑定到页面combobox中的下拉框

			map.put("ID", duty.getId());// 绑定到页面combobox中的下拉框
			list.add(map);
		}
        printHttpServletResponse(GsonUtil.toJson(list));
        //Tree treeDeparts = null;
        // 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
      /*  boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} */
        
       /* if (null == id) {
            //如果传过来的id为空的,则从根节点开始取
            treeDeparts = attendanceService.getClassesTree();
        }
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        builder.append(new Gson().toJson(treeDeparts));
        builder.append("]");
        printHttpServletResponse(builder.toString());*/
        
        
    }
	
    //根据duty_id查询排班信息guibo.chen
	public void getDutystaffInfo(){
		  HttpServletRequest request = getHttpServletRequest();
		  String page=request.getParameter("page");
		  String rows=request.getParameter("rows");
	      String dept_num = request.getParameter("dept_num");
	      String data = request.getParameter("data");
	      PunchCardModel punch=GsonUtil.toBean(data, PunchCardModel.class);
	      
	      if(punch.getDuty_id()!=null){
	    	  punch.setId(Integer.valueOf(punch.getDuty_id()));  
	      }else{
	    	  List<PunchCardModel> list=attendanceService.getdutyId();
	    	  if(list.size()!=0){
	    		  punch.setId(list.get(0).getId());  
	    	 }
	      }
	      if(!"".equals(dept_num)){
	    	  punch.setDept_num(dept_num);
	      } 
	      
	      
	     // 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
         boolean isLayer = roleService.isLayer();
         String layerDeptNum = null;
		 if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 			 punch.setLayerDeptNum(layerDeptNum);
 		 } 
			
	      Grid grid=attendanceService.getDutyUserStaffById(punch,page,rows);
	      printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//查询非弹性时间
	public void comboboxDutyTime(){
		List<Map> lists = new ArrayList<Map>();
		 List<PunchCardModel> list=attendanceService.getdutyId();
   	  	if(list.size()!=0){
   	  	List<String> listStr = new ArrayList<String>();
   	  	
   	  	List<PunchCardModel> punch=attendanceService.getdutyTimeById(String.valueOf(list.get(0).getId()));
   	  	for (PunchCardModel punchCardModel : punch) {
   	  	listStr.add(punchCardModel.getDuty_time1());
   	  	listStr.add(punchCardModel.getDuty_time2());
   	  	listStr.add(punchCardModel.getDuty_time3());
   	  	listStr.add(punchCardModel.getDuty_time4());
		}
   	  	if(listStr.size()!=0){
   	  		for (int i=0;i<listStr.size();i++) {
   	  		Map<String, Object> map = new HashMap<String, Object>();
   			map.put("TEXT", listStr.get(i));// 绑定到页面combobox中的下拉框
   			map.put("ID", listStr.get(i));// 绑定到页面combobox中的下拉框
   			lists.add(map);
			}
   	  	}
   	  	} 
   	  	printHttpServletResponse(GsonUtil.toJson(lists));
	}
	
	//查询是否是弹性班次
	public void getIsElasticById(){
		  HttpServletRequest request = getHttpServletRequest();
		 String str=null;
	      String id = request.getParameter("id");
		  if(!"".equals(id)){
			str= attendanceService.getIsElasticById(id);
	    	  //punch.setId(Integer.valueOf(id));  
	      }else{
	    	  List<PunchCardModel> list=attendanceService.getdutyId();
	    	  if(list.size()!=0){
	    		  str= attendanceService.getIsElasticById(String.valueOf(list.get(0).getId()));
	    	  } 
	      }
		 printHttpServletResponse(GsonUtil.toJson(str));
	}
	
	/**
	 * 查询当前已统计条数
	 * @author yuman.gao
	 */
	public void getCount(){
		printHttpServletResponse(GsonUtil.toJson(waitedCount + "|" +finishedCount));
	}
	
	//查询打卡时间点区间
	
			public String toPunchCardPageCheckTime(){
				  HttpServletRequest request = getHttpServletRequest();
				      String id = request.getParameter("duty_id");
				      String checkdate = request.getParameter("checkdate");
				      PunchCardModel punchCard= attendanceService.getCheckTimeById(id);
				    	  if("0".equals(punchCard.getIs_elastic())){//判断是不是弹性班  （0表示非弹性）
				    		  Date duty1=getSimpleDate(checkdate+" "+punchCard.getDuty_time1());
				    		  Date duty2=getSimpleDate(checkdate+" "+punchCard.getDuty_time2());
				    		  Date duty3=getSimpleDate(checkdate+" "+punchCard.getDuty_time3());
							  Date duty4=getSimpleDate(checkdate+" "+punchCard.getDuty_time4());
				    		  if(duty1.compareTo(duty4)>0){
				    			  SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
				    				Date date;
									try {
										date = format.parse(checkdate);
										date.setHours(date.getHours()+24);
					    				String checkdates=format.format(date);
					    				  if(duty1.compareTo(duty2)>0){
					    					  if(duty1.compareTo(duty3)>0){
					    						  request.getSession().setAttribute("checkTime1", checkdate+" "+punchCard.getDuty_time1());
										    		request.getSession().setAttribute("checkTime2", checkdate+" "+punchCard.getDuty_time2());
										    		request.getSession().setAttribute("checkTime3", checkdates+" "+punchCard.getDuty_time3());
										    		request.getSession().setAttribute("checkTime4", checkdates+" "+punchCard.getDuty_time4());
										    		return "notElastic"; 
					    					  }else{
					    						  	request.getSession().setAttribute("checkTime1", checkdate+" "+punchCard.getDuty_time1());
										    		request.getSession().setAttribute("checkTime2", checkdate+" "+punchCard.getDuty_time2());
										    		request.getSession().setAttribute("checkTime3", checkdate+" "+punchCard.getDuty_time3());
										    		request.getSession().setAttribute("checkTime4", checkdates+" "+punchCard.getDuty_time4());
										    		return "notElastic";
					    					  }
						    			  }else{
						    				 	request.getSession().setAttribute("checkTime1", checkdate+" "+punchCard.getDuty_time1());
									    		request.getSession().setAttribute("checkTime2", checkdates+" "+punchCard.getDuty_time2());
									    		request.getSession().setAttribute("checkTime3", checkdates+" "+punchCard.getDuty_time3());
									    		request.getSession().setAttribute("checkTime4", checkdates+" "+punchCard.getDuty_time4());
									    		return "notElastic";
						    			  }
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				    		  }else{
							    		request.getSession().setAttribute("checkTime1", checkdate+" "+punchCard.getDuty_time1());
										request.getSession().setAttribute("checkTime2", checkdate+" "+punchCard.getDuty_time2());
										request.getSession().setAttribute("checkTime3", checkdate+" "+punchCard.getDuty_time3());
										request.getSession().setAttribute("checkTime4", checkdate+" "+punchCard.getDuty_time4()); 
										return "notElastic"; 	  
				    		  }
				    	  }else{
				    		 Date startDate=getSimpleDate(checkdate+" "+punchCard.getElastic_default_duty_time1());
							 Date endDate=getSimpleDate(checkdate+" "+punchCard.getElastic_default_duty_time2());
				    		  if(startDate.compareTo(endDate)>0){
				    			  SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
				    				Date date;
									try {
										date = format.parse(checkdate);
										date.setHours(date.getHours()+24);
					    				String checkdates=format.format(date);
					    				request.getSession().setAttribute("checkTime","提示:打卡时间应为"+ checkdate+" "+punchCard.getElastic_default_duty_time1()+"到"+
					    						checkdates+" "+punchCard.getElastic_default_duty_time2());
					    				request.getSession().setAttribute("beginchecktime",checkdate+" "+punchCard.getElastic_default_duty_time1());
					    				request.getSession().setAttribute("endchecktime",checkdates+" "+punchCard.getElastic_default_duty_time2());
					    				
							    		return "isElastic";
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
				    		  }else{
				    				request.getSession().setAttribute("checkTime","提示:打卡时间应为   "+ checkdate+" "+
				    		  punchCard.getElastic_default_duty_time1()+" 到  "+
				    						checkdate+" "+punchCard.getElastic_default_duty_time2());
				    				request.getSession().setAttribute("beginchecktime",checkdate+" "+punchCard.getElastic_default_duty_time1());
				    				request.getSession().setAttribute("endchecktime",checkdate+" "+punchCard.getElastic_default_duty_time2());
				    			  return "isElastic";
				    		  }
				    	  }
				      return "isElastic"; 
			}
	
			
			
			//查看补打卡信息
		public void viewPunchCard(){
			 HttpServletRequest request = getHttpServletRequest();
			  String page=request.getParameter("page");
			  String rows=request.getParameter("rows");
		      String data = request.getParameter("data");
		      PunchCardModel punch=GsonUtil.toBean(data, PunchCardModel.class);
		      Grid<PunchCardModel> grid=attendanceService.getPunchCardInfo(punch, page, rows);
		      printHttpServletResponse(GsonUtil.toJson(grid));     
		}	
			
			
		/**
		 * @创建人　: 陈桂波
		 * @创建时间: 2016-12-24 上午11:42:15
		 * @功能描述: 下载批量导入模板
		 * @参数描述:
		 */
		public void downloadPunchCardTemplate(){
			   HttpServletResponse response = getHttpServletResponse();
			   OutputStream out = null;
			   ExcelExportTemplate excelExport = new ExcelExportTemplate();
			   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
			   
			   //List<String> list = holidayService.getAllHolidayNameType();
			   
			  // List<String> list = dutyUserService.getAllDutyName();//获取全部排班类型
			   //List<TitleRowCell> tRCs = new ArrayList<TitleRowCell>();
			  // List<String> list = employeeService.getAllStaffDept();//获取全部部门代码
			   TitleRowCell t1t = new TitleRowCell("员工工号",true);
			   /*for(String holiday_types:list){
				   t1t.addSuggest(holiday_types);
			   }*/
			   TitleRowCell t2t = new TitleRowCell("员工名称",true);
			   TitleRowCell t3t = new TitleRowCell("补打卡时间",true);
			  
			  
			   titleRowCells.add(t1t);
			   titleRowCells.add(t2t);
			   titleRowCells.add(t3t);
			  
			   excelExport.createLongTitleRow(titleRowCells);
			   try {
					out = response.getOutputStream();
					response.setContentType("application/x-msexcel");
					String fileName="补打卡信息模版.xls";
					response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
					excelExport.getWorkbook().write(out);
			        out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
		   }
				
		 /**
		 * @创建人　: 陈桂波
		 * @创建时间: 2016-3-31 上午9:43:12
		 * @功能描述: 根据模板导入补打卡信息
		 */
	   public String uploadPunchCardByTemplate(){
	   	HttpServletRequest request = getHttpServletRequest();
	   	FileInputStream fis=null;
	   	try {
	 		fis=new FileInputStream(uploadPunchCardFile);
	   		HSSFWorkbook workbook=new HSSFWorkbook(fis);
	   		HSSFSheet sheet=workbook.getSheetAt(0);
	   		int totalRow=sheet.getLastRowNum();
	        List<PunchCardModel> list=new ArrayList<PunchCardModel>();
		
	        if(totalRow<5){
	        	request.setAttribute("message", "上传失败，文件内容为空！");
	        	return "success";
	        }
	   		for (int i = 6; i <=totalRow; i++) {
	       		HSSFRow row=sheet.getRow(i);
	       		PunchCardModel punch = new PunchCardModel();
			
	       		String staffNo = "";
	       		String staffName = "";
	       		String staffChecktime = "";
	       		
	       		
	       		
	       		// 员工工号判断
	       		Cell staff_no_cell= row.getCell(0);
	       		if (null != staff_no_cell) {
	       			staff_no_cell.setCellType(Cell.CELL_TYPE_STRING);
	       			staffNo = staff_no_cell.getStringCellValue();
					if (null==staffNo||"".equals(staffNo)) {
						request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“员工工号”是否为空！");
						return "success";
					}
				} else {
					request.setAttribute("message", "导入失败，请检查的“员工工号”是否为空！");
					return "success";
				}
	       		
	       		// 员工名称判断
	       		Cell staff_name_cell= row.getCell(1);
	       		if (null!=staff_name_cell) {
	       		
	       			staff_name_cell.setCellType(Cell.CELL_TYPE_STRING);
	       			staffName=staff_name_cell.getStringCellValue().trim();
	       			if (null==staffName||"".equals(staffName)) {
	       				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“员工名称”是否为空！");
	       				return "success";
	       			}
				}else{
					request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“员工名称”是否为空！");
					return "success";
				}
	       		
	       		// 打卡时间判断
	       		Cell staff_checktime_cell= row.getCell(2);
	       		if (null!=staff_checktime_cell) {
	       			
	       			staff_checktime_cell.setCellType(Cell.CELL_TYPE_STRING);
	       			staffChecktime=staff_checktime_cell.getStringCellValue().trim();
	       			if (null==staffChecktime||"".equals(staffChecktime)) {
	       				request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“补打卡时间”是否为空！");
	       				return "success";
					}
				}else{
					request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“补打卡时间”是否为空！");
					return "success";
				}
	       		
	       		/*
	       		if(staffNo.length()>10){
	       			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“员工工号”长度是否正确！");
	       			return "success";
	       		}
	       		
	       		if(staffName.length()>20){
	       			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“员工名称”长度是否正确！");
	       			return "success";
	       		}*/
	       	
	       		if(!staffChecktime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
	       			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“补打卡" +
	       					"时间”格式是否正确！");
	       			return "success";
	       		}
	       		
	       		//User loginUser = (User)request.getSession().getAttribute(GlobalConstant.LOGIN_USER);
	       		
	       		// 检查工号和名称是否正确
	       		Map map = attendanceService.getStaffByNO(staffNo, staffName);
	       		if(map == null){
	       			request.setAttribute("message", "导入失败，请检查第"+(i+1)+"行的“员工工号”与“员工名称”是否正确！");
	       			return "success";
	       		} else {
	       			String staffNum = (String)map.get("staff_num");
	       			punch.setChecktime(staffChecktime);
	       			punch.setFlag_status("1");
	       			punch.setStaff_num(staffNum);
	       			punch.setStaff_name(staffName);
	       			list.add(punch);		
	       		}
			}
	   		
	   		attendanceService.PunchCardsAdd(list);
	       	request.setAttribute("message", "导入成功!");
	       	
	       	
		} catch (Exception e) {
   			request.setAttribute("message", "导入文件错误，或者选择ms office的.xls格式文档可以解决问题！");
		} finally {
			if (null!=fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	   	return "success";
	 }  	
	   
	   
	   
	   /**
		 * 导出报表
		 * @author yuman.gao
		 */
		public void reportCheckRecord(){
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String data = request.getParameter("exportData");
			
			AttendModel attendModel = GsonUtil.toBean(data,AttendModel.class);
			if(attendModel.getTo_time() != null){
				attendModel.setTo_time(attendModel.getTo_time() + addedTime);
			}
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			attendModel.setLayerDeptNum(layerDeptNum);
			
					
			List<AttendModel> allAttend = attendanceService.getAllExportAttend(attendModel);
			
			for (AttendModel attend : allAttend) {
				if("0".equals(attend.getFlag_status())){
					attend.setFlag_status("否");
				} else {
					attend.setFlag_status("是");
				}
			}
			
			String jsonList = GsonUtil.toJson(allAttend);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			
			// 设置列表头和列数据键
			List<String> colList = new ArrayList<String>();
			List<String> colTitleList = new ArrayList<String>();
			colTitleList.add("员工工号");
			colTitleList.add("员工名称");
			colTitleList.add("刷卡时间");
			colTitleList.add("刷卡设备ID");
			colTitleList.add("刷卡设备名称");
			colTitleList.add("是否补打卡");   
			colTitleList.add("创建时间");

			colList.add("staff_no");
			colList.add("staff_name");
			colList.add("checktime");
			colList.add("device_id");
			colList.add("door_name");
			colList.add("flag_status");
			colList.add("create_time");

			String[] colTitles = new String[colList.size()];
			String[] cols = new String[colList.size()];
			for (int i = 0; i < colList.size(); i++) {
				cols[i] = colList.get(i);
				colTitles[i] = colTitleList.get(i);
			}

			// 导出到Excel
			try {
				OutputStream out = response.getOutputStream();
				response.setContentType("application/x-msexcel");
				String fileName="员工考勤记录表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				Workbook workbook = ExcelUtilSpecial.exportExcel("员工考勤记录表",colTitles, cols, list);
				workbook.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}