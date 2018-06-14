package com.kuangchi.sdd.attendanceConsole.attendCount.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDateModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDetailInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendViewDateInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.service.AttendDateService;
import com.kuangchi.sdd.attendanceConsole.attendCount.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.util.file.PropertyUtils;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-27下午6:16:01
 * @功能描述:员工每日考勤统计-Action
 * @参数描述:
 */
@Controller("attendDateAction")
public class AttendDateAction extends BaseActionSupport{

	private static final long serialVersionUID = 1L;
	private  AttendDateModel attendeDate;
	private AttendViewDateInfoModel viewModel=new AttendViewDateInfoModel();
	public AttendDateAction(){
		attendeDate=new AttendDateModel();
	}
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	@Resource(name="attendDateService")
	private AttendDateService attendServcie;
	@Resource(name="startQuertz")
    Scheduler scheduler;
	@Resource(name="employeeService")
	private EmployeeService employeeService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	public Object getModel() {
		return attendeDate;
	}
	
	/**
	 * 根据查询条件查询员工每日考勤信息(分页)
	 */
	public void getAttendDateByparam(){
		
		HttpServletRequest request = getHttpServletRequest();
		//Staff loginUser = (Staff) getHttpServletRequest().getSession()
				//.getAttribute(GlobalConstant.LOGIN_STAFF);
		//request.getSession().setAttribute("date_staffNo", loginUser.getStaff_no());
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		AttendDateModel attendDate=GsonUtil.toBean(beanObject,AttendDateModel.class);
	
		Integer page=Integer.valueOf(request.getParameter("page"));
		Integer rows=Integer.valueOf(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		String staff_no = attendDate.getStaffNo();
		String attendType = attendDate.getAttendType();
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("staffName", attendDate.getStaffName());
		map.put("staffNo", staff_no);
		map.put("deptNum", attendDate.getDeptNum());
		map.put("beginDate", attendDate.getSearchSDate());
		map.put("endDate", attendDate.getSearchEDate());
		//map.put("attendType", attendType);
/*		if(attendType != null){
			if(attendType!=null){
				map.put("isException", "0");
			} else {
				map.put("isException", "1");
			}
		}*/
		map.put("skip", skip);
		map.put("rows", rows);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		map.put("layerDeptNum", layerDeptNum);
		
		
		Grid<AttendDateModel> attendGrid=attendServcie.getAllAttendDate(map); 
		
		printHttpServletResponse(GsonUtil.toJson(attendGrid));
		 
	}
	
	/**
	 * 根据查询条件查询员工每日考勤信息(分页)(门户)
	 */
	public void getAttendPortalDateByparam(){
		
		HttpServletRequest request = getHttpServletRequest();
		Staff loginUser = (Staff) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_STAFF);
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		AttendDateModel attendDate=GsonUtil.toBean(beanObject,AttendDateModel.class);
	
		Integer page=Integer.valueOf(request.getParameter("page"));
		Integer rows=Integer.valueOf(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		String attendType = attendDate.getAttendType();
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("staffNum", loginUser.getStaff_num());
		map.put("beginDate", attendDate.getSearchSDate());
		map.put("endDate", attendDate.getSearchEDate());
		//map.put("attendType", attendType);
/*		if(attendType != null){
			if(attendType!=null){
				map.put("isException", "0");
			} else {
				map.put("isException", "1");
			}
		}*/
		map.put("skip", skip);
		map.put("rows", rows);
		Grid<AttendDateModel> attendGrid=attendServcie.getAllAttendDate(map); 
		printHttpServletResponse(GsonUtil.toJson(attendGrid));
		 
	}
	
	
	
	/**
	 * 点击查看详情
	 */
	public String getById(){
		HttpServletRequest request = getHttpServletRequest();
		String staffNum= request.getParameter("staffNum");
		String dutyId=request.getParameter("dutyID");
		String everyDate=request.getParameter("everyDate");
		List<AttendDetailInfoModel> detailnfoModelList=attendServcie.getDetailByStaffNumAndDutyId(staffNum, dutyId, everyDate);
		request.setAttribute("detailnfoModelList", detailnfoModelList);
		return "view";
	}
	
	/**
	 * 点击查看详情(门户)guibo.chen
	 */
	public String getPortalById(){
		HttpServletRequest request = getHttpServletRequest();
		String staffNum= request.getParameter("staffNum");
		String dutyId=request.getParameter("dutyID");
		String everyDate=request.getParameter("everyDate");
		List<AttendDetailInfoModel> detailnfoModelList=attendServcie.getDetailByStaffNumAndDutyId(staffNum, dutyId, everyDate);
		request.setAttribute("detailnfoModelList", detailnfoModelList);
		return "view";
	}
	
	/**
	 * 员工每日考勤记录导出
	 */
	public void exporteAttendDate(){
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); // 获取前台序列化的数据
		AttendDateModel attendDate=GsonUtil.toBean(beanObject,AttendDateModel.class);
		
		String staff_no = attendDate.getStaffNo();
		String attendType = attendDate.getAttendType();
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("staffName", attendDate.getStaffName());
		map.put("staffNo", staff_no);
		map.put("deptNum", attendDate.getDeptNum());
		map.put("beginDate", attendDate.getSearchSDate());
		map.put("endDate", attendDate.getSearchEDate());
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*boolean isLayer = roleService.isLayer();
		if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			map.put("jsDm", role.getJsDm());
			
			User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
			map.put("yhDm", user.getYhDm());
		} else {
			map.put("jsDm", "0");
		}*/
		
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		map.put("layerDeptNum", layerDeptNum);
		
		
		List<AttendDateModel> attendList=attendServcie.exportAllToExcel(map);
		
