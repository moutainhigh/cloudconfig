package com.kuangchi.sdd.consumeConsole.deptBonusReport.action;

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

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.model.DeptBonusReportModel;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.service.DeptBonusReportService;
import com.kuangchi.sdd.consumeConsole.deptBonusReport.util.ExcelUtilReport2;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("DeptBonusReportAction")
public class DeptBonusReportAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "deptBonusReportServiceImpl")
	private DeptBonusReportService deptBonusReportService;

	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 模糊查询全部部门补助信息[分页]
	 * @参数描述: dept_num,begin_time,end_time,page,rows
	 */
	public  void  getDeptBonusReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		DeptBonusReportModel  deptBonus=GsonUtil.toBean(data,DeptBonusReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptBonus.getDept_num());
		map.put("begin_time", deptBonus.getBegin_time());
		map.put("end_time", deptBonus.getEnd_time());
		map.put("amount", deptBonus.getAmount());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<DeptBonusReportModel> list=deptBonusReportService.getDeptBonusReport(map);
		Integer count=deptBonusReportService.getDeptBonusReportCount(map);
		
		Grid<DeptBonusReportModel> grid=new Grid<DeptBonusReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 导出部门补助信息
	 * @参数描述: dept_num,begin_time,end_time
	 */
	public  void  exportDeptBonusReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		DeptBonusReportModel  deptBonus=GsonUtil.toBean(data,DeptBonusReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptBonus.getDept_num());
		map.put("begin_time", deptBonus.getBegin_time());
		map.put("end_time", deptBonus.getEnd_time());
		map.put("amount", deptBonus.getAmount());
		
		List<DeptBonusReportModel> deptBonuslist=deptBonusReportService.getDeptBonusReportNoLimit(map);
		String jsonList=GsonUtil.toJson(deptBonuslist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("every_date");
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("amount");
		colList.add("money");
		
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
			String fileName="部门补助报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("部门补助报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 按筛选条件获取部门补助信息[分页]
	 * @参数描述: dept_num,vendor_num,begin_time,end_time,page,rows
	 */
	public  void  countDeptBonusReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		DeptBonusReportModel  deptBonus=GsonUtil.toBean(data,DeptBonusReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptBonus.getDept_num());
		map.put("begin_time", deptBonus.getBegin_time());
		map.put("end_time", deptBonus.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<DeptBonusReportModel> list=deptBonusReportService.countDeptBonusReport(map);
		Integer count=deptBonusReportService.countDeptBonusReportCount(map);
		
		Grid<DeptBonusReportModel> grid=new Grid<DeptBonusReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 按筛选条件导出部门补助信息
	 * @参数描述: dept_num,begin_time,end_time
	 */
	public  void  exportDeptBonusReprort2(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		DeptBonusReportModel  deptBonus=GsonUtil.toBean(data,DeptBonusReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptBonus.getDept_num());
		map.put("begin_time", deptBonus.getBegin_time());
		map.put("end_time", deptBonus.getEnd_time());
		
		List<DeptBonusReportModel> deptBonuslist=deptBonusReportService.countDeptBonusReportNoLimit(map);
		String jsonList=GsonUtil.toJson(deptBonuslist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(deptBonus.getBegin_time());
		cloTitleList.add(deptBonus.getEnd_time());
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("amount");
		colList.add("money");
		
		String[] colTitles=new String[cloTitleList.size()];
		String[] cols=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
		}
		for(int i=0;i<cloTitleList.size();i++){
			colTitles[i]=cloTitleList.get(i);
		}
		
	    OutputStream  out=null;
	    try {
			out=response.getOutputStream();
			response.setContentType("application/x-msecxel");
			String fileName="部门补助报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilReport2.exportExcel("部门补助报表", colTitles, cols, list);    
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
