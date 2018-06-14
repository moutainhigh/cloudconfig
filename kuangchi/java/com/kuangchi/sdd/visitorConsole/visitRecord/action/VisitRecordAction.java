package com.kuangchi.sdd.visitorConsole.visitRecord.action;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.visitorConsole.visitRecord.service.VisitRecordService;
@Controller("visitRecordAction")
public class VisitRecordAction extends BaseActionSupport{

	@Override
	public Object getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	private VisitRecordService visitRecordService;
	
	/**
	 * 查询来访记录
	 * by gengji.yang
	 */
	public void getMainVisitRecords(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		printHttpServletResponse(GsonUtil.toJson(visitRecordService.getMainVisitRecords(map)));
	}
	
	/**
	 * 查询被访记录
	 * by gengji.yang
	 */
	public void getPVisitRecords(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		printHttpServletResponse(GsonUtil.toJson(visitRecordService.getPVisitRecords(map)));
	}
	
	/**
	 * 查询随访记录
	 * by gengji.yang
	 */
	public void getFVisitRecords(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		printHttpServletResponse(GsonUtil.toJson(visitRecordService.getFVisitRecords(map)));
	}
	
	/**
	 * 查询被访人员 
	 * by gengji.yang
	 */
	public void getPassivePeople(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data,HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		map.put("rows", rows);
		printHttpServletResponse(GsonUtil.toJson(visitRecordService.getPassivePeople(map)));
	}
	
	
	/**
	 *  来访记录查询(没分页)  
	 *  by huixian.pan 
	 */
	public void exportMainVisitRecords() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		HttpServletResponse response = getHttpServletResponse();
		try{
		List<Map> list2= visitRecordService.getMainVisitRecordsNoLimit(map);
		for(Map map2:list2){
			if("0".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "正在访问");
			}else if("1".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "离开");
			}else if("2".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "预约");
			}else if("3".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "超时");
			}else if("4".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "超时后离开");
			}else if("5".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "取消预约");
			}else{
				map2.put("recordIdentity", "预约超时");
			}
			
			if(null == map2.get("visitorCard") || "".equals(map2.get("visitorCard"))){
				map2.put("visitorCard", "-");
			}
			if(null == map2.get("leftDate") || "".equals(map2.get("leftDate"))){
				map2.put("leftDate", "-");
			}
			if(null == map2.get("mMobile") || "".equals(map2.get("mMobile"))){
				map2.put("mMobile", "-");
			}
			if(null == map2.get("visitotrMatter") || "".equals(map2.get("visitotrMatter"))){
				map2.put("visitotrMatter", "-");
			}
			if(null == map2.get("mVisitorCompany") || "".equals(map2.get("mVisitorCompany"))){
				map2.put("mVisitorCompany", "-");
			}
			if(null == map2.get("carryGoods") || "".equals(map2.get("carryGoods"))){
				map2.put("carryGoods", "-");
			}
			if(null == map2.get("mAddress") || "".equals(map2.get("mAddress"))){
				map2.put("mAddress", "-");
			}
			if(null == map2.get("carInfo") || "".equals(map2.get("carInfo"))){
				map2.put("carInfo", "-");
			}
			if("0".equals(map2.get("mVisitorSex"))){
				map2.put("mVisitorSex", "男");
			}else if("1".equals(map2.get("mVisitorSex"))){
				map2.put("mVisitorSex", "女");
			}else{
				map2.put("mVisitorSex", "-");
			}
			
			map2.put("visitNumber", map2.get("visitNumber").toString());
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(list2), ArrayList.class);
		OutputStream out=null;
		List<String> colList=new ArrayList<String>();
		List<String> colTitleList=new ArrayList<String>();
		colTitleList.add("访客单号");
		colList.add("visitorNum");
		colTitleList.add("卡号");
		colList.add("visitorCard");
		colTitleList.add("访客姓名");
		colList.add("mVisitorName");
		colTitleList.add("性别");
		colList.add("mVisitorSex");
		colTitleList.add("证件类型");
		colList.add("mPaperType");
		colTitleList.add("证件号码");
		colList.add("mPaperNum");
		colTitleList.add("来访时间");
		colList.add("visitDate");
		colTitleList.add("离开时间");
		colList.add("leftDate");
		colTitleList.add("有效期限");
		colList.add("validityDate");
		colTitleList.add("来访状态");
		colList.add("recordIdentity");
		colTitleList.add("来访人数");
		colList.add("visitNumber");
		colTitleList.add("联系电话");
		colList.add("mMobile");
		colTitleList.add("来访事由");
		colList.add("visitotrMatter");
		colTitleList.add("来访单位");
		colList.add("mVisitorCompany");
		colTitleList.add("携带物品");
		colList.add("carryGoods");
		colTitleList.add("地址");
		colList.add("mAddress");
		colTitleList.add("车辆信息");
		colList.add("carInfo");
		String[] cols=new String[colList.size()];
		String[] colTitles=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
		}
		
			out=response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="来访记录表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook=ExcelUtilSpecial.exportExcel("来访记录表", colTitles,cols, list);
			workbook.write(out);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 被访记录查询(没分页) 
	 * by huixian.pan 
	 */
	public void exportPVisitRecords() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		HttpServletResponse response = getHttpServletResponse();
		try{
		List<Map> list2= visitRecordService.getPVisitRecordsNoLimit(map);
		for(Map map2:list2){
			if("0".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "正在访问");
			}else if("1".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "离开");
			}else if("2".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "预约");
			}else if("3".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "超时");
			}else if("4".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "超时后离开");
			}else if("5".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "取消预约");
			}else{
				map2.put("recordIdentity", "预约超时");
			}
			
			if(null == map2.get("mVisitorName") || "".equals(map2.get("mVisitorName"))){
				map2.put("mVisitorName", "-");
			}
			if(null == map2.get("roomNum") || "".equals(map2.get("roomNum"))){
				map2.put("roomNum", "-");
			}
			if(null == map2.get("pMobile") || "".equals(map2.get("pMobile"))){
				map2.put("pMobile", "-");
			}
			if(null == map2.get("leftDate") || "".equals(map2.get("leftDate"))){
				map2.put("leftDate", "-");
			}
			if("0".equals(map2.get("pVisitorSex"))){
				map2.put("pVisitorSex", "男");
			}else if("1".equals(map2.get("pVisitorSex"))){
				map2.put("pVisitorSex", "女");
			}else{
				map2.put("pVisitorSex", "-");
			}
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(list2), ArrayList.class);
		OutputStream out=null;
		List<String> colList=new ArrayList<String>();
		List<String> colTitleList=new ArrayList<String>();
		colTitleList.add("访客单号");
		colList.add("visitorNum");
		colTitleList.add("被访人姓名");
		colList.add("pVisitorName");
		colTitleList.add("访客姓名");
		colList.add("mVisitorName");
		colTitleList.add("性别");
		colList.add("pVisitorSex");
		colTitleList.add("组织机构");
		colList.add("pOrgName");
		colTitleList.add("房间号");
		colList.add("roomNum");
		colTitleList.add("联系电话");
		colList.add("pMobile");
		colTitleList.add("来访时间");
		colList.add("visitDate");
		colTitleList.add("离开时间");
		colList.add("leftDate");
		colTitleList.add("有效期限");
		colList.add("validityDate");
		colTitleList.add("来访状态");
		colList.add("recordIdentity");
		
		String[] cols=new String[colList.size()];
		String[] colTitles=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
		}
		
			out=response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="被访记录表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook=ExcelUtilSpecial.exportExcel("被访记录表", colTitles,cols, list);
			workbook.write(out);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 *  随访记录查询(没分页)  
	 *  by huixian.pan 
	 */
	public void exportFVisitRecords() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		HttpServletResponse response = getHttpServletResponse();
		try{
		List<Map> list2= visitRecordService.getFVisitRecordsNoLimit(map);
		for(Map map2:list2){
			if("0".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "正在访问");
			}else if("1".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "离开");
			}else if("2".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "预约");
			}else if("3".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "超时");
			}else if("4".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "超时后离开");
			}else if("5".equals(map2.get("recordIdentity"))){
				map2.put("recordIdentity", "取消预约");
			}else{
				map2.put("recordIdentity", "预约超时");
			}
			
			if(null == map2.get("mVisitorName") || "".equals(map2.get("mVisitorName"))){
				map2.put("mVisitorName", "-");
			}
			if(null == map2.get("fVisitorName") || "".equals(map2.get("fVisitorName"))){
				map2.put("fVisitorName", "-");
			}
			if(null == map2.get("fPaperType") || "".equals(map2.get("fPaperType"))){
				map2.put("fPaperType", "-");
			}
			if(null == map2.get("fPaperNum") || "".equals(map2.get("fPaperNum"))){
				map2.put("fPaperNum", "-");
			}
			if(null == map2.get("fMobile") || "".equals(map2.get("fMobile"))){
				map2.put("fMobile", "-");
			}
			if(null == map2.get("fAddress") || "".equals(map2.get("fAddress"))){
				map2.put("fAddress", "-");
			}
			if(null == map2.get("leftDate") || "".equals(map2.get("leftDate"))){
				map2.put("leftDate", "-");
			}
			if("0".equals(map2.get("fVisitorSex"))){
				map2.put("fVisitorSex", "男");
			}else if("1".equals(map2.get("fVisitorSex"))){
				map2.put("fVisitorSex", "女");
			}else{
				map2.put("fVisitorSex", "-");
			}
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(list2), ArrayList.class);
		OutputStream out=null;
		List<String> colList=new ArrayList<String>();
		List<String> colTitleList=new ArrayList<String>();
		colTitleList.add("访客单号");
		colList.add("visitorNum");
		colTitleList.add("随访人姓名");
		colList.add("fVisitorName");
		colTitleList.add("访客姓名");
		colList.add("mVisitorName");
		colTitleList.add("性别");
		colList.add("fVisitorSex");
		colTitleList.add("证件类型");
		colList.add("fPaperType");
		colTitleList.add("证件号码");
		colList.add("fPaperNum");
		colTitleList.add("联系电话");
		colList.add("fMobile");
		colTitleList.add("家庭地址");
		colList.add("fAddress");
		colTitleList.add("来访时间");
		colList.add("visitDate");
		colTitleList.add("离开时间");
		colList.add("leftDate");
		colTitleList.add("有效期限");
		colList.add("validityDate");
		colTitleList.add("来访状态");
		colList.add("recordIdentity");
		
		String[] cols=new String[colList.size()];
		String[] colTitles=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
		}
		
			out=response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="随访人员表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook=ExcelUtilSpecial.exportExcel("随访人员表", colTitles,cols, list);
			workbook.write(out);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 *  被访人员查询(没分页) 
	 *  by huixian.pan 
	 */
	public void exportPassivePeople() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data,HashMap.class);
		HttpServletResponse response = getHttpServletResponse();
		try{
		List<Map> list2= visitRecordService.getPassivePeopleNoLimit(map);
		for(Map map2:list2){
			if(null == map2.get("staffMobile") || "".equals(map2.get("staffMobile"))){
				map2.put("staffMobile", "-");
			}
			
			/*if("0".equals(map2.get("staffSex"))){
				map2.put("staffSex", "男");
			}else{
				map2.put("staffSex", "女");
			}*/
		}
		List list = GsonUtil.getListFromJson(GsonUtil.toJson(list2), ArrayList.class);
		OutputStream out=null;
		List<String> colList=new ArrayList<String>();
		List<String> colTitleList=new ArrayList<String>();
		
		colTitleList.add("被访人姓名");
		colList.add("staffName");
		colTitleList.add("组织机构");
		colList.add("deptName");
		colTitleList.add("联系电话");
		colList.add("staffMobile");
		/*colTitleList.add("性别");
		colList.add("staffSex");*/
		
		String[] cols=new String[colList.size()];
		String[] colTitles=new String[colList.size()];
		for(int i=0;i<colList.size();i++){
			cols[i]=colList.get(i);
			colTitles[i]=colTitleList.get(i);
		}
		
			out=response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName="被访人员表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook=ExcelUtilSpecial.exportExcel("被访人员表", colTitles,cols, list);
			workbook.write(out);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