		for (AttendDateModel attendDateModel : attendList) {
			if("0".equals(attendDateModel.getAttendType())){
				attendDateModel.setAttendType("异常");
			}else{
				attendDateModel.setAttendType("正常");
			}
			
			if("0".equals(attendDateModel.getInWork())){
				attendDateModel.setInWork("离职");
			}else {
				attendDateModel.setInWork("在职");
			}
			if("1".equals(attendDateModel.getTodayWork())){
				attendDateModel.setTodayWork("是");
			}else{
				attendDateModel.setTodayWork("否");
			}
			if("1".equals(attendDateModel.getIsHoliday())){
				attendDateModel.setIsHoliday("是");
			}else{
				attendDateModel.setIsHoliday("否");
			}
			if("1".equals(attendDateModel.getIsVocation())){
				attendDateModel.setIsVocation("是");
			}else{
				attendDateModel.setIsVocation("否");
			}
			String morningworkBeginTime = attendDateModel.getMorningworkBeginTime();
			if(morningworkBeginTime != null){
				attendDateModel.setMorningworkBeginTime(morningworkBeginTime.split(" ")[1]);
			}
			String afternoonworkEndTime = attendDateModel.getAfternoonworkEndTime();
			if(afternoonworkEndTime != null){
				attendDateModel.setAfternoonworkEndTime(afternoonworkEndTime.split(" ")[1]);
			}
			
			Integer noCheckSet = attendDateModel.getNoCheckSet();
			String noCheckSetStr = "";
			if((noCheckSet & 1) > 0){
				noCheckSetStr += "上午上班、";
			}
			if((noCheckSet & 2) > 0){
				noCheckSetStr += "上午下班、";
			}
			if((noCheckSet & 4) > 0){
				noCheckSetStr += "下午上班、";
			}
			if((noCheckSet & 8) >0){
				noCheckSetStr += "下午下班、";
			}
			if(noCheckSetStr != ""){
				noCheckSetStr = noCheckSetStr.substring(0, noCheckSetStr.length()-1);
			}
			
			attendDateModel.setNoCheckSetStr(noCheckSetStr);
		}
		
		String jsonList = GsonUtil.toJson(attendList);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("一级机构");
		colTitleList.add("二级机构");
		colTitleList.add("日期");
		
		//colTitleList.add("班次编号");
		colTitleList.add("班次");
		colTitleList.add("出勤情况");
		colTitleList.add("上班打卡");
		colTitleList.add("下班打卡");
		
		colTitleList.add("上班时间（小时）");
		colTitleList.add("请假时段");
		colTitleList.add("请假类型");
		colTitleList.add("因公外出时间段");
		colTitleList.add("免打卡类型/时段");
		

		colList.add("staffNo");
		colList.add("staffName");
		colList.add("sjbm_mc");
		colList.add("deptName");
		colList.add("everyDate");
		
		//colList.add("dutyId");
		colList.add("dutyName");
		colList.add("attendType");
		colList.add("morningworkBeginTime");
		colList.add("afternoonworkEndTime");
		
		colList.add("workTime");
		colList.add("leaveType");
		colList.add("leaveTimePeriod");
		colList.add("outTimePeriod");
		colList.add("noCheckSetStr");
		
		String[] colTitles = new String[colList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}

		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="员工每日考勤统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("员工每日考勤统计表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 员工每日考勤页面
	 */
	public String attendDateView(){
		return "attendDateView";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-17 下午3:43:57
	 * @功能描述: 设置每日统计频率
	 * @参数描述:
	 */
	public void changeDateCountInterval() {
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String interval = request.getParameter("interval");
		LOG.info(interval);
		
		String cronExpression = "0 0 0/" + interval + " * * ?";
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "personalAttendanceDayStatisticCron", cronExpression,null);
		
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey("personalAttendanceDayStatisticTaskTimer");
				
				CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
				trigger.setCronExpression(cronExpression);
				scheduler.rescheduleJob(triggerKey, trigger);
				result.setSuccess(true);
				result.setMsg("设置成功");
			} catch (Exception e) {
				result.setSuccess(false);
				result.setMsg("设置失败");
				e.printStackTrace();
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-17 下午4:28:39
	 * @功能描述: 查询初始化备份天数
	 * @参数描述:
	 */
	public void getInitInterval(){
    	HttpServletRequest request=getHttpServletRequest();
    	
    	String propertyFile=request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
    	Properties  property=PropertyUtils.readProperties(propertyFile);
		String cron1=property.getProperty("personalAttendanceDayStatisticCron");
		String cron2=property.getProperty("personalAttendanceMonthStatisticCron");
		String cron3=property.getProperty("departmentAttendanceMonthStatisticCron");
		int index=cron1.lastIndexOf("/");
		String inter=cron1.substring(index+1, index+3);
		int index2=cron2.lastIndexOf("/");
		String inter2=cron2.substring(index2+1, index2+3);
		int index3=cron3.lastIndexOf("/");
		String inter3=cron3.substring(index3+1, index3+3);
		
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("interval", inter);
		map.put("interval2", inter2);
		map.put("interval3", inter3);
		printHttpServletResponse(GsonUtil.toJson(map));
    }
    
}
