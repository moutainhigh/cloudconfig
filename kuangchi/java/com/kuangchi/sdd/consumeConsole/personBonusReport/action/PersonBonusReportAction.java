package com.kuangchi.sdd.consumeConsole.personBonusReport.action;

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
import com.kuangchi.sdd.consumeConsole.personBonusReport.model.PersonBonusrReportModel;
import com.kuangchi.sdd.consumeConsole.personBonusReport.service.PersonBonusReportService;
import com.kuangchi.sdd.consumeConsole.personBonusReport.util.ExcelUtilReport;
import com.kuangchi.sdd.consumeConsole.personBonusReport.util.ExcelUtilSpecial;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("PersonBonusReportAction")
public class PersonBonusReportAction extends BaseActionSupport{
	private static final  Logger LOG = Logger.getLogger(DiscountAction.class);
	private static final long serialVersionUID = -4559409507804703403L;
	
	@Resource(name = "personBonusReportServiceImpl")
	private PersonBonusReportService personBonusReportService;
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 模糊查询全部人员补助信息[分页]
	 * @参数描述: staff_no,staff_name,dept_num,begin_time,end_time,page,rows
	 */
	public  void  getPersonBonusReprort(){
        HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		PersonBonusrReportModel  personBonus=GsonUtil.toBean(data,PersonBonusrReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_no", personBonus.getStaff_no());
		map.put("staff_name",personBonus.getStaff_name());
		map.put("dept_num", personBonus.getDept_num());
		map.put("begin_time", personBonus.getBegin_time());
		map.put("end_time", personBonus.getEnd_time());
		map.put("amount", personBonus.getAmount());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<PersonBonusrReportModel> list=personBonusReportService.getPersonBonusReport(map);
		Integer count=personBonusReportService.getPersonBonusReportCount(map);
		
		Grid<PersonBonusrReportModel> grid=new Grid<PersonBonusrReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 导出人员补助信息
	 * @参数描述: staff_no,staff_name,dept_num,begin_time,end_time
	 */
	public  void  exportPersonBonusReprort(){
        HttpServletRequest request = getHttpServletRequest();
        HttpServletResponse  response=getHttpServletResponse();
        
		String data = request.getParameter("data");
		PersonBonusrReportModel  personBonus=GsonUtil.toBean(data,PersonBonusrReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_no", personBonus.getStaff_no());
		map.put("staff_name",personBonus.getStaff_name());
		map.put("dept_num", personBonus.getDept_num());
		map.put("begin_time", personBonus.getBegin_time());
		map.put("end_time", personBonus.getEnd_time());
		map.put("amount", personBonus.getAmount());
		
		List<PersonBonusrReportModel> personBonuslist=personBonusReportService.getPersonBonusReportNoLimit(map);
		String jsonList=GsonUtil.toJson(personBonuslist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add("统计日期");
		cloTitleList.add("员工工号");
		cloTitleList.add("员工名称");
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("every_date");
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("amount");
		colList.add("money");
		
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
			String fileName="人员补助报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
            Workbook  workbook=ExcelUtilSpecial.exportExcel("人员补助报表", colTitles, cols, list);
	        workbook.write(out);
	        out.flush();
	    } catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 按筛选条件统计人员补助信息[分页]
	 * @参数描述: staff_num,dept_num,begin_time,end_time,page,rows
	 */
	public  void  countPersonBonusReprort(){
		HttpServletRequest request = getHttpServletRequest();
		
		String data = request.getParameter("data");
		PersonBonusrReportModel  personBonus=GsonUtil.toBean(data,PersonBonusrReportModel.class);
		
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", personBonus.getStaff_num());
		map.put("dept_num", personBonus.getDept_num());
		map.put("begin_time", personBonus.getBegin_time());
		map.put("end_time", personBonus.getEnd_time());
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		
		List<PersonBonusrReportModel> list=personBonusReportService.countPersonBonusReport(map);
		Integer count=personBonusReportService.countPersonBonusReportCount(map);
		
		Grid<PersonBonusrReportModel> grid=new Grid<PersonBonusrReportModel>();
		grid.setRows(list);
		grid.setTotal(count);
		
		printHttpServletResponse(new Gson().toJson(grid));
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-29 
	 * @功能描述: 按筛选条件导出统计的人员补助信息
	 * @参数描述: staff_num,dept_num,begin_time,end_time
	 */
	public  void  exportPersonBonusReprort2(){
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse  response=getHttpServletResponse();
		
		String data = request.getParameter("data");
		PersonBonusrReportModel  personBonus=GsonUtil.toBean(data,PersonBonusrReportModel.class);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", personBonus.getStaff_num());
		map.put("dept_num", personBonus.getDept_num());
		map.put("begin_time", personBonus.getBegin_time());
		map.put("end_time", personBonus.getEnd_time());
		
		List<PersonBonusrReportModel> personBonuslist=personBonusReportService.countPersonBonusReportNoLimit(map);
		String jsonList=GsonUtil.toJson(personBonuslist);
		List list=GsonUtil.getListFromJson(jsonList, ArrayList.class);
		//设置 列表头和列数据键
		List<String> colList=new ArrayList<String>();
		List<String> cloTitleList=new ArrayList<String>();
		
		cloTitleList.add(personBonus.getBegin_time());
		cloTitleList.add(personBonus.getEnd_time());
		cloTitleList.add("员工工号");
		cloTitleList.add("员工名称");
		cloTitleList.add("部门编号");
		cloTitleList.add("部门名称");
		cloTitleList.add("合计次数");
		cloTitleList.add("合计金额（元）");
		
		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("dept_no");
		colList.add("dept_name");
		colList.add("amount");
		colList.add("money");
		
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
			String fileName="人员补助报表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook  workbook=ExcelUtilReport.exportExcel("人员补助报表", colTitles, cols, list);
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
