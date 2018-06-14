package com.kuangchi.sdd.consumeConsole.goodConsumeReport.action;

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
import com.kuangchi.sdd.consumeConsole.good.model.Good;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.model.GoodConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.goodConsumeReport.service.GoodConsumeReportService;
import com.kuangchi.sdd.consumeConsole.goodType.model.GoodType;
import com.kuangchi.sdd.consumeConsole.personBonusReport.util.ExcelUtilReport;
import com.kuangchi.sdd.consumeConsole.personBonusReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;


@Controller("GoodConsumeReportAction")
public class GoodConsumeReportAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "goodConsumeReportServiceImpl")
	private GoodConsumeReportService goodConsumeReportService;
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-16 
	 * @功能描述: 模糊查询全部商品消费信息[分页]
	 * @参数描述: good_type,good_num,good_name,page,rows
	 */
	public  void  getGoodConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		GoodConsumeReportModel  goodConsume=GsonUtil.toBean(data,GoodConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_type_num", goodConsume.getGood_type_num());
		map.put("good_num", goodConsume.getGood_num());
		map.put("good_name", goodConsume.getGood_name());
		map.put("begin_time", goodConsume.getBegin_time());
		map.put("end_time", goodConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<GoodConsumeReportModel> list=goodConsumeReportService.getGoodConsumeReportByParam(map);
		Integer count=goodConsumeReportService.getGoodConsumeReportCount(map);
		
		Grid<GoodConsumeReportModel> grid=new Grid<GoodConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-24 
	 * @功能描述: 按照商品编号统计商品消费信息报表
	 * @参数描述: good_type,good_num,good_name,page,rows
	 */
	public  void  countGoodConsumeReprortByGoodNum(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		GoodConsumeReportModel  goodConsume=GsonUtil.toBean(data,GoodConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num", goodConsume.getGood_num());
		map.put("begin_time", goodConsume.getBegin_time());
		map.put("end_time", goodConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<GoodConsumeReportModel> list=goodConsumeReportService.countGoodConsumeReportByGoodNum(map);
		Integer count=goodConsumeReportService.countGoodConsumeReportByGoodNumCounts(map);
		
		Grid<GoodConsumeReportModel> grid=new Grid<GoodConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-24 
	 * @功能描述: 按照商品类型统计商品消费信息报表
	 * @参数描述: good_type,good_num,good_name,page,rows
	 */
	public  void  countGoodConsumeReprortByGoodType(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		GoodConsumeReportModel  goodConsume=GsonUtil.toBean(data,GoodConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_type_num", goodConsume.getSelect_num());
		map.put("good_num", goodConsume.getGood_num());
		map.put("good_name", goodConsume.getGood_name());
		map.put("begin_time", goodConsume.getBegin_time());
		map.put("end_time", goodConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<GoodConsumeReportModel> list=goodConsumeReportService.countGoodConsumeReportByGoodType(map);
		Integer count=goodConsumeReportService.countGoodConsumeReportByGoodTypeCounts(map);
		
		Grid<GoodConsumeReportModel> grid=new Grid<GoodConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-16 
	 * @功能描述: 导出全部商品消费信息
	 * @参数描述: good_type,good_num,good_name,page,rows
	 */
	public  void  exportGoodConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		GoodConsumeReportModel  goodConsume=GsonUtil.toBean(data,GoodConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_type_num", goodConsume.getGood_type_num());
		map.put("good_num", goodConsume.getGood_num());
		map.put("good_name", goodConsume.getGood_name());
		map.put("begin_time", goodConsume.getBegin_time());
		map.put("end_time", goodConsume.getEnd_time());
		
		List<GoodConsumeReportModel> goodConsumeList=goodConsumeReportService.getGoodConsumeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(goodConsumeList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("商品编号");
		cloTitleList.add("商品名称");
		cloTitleList.add("商品类型");
		cloTitleList.add("商家名称");
		cloTitleList.add("单价（元）");
		cloTitleList.add("合计数量");
		cloTitleList.add("折前商品总额（元）");
		cloTitleList.add("折后商品总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		cloTitleList.add("早餐个数");
		cloTitleList.add("早餐金额（元）");
		cloTitleList.add("午餐个数");
		cloTitleList.add("午餐金额（元）");
		cloTitleList.add("晚餐个数");
		cloTitleList.add("晚餐金额（元）");
		cloTitleList.add("夜宵个数");
		cloTitleList.add("夜宵金额（元）");
		cloTitleList.add("餐次外个数");
		cloTitleList.add("餐次外金额（元）");
		
		colList.add("every_date");
		colList.add("good_num");
		colList.add("good_name");
		colList.add("type_name");
		colList.add("vendor_dealer_name");
		colList.add("price");
		colList.add("amount");
		colList.add("total");
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
			String fileName="商品消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("商品消费报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-24 
	 * @功能描述: 按照商品编号统计商品消费信息[导出用]
	 * @参数描述: good_type,good_num,good_name,page,rows
	 */
	public  void  exportGoodConsumeReprortByGoodNum(){
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		
		String data = request.getParameter("data");
		GoodConsumeReportModel  goodConsume=GsonUtil.toBean(data,GoodConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_num", goodConsume.getGood_num());
		map.put("begin_time", goodConsume.getBegin_time());
		map.put("end_time", goodConsume.getEnd_time());
		
		List<GoodConsumeReportModel> goodConsumeList=goodConsumeReportService.countGoodConsumeReportByGoodNumNoLimit(map);
		String jsonList=GsonUtil.toJson(goodConsumeList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(goodConsume.getBegin_time());
		cloTitleList.add(goodConsume.getEnd_time());
		cloTitleList.add("商品编号");
		cloTitleList.add("商品名称");
		cloTitleList.add("商品类型");
		cloTitleList.add("商家名称");
		cloTitleList.add("单价（元）");
		cloTitleList.add("合计数量");
		cloTitleList.add("折前商品总额（元）");
		cloTitleList.add("折后商品总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		cloTitleList.add("早餐个数");
		cloTitleList.add("早餐金额（元）");
		cloTitleList.add("午餐个数");
		cloTitleList.add("午餐金额（元）");
		cloTitleList.add("晚餐个数");
		cloTitleList.add("晚餐金额（元）");
		cloTitleList.add("夜宵个数");
		cloTitleList.add("夜宵金额（元）");
		cloTitleList.add("餐次外个数");
		cloTitleList.add("餐次外金额（元）");
		
		colList.add("good_num");
		colList.add("good_name");
		colList.add("type_name");
		colList.add("vendor_dealer_name");
		colList.add("price");
		colList.add("amount");
		colList.add("total");
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
			String fileName="商品消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook  workbook=ExcelUtilReport.exportExcel("商品消费报表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-24 
	 * @功能描述: 按照商品类型统计商品消费信息[导出用]
	 * @参数描述: good_type,good_num,good_name,page,rows
	 */
	public  void  exportGoodConsumeReprortByGoodType(){
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		
		String data = request.getParameter("data");
		GoodConsumeReportModel  goodConsume=GsonUtil.toBean(data,GoodConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("good_type_num", goodConsume.getSelect_num());
		map.put("good_num", goodConsume.getGood_num());
		map.put("good_name", goodConsume.getGood_name());
		map.put("begin_time", goodConsume.getBegin_time());
		map.put("end_time", goodConsume.getEnd_time());
		
		List<GoodConsumeReportModel> goodConsumeList=goodConsumeReportService.countGoodConsumeReportByGoodTypeNoLimit(map);
		String jsonList=GsonUtil.toJson(goodConsumeList);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(goodConsume.getBegin_time());
		cloTitleList.add(goodConsume.getEnd_time());
		cloTitleList.add("商品类型");
	/*	cloTitleList.add("商家名称");
		cloTitleList.add("单价（元）");*/
		cloTitleList.add("合计数量");
		cloTitleList.add("折前商品总额（元）");
		cloTitleList.add("折后商品总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		cloTitleList.add("早餐个数");
		cloTitleList.add("早餐金额（元）");
		cloTitleList.add("午餐个数");
		cloTitleList.add("午餐金额（元）");
		cloTitleList.add("晚餐个数");
		cloTitleList.add("晚餐金额（元）");
		cloTitleList.add("夜宵个数");
		cloTitleList.add("夜宵金额（元）");
		cloTitleList.add("餐次外个数");
		cloTitleList.add("餐次外金额（元）");
		
		colList.add("type_name");
		/*colList.add("vendor_dealer_name");
		colList.add("price");*/
		colList.add("amount");
		colList.add("total");
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
			String fileName="商品消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook  workbook=ExcelUtilReport.exportExcel("商品消费报表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-16 
	 * @功能描述: 获取所有商品类型
	 */
	public  void  getGoodType(){
	List<GoodType> list=goodConsumeReportService.getGoodType();
	printHttpServletResponse(new Gson().toJson(list));
		
	}
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-24 
	 * @功能描述: 获取所有商品名称，编号
	 */
	public  void  getGoodName(){
		List<Good> list=goodConsumeReportService.getGoodName();
		printHttpServletResponse(new Gson().toJson(list));
	}
	
	
	
	@Override
	public Object getModel() {
		return null;
	}

}
