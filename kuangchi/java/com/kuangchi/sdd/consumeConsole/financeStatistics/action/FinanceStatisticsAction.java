package com.kuangchi.sdd.consumeConsole.financeStatistics.action;


import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.AccountStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.model.FinanceStatistics;
import com.kuangchi.sdd.consumeConsole.financeStatistics.service.IFinancestatisticsService;
import com.kuangchi.sdd.consumeConsole.financeStatistics.util.ExcelUtilSpecial;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("financeStatisticsAction")
public class FinanceStatisticsAction extends BaseActionSupport {
	private static final  Logger LOG = Logger.getLogger(FinanceStatisticsAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "financestatisticsServiceImpl")
	private IFinancestatisticsService financestatisticsService;
	
	private FinanceStatistics model;
	public FinanceStatisticsAction(){
		model=new FinanceStatistics();
	}
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到个人收支信息主页面
	public String toMyFinanceStatistics(){
		return "success";
	}
	
	//查询全部财务信息
	public void getSelFinanceStatistics(){
		String data= getHttpServletRequest().getParameter("data");
		FinanceStatistics finance=GsonUtil.toBean(data,FinanceStatistics.class);
		List<FinanceStatistics> allDiscount=financestatisticsService.SelectAllStatistics(finance);
		printHttpServletResponse(GsonUtil.toJson(allDiscount));
	}
	
	//查询财务统计表信息
		public void getAccountStatistics(){
			String page=getHttpServletRequest().getParameter("page");
			String rows=getHttpServletRequest().getParameter("rows");
			String data= getHttpServletRequest().getParameter("data");
			AccountStatistics account=GsonUtil.toBean(data,AccountStatistics.class);
			Grid allDiscount=financestatisticsService.selectAccountStatic(account, page, rows);
			printHttpServletResponse(GsonUtil.toJson(allDiscount));
		}
	
	
	//导出财务汇总记录
	public void exportFinanceStatistics() {
		HttpServletResponse response = getHttpServletResponse();
		String data= getHttpServletRequest().getParameter("data");
		FinanceStatistics finance=GsonUtil.toBean(data,FinanceStatistics.class);
		List<FinanceStatistics> consumeRecord=financestatisticsService.ExportAllStatistics(finance);
					OutputStream out = null;
					try {
						out = response.getOutputStream();
						response.setContentType("application/x-msexcel");
						String fileName="财务汇总统计表.xls";
						response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
						Workbook workbook = ExcelUtilSpecial.exportExcel("财务汇总统计表",consumeRecord);
						workbook.write(out);
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
	}
	
}