package com.kuangchi.sdd.attendanceConsole.deptAverageTime.action;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.attendanceConsole.deptAverageTime.model.DeptAvgTime;
import com.kuangchi.sdd.attendanceConsole.deptAverageTime.service.IDeptAvgTimeService;
import com.kuangchi.sdd.attendanceConsole.deptAverageTime.util.ExcelUtil;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;


@Controller("deptAvgTimeAction")
public class DeptAvgTimeAction extends BaseActionSupport {
	@Resource(name="deptAvgTimeServiceImpl")
	private IDeptAvgTimeService deptAvgTimeService;
	
	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;
	
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	
	//部门日均统计
	public void getserachAverageTimeInfo(){
		String data=getHttpServletRequest().getParameter("data");
		String pages=getHttpServletRequest().getParameter("page");
		String row=getHttpServletRequest().getParameter("rows");
		Integer page=Integer.valueOf(pages);
		Integer rows=Integer.valueOf(row);
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		/*List<DeptAvgTime> deptStatisticsList;
		
		// 如果重新统计，则销毁session中已统计数据，重新统计； 否则从session中查询已统计数据，避免重复统计
		String reFlag=getHttpServletRequest().getParameter("reFlag");
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("deptStatisticsList");
			deptStatisticsList = deptAvgTimeService.getSearchStatistics(deptAvgTime,page,rows);
			getHttpServletRequest().getSession().setAttribute("deptStatisticsList",deptStatisticsList);
		} else {
			deptStatisticsList = (List)getHttpServletRequest().getSession().getAttribute("deptStatisticsList");
			//　如果session中该数据为空，则重新统计，以防session数据丢失
			if(deptStatisticsList == null || deptStatisticsList.size() == 0){
				deptStatisticsList = deptAvgTimeService.getSearchStatistics(deptAvgTime,page,rows);
				getHttpServletRequest().getSession().setAttribute("deptStatisticsList",deptStatisticsList);
			}
		}
		
		Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
		if(deptStatisticsList != null && deptStatisticsList.size() != 0){
			int total=deptStatisticsList.size();
			List<DeptAvgTime> newList = deptStatisticsList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
			grid.setRows(newList);
			grid.setTotal(total);
		}
			*/
		
		Grid<DeptAvgTime> grid = deptAvgTimeService.getSearchStatistics(deptAvgTime,page,rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	
	public void exportAverageTimeInfo(){
		HttpServletResponse response = getHttpServletResponse();
		String data=getHttpServletRequest().getParameter("data");
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		String begin_time = deptAvgTime.getFrom_Time();
		String end_time = deptAvgTime.getTo_Time();
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
						
				
		/*List<DeptAvgTime> deptRecord=new ArrayList<DeptAvgTime>();
		List<DeptAvgTime> deptStatisticsList = (List<DeptAvgTime>)getHttpServletRequest().getSession().getAttribute("deptStatisticsList");
		if(deptStatisticsList!=null){
			deptRecord=deptStatisticsList;
		} */
		
		List<DeptAvgTime> deptRecord = deptAvgTimeService.exportSearchStatistics(deptAvgTime);
		String jsonList = GsonUtil.toJson(deptRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("部门");
		colTitleList.add("起始时间");
		colTitleList.add("结束时间");
		colTitleList.add("日均时间");
		
		colList.add("dept_name");
		colList.add("from_Time");
		colList.add("to_Time");
		colList.add("avgTime");
	
		
		String[] colTitles = new String[colTitleList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colTitleList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="部门日均时间统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtil.exportExcel("部门日均时间统计表（"+begin_time+"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//查询员工日均信息
	public void getStaffAverageTimeInfo(){
		String data=getHttpServletRequest().getParameter("data");
		String pages=getHttpServletRequest().getParameter("page");
		String row=getHttpServletRequest().getParameter("rows");
		Integer page=Integer.valueOf(pages);
		Integer rows=Integer.valueOf(row);
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);

		
		/*// 如果重新统计，则销毁session中已统计数据，重新统计； 否则从session中查询已统计数据，避免重复统计
		
		List<DeptAvgTime> staffStatisticsList;
		String reFlag=getHttpServletRequest().getParameter("reFlag");
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("staffStatisticsList");
			staffStatisticsList=deptAvgTimeService.getStaffTimeStatistics(deptAvgTime);
			getHttpServletRequest().getSession().setAttribute("staffStatisticsList",staffStatisticsList);
		} else {
			staffStatisticsList = (List)getHttpServletRequest().getSession().getAttribute("staffStatisticsList");
			//　如果session中该数据为空，则重新统计，以防session数据丢失
			if(staffStatisticsList==null || staffStatisticsList.size()==0){
				staffStatisticsList=deptAvgTimeService.getStaffTimeStatistics(deptAvgTime);
				getHttpServletRequest().getSession().setAttribute("staffStatisticsList",staffStatisticsList);
			}
		}
		
		Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
		if(staffStatisticsList != null && staffStatisticsList.size() != 0){
			int total = staffStatisticsList.size();
			List<DeptAvgTime> newList = staffStatisticsList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
	        grid.setRows(newList);
	        grid.setTotal(total);
		}*/
		
		Grid<DeptAvgTime> grid = deptAvgTimeService.getStaffTimeStatistics(deptAvgTime, page, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//导出员工日均信息
	public void exportStaffTimeInfo(){
		HttpServletResponse response = getHttpServletResponse();
		String data=getHttpServletRequest().getParameter("data");
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		String begin_time = deptAvgTime.getFrom_Time();
		String end_time = deptAvgTime.getTo_Time();
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
						
		/*		
		List<DeptAvgTime> staffStatisticsList = (List)getHttpServletRequest().getSession().
				getAttribute("staffStatisticsList");
		
		List<DeptAvgTime> deptRecord=new ArrayList<DeptAvgTime>();
		if(staffStatisticsList != null){
			deptRecord=staffStatisticsList;
		}*/
		
		
		List<DeptAvgTime> deptRecord = deptAvgTimeService.exportStaffTimeStatistics(deptAvgTime);
		String jsonList = GsonUtil.toJson(deptRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("部门");
		colTitleList.add("员工名称");
		colTitleList.add("员工工号");
		colTitleList.add("起始时间");
		colTitleList.add("结束时间");
		colTitleList.add("总时间");
		colTitleList.add("日均时间");
		
		colList.add("dept_name");
		colList.add("staff_name");
		colList.add("staff_no");
		colList.add("from_Time");
		colList.add("to_Time");
		colList.add("staffSumTime");
		colList.add("avgTime");
		
		String[] colTitles = new String[colTitleList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colTitleList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="员工日均时间统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtil.exportExcel("员工日均时间统计表（"+begin_time+"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//所有部门日均时间统计
	public void getAllAvgTimeStatistics(){
		String data=getHttpServletRequest().getParameter("data");
		String pages=getHttpServletRequest().getParameter("page");
		String row=getHttpServletRequest().getParameter("rows");
		Integer page=Integer.valueOf(pages);
		Integer rows=Integer.valueOf(row);
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
				
		
		/*// 如果重新统计，则销毁session中已统计数据，重新统计； 否则从session中查询已统计数据，避免重复统计
		
		List<DeptAvgTime> AlldeptStatisticsList;
		String reFlag=getHttpServletRequest().getParameter("reFlag");
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("AlldeptStatisticsList");
			AlldeptStatisticsList = deptAvgTimeService.getAllAvgTimeStatistics(deptAvgTime);
			getHttpServletRequest().getSession().setAttribute("AlldeptStatisticsList",AlldeptStatisticsList);
			
		} else {
			AlldeptStatisticsList = (List)getHttpServletRequest().getSession().getAttribute("AlldeptStatisticsList");	
			//　如果session中该数据为空，则重新统计，以防session数据丢失
			if(AlldeptStatisticsList==null || AlldeptStatisticsList.size() == 0){
				AlldeptStatisticsList = deptAvgTimeService.getAllAvgTimeStatistics(deptAvgTime);
				getHttpServletRequest().getSession().setAttribute("AlldeptStatisticsList",AlldeptStatisticsList);
			}
		}
		
		Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
		if(AlldeptStatisticsList!=null){
			int total = AlldeptStatisticsList.size();
			List<DeptAvgTime> newList = AlldeptStatisticsList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
	        grid.setRows(newList);
	        grid.setTotal(total);
		}	*/
		Grid<DeptAvgTime> grid = deptAvgTimeService.getAllAvgTimeStatistics(deptAvgTime, page, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	public void exportAllAvgTimeInfo(){
		HttpServletResponse response = getHttpServletResponse();
		String data=getHttpServletRequest().getParameter("data");
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		String begin_time = deptAvgTime.getFrom_Time();
		String end_time = deptAvgTime.getTo_Time();
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
						
				
		/*List<DeptAvgTime> deptRecord =new ArrayList<DeptAvgTime>();
		List<DeptAvgTime> AlldeptStatisticsList = (List)getHttpServletRequest().getSession().
				getAttribute("AlldeptStatisticsList");
		if(AlldeptStatisticsList!=null){
			deptRecord=AlldeptStatisticsList;
		}*/
		List<DeptAvgTime> deptRecord = deptAvgTimeService.exportAllAvgTimeStatistics(deptAvgTime);
		String jsonList = GsonUtil.toJson(deptRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("部门");
		colTitleList.add("起始时间");
		colTitleList.add("结束时间");
		colTitleList.add("日均时间");
		
		colList.add("dept_name");
		colList.add("from_Time");
		colList.add("to_Time");
		colList.add("avgTime");
		
		String[] colTitles = new String[colTitleList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colTitleList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="部门日均时间统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtil.exportExcel("部门日均时间统计表（"+begin_time+"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getAllTwoDeptAvgTime(){
		String data=getHttpServletRequest().getParameter("data");
		String pages=getHttpServletRequest().getParameter("page");
		String row=getHttpServletRequest().getParameter("rows");
		Integer page=Integer.valueOf(pages);
		Integer rows=Integer.valueOf(row);
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
				
		
		/*// 如果重新统计，则销毁session中已统计数据，重新统计； 否则从session中查询已统计数据，避免重复统计
		
		List<DeptAvgTime> AllTwodeptStatisticsList;
		String reFlag=getHttpServletRequest().getParameter("reFlag");
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("AllTwodeptStatisticsList");
			AllTwodeptStatisticsList = deptAvgTimeService.getAllTwoDeptAvgTime(deptAvgTime);
			getHttpServletRequest().getSession().setAttribute("AllTwodeptStatisticsList",AllTwodeptStatisticsList);
			
		} else {
			AllTwodeptStatisticsList = (List)getHttpServletRequest().getSession().getAttribute("AllTwodeptStatisticsList");	
			//　如果session中该数据为空，则重新统计，以防session数据丢失
			if(AllTwodeptStatisticsList==null || AllTwodeptStatisticsList.size() == 0){
				AllTwodeptStatisticsList = deptAvgTimeService.getAllTwoDeptAvgTime(deptAvgTime);
				getHttpServletRequest().getSession().setAttribute("AllTwodeptStatisticsList",AllTwodeptStatisticsList);
			}
		}
		
		Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
		if(AllTwodeptStatisticsList != null && AllTwodeptStatisticsList.size() !=0){
			int total = AllTwodeptStatisticsList.size();
			List<DeptAvgTime> newList = AllTwodeptStatisticsList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
	        grid.setRows(newList);
	        grid.setTotal(total);
		}*/
		Grid<DeptAvgTime> grid = deptAvgTimeService.getAllTwoDeptAvgTime(deptAvgTime, page, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	public void exportAllTwoAvgTimeInfo(){
		HttpServletResponse response = getHttpServletResponse();
		String data = getHttpServletRequest().getParameter("data");
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		String begin_time = deptAvgTime.getFrom_Time();
		String end_time = deptAvgTime.getTo_Time();
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
				
		/*
		List<DeptAvgTime> deptRecord =new ArrayList<DeptAvgTime>();
		List<DeptAvgTime> AllTwodeptStatisticsList = (List)getHttpServletRequest().getSession().
				getAttribute("AllTwodeptStatisticsList");
		if(AllTwodeptStatisticsList!=null){
			deptRecord = AllTwodeptStatisticsList;
		}*/
		List<DeptAvgTime> deptRecord = deptAvgTimeService.exportAllTwoDeptAvgTime(deptAvgTime);
		String jsonList = GsonUtil.toJson(deptRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("部门");
		colTitleList.add("起始时间");
		colTitleList.add("结束时间");
		colTitleList.add("日均时间");
		
		colList.add("dept_name");
		colList.add("from_Time");
		colList.add("to_Time");
		colList.add("avgTime");
		
		String[] colTitles = new String[colTitleList.size()];
		String[] cols = new String[colList.size()];
		for (int i = 0; i < colTitleList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="部门日均时间统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtil.exportExcel("部门日均时间统计表（"+begin_time+"至"+end_time+"）", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//所有员工日均统计
	public void getAllStaffAvgTime(){
		String data=getHttpServletRequest().getParameter("data");
		String pages=getHttpServletRequest().getParameter("page");
		String row=getHttpServletRequest().getParameter("rows");
		Integer page=Integer.valueOf(pages);
		Integer rows=Integer.valueOf(row);
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
		
		
		/*// 如果重新统计，则销毁session中已统计数据，重新统计； 否则从session中查询已统计数据，避免重复统计
		
		List<DeptAvgTime> AllStaffStatisticsList;
		String reFlag=getHttpServletRequest().getParameter("reFlag");
		if("0".equals(reFlag)){
			getHttpServletRequest().getSession().removeAttribute("AllStaffStatisticsList");
			AllStaffStatisticsList = deptAvgTimeService.getAllStaffAvgTime(deptAvgTime);
			getHttpServletRequest().getSession().setAttribute("AllStaffStatisticsList",AllStaffStatisticsList);
			
		} else {
			AllStaffStatisticsList = (List)getHttpServletRequest().getSession().getAttribute("AllStaffStatisticsList");
			//　如果session中该数据为空，则重新统计，以防session数据丢失
			if(AllStaffStatisticsList==null || AllStaffStatisticsList.size() == 0){
				AllStaffStatisticsList = deptAvgTimeService.getAllStaffAvgTime(deptAvgTime);
				getHttpServletRequest().getSession().setAttribute("AllStaffStatisticsList",AllStaffStatisticsList);
			}
		}
		
		Grid<DeptAvgTime> grid = new Grid<DeptAvgTime>();
		if(AllStaffStatisticsList !=null && AllStaffStatisticsList.size() != 0){
			List<DeptAvgTime> newList=null;
			int total=AllStaffStatisticsList.size();
			newList=AllStaffStatisticsList.subList(rows*(page-1), (rows*page)>total?total:(rows*page)); 
	        grid.setRows(newList);
	        grid.setTotal(total);
		}
		*/
		
		Grid<DeptAvgTime> grid = deptAvgTimeService.getAllStaffAvgTime(deptAvgTime, page, rows);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	
	//导出所有员工日均统计
	public void exportAllStaffAvgTime(){
		HttpServletResponse response = getHttpServletResponse();
		String data=getHttpServletRequest().getParameter("data");
		DeptAvgTime deptAvgTime=GsonUtil.toBean(data,DeptAvgTime.class);
		
		String begin_time = deptAvgTime.getFrom_Time();
		String end_time = deptAvgTime.getTo_Time();
		
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
        String layerDeptNum = null;
		if(isLayer){
 			 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
 			 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
 			 layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(), role.getJsDm());
 		} 
		deptAvgTime.setLayerDeptNum(layerDeptNum);
				
		/*
		List<DeptAvgTime> deptRecord =new ArrayList<DeptAvgTime>();
		List<DeptAvgTime> AllTwodeptStatisticsList = (List)getHttpServletRequest().getSession().
				getAttribute("AllStaffStatisticsList");
		if(AllTwodeptStatisticsList!=null){
			deptRecord=AllTwodeptStatisticsList;
		}*/
		
		List<DeptAvgTime> deptRecord = deptAvgTimeService.exportAllStaffAvgTime(deptAvgTime);
		String jsonList = GsonUtil.toJson(deptRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					colTitleList.add("部门");
					colTitleList.add("员工名称");
					colTitleList.add("员工工号");
					colTitleList.add("起始时间");
					colTitleList.add("结束时间");
					colTitleList.add("总时间");
					colTitleList.add("日均时间");
					
					colList.add("dept_name");
					colList.add("staff_name");
					colList.add("staff_no");
					colList.add("from_Time");
					colList.add("to_Time");
					colList.add("staffSumTime");
					colList.add("avgTime");
					
					String[] colTitles = new String[colTitleList.size()];
					String[] cols = new String[colList.size()];
					for (int i = 0; i < colTitleList.size(); i++) {
						cols[i] = colList.get(i);
						colTitles[i] = colTitleList.get(i);
					}
					
					OutputStream out = null;
					try {
						out = response.getOutputStream();
						response.setContentType("application/x-msexcel");
						String fileName="员工日均时间统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtil.exportExcel("员工日均时间统计表（"+begin_time+"至"+end_time+"）", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
}
