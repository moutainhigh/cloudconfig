package com.kuangchi.sdd.consumeConsole.deptStatistics.action;


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
import com.kuangchi.sdd.consumeConsole.deptStatistics.model.DeptStatistics;
import com.kuangchi.sdd.consumeConsole.deptStatistics.service.IDeptstatisticsService;
import com.kuangchi.sdd.consumeConsole.deptStatistics.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.deptStatistics.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("deptStatisticsAction")
public class DeptStatisticsAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(DeptStatisticsAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "deptstatisticsServiceImpl")
	private IDeptstatisticsService deptstatisticsService;
	
	private DeptStatistics model;
	public DeptStatisticsAction(){
		model=new DeptStatistics();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到个人收支信息主页面
	public String toMyDeptStatistics(){
		return "success";
	}
	
	//查询部门全部信息
	public void getSelDeptStatistics(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		DeptStatistics dept_record=GsonUtil.toBean(data,DeptStatistics.class);
		Grid allDiscount=deptstatisticsService.selectAllDeptStatistics(dept_record, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	
	}
	//导出部门收支记录
	public void exportDeptStatistics() {
		HttpServletResponse response = getHttpServletResponse();
		String data= getHttpServletRequest().getParameter("data");
		DeptStatistics dept_record=GsonUtil.toBean(data,DeptStatistics.class);
		List<DeptStatistics> incomeRecord=deptstatisticsService.exportAllDeptstatistics(dept_record);
		if(incomeRecord.size()!=0){
			for (DeptStatistics incomeStatistics : incomeRecord) {
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
					colTitleList.add("部门编号");
					colTitleList.add("部门名称");
					colTitleList.add("员工工号");
					colTitleList.add("员工名称");
					colTitleList.add("收入金额(元)");
					colTitleList.add("支出金额(元)");
					//colTitleList.add("余额(元)");
					colTitleList.add("账单时间");
					colTitleList.add("收支类型");
					colTitleList.add("备注");
					
					colList.add("account_num");
					colList.add("account_type_name");
					colList.add("dept_no");
					colList.add("dept_name");
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("inbound");
					colList.add("outbound");
					//colList.add("balance");
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
						String fileName="部门人员收支统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecialCount.exportExcel("部门人员收支统计表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	
	//查询处理部门收支报表信息
	public void DeptStatisticsInfo(){
		String data= getHttpServletRequest().getParameter("data");
		DeptStatistics dept_record=GsonUtil.toBean(data,DeptStatistics.class);
		Grid deptInfo=deptstatisticsService.selectDeptStatistics(dept_record);
		printHttpServletResponse(GsonUtil.toJson(deptInfo));
	
	}
	
	//导出部门收支汇总
		public void exportAllDeptStatistics() {
			HttpServletResponse response = getHttpServletResponse();
			String data= getHttpServletRequest().getParameter("data");
			DeptStatistics dept_record=GsonUtil.toBean(data,DeptStatistics.class);
			dept_record.setEndTime(dept_record.getEnd_time());
			List<DeptStatistics> incomeRecord=deptstatisticsService.ExportDeptStatistics(dept_record);
			String jsonList = GsonUtil.toJson(incomeRecord);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("部门编号");
						colTitleList.add("部门名称");
						colTitleList.add("部门充值总额（元）");
						colTitleList.add("部门补助总额（元）");
						colTitleList.add("部门撤销消费总额（元）");
						colTitleList.add("部门退款总额（元）");
						colTitleList.add("部门转入总额（元）");
						colTitleList.add("部门消费总额（元）");
						colTitleList.add("部门补扣总额（元）");
						colTitleList.add("部门转出总额（元）");
						
						colList.add("dept_no");//部门编号
						colList.add("dept_name");//部门名称
						colList.add("inbound_a");//充值总额
						colList.add("inbound_b");//补助总额
						colList.add("inbound_c");//撤销消费总额
						colList.add("outbound_c");//退款总额
						colList.add("inbound_d");//转入总额
						colList.add("outbound_a");//消费总额					
						colList.add("outbound_b");//补扣总额
						colList.add("outbound_d");//转出总额
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
							String fileName="部门收支汇总表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecial.exportExcel("部门收支汇总表",colTitles,cols,list,
									dept_record.getBegin_time(),dept_record.getEndTime());
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
			}
		//查询所有部门编号
				public void selectByDeptNo(){
					List<Map> list = new ArrayList<Map>();
					List<DeptStatistics> incomeInfo=deptstatisticsService.selectByDept();
					for (DeptStatistics deptStatistics : incomeInfo) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("TEXT", deptStatistics.getDept_name());// 绑定到页面combobox中的下拉框
						map.put("ID", deptStatistics.getDept_num());// 绑定到页面combobox中的下拉框
						list.add(map);
					}
					printHttpServletResponse(GsonUtil.toJson(list));
				}
	
}