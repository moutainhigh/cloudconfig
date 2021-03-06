package com.kuangchi.sdd.attendanceConsole.attendCount.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Controller;


import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendMonthModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.service.AttendMonthService;
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

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-27下午6:16:01
 * @功能描述:员工每月考勤统计-Action
 * @参数描述:
 */
@Controller("attendMonthAction")
public class AttendMonthAction extends BaseActionSupport{
	private AttendMonthModel attendMonth;
	
	@Resource(name="attendMonthService")
	private AttendMonthService attendMonthService;
	@Resource(name="employeeService")
	private EmployeeService employeeService;
	@Resource(name="startQuertz")
    Scheduler scheduler;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	public AttendMonthAction(){
		attendMonth=new AttendMonthModel();
	}
	@Override
	public Object getModel() {
		return attendMonth;
	}
	
	/**
	 * 根据查询条件查询（分页）
	 */
	public void getAttendMonthByparam(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		AttendMonthModel monthModel=GsonUtil.toBean(beanObject,AttendMonthModel.class);
		
		Integer page=Integer.valueOf(request.getParameter("page"));
		Integer rows=Integer.valueOf(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		String staffNo = monthModel.getStaffNo();
		String staffNum = employeeService.selectStaffNum(staffNo);
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNum", staffNum);
		map.put("staffNo", monthModel.getStaffNo());
		map.put("staffName", monthModel.getStaffName());
		map.put("deptNum", monthModel.getDeptNum());
		map.put("month", monthModel.getEveryMonth());
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
		
		Grid<AttendMonthModel> monthgrid=attendMonthService.getAllAttendMonth(map);
		 
		printHttpServletResponse(GsonUtil.toJson(monthgrid));
	}
	
	/**
	 * 根据查询条件查询（分页）(门户)
	 */
	public void getAttendPortalMonthByparam(){
		
		Staff loginUser = (Staff) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_STAFF);
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		AttendMonthModel monthModel=GsonUtil.toBean(beanObject,AttendMonthModel.class);
		
		Integer page=Integer.valueOf(request.getParameter("page"));
		Integer rows=Integer.valueOf(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		//String staffNum = employeeService.selectStaffNum(loginUser.getStaff_no());
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNum", loginUser.getStaff_num());
		map.put("month", monthModel.getEveryMonth());
		map.put("skip", skip);
		map.put("rows", rows);
		
		Grid<AttendMonthModel> monthgrid=attendMonthService.getAllAttendMonth(map);
		 
		printHttpServletResponse(GsonUtil.toJson(monthgrid));
	}
	
	
	/**
	 * 员工每月考勤记录导出
	 */
	public void exporteAttendMonth(){
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); // 获取前台序列化的数据

		AttendMonthModel monthModel = GsonUtil.toBean(beanObject,AttendMonthModel.class);
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNo", monthModel.getStaffNo());
		map.put("staffName", monthModel.getStaffName());
		map.put("deptNum", monthModel.getDeptNum());
		map.put("month", monthModel.getEveryMonth());
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		map.put("layerDeptNum", layerDeptNum);
		
		List<AttendMonthModel> attendList=attendMonthService.exportAllToExcel(map);
		
		String jsonList = GsonUtil.toJson(attendList);

		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);

		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("一级机构");
		colTitleList.add("二级机构");
		colTitleList.add("月份");
		colTitleList.add("应出勤天数");
		colTitleList.add("实际出勤天数");
		colTitleList.add("请假时间（时）");
		colTitleList.add("缺打卡次数");
		colTitleList.add("工作日出勤时间合计（时）");
		colTitleList.add("周末出勤时间合计（时）");
		colTitleList.add("节假日出勤时间合计（时）");
		colTitleList.add("正常出勤时间合计（时）");
		colTitleList.add("本月平均在岗时间（时）");
		colTitleList.add("迟到早退次数");
		colTitleList.add("补卡次数");
		colTitleList.add("旷工次数");

		colList.add("staffNo");
		colList.add("staffName");
		colList.add("sjbm_mc");
		colList.add("deptName");
		colList.add("everyMonth");
		colList.add("workDaysTotal");
		colList.add("workDay");
		colList.add("leaveTime");
		colList.add("cardNot");
		colList.add("workTimeNormal");
		colList.add("overWorkTimeWeekend");
		colList.add("overWorkTimeHoliday");
		colList.add("workTimeAll");
		colList.add("workTimeAvg");
		colList.add("inZtcd");
		colList.add("cardStatus");
		colList.add("inKg");
		
		

		

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
			String fileName="员工每月考勤统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("员工每月考勤统计表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 员工每月考勤页面
	 */
	public String attendMonthView(){
		return "attendMonthView";
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-17 下午3:43:57
	 * @功能描述: 设置每月统计频率
	 * @参数描述:
	 */
	public void changeMonthCountInterval() {
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String interval = request.getParameter("interval");
		LOG.info(interval);
		
		String cronExpression = "0 0 0 1/" + interval + " * ?";
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "personalAttendanceMonthStatisticCron", cronExpression,null);
		
		
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey("personalAttendanceMonthStatisticTimer");
				
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
}
