package com.kuangchi.sdd.consumeConsole.terminalConsumeReport.action;

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
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.model.TerminalConsumeModel;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.service.TerminalConsumeReportService;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.util.ExcelUtilReport;
import com.kuangchi.sdd.consumeConsole.terminalConsumeReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("TerminalConsumeReportAction")
public class TerminalConsumeReportAction extends BaseActionSupport {

	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "terminalConsumeReportServiceImpl")
	private TerminalConsumeReportService terminalConsumeReportService;
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-25 
	 * @功能描述: 模糊查询全部终端消费信息[分页]
	 * @参数描述: device_num,device_name,vendor_dealer_name,begin_time,end_time,page,rows
	 */
	public  void  getTerminalConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		TerminalConsumeModel  terminalConsume=GsonUtil.toBean(data,TerminalConsumeModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalConsume.getDevice_num());
		map.put("device_name", terminalConsume.getDevice_name());
		map.put("vendor_dealer_name", terminalConsume.getVendor_dealer_name());
		map.put("begin_time", terminalConsume.getBegin_time());
		map.put("end_time", terminalConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<TerminalConsumeModel> list=terminalConsumeReportService.getTerminalConsumeReport(map);
		Integer count=terminalConsumeReportService.getTerminalConsumeReportCount(map);
		
		Grid<TerminalConsumeModel> grid=new Grid<TerminalConsumeModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-25 
	 * @功能描述: 导出全部终端消费信息
	 * @参数描述: device_num,device_name,good_name,begin_time,end_time
	 */
	public  void  exportTerminalConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		TerminalConsumeModel  terminalConsume=GsonUtil.toBean(data,TerminalConsumeModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalConsume.getDevice_num());
		map.put("device_name", terminalConsume.getDevice_name());
		map.put("vendor_dealer_name", terminalConsume.getVendor_dealer_name());
		map.put("begin_time", terminalConsume.getBegin_time());
		map.put("end_time", terminalConsume.getEnd_time());
		
		List<TerminalConsumeModel> terminalConsumeList=terminalConsumeReportService.getTerminalConsumeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(terminalConsumeList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("设备编号");
		cloTitleList.add("设备名称");
		cloTitleList.add("商家名称");
		cloTitleList.add("合计数量");
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
		colList.add("device_num");
		colList.add("device_name");
		colList.add("vendor_dealer_name");
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
			String fileName="终端消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("终端消费报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-25 
	 * @功能描述: 按照筛选条件统计终端消费信息[分页]
	 * @参数描述: device_num,begin_time,end_time,page,rows
	 */
	public  void  countTerminalConsumeReprort(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		TerminalConsumeModel  terminalConsume=GsonUtil.toBean(data,TerminalConsumeModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalConsume.getDevice_num());
		map.put("begin_time", terminalConsume.getBegin_time());
		map.put("end_time", terminalConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<TerminalConsumeModel> list=terminalConsumeReportService.countTerminalConsumeReport(map);
		Integer count=terminalConsumeReportService.countTerminalConsumeReportCount(map);
		
		Grid<TerminalConsumeModel> grid=new Grid<TerminalConsumeModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-25 
	 * @功能描述: 按筛选条件导出统计的终端消费信息
	 * @参数描述: device_num,begin_time,end_time
	 */
	public  void  exportTerminalConsumeReprort2(){
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		
		String data = request.getParameter("data");
		TerminalConsumeModel  terminalConsume=GsonUtil.toBean(data,TerminalConsumeModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_num", terminalConsume.getDevice_num());
		map.put("begin_time", terminalConsume.getBegin_time());
		map.put("end_time", terminalConsume.getEnd_time());
		
		List<TerminalConsumeModel> terminalConsumeList=terminalConsumeReportService.countTerminalConsumeReportNoLimit(map);
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
		cloTitleList.add("合计数量");
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
		
		colList.add("device_num");
		colList.add("device_name");
		colList.add("vendor_dealer_name");
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
			String fileName="终端消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook  workbook=ExcelUtilReport.exportExcel("终端消费报表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-25 
	 * @功能描述: 获取所有设备编号
	 */
	public  void  getDeviceNum(){
    List<Device> list=terminalConsumeReportService.getDeviceNum();
	printHttpServletResponse(new Gson().toJson(list));
		
	}
	
	

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
