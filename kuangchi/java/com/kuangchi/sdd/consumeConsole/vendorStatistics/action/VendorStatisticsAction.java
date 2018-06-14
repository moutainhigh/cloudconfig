package com.kuangchi.sdd.consumeConsole.vendorStatistics.action;


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



import com.kuangchi.sdd.consumeConsole.vendorStatistics.model.VendorStatistics;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.service.IVendorstatisticsService;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.util.ExcelUtilSpecial;
import com.kuangchi.sdd.consumeConsole.vendorStatistics.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("vendorStatisticsAction")
public class VendorStatisticsAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(VendorStatisticsAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "vendorstatisticsServiceImpl")
	private IVendorstatisticsService vendorstatisticsService;
	
	private VendorStatistics model;
	public VendorStatisticsAction(){
		model=new VendorStatistics();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到个人收支信息主页面
	public String toMyVendorStatistics(){
		return "success";
	}
	//查询所有商户编号
	public void selectByVendorNo(){
		List<Map> list = new ArrayList<Map>();
		List<VendorStatistics> incomeInfo=vendorstatisticsService.selectByVendor();
		for (VendorStatistics deptStatistics : incomeInfo) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("TEXT", deptStatistics.getVendor_name());// 绑定到页面combobox中的下拉框
			map.put("ID", deptStatistics.getVendor_num());// 绑定到页面combobox中的下拉框
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	
	
	
	//查询部门全部信息
	public void getSelVendorStatistics(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		VendorStatistics vendor_record=GsonUtil.toBean(data,VendorStatistics.class);
		Grid allDiscount=vendorstatisticsService.selectAllVendorStatistics(vendor_record, page, rows);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	
	}
	//导出部门收支记录
	public void exportVendorStatistics() {
		HttpServletResponse response = getHttpServletResponse();
		String data= getHttpServletRequest().getParameter("data");
		VendorStatistics vendor_record=GsonUtil.toBean(data,VendorStatistics.class);
		List<VendorStatistics> incomeRecord=vendorstatisticsService.exportAllVendorstatistics(vendor_record);
		try {
			for (VendorStatistics incomeStatistics : incomeRecord) {
				if(incomeStatistics.getType().equals("1")){
					incomeStatistics.setType("收入");
				}else if(incomeStatistics.getType().equals("2")){
					incomeStatistics.setType("支出");
				}else if(incomeStatistics.getType().equals("4")){
					incomeStatistics.setType("支出");
				}else if(incomeStatistics.getType().equals("3")){
					incomeStatistics.setType("平账");
				}else{
					incomeStatistics.setType("-");
				}
			}
		} catch (Exception e) {
		}
		String jsonList = GsonUtil.toJson(incomeRecord);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		// 设置列表头和列数据键
					List<String> colList = new ArrayList<String>();
					List<String> colTitleList = new ArrayList<String>();
					//colTitleList.add("账号");
					colTitleList.add("卡号");
					colTitleList.add("商户编号");
					colTitleList.add("商户名称");
					colTitleList.add("商家名称");
					colTitleList.add("消费员工编号");
					colTitleList.add("消费员工名称");
					colTitleList.add("收入金额(元)");
					colTitleList.add("支出金额(元)");
					//colTitleList.add("余额(元)");
					colTitleList.add("收支类型");
					colTitleList.add("消费时间");
					colTitleList.add("备注");
					
					//colList.add("account_num");
					colList.add("card_num");
					colList.add("vendor_num");
					colList.add("vendor_name");
					colList.add("vendor_dealer_name");
					colList.add("staff_no");
					colList.add("staff_name");
					colList.add("outbound");
					colList.add("inbound");
					//colList.add("balance");
					colList.add("type");
					colList.add("consume_time");
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
						String fileName="商户收支统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecialCount.exportExcel("商户收支统计表", colTitles, cols, list);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}

	//查询处理部门收支报表信息
	public void VendorStatisticsInfo(){
		String page=getHttpServletRequest().getParameter("page");
		String rows=getHttpServletRequest().getParameter("rows");
		String data= getHttpServletRequest().getParameter("data");
		VendorStatistics vendor_record=GsonUtil.toBean(data,VendorStatistics.class);
		Grid vendorInfo=vendorstatisticsService.selectVendorStatistics(vendor_record,page,rows);
		printHttpServletResponse(GsonUtil.toJson(vendorInfo));
	
	}
	
	//导出商户收支汇总
		public void exportAllVendorStatistics() {
			HttpServletResponse response = getHttpServletResponse();
			String data= getHttpServletRequest().getParameter("data");
			VendorStatistics vendor_record=GsonUtil.toBean(data,VendorStatistics.class);
			vendor_record.setEndTime(vendor_record.getEnd_time());
			List<VendorStatistics> vendorRecord=vendorstatisticsService.ExportVendorStatistics(vendor_record);
			String jsonList = GsonUtil.toJson(vendorRecord);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("商户编号");
						colTitleList.add("商户名称");
						colTitleList.add("商家名称");
						//colTitleList.add("商户充值收入金额（元）");
						//colTitleList.add("商户撤销消费收入金额（元）");
						colTitleList.add("商户收入总金额（元）");
						//colTitleList.add("商户消费支出金额（元）");
						//colTitleList.add("商户退款支出金额（元）");
						colTitleList.add("商户支出总金额（元）");
						
						colList.add("vendor_num");//商户编号
						colList.add("vendor_name");//商户名称
						colList.add("vendor_dealer_name");//商户姓名
						//colList.add("outbound_a");//充值总额
						//colList.add("outbound_a");//撤销消费总额
						colList.add("outbound_money");//收入总额
						//colList.add("inbound_b");//消费总额					
						//colList.add("inbound_b");//退款总额
						colList.add("inbound_money");//支出总额
						
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
							String fileName="商户收支汇总表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecial.exportExcel("商户收支汇总表",colTitles,cols,list,
									vendor_record.getBegin_time(),vendor_record.getEndTime());
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
			}
	
	
}