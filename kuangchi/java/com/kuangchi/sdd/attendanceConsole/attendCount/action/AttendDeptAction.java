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

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDeptModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.service.AttendDeptService;
import com.kuangchi.sdd.attendanceConsole.attendCount.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;


/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-27下午6:16:01
 * @功能描述:部门考勤统计-Action
 * @参数描述:
 */
@Controller("attendDeptAction")
public class AttendDeptAction extends BaseActionSupport {
	private AttendDeptModel model;
	
	public AttendDeptAction(){
		model=new AttendDeptModel();
	}
	

	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	@Resource(name="attendDeptService")
	private AttendDeptService attendDeptService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	@Resource(name="startQuertz")
    Scheduler scheduler;
	
	@Override
	public Object getModel() {
		return model;
	}
	
	/**
	 * 模糊查询部门考勤（分页）
	 */
	public void getAttendDeptByparam(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); // 获取前台序列化的数据
		AttendDeptModel attendDept=GsonUtil.toBean(beanObject,AttendDeptModel.class);
		
		Integer page=Integer.valueOf(request.getParameter("page"));
		Integer rows=Integer.valueOf(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		String month = attendDept.getEveryMonth();
		String deptNum = attendDept.getDeptNum();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptNum", deptNum);
		map.put("month", month);
		map.put("skip", skip);
		map.put("rows", rows);
		
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
		
		Grid<AttendDeptModel> attendDeptGrid=attendDeptService.getAllAttendDept(map);
		printHttpServletResponse(GsonUtil.toJson(attendDeptGrid));
	}
	
	/**
	 * 部门月考勤导出
	 */
	public void exporteAttendDept(){
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); // 获取前台序列化的数据
		AttendDeptModel attendDept=GsonUtil.toBean(beanObject,AttendDeptModel.class);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deptNum", attendDept.getDeptNum());
		map.put("month", attendDept.getEveryMonth());
		
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
		
		List<AttendDeptModel> attendList=attendDeptService.exportAllToExcel(map);
		String jsonList = GsonUtil.toJson(attendList);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);

		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("部门编号");
		colTitleList.add("部门名称");
		colTitleList.add("月份");
		colTitleList.add("参与计算人数");
		colTitleList.add("正常出勤天数");
		colTitleList.add("缺打卡次数");
		colTitleList.add("正常出勤时间合计");
		colTitleList.add("本月平均在岗时间");
		colTitleList.add("迟到早退次数");
		colTitleList.add("旷工次数");
		
	

		colList.add("deptNo");
		colList.add("deptName");
		colList.add("everyMonth");
		colList.add("membersNum");
		colList.add("workDays");
		colList.add("cardNot");
		colList.add("allWorkTime");
		colList.add("avgWorkTime");
		colList.add("inZtcd");
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
			String fileName="部门每月考勤统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("部门每月考勤统计表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 部门月考勤页面
	 */
	public String attendDeptView(){
		return "attendDeptView";
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-17 下午3:43:57
	 * @功能描述: 设置部门统计频率
	 * @参数描述:
	 */
	public void changeDeptCountInterval() {
		
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String interval = request.getParameter("interval");
		LOG.info(interval); 
		
		String cronExpression = "0 0 0 1/" + interval + " * ?";
		String propertyFile = request.getSession().getServletContext().getRealPath("/WEB-INF/classes/conf/properties/damon_thread_setting.properties");
		
		boolean flag = com.kuangchi.sdd.util.file.PropertyUtils.setProperties(propertyFile, "departmentAttendanceMonthStatisticCron", cronExpression,null);
		
		if (flag) {
			try {
				TriggerKey triggerKey = new TriggerKey("departmentAttendanceMonthStatisticTimer");
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
