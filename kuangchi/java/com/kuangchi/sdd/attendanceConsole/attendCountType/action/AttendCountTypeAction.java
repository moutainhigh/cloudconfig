package com.kuangchi.sdd.attendanceConsole.attendCountType.action;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.attendCountType.model.AttendCountTypeModel;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.StaffAttendCount;
import com.kuangchi.sdd.attendanceConsole.attendCountType.service.AttendCountTypeService;
import com.kuangchi.sdd.attendanceConsole.attendCountType.util.ExcelUtilSpecialCount2;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.count.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("attendCountTypeAction")
public class AttendCountTypeAction extends BaseActionSupport{
	
	private static final long serialVersionUID = 1L;
	@Resource(name="attendCountTypeServiceImpl")
	private AttendCountTypeService attendCountTypeService;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 查询员工考勤统计
	 */
	public void getLeaveStatis(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String reFlag=getHttpServletRequest().getParameter("reFlag");  
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		model.setLayerDeptNum(layerDeptNum);
				
		List<AttendCountTypeModel> leaveStatisList;
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("leaveStatisList");
			leaveStatisList = attendCountTypeService.getLeaveStatis(model);
			getHttpServletRequest().getSession().setAttribute("leaveStatisList",leaveStatisList);
		} else {
			leaveStatisList = (List)getHttpServletRequest().getSession().getAttribute("leaveStatisList");
			if(leaveStatisList == null || leaveStatisList.size() == 0){
				leaveStatisList = attendCountTypeService.getLeaveStatis(model);
				getHttpServletRequest().getSession().setAttribute("leaveStatisList",leaveStatisList);
			}
		}
		
