
package com.kuangchi.sdd.attendanceConsole.attend.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.forgetcheckModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.outworkModel;
import com.kuangchi.sdd.attendanceConsole.attend.service.IAttendanceService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.count.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.role.service.impl.RoleServiceImpl;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
@Controller("attendanceAction")
public class AttendanceAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(AttendanceAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "attendanceServiceImpl")
	private IAttendanceService attendanceService;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	 
	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	private LeavetimeModel model;
	public AttendanceAction(){
		model=new LeavetimeModel();}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//管理员请假查看页面
	public String toMyAttend(){
		return "success";
	}
	//用户请假查看页面
	public String toMyAttendUser(){
		return "success";
	}
	//管理员忘打卡查看页面
	public String toMyforgetcheck(){
		return "forget";
	}
	//用户忘打卡查看页面
	public String toMyforgetcheckUser(){
		return "forget";
	}
	//管理员忘打卡查看页面
	public String toMyoutwork(){
		return "outwork";
	}
	//用户忘打卡查看页面
	public String toMyoutworkUser(){
		return "outwork";
	}
	//查询忘记打卡全部信息
	public void getAlladdends(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		forgetcheckModel forgetcheck=GsonUtil.toBean(data,forgetcheckModel.class);
		
		if(forgetcheck.getStaff_no()!=null){
			forgetcheck.setStaff_no(forgetcheck.getStaff_no().trim());
			String staff_num = employeeService.selectStaffNum(forgetcheck.getStaff_no());
			forgetcheck.setStaff_num(staff_num);
		}else if(forgetcheck.getStaff_name()!=null){
			forgetcheck.setStaff_name(forgetcheck.getStaff_name().trim());
		}
	
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		forgetcheck.setLayerDeptNum(layerDeptNum);
		
		
		Grid allCard=attendanceService.selectAllforgetchecks(forgetcheck, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allCard));
	
	}
	//查询请假全部信息
		public void getAllLeavetimes(){
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			LeavetimeModel leavetime=GsonUtil.toBean(data,LeavetimeModel.class);
			if(leavetime.getStaff_no()!=null){
				leavetime.setStaff_no(leavetime.getStaff_no().trim());
				String staff_num = employeeService.selectStaffNum(leavetime.getStaff_no());
				leavetime.setStaff_num(staff_num);
			}else if(leavetime.getStaff_name()!=null){
				leavetime.setStaff_name(leavetime.getStaff_name().trim());
			}
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				leavetime.setJsDm(role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				leavetime.setYhDm(user.getYhDm());
			} else {
				leavetime.setJsDm("0");
			}*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			leavetime.setLayerDeptNum(layerDeptNum);
			
			Grid allCard=attendanceService.selectAllleavetimes(leavetime, page, rows);
			printHttpServletResponse(GsonUtil.toJson(allCard));
		
		}
		//查询所有外出记录
		public void selectAllOutwork(){
			HttpServletRequest request = getHttpServletRequest();
			String page=request.getParameter("page");
			String rows=request.getParameter("rows");
			String data= request.getParameter("data");
			outworkModel outworks=GsonUtil.toBean(data,outworkModel.class);
			if(outworks.getStaff_no()!=null){
				outworks.setStaff_no(outworks.getStaff_no().trim());
				String staff_num = employeeService.selectStaffNum(outworks.getStaff_no());
				outworks.setStaff_num(staff_num);
			}else if(outworks.getStaff_name()!=null){
				outworks.setStaff_name(outworks.getStaff_name().trim());
			}
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				outworks.setJsDm(role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				outworks.setYhDm(user.getYhDm());
			} else {
				outworks.setJsDm("0");
			}*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			outworks.setLayerDeptNum(layerDeptNum);
			
			
			
			Grid allOutworks=attendanceService.selectAllOutwork(outworks, page, rows);
			printHttpServletResponse(GsonUtil.toJson(allOutworks));
		}
		
		//根据ID查询请假信息
		public void selectLeaveTimeById(){
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			LeavetimeModel leavetime=GsonUtil.toBean(data,LeavetimeModel.class);
			Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			leavetime.setStaff_num(session_staff.getStaff_num());
			Grid leavetimes=attendanceService.selectLeavetimeById(leavetime,page,rows);
			printHttpServletResponse(GsonUtil.toJson(leavetimes));
		}
		//根据ID查询外出信息
		public void selectOutWorkById(){
				String page=getHttpServletRequest().getParameter("page");
				String rows=getHttpServletRequest().getParameter("rows");
				String data= getHttpServletRequest().getParameter("data");
				outworkModel outwork=GsonUtil.toBean(data,outworkModel.class);
				Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
				outwork.setStaff_num(session_staff.getStaff_num());
				Grid outworks=attendanceService.selectOutworkById(outwork,page,rows);
				printHttpServletResponse(GsonUtil.toJson(outworks));
		}
		//根据ID查询忘打卡信息
		public void selectforgetcheckById(){
				String page=getHttpServletRequest().getParameter("page");
				String rows=getHttpServletRequest().getParameter("rows");
				String data= getHttpServletRequest().getParameter("data");
				forgetcheckModel forget=GsonUtil.toBean(data,forgetcheckModel.class);
				Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
				forget.setStaff_num(session_staff.getStaff_num());
				LOG.info(forget.getStaff_num());
				Grid forgets=attendanceService.selectForgetcheckById(forget,page,rows);
				printHttpServletResponse(GsonUtil.toJson(forgets));
		}
		//导出忘记打卡记录
		public void exportAllforgetcheck() {
			HttpServletResponse response = getHttpServletResponse();
			String data = getHttpServletRequest().getParameter("data");
			forgetcheckModel forgetcheck=GsonUtil.toBean(data,forgetcheckModel.class);
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				forgetcheck.setJsDm(role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				forgetcheck.setYhDm(user.getYhDm());
			} else {
				forgetcheck.setJsDm("0");
			}*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			forgetcheck.setLayerDeptNum(layerDeptNum);
						
						
			List<forgetcheckModel> forgetche=attendanceService.exportforgetchecks(forgetcheck);
			String jsonList = GsonUtil.toJson(forgetche);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("员工工号");
						colTitleList.add("员工名字");
						colTitleList.add("部门名称");
						colTitleList.add("部门编号");
						colTitleList.add("忘打卡时间");
						colTitleList.add("忘打卡时间点");
						colTitleList.add("忘打卡原因");
						
						colList.add("staff_no");
						colList.add("staff_name");
						colList.add("BM_MC");
						colList.add("BM_NO");
						colList.add("time");
						colList.add("forget_point");
						colList.add("reason");
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
							String fileName="忘打卡统计表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("忘打卡统计表", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
		
		//导出外出记录
		public void exportAlloutwork() {
			HttpServletResponse response = getHttpServletResponse();
			/*String staff_no = getHttpServletRequest().getParameter("staff_no").trim();
			String staff_name = getHttpServletRequest().getParameter("staff_name").trim();
			String BM_MC = getHttpServletRequest().getParameter("BM_MC");
			outworkModel outworks=new outworkModel();
			outworks.setStaff_no(staff_no);
			String staff_num = employeeService.selectStaffNum(staff_no);
			outworks.setStaff_num(staff_num);
			outworks.setStaff_name(staff_name);
			outworks.setBM_MC(BM_MC);*/
			String data = getHttpServletRequest().getParameter("data");
			outworkModel outworks=GsonUtil.toBean(data,outworkModel.class);
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				outworks.setJsDm(role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				outworks.setYhDm(user.getYhDm());
			} else {
				outworks.setJsDm("0");
			}*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			outworks.setLayerDeptNum(layerDeptNum);
						
			List<outworkModel> outwork=attendanceService.exportOutwork(outworks);
			String jsonList = GsonUtil.toJson(outwork);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("员工工号");
						colTitleList.add("员工名字");
						colTitleList.add("部门名称");
						colTitleList.add("部门编号");
						colTitleList.add("开始时间");
						colTitleList.add("结束时间");
						colTitleList.add("外出原因");
						
						colList.add("staff_no");
						colList.add("staff_name");
						colList.add("BM_MC");
						colList.add("BM_NO");
						colList.add("from_time");
						colList.add("to_time");
						colList.add("reason");
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
							String fileName="外出统计表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("外出统计表", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
		//导出请假信息
		public void exportAllleavetime() {
			HttpServletResponse response = getHttpServletResponse();
			/*String staff_no = getHttpServletRequest().getParameter("staff_no").trim();
			String staff_name = getHttpServletRequest().getParameter("staff_name").trim();
			String BM_MC = getHttpServletRequest().getParameter("BM_MC");
			model.setStaff_no(staff_no);
			String staff_num = employeeService.selectStaffNum(staff_no);
			model.setStaff_num(staff_num);
			model.setStaff_name(staff_name);
			model.setBM_MC(BM_MC);*/
			String data = getHttpServletRequest().getParameter("data");
			LeavetimeModel leavetime=GsonUtil.toBean(data,LeavetimeModel.class);
			
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				leavetime.setJsDm(role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				leavetime.setYhDm(user.getYhDm());
			} else {
				leavetime.setJsDm("0");
			}*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			leavetime.setLayerDeptNum(layerDeptNum);
						
			List<LeavetimeModel> leave=attendanceService.exportLeavetime(leavetime);
			String jsonList = GsonUtil.toJson(leave);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);

			// 设置列表头和列数据键
			List<String> colList = new ArrayList<String>();
			List<String> colTitleList = new ArrayList<String>();
			colTitleList.add("员工工号");
			colTitleList.add("员工名字");
			colTitleList.add("部门名称");
			colTitleList.add("部门编号");
			colTitleList.add("开始时间");
			colTitleList.add("结束时间");
			colTitleList.add("请假类型");
			colTitleList.add("请假原因");
			
			colList.add("staff_no");
			colList.add("staff_name");
			colList.add("BM_MC");
			colList.add("BM_NO");
			colList.add("from_time");
			colList.add("to_time");
			colList.add("wordbook_name");
			colList.add("reason");
			
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
				String fileName="请假统计表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				Workbook workbook = ExcelUtilSpecialCount.exportExcel("请假统计表", colTitles, cols, list);
				workbook.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * 获取加班申请记录
		 * 用于门户 
		 * by gengji.yang
		 */
		public void getOtRecords(){
			Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			String staffNum=session_staff.getStaff_num();
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			Integer skip=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
			Map<String,Object> map=GsonUtil.toBean(data, HashMap.class);
			if(map.get("otBegin")!=null&&!"".equals(map.get("otBegin"))){
				map.put("otBegin", map.get("otBegin")+" 00:00:00");
			}
			if(map.get("otEnd")!=null&&!"".equals(map.get("otEnd"))){
				map.put("otEnd", map.get("otEnd")+" 23:59:59");
			}
			map.put("staffNum", staffNum);
			map.put("skip", skip);
			map.put("rows", Integer.parseInt(rows));
			Grid grid=attendanceService.getOtRecords(map);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
		
		/**
		 * 获取所有的加班申请记录
		 * 用于后台
		 * by gengji.yang
		 */
		public void getAllOts(){
			HttpServletRequest request=getHttpServletRequest();
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= request.getParameter("data");
			Integer skip=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
			Map<String,Object> map=GsonUtil.toBean(data, HashMap.class);
			
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
						
			if(map.get("otBegin")!=null&&!"".equals(map.get("otBegin"))){
				map.put("otBegin", map.get("otBegin")+" 00:00:00");
			}
			if(map.get("otEnd")!=null&&!"".equals(map.get("otEnd"))){
				map.put("otEnd", map.get("otEnd")+" 23:59:59");
			}
			map.put("skip", skip);
			map.put("rows", Integer.parseInt(rows));
			Grid grid=attendanceService.getAllOts(map);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
		
		/**
		 * 导出加班申请到Excel
		 * by gengji.yang
		 */
		public void exportOtExcel() {
			HttpServletResponse response = getHttpServletResponse();
			String staffNo = getHttpServletRequest().getParameter("staffNo").trim();
			String staffName = getHttpServletRequest().getParameter("staffName").trim();
			String deptName = getHttpServletRequest().getParameter("deptName");
			String otBegin = getHttpServletRequest().getParameter("otBegin");
			String otEnd = getHttpServletRequest().getParameter("otEnd");
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("staffNo", staffNo);
			map.put("staffName", staffName);
			map.put("deptName", deptName);
			map.put("otBegin", otBegin);
			map.put("otEnd", otEnd);
			if(map.get("otBegin")!=null&&!"".equals(map.get("otBegin"))){
				map.put("otBegin", map.get("otBegin")+" 00:00:00");
			}
			if(map.get("otEnd")!=null&&!"".equals(map.get("otEnd"))){
				map.put("otEnd", map.get("otEnd")+" 23:59:59");
			}
			
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
			
						
			List list=attendanceService.exportAllOts(map);
			// 设置列表头和列数据键
			List<String> colList = new ArrayList<String>();
			List<String> colTitleList = new ArrayList<String>();
			colTitleList.add("员工工号");
			colTitleList.add("员工名字");
			colTitleList.add("部门名称");
			colTitleList.add("加班时间起");
			colTitleList.add("加班时间止");
			colTitleList.add("申请时间");
			colTitleList.add("加班时长");
			colTitleList.add("加班类型");
			colTitleList.add("加班原因");
			
			colList.add("staffNo");
			colList.add("staffName");
			colList.add("deptName");
			colList.add("otBegin");
			colList.add("otEnd");
			colList.add("otApplyDate");
			colList.add("otTime");
			colList.add("otType");
			colList.add("otReason");
			
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
				String fileName="加班统计表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				Workbook workbook = ExcelUtilSpecialCount.exportExcel("加班统计表", colTitles, cols, list);
				workbook.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * 获取销假申请记录
		 * 用于门户 
		 * by gengji.yang
		 */
		public void getCancelRecords(){
			Staff session_staff = (Staff) getHttpSession().getAttribute(GlobalConstant.LOGIN_STAFF);
			String staffNum=session_staff.getStaff_num();
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			Integer skip=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
			Map<String,Object> map=GsonUtil.toBean(data, HashMap.class);
			map.put("staffNum", staffNum);
			map.put("skip", skip);
			map.put("rows", Integer.parseInt(rows));
			Grid grid=attendanceService.getMyCancelLeaveTime(map);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
		
		/**
		 * 获取所有的销假申请记录
		 * 用于后台
		 * by gengji.yang
		 */
		public void getAllCancelRecords(){
			HttpServletRequest request=getHttpServletRequest();
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= request.getParameter("data");
			Integer skip=(Integer.parseInt(page)-1)*Integer.parseInt(rows);
			Map<String,Object> map=GsonUtil.toBean(data, HashMap.class);
			map.put("skip", skip);
			map.put("rows", Integer.parseInt(rows));
			
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
						
			Grid grid=attendanceService.getAllCancelLeaveTime(map);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
		
		/**
		 * 导出 销假申请记录
		 * 用于后台
		 * by gengji.yang
		 */
		public void exportCancelRecords(){
			HttpServletResponse response = getHttpServletResponse();
			String staff_no = getHttpServletRequest().getParameter("staff_no").trim();
			String staff_name = getHttpServletRequest().getParameter("staff_name").trim();
			String BM_MC = getHttpServletRequest().getParameter("BM_MC");
			String begin_time = getHttpServletRequest().getParameter("begin_time");
			String end_time = getHttpServletRequest().getParameter("end_time");
			String staff_num = employeeService.selectStaffNum(staff_no);
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("begin_time", begin_time);
			map.put("end_time", end_time);
			map.put("staff_num", staff_num);
			map.put("staff_name", staff_name);
			map.put("BM_MC", BM_MC);
			
			// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
			/*boolean isLayer = roleService.isLayer();
			if(isLayer){
				Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
				map.put("jsDm", role.getJsDm());
				
				User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
				map.put("yhDm", user.getYhDm());
			} else {
				map.put("jsDm", "0");
			}
			*/
			
			boolean isLayer = roleService.isLayer();
	        String layerDeptNum = null;
			if(isLayer){
	 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
	 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
	 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
	 		} 
			map.put("layerDeptNum", layerDeptNum);
			
			List<Map> leave=attendanceService.exportAllCancelLeavetimes(map);
			String jsonList = GsonUtil.toJson(leave);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);

			// 设置列表头和列数据键
			List<String> colList = new ArrayList<String>();
			List<String> colTitleList = new ArrayList<String>();
			colTitleList.add("员工工号");
			colTitleList.add("员工名字");
			colTitleList.add("部门名称");
			colTitleList.add("部门编号");
			colTitleList.add("开始时间");
			colTitleList.add("结束时间");
			colTitleList.add("销假类型");
			colTitleList.add("销假原因");
			
			colList.add("staff_no");
			colList.add("staff_name");
			colList.add("BM_MC");
			colList.add("BM_NO");
			colList.add("from_time");
			colList.add("to_time");
			colList.add("wordbook_name");
			colList.add("reason");
			
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
				String fileName="销假统计表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				Workbook workbook = ExcelUtilSpecialCount.exportExcel("销假统计表", colTitles, cols, list);
				workbook.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
		}
}