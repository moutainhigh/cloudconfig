package com.kuangchi.sdd.consumeConsole.incomeStatistics.action;


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

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.model.IncomeStatistics;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.service.IIncomestatisticsService;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.incomeStatistics.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("incomeStatisticsAction")
public class IncomeStatisticsAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(IncomeStatisticsAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "IncomestatisticsServiceImpl")
	private IIncomestatisticsService incomestatisticsService;
	
	private IncomeStatistics model;
	public IncomeStatisticsAction(){
		model=new IncomeStatistics();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到个人收支信息主页面
	public String toMyIncomeStatistics(){
		return "success";
	}
	
	//查询全部信息
	public void getSelIncomeStatistics(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		IncomeStatistics income_record=GsonUtil.toBean(data,IncomeStatistics.class);
		Grid allDiscount=incomestatisticsService.selectAllConsumeRecords(income_record, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	
	}
	//导出个人收支记录
	public void exportIncomeStatistics() {
		HttpServletResponse response = getHttpServletResponse();
		String data= getHttpServletRequest().getParameter("data");
		IncomeStatistics income_record=GsonUtil.toBean(data,IncomeStatistics.class);
		List<IncomeStatistics> incomeRecord=incomestatisticsService.exportIncomeStatistics(income_record);
		if(incomeRecord.size()!=0){
			for (IncomeStatistics incomeStatistics : incomeRecord) {
				if(incomeStatistics.getType().equals("0")){
					incomeStatistics.setType("充值");
				}else if(incomeStatistics.getType().equals("1")){
					incomeStatistics.setType("消费");
				}else if(incomeStatistics.getType().equals("2")){
					incomeStatistics.setType("补助");
				}else if(incomeStatistics.getType().equals("3")){
					incomeStatistics.setType(" 补扣");
				}else if(incomeStatistics.getType().equals("4")){
					incomeStatistics.setType(" 撤销消费");
				}else if(incomeStatistics.getType().equals("5")){
					incomeStatistics.setType("退款");
				}else if(incomeStatistics.getType().equals("6")){
					incomeStatistics.setType("转入");
				}else if(incomeStatistics.getType().equals("7")){
					incomeStatistics.setType("转出");
				}else if(incomeStatistics.getType().equals("8")){
					incomeStatistics.setType("平账");
				}
			}
		}
		String jsonList = GsonUtil.toJson(incomeRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					colTitleList.add("账号");
					colTitleList.add("账号名称");
					colTitleList.add("员工工号");
					colTitleList.add("员工名称");
					colTitleList.add("收入金额(元)");
					colTitleList.add("支出金额(元)");
					colTitleList.add("余额(元)");
					colTitleList.add("部门编号");
					colTitleList.add("部门名称");
					colTitleList.add("账单时间");
					colTitleList.add("收支类型");
					colTitleList.add("备注");
					
					colList.add("account_num");
					colList.add("account_type_name");
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("inbound");
					colList.add("outbound");
					colList.add("balance");
					colList.add("dept_no");
					colList.add("dept_name");
					colList.add("time");
					colList.add("type");
					colList.add("deal_reason");
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
						String fileName="个人收支统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecialCount.exportExcel("个人收支统计表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	
	
	//处理个人收支报表信息
	public void IncomeStatisticsInfo(){
		//String page=getHttpServletRequest().getParameter("page");
		//String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		IncomeStatistics income_record=GsonUtil.toBean(data,IncomeStatistics.class);
		//Grid incomeInfo=incomestatisticsService.selectStatistics(income_record,page,rows);
		IncomeStatistics incomeInfo=incomestatisticsService.ExportAllStatistics(income_record);
		List<IncomeStatistics> income=new ArrayList<IncomeStatistics>();
		if(incomeInfo.getStaff_no()!=null){
			income.add(incomeInfo);
		}
		printHttpServletResponse(GsonUtil.toJson(income));
	
	}
	
	//导出个人收支汇总
		public void exportAllIncomeStatistics() {
			HttpServletResponse response = getHttpServletResponse();
			String data= getHttpServletRequest().getParameter("data");
			IncomeStatistics income_record=GsonUtil.toBean(data,IncomeStatistics.class);
			income_record.setEndTime(income_record.getEnd_time());
			IncomeStatistics incomeRecord=incomestatisticsService.ExportAllStatistics(income_record);
			incomeRecord.setBegin_time(income_record.getBegin_time());
			incomeRecord.setEnd_time(income_record.getEndTime());
			// 设置列表头和列数据键
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("项目");
						colTitleList.add("次数");
						colTitleList.add("金额（元）");
						colTitleList.add("项目");
						colTitleList.add("次数");
						colTitleList.add("金额（元）");

						String[] colTitles = new String[colTitleList.size()];
						for (int i = 0; i < colTitleList.size(); i++) {
						colTitles[i] = colTitleList.get(i);
					}
						OutputStream out = null;
						try {
							out = response.getOutputStream();
							response.setContentType("application/x-msexcel");
							String fileName="个人收支汇总统计表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecial.exportExcel("个人收支汇总统计表",colTitles,incomeRecord);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
			}
	
		//查询所有员工编号
		public void selectBystaffNo(){
			List<Map> list = new ArrayList<Map>();
			List<IncomeStatistics> incomeInfo=incomestatisticsService.selectByStaffno();
			for (IncomeStatistics incomeStatistics : incomeInfo) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("TEXT", incomeStatistics.getStaff_name());// 绑定到页面combobox中的下拉框
				map.put("ID", incomeStatistics.getStaff_no());// 绑定到页面combobox中的下拉框
				list.add(map);
			}
			printHttpServletResponse(GsonUtil.toJson(list));
		}
		
		
		
	
}