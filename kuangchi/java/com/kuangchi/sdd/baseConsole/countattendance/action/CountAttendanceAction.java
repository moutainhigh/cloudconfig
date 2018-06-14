package com.kuangchi.sdd.baseConsole.countattendance.action;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.countattendance.model.CountAttendance;
import com.kuangchi.sdd.baseConsole.countattendance.service.ICountAttendanceService;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("countAttendanceAction")
public class CountAttendanceAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(CountAttendanceAction.class);
	private CountAttendance model;
	@Resource(name = "countAttendanceServiceImpl")
	private ICountAttendanceService countAttendanceService;
	
	/**
	 *响应前台请求
	 */
	public String toCountAttendance(){
		
		return "success";
	}
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * Description:根据条件门禁刷卡信息
	 * date:2016年3月14日
	 */
	public void getAllCountAttendByParam(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		Param param=GsonUtil.toBean(data,Param.class);
		Grid allCard= countAttendanceService.getAllCountAttendByParam(param, page, rows);
		printHttpServletResponse(new Gson().toJson(allCard));
	}
	/**
	 * 根据条件导出出入统计  
	 */
	public void reportData(){
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("exportData");
		Param param=GsonUtil.toBean(data,Param.class);
		List<CountAttendance>  modelInfo=countAttendanceService.reportData(param);
		
		String jsonList = GsonUtil.toJson(modelInfo);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					
					colTitleList.add("部门名称");
					colTitleList.add("员工工号");
					colTitleList.add("员工名称");
					colTitleList.add("卡号");
					colTitleList.add("刷卡时间");
					colTitleList.add("设备本地IP地址");
					colTitleList.add("设备名称");
					colTitleList.add("门名称");
					colTitleList.add("事件名称");
					
					colList.add("dept_name");
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("card_num");
					colList.add("attendance_time");
					colList.add("device_ip");
					colList.add("device_num");
					colList.add("door_num");
					colList.add("od_type");
					
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
						String fileName="出入统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecial.exportExcel("出入统计表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	
	}
	
	

}