		Grid<AttendCountTypeModel> grid=new Grid<AttendCountTypeModel>();
		if(leaveStatisList!=null){
			int total=leaveStatisList.size();
			List<AttendCountTypeModel> newList = leaveStatisList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
			grid.setRows(newList);
			grid.setTotal(total);
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 导出员工考勤统计
	 */
	public void exportLeaveStatis() {
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); 
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		String begin_time = model.getBegin_time();
		String end_time = model.getEnd_time();
		
		HttpServletResponse response = getHttpServletResponse();
		List<AttendCountTypeModel> typemodel=new ArrayList<AttendCountTypeModel>();
		List<AttendCountTypeModel> modelInfo = (List)getHttpServletRequest().getSession().getAttribute("leaveStatisList");
		if(modelInfo!=null){
			typemodel=modelInfo;
		}
		String jsonList = GsonUtil.toJson(typemodel);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		
		colTitleList.add("单位");
		colTitleList.add("部门");
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("请假次数");
		colTitleList.add("请假情况");
		
		colList.add("sjbm_mc");
		colList.add("bm_mc");
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("leaveTotalNumber");
		colList.add("totalNumber");
		
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
			String fileName="员工考勤统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount2.exportExcel("员工考勤统计表（"+begin_time+"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询单位考勤汇总
	 */
	public void getAttendExceptionTotalTime(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String reFlag=getHttpServletRequest().getParameter("reFlag");  //用的是页面自定义的param.reFlag值
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		model.setLayerDeptNum(layerDeptNum);
		
		// 如果重新统计，则销毁session中已统计数据，重新统计
		List<AttendCountTypeModel> attendTotalTimeList;
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("attendTotalTimeList");
			attendTotalTimeList = attendCountTypeService.getAttendExceptionTotalTime(model);
			getHttpServletRequest().getSession().setAttribute("attendTotalTimeList",attendTotalTimeList);
		} else {
			attendTotalTimeList = (List)getHttpServletRequest().getSession().getAttribute("attendTotalTimeList");
			if(attendTotalTimeList == null || attendTotalTimeList.size() == 0){
				attendTotalTimeList = attendCountTypeService.getAttendExceptionTotalTime(model);
				getHttpServletRequest().getSession().setAttribute("attendTotalTimeList",attendTotalTimeList);
			}
		}
		
		Grid<AttendCountTypeModel> grid=new Grid<AttendCountTypeModel>();
		if(attendTotalTimeList!=null){
			int total=attendTotalTimeList.size();
			List<AttendCountTypeModel> newList=attendTotalTimeList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
			grid.setRows(newList);
			grid.setTotal(total);
		}
		
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * 导出单位考勤汇总
	 */
	public void exportAttendExceptionTotalTime() {
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); 
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		String begin_time = model.getBegin_time();
		String end_time = model.getEnd_time();
		
		HttpServletResponse response = getHttpServletResponse();
		List<AttendCountTypeModel> typemodel=new ArrayList<AttendCountTypeModel>();
		List<AttendCountTypeModel> modelInfo = (List)getHttpServletRequest().getSession().getAttribute("attendTotalTimeList");
		if(modelInfo!=null){
			typemodel=modelInfo;
		}
		String jsonList = GsonUtil.toJson(typemodel);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("单位");
		colTitleList.add("部门");
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("类型");
		colTitleList.add("天数/次");
		colTitleList.add("日期");
		
		colList.add("sjbm_mc");
		colList.add("bm_mc");
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("exception_type");
		colList.add("totalNumber");
		colList.add("totalTime");
		
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
			String fileName="单位考勤汇总表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount2.exportExcel("单位考勤汇总表（"+ begin_time +"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询迟到、早退、旷工情况统计
	 */
	public void getAttendExceptionTotalNumber(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String reFlag=getHttpServletRequest().getParameter("reFlag");  //用的是页面param.reFlag的值
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		model.setLayerDeptNum(layerDeptNum);
		
		List<AttendCountTypeModel> attendTotalNumberList;
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("attendTotalNumberList");
			attendTotalNumberList = attendCountTypeService.getAttendExceptionTotalNumber(model);
			getHttpServletRequest().getSession().setAttribute("attendTotalNumberList",attendTotalNumberList);
		} else {
			attendTotalNumberList = (List)getHttpServletRequest().getSession().getAttribute("attendTotalNumberList");
			if(attendTotalNumberList == null || attendTotalNumberList.size() == 0){
				attendTotalNumberList = attendCountTypeService.getAttendExceptionTotalNumber(model);
				getHttpServletRequest().getSession().setAttribute("attendTotalNumberList",attendTotalNumberList);
			}
		}
		
		Grid<AttendCountTypeModel> grid = new Grid<AttendCountTypeModel>();
		if(attendTotalNumberList != null){
			int total = attendTotalNumberList.size();
			List<AttendCountTypeModel> newList = attendTotalNumberList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
			grid.setRows(newList);
			grid.setTotal(total);
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	/**
	 * 导出迟到、早退、旷工情况
	 */
	public void exportAttendExceptionTotalNumber() {
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); 
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		String begin_time = model.getBegin_time();
		String end_time = model.getEnd_time();
		
		HttpServletResponse response = getHttpServletResponse();
		List<AttendCountTypeModel> typemodel=new ArrayList<AttendCountTypeModel>();
		List<AttendCountTypeModel> modelInfo = (List)getHttpServletRequest().getSession().getAttribute("attendTotalNumberList");
		if(modelInfo!=null){
			typemodel=modelInfo;
		}
		String jsonList = GsonUtil.toJson(typemodel);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		
		colTitleList.add("单位");
		colTitleList.add("部门");
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("迟到");
		colTitleList.add("早退");
		colTitleList.add("旷工");
		
		colList.add("sjbm_mc");
		colList.add("bm_mc");
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("exception_type1");
		colList.add("exception_type2");
		colList.add("exception_type4");
		
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
			String fileName="迟到早退旷工统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("迟到早退旷工统计表（"+ begin_time +"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 查询迟到、早退、旷工情况汇总
	 */
	public void getDeptExceptionTotalNumber(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		String reFlag=getHttpServletRequest().getParameter("reFlag");  //用的是页面param.reFlag的值
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		model.setLayerDeptNum(layerDeptNum);
		
		List<AttendCountTypeModel> deptTotalNumberList;
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("deptTotalNumberList");
			deptTotalNumberList = attendCountTypeService.getDeptExceptionTotalNumber(model);
			getHttpServletRequest().getSession().setAttribute("deptTotalNumberList",deptTotalNumberList);
		} else {
			deptTotalNumberList = (List)getHttpServletRequest().getSession().getAttribute("attendTotalNumberList");
			if(deptTotalNumberList == null || deptTotalNumberList.size() == 0){
				deptTotalNumberList = attendCountTypeService.getDeptExceptionTotalNumber(model);
				getHttpServletRequest().getSession().setAttribute("deptTotalNumberList",deptTotalNumberList);
			}
		}
		
		Grid<AttendCountTypeModel> grid = new Grid<AttendCountTypeModel>();
		if(deptTotalNumberList != null){
			int total = deptTotalNumberList.size();
			List<AttendCountTypeModel> newList = deptTotalNumberList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
			grid.setRows(newList);
			grid.setTotal(total);
		}
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	/**
	 * 导出迟到、早退、旷工汇总报表
	 */
	public void exportDeptExceptionTotalNumber() {
		
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData"); 
		AttendCountTypeModel model=GsonUtil.toBean(beanObject,AttendCountTypeModel.class);
		
		String begin_time = model.getBegin_time();
		String end_time = model.getEnd_time();
		
		HttpServletResponse response = getHttpServletResponse();
		List<AttendCountTypeModel> typemodel=new ArrayList<AttendCountTypeModel>();
		List<AttendCountTypeModel> modelInfo = (List)getHttpServletRequest().getSession().getAttribute("deptTotalNumberList");
		if(modelInfo!=null){
			typemodel=modelInfo;
		}
		String jsonList = GsonUtil.toJson(typemodel);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		
		colTitleList.add("单位");
		colTitleList.add("部门");
		colTitleList.add("迟到");
		colTitleList.add("早退");
		colTitleList.add("旷工");
		
		colList.add("sjbm_mc");
		colList.add("bm_mc");
		colList.add("exception_type1");
		colList.add("exception_type2");
		colList.add("exception_type4");
		
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
			String fileName="迟到早退旷工汇总表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount2.exportExcel("迟到早退旷工汇总表（"+ begin_time +"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询农行请假统计
	 */
	public void getAttendLeaveCount(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		Map<String,Object> map=GsonUtil.toBean(beanObject, HashMap.class);
		map.put("page", (page - 1) * rows);
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
				
		Grid grid=attendCountTypeService.getAttendLeaveCount(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	}
	/**
	 * 查询农行外出统计
	 */
	public void getAttendOutWorkCount(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		Map<String,Object> map=GsonUtil.toBean(beanObject, HashMap.class);
		map.put("page", (page - 1) * rows);
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
				
		Grid grid=attendCountTypeService.getAttendOutWorkCount(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	}
	/**
	 * 导出农行请假统计
	 */
	public void exportAttendLeaveCount() {
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData");
		Map<String,Object> map=GsonUtil.toBean(beanObject, HashMap.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		map.put("layerDeptNum", layerDeptNum);
		
		List<Map> mapInfo=attendCountTypeService.exportAttendLeaveCount(map);
		
		String jsonList = GsonUtil.toJson(mapInfo);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					
					/*colTitleList.add("部门");*/
					colTitleList.add("员工工号");
					colTitleList.add("员工名称");
					colTitleList.add("开始时间");
					colTitleList.add("结束时间");
					colTitleList.add("请假时段");
					colTitleList.add("请假类型");
					colTitleList.add("标记");
					colTitleList.add("单号");
					
					/*colList.add("bm_mc");*/
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("begin_time");
					colList.add("end_time");
					colList.add("daykindname");
					colList.add("type");
					colList.add("flag");
					colList.add("id");
					
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
						String fileName="请假统计信息表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecialCount.exportExcel("请假统计信息表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}

	/**
	 * 导出农行外出统计
	 */
	public void exportAttendOutWorkCount() {
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData");
		Map<String,Object> map=GsonUtil.toBean(beanObject, HashMap.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		map.put("layerDeptNum", layerDeptNum);
				
		List<Map> mapInfo=attendCountTypeService.exportAttendOutWorkCount(map);
		
		String jsonList = GsonUtil.toJson(mapInfo);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					/*colTitleList.add("部门");*/
					colTitleList.add("员工工号");
					colTitleList.add("员工名称");
					colTitleList.add("开始时间");
					colTitleList.add("结束时间");
					colTitleList.add("外出类型");
					colTitleList.add("标记");
					colTitleList.add("单号");
					
					/*colList.add("bm_mc");*/
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("begin_time");
					colList.add("end_time");
					colList.add("type");
					colList.add("flag");
					colList.add("id");
					
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
						String fileName="外出统计信息表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecialCount.exportExcel("外出统计信息表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	
	
	
	
	/**
	 * 查询员工出勤登记
	 */
	public void getStaffAttendCount(){
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data"); 
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		
		Map<String,Object> map=GsonUtil.toBean(beanObject, HashMap.class);
		map.put("page", (page - 1) * rows);
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
				
		Grid grid = attendCountTypeService.getStaffAttendCountList(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
	}
	
	
	/**
	 * 导出员工出勤统计
	 */
	public void exportStaffAttendCount() {
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("exportData");
		Map<String,Object> map=GsonUtil.toBean(beanObject, HashMap.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		map.put("layerDeptNum", layerDeptNum);
				
		List<StaffAttendCount> staffAttendCount = attendCountTypeService.getStaffAttendCountList(map).getRows();
		for (StaffAttendCount staffAttend : staffAttendCount) {
			if("1".equals(staffAttend.getIsEarly())){
				staffAttend.setIsEarly("是");
			} else {
				staffAttend.setIsEarly("否");
			}
			if("1".equals(staffAttend.getIsKg())){
				staffAttend.setIsKg("是");
			} else {
				staffAttend.setIsKg("否");
			}
			if("1".equals(staffAttend.getIsLater())){
				staffAttend.setIsLater("是");
			} else {
				staffAttend.setIsLater("否");
			}
		}
		
		
		
		String jsonList = GsonUtil.toJson(staffAttendCount);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("单位");
		colTitleList.add("部门");
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("日期");
		colTitleList.add("上班打卡时间");
		colTitleList.add("上班打卡地点");
		colTitleList.add("下班打卡时间");
		colTitleList.add("下班打卡地点");
		colTitleList.add("是否迟到");
		colTitleList.add("是否早退");
		colTitleList.add("是否旷工");
		colTitleList.add("备注");
		
		/*colList.add("bm_mc");*/
		colList.add("sjbm_mc");
		colList.add("bm_mc");
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("every_date");
		colList.add("check_time1");
		colList.add("check_device1");
		colList.add("check_time2");
		colList.add("check_device2");
		colList.add("isLater");
		colList.add("isEarly");
		colList.add("isKg");
		colList.add("remark");
		
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
			String fileName="员工出勤登记表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("员工出勤登记表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
