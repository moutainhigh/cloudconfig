package com.kuangchi.sdd.consumeConsole.mealConsumeReport.action;

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
import com.kuangchi.sdd.consumeConsole.discount.action.DiscountAction;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.model.MealConsumeReportModel;
import com.kuangchi.sdd.consumeConsole.mealConsumeReport.service.MealConsumeReportService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("mealConsumeReportAction")
public class MealConsumeReportAction extends BaseActionSupport{

	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "mealConsumeReportServiceImpl")
	private MealConsumeReportService mealConsumeReportService;
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-06 
	 * @功能描述: 模糊查询全部餐次消费信息[分页]
	 * @参数描述: meal_name,meal_num,begin_time,end_time,page,rows
	 */
	public  void  getMealConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		MealConsumeReportModel  mealConsume=GsonUtil.toBean(data,MealConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meal_name",mealConsume.getMeal_name());
		map.put("meal_num", mealConsume.getMeal_num());
		map.put("begin_time", mealConsume.getBegin_time());
		map.put("end_time", mealConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<MealConsumeReportModel> list=mealConsumeReportService.getMealConsumeReport(map);
		Integer count=mealConsumeReportService.getMealConsumeReportCount(map);
		
		Grid<MealConsumeReportModel> grid=new Grid<MealConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-06 
	 * @功能描述: 导出餐次消费信息
	 * @参数描述: meal_name,meal_num,begin_time,end_time
	 */
	public  void  exportMealConsumeReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		MealConsumeReportModel  mealConsume=GsonUtil.toBean(data,MealConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meal_name",mealConsume.getMeal_name());
		map.put("meal_num", mealConsume.getMeal_num());
		map.put("begin_time", mealConsume.getBegin_time());
		map.put("end_time", mealConsume.getEnd_time());
		
		List<MealConsumeReportModel> mealConsumelist=mealConsumeReportService.getMealConsumeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(mealConsumelist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("餐次编号");
		cloTitleList.add("餐次名称");
		cloTitleList.add("合计商品数量");
		cloTitleList.add("折前商品总额（元）");
		cloTitleList.add("折后商品总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		
		colList.add("every_date");
		colList.add("meal_num");
		colList.add("meal_name");
		colList.add("amount");
		colList.add("money");
		colList.add("after_discount_money");
		colList.add("refund_total");
		colList.add("cancel_total");
		
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
			String fileName="餐次消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("餐次消费报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-06 
	 * @功能描述: 按筛选条件统计餐次消费信息[分页]
	 * @参数描述: meal_num,begin_time,end_time,page,rows
	 */
	public  void  countMealConsumeReprort(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		MealConsumeReportModel  mealConsume=GsonUtil.toBean(data,MealConsumeReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meal_num", mealConsume.getMeal_num());
		map.put("begin_time", mealConsume.getBegin_time());
		map.put("end_time", mealConsume.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<MealConsumeReportModel> list=mealConsumeReportService.countMealConsumeReport(map);
		Integer count=mealConsumeReportService.countMealConsumeReportCount(map);
		
		Grid<MealConsumeReportModel> grid=new Grid<MealConsumeReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-06 
	 * @功能描述: 按筛选条件导出统计的餐次消费信息
	 * @参数描述: meal_num,begin_time,end_time
	 */
	public  void  exportMealConsumeReprort2(){
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		
		String data = request.getParameter("data");
		MealConsumeReportModel  mealConsume=GsonUtil.toBean(data,MealConsumeReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("meal_num", mealConsume.getMeal_num());
		map.put("begin_time", mealConsume.getBegin_time());
		map.put("end_time", mealConsume.getEnd_time());
		
		List<MealConsumeReportModel> mealConsumelist=mealConsumeReportService.countMealConsumeReportNoLimit(map);
		String jsonList=GsonUtil.toJson(mealConsumelist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(mealConsume.getBegin_time());
		cloTitleList.add(mealConsume.getEnd_time());
		cloTitleList.add("餐次编号");
		cloTitleList.add("餐次名称");
		cloTitleList.add("合计商品数量");
		cloTitleList.add("折前商品总额（元）");
		cloTitleList.add("折后商品总额（元）");
		cloTitleList.add("退款总额（元）");
		cloTitleList.add("撤销消费总额（元）");
		
		colList.add("meal_num");
		colList.add("meal_name");
		colList.add("amount");
		colList.add("money");
		colList.add("after_discount_money");
		colList.add("refund_total");
		colList.add("cancel_total");
		
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
			String fileName="餐次消费报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook  workbook=ExcelUtilReport2.exportExcel("餐次消费报表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	/*获取餐次编号和餐次名称*/
	public  void  getMeal(){
		List<MealModel> mealList=mealConsumeReportService.getMeal();
		printHttpServletResponse(new Gson().toJson(mealList));
	}
	
	
	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
