package com.kuangchi.sdd.consumeConsole.deptConsumeReport.action;

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
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.model.DeptConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.service.DeptConsumeReportService;
import com.kuangchi.sdd.consumeConsole.deptConsumeReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.consumeConsole.vendor.model.Vendor;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;


@Controller("DeptConsumeReportAction")
public class DeptConsumeReportAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "deptConsumeReportServiceImpl")
	private DeptConsumeReportService deptConsumeReportService;
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-19 
	 * @功能描述: 模糊查询全部部门消费信息[分页]
	 * @参数描述: dept_num,begin_time,end_time,page,rows
	 */
	public  void  getDeptConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		DeptConsumeReportModel  deptConsume=GsonUtil.toBean(data,DeptConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptConsume.getDept_num());
		map.put("begin_time", deptConsume.getBegin_time());
		map.put("end_time", deptConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<DeptConsumeReportModel> list=deptConsumeReportService.getDeptConsumeReportByParam(map);
		Integer count=deptConsumeReportService.getDeptConsumeReportCount(map);
		
		Grid<DeptConsumeReportModel> grid=new Grid<DeptConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-23 
	 * @功能描述: 按筛选条件获取部门消费信息[分页]
	 * @参数描述: dept_num,vendor_num,begin_time,end_time,page,rows
	 */
	public  void  initDeptConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		DeptConsumeReportModel  deptConsume=GsonUtil.toBean(data,DeptConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptConsume.getDept_num());
		map.put("begin_time", deptConsume.getBegin_time());
		map.put("end_time", deptConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<DeptConsumeReportModel> list=deptConsumeReportService.initDeptConsumeReport(map);
		Integer count=deptConsumeReportService.initDeptConsumeReportCount(map);
		
		Grid<DeptConsumeReportModel> grid=new Grid<DeptConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-19 
	 * @功能描述: 导出部门消费信息
	 * @参数描述: dept_num,begin_time,end_time
	 */
	public  void  exportDeptConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		DeptConsumeReportModel  deptConsume=GsonUtil.toBean(data,DeptConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptConsume.getDept_num());
		map.put("begin_time", deptConsume.getBegin_time());
		map.put("end_time", deptConsume.getEnd_time());
		
		List<DeptConsumeReportModel> deptConsumelist=deptConsumeReportService.getDeptConsumeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(deptConsumelist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("折前总额（元）");
		cloTitleList.add("折后总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		cloTitleList.add("早餐次数");
		cloTitleList.add("早餐金额（元）");
		cloTitleList.add("午餐次数");
		cloTitleList.add("午餐金额（元）");
		cloTitleList.add("晚餐次数");
		cloTitleList.add("晚餐金额（元）");
		cloTitleList.add("夜宵次数");
		cloTitleList.add("夜宵金额（元）");
		cloTitleList.add("餐次外次数");
		cloTitleList.add("餐次外金额（元）");
		
		colList.add("every_date");
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("amount");
		colList.add("money");
		colList.add("after_discount_money");
		colList.add("refund_total");
		colList.add("cancel_total");
		colList.add("meal1_amount");
		colList.add("meal1_money");
		colList.add("meal2_amount");
		colList.add("meal2_money");
		colList.add("meal3_amount");
		colList.add("meal3_money");
		colList.add("meal4_amount");
		colList.add("meal4_money");
		colList.add("meal5_amount");
		colList.add("meal5_money");
		
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
			String fileName="部门消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("部门消费报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-23 
	 * @功能描述: 按筛选条件导出部门消费信息
	 * @参数描述: dept_num,dept_name,begin_time,end_time
	 */
	public  void  exportDeptConsumeReprort2(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		DeptConsumeReportModel  deptConsume=GsonUtil.toBean(data,DeptConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dept_num", deptConsume.getDept_num());
		map.put("begin_time", deptConsume.getBegin_time());
		map.put("end_time", deptConsume.getEnd_time());
		
		List<DeptConsumeReportModel> deptConsumelist=deptConsumeReportService.initDeptConsumeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(deptConsumelist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(deptConsume.getBegin_time());
		cloTitleList.add(deptConsume.getEnd_time());
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("折前总额（元）");
		cloTitleList.add("折后总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		cloTitleList.add("早餐次数");
		cloTitleList.add("早餐金额（元）");
		cloTitleList.add("午餐次数");
		cloTitleList.add("午餐金额（元）");
		cloTitleList.add("晚餐次数");
		cloTitleList.add("晚餐金额（元）");
		cloTitleList.add("夜宵次数");
		cloTitleList.add("夜宵金额（元）");
		cloTitleList.add("餐次外次数");
		cloTitleList.add("餐次外金额（元）");
		
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("amount");
		colList.add("money");
		colList.add("after_discount_money");
		colList.add("refund_total");
		colList.add("cancel_total");
		colList.add("meal1_amount");
		colList.add("meal1_money");
		colList.add("meal2_amount");
		colList.add("meal2_money");
		colList.add("meal3_amount");
		colList.add("meal3_money");
		colList.add("meal4_amount");
		colList.add("meal4_money");
		colList.add("meal5_amount");
		colList.add("meal5_money");
		
		
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
			String fileName="部门消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilReport2.exportExcel("部门消费报表", colTitles, cols, list);
            workbook.write(out);
            out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void  getVendor(){
	    List<Vendor> list=deptConsumeReportService.getVendor();
		printHttpServletResponse(new Gson().toJson(list));
	}
	
	
	@Override
	public Object getModel() {
		return null;
	}

}
