package com.kuangchi.sdd.consumeConsole.terminalRechargeReport.action;

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
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.util.ExcelUtilReport;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.model.TerminalRechargeModel;
import com.kuangchi.sdd.consumeConsole.terminalRechargeReport.service.TerminalRechargeService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("TerminalRechargeAction")
public class TerminalRechargeAction extends BaseActionSupport{
	
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "terminalRechargeServiceImpl")
	private TerminalRechargeService terminalRechargeService;

	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 模糊查询全部终端充值信息[分页]
	 * @参数描述: device_num,device_name,vendor_dealer_name,begin_time,end_time,page,rows
	 */
	public  void  getTerminalRechargeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		TerminalRechargeModel  terminalRecharge=GsonUtil.toBean(data,TerminalRechargeModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalRecharge.getDevice_num());
		map.put("device_name", terminalRecharge.getDevice_name());
		map.put("vendor_healer_name", terminalRecharge.getVendor_healer_name());
		map.put("begin_time", terminalRecharge.getBegin_time());
		map.put("end_time", terminalRecharge.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<TerminalRechargeModel> list=terminalRechargeService.getTerminalRechargeReport(map);
		Integer count=terminalRechargeService.getTerminalRechargeReportCount(map);
		
		Grid<TerminalRechargeModel> grid=new Grid<TerminalRechargeModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 导出全部终端充值信息
	 * @参数描述: device_num,device_name,good_name,begin_time,end_time
	 */
	public  void  exportTerminalRechargeReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		TerminalRechargeModel  terminalRecharge=GsonUtil.toBean(data,TerminalRechargeModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalRecharge.getDevice_num());
		map.put("device_name", terminalRecharge.getDevice_name());
		map.put("vendor_healer_name", terminalRecharge.getVendor_healer_name());
		map.put("begin_time", terminalRecharge.getBegin_time());
		map.put("end_time", terminalRecharge.getEnd_time());
		
		List<TerminalRechargeModel> terminalConsumeList=terminalRechargeService.getTerminalRechargeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(terminalConsumeList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("设备编号");
		cloTitleList.add("设备名称");
		cloTitleList.add("商家名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("every_date");
		colList.add("device_num");
		colList.add("device_name");
		colList.add("vendor_healer_name");
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
			String fileName="终端充值报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("终端充值报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 按照筛选条件统计终端充值信息[分页]
	 * @参数描述: device_num,begin_time,end_time,page,rows
	 */
	public  void  countTerminalRechargeReprort(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		TerminalRechargeModel  terminalConsume=GsonUtil.toBean(data,TerminalRechargeModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalConsume.getDevice_num());
		map.put("begin_time", terminalConsume.getBegin_time());
		map.put("end_time", terminalConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<TerminalRechargeModel> list=terminalRechargeService.countTerminalRechargeReport(map);
		Integer count=terminalRechargeService.countTerminalRechargeReportCount(map);
		
		Grid<TerminalRechargeModel> grid=new Grid<TerminalRechargeModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-26 
	 * @功能描述: 按筛选条件导出统计的终端充值信息
	 * @参数描述: device_num,begin_time,end_time
	 */
	public  void  exportTerminalRechargeReprort2(){
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		
		String data = request.getParameter("data");
		TerminalRechargeModel  terminalConsume=GsonUtil.toBean(data,TerminalRechargeModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalConsume.getDevice_num());
		map.put("begin_time", terminalConsume.getBegin_time());
		map.put("end_time", terminalConsume.getEnd_time());
		
		List<TerminalRechargeModel> terminalConsumeList=terminalRechargeService.countTerminalRechargeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(terminalConsumeList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(terminalConsume.getBegin_time());
		cloTitleList.add(terminalConsume.getEnd_time());
		cloTitleList.add("设备编号");
		cloTitleList.add("设备名称");
		cloTitleList.add("商家名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("device_num");
		colList.add("device_name");
		colList.add("vendor_healer_name");
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
			String fileName="终端充值报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook  workbook=ExcelUtilReport.exportExcel("终端充值报表", colTitles, cols, list);
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
