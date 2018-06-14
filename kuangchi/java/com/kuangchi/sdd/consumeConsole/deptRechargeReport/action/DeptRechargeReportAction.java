package com.kuangchi.sdd.consumeConsole.deptRechargeReport.action;

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
import com.kuangchi.sdd.consumeConsole.deptBonusReport.util.ExcelUtilReport2;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.deptRechargeReport.model.DeptRechargeReportModel;
import com.kuangchi.sdd.consumeConsole.deptRechargeReport.service.DeptRechargeReportService;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("DeptRechargeReportAction")
public class DeptRechargeReportAction extends BaseActionSupport {
    
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "deptRechargeReportServiceImpl")
	private DeptRechargeReportService deptRechargeReportService;

	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 模糊查询全部部门充值信息[分页]
	 * @参数描述: dept_num,begin_time,end_time,page,rows
	 */
	public  void  getDeptRechargeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		DeptRechargeReportModel  deptRecharge=GsonUtil.toBean(data,DeptRechargeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptRecharge.getDept_num());
		map.put("begin_time", deptRecharge.getBegin_time());
		map.put("end_time", deptRecharge.getEnd_time());
		map.put("total_amount", deptRecharge.getTotal_amount());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<DeptRechargeReportModel> list=deptRechargeReportService.getDeptRechargeReportByParam(map);
		Integer count=deptRechargeReportService.getDeptRechargeReportCount(map);
		
		Grid<DeptRechargeReportModel> grid=new Grid<DeptRechargeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 导出部门充值信息
	 * @参数描述: dept_num,begin_time,end_time
	 */
	public  void  exportDeptRechargeReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		DeptRechargeReportModel  deptRecharge=GsonUtil.toBean(data,DeptRechargeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptRecharge.getDept_num());
		map.put("begin_time", deptRecharge.getBegin_time());
		map.put("end_time", deptRecharge.getEnd_time());
		map.put("total_amount", deptRecharge.getTotal_amount());
		
		List<DeptRechargeReportModel> deptRechargelist=deptRechargeReportService.getDeptRechargeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(deptRechargelist);
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
		colList.add("total_amount");
		colList.add("total_money");
		
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
			String fileName="部门充值报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("部门充值报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 按筛选条件获取部门充值信息[分页]
	 * @参数描述: dept_num,vendor_num,begin_time,end_time,page,rows
	 */
	public  void  countDeptRechargeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		DeptRechargeReportModel  deptRecharge=GsonUtil.toBean(data,DeptRechargeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptRecharge.getDept_num());
		map.put("begin_time", deptRecharge.getBegin_time());
		map.put("end_time", deptRecharge.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<DeptRechargeReportModel> list=deptRechargeReportService.countDeptRechargeReport(map);
		Integer count=deptRechargeReportService.countDeptRechargeReportCount(map);
		
		Grid<DeptRechargeReportModel> grid=new Grid<DeptRechargeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 按筛选条件导出部门充值信息
	 * @参数描述: dept_num,begin_time,end_time
	 */
	public  void  exportDeptRechargeReprort2(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		DeptRechargeReportModel  deptRecharge=GsonUtil.toBean(data,DeptRechargeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptRecharge.getDept_num());
		map.put("begin_time", deptRecharge.getBegin_time());
		map.put("end_time", deptRecharge.getEnd_time());
		
		List<DeptRechargeReportModel> deptRechargelist=deptRechargeReportService.countDeptRechargeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(deptRechargelist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(deptRecharge.getBegin_time());
		cloTitleList.add(deptRecharge.getEnd_time());
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("total_amount");
		colList.add("total_money");
		
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
			String fileName="部门充值报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilReport2.exportExcel("部门充值报表", colTitles, cols, list);
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
