package com.kuangchi.sdd.baseConsole.count.action;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.count.model.CardHistoryModel;
import com.kuangchi.sdd.baseConsole.count.model.CardInfo;
import com.kuangchi.sdd.baseConsole.count.model.CountWarining;
import com.kuangchi.sdd.baseConsole.count.model.DepartmentInfo;
import com.kuangchi.sdd.baseConsole.count.model.KcardState;
import com.kuangchi.sdd.baseConsole.count.service.ICountService;
import com.kuangchi.sdd.baseConsole.count.util.ExcelUtilSpecialCount;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-4-11下午1:49:22
 * @功能描述:统计分析的Action
 * @参数描述:
 */

@Controller("countAction")
public class CountAction extends BaseActionSupport{

	@Override
	public Object getModel() {
		return null;
	}

	@Resource(name = "countServiceImpl")
	private ICountService countService;
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-13 上午9:14:27
	 * @功能描述:按条件查询卡片日志（分页） 
	 * @参数描述:
	 */
	public void getCardHistoryListByParam(){
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map<String, Object> map = GsonUtil.toBean(data, HashMap.class);
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		Integer skips = (Integer.parseInt(request.getParameter("page"))-1)*rows;
		map.put("rows", rows);
		map.put("skips", skips);
		
		Grid<CardHistoryModel> cardHistoryList = countService.getCardHistoryListByParam(map);
		printHttpServletResponse(GsonUtil.toJson(cardHistoryList));
	}
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-13 上午9:26:32
	 * @功能描述:获取卡片状态 
	 * @描述: update by chudan.guo 2016-12-13
	 */
	public void getCardHistoryOperate(){
		List<Map> listMap = countService.getCardHistoryOperate();
		List<Map> listMap2 = new ArrayList();
		for(Map map:listMap){
			//text和value都使用state_name,查询时可以根据state_name查询
			map.put("operate", map.get("state_name"));  
			map.put("state_name", map.get("state_name"));
			listMap2.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(listMap2));
	}
	
	/***
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-13 上午10:18:49
	 * @功能描述:按条件导出卡片日志 
	 * @参数描述:
	 */
	public void downloadCardHistoryByParam(){
		 HttpServletResponse response = getHttpServletResponse();
	        HttpServletRequest request = getHttpServletRequest();
	        String datas = request.getParameter("data");
	        Map<String,Object> map = GsonUtil.toBean(datas,HashMap.class);
	        List<CardHistoryModel> cardHistoryList = countService.getCardHistoryList(map);
	        
	        String jsonList= GsonUtil.toJson(cardHistoryList);
	        //list中的map为 ：    列键----列值的方式
	        List list =GsonUtil.getListFromJson(jsonList, ArrayList.class);
	        
	        LinkedTreeMap<String, Object> temp=null;
	        for (int i = 0; i < list.size(); i++) {
	        	temp =  (LinkedTreeMap) list.get(i);
	        	if(temp.get("dept_name")==null){
	        		temp.put("dept_name", "-");
	        	}
	        	if(temp.get("staff_no")==null){
	        		temp.put("staff_no", "-");
	        	}else{
	        		temp.put("staff_no", temp.get("staff_no").toString().replace(".0", ""));
	        	}
	        	if(temp.get("staff_name")==null){
	        		temp.put("staff_name", "-");
	        	}else{
	        		temp.put("staff_name", temp.get("staff_name").toString().replace(".0", ""));
	        	}
	        	
	        	if(temp.get("id")==null){
	        		temp.put("id", "-");
	        	}else{
	        		temp.put("id", temp.get("id").toString().replace(".0", ""));
	        	}
	        	
	        	if(temp.get("card_num")==null){
	        		temp.put("card_num", "-");
	        	}else{
	        		temp.put("card_num", temp.get("card_num").toString().replace(".0", ""));
	        	}
	        	
	        	temp.put("state_name", temp.get("state_name"));
	        	temp.put("operate_time", temp.get("operate_time"));
	        	list.set(i, temp);
			}
	        List<String> colList = new ArrayList<String>();
			List<String> colTitleList = new ArrayList<String>();
			colTitleList.add("部门名称");
			colTitleList.add("员工工号");
			colTitleList.add("员工名称");
			colTitleList.add("卡号");
			colTitleList.add("卡片状态");
			colTitleList.add("改变时间");
			colTitleList.add("操作员");
			
			colList.add("dept_name");
			colList.add("staff_no");
			colList.add("staff_name");
			colList.add("card_num");
			colList.add("state_name");
			colList.add("operate_time");
			colList.add("create_user");
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
				String fileName="卡片日志表.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				Workbook workbook = ExcelUtilSpecialCount.exportExcel("卡片日志表", colTitles, cols, list);
				workbook.write(out);
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	
	
	/**
	 *获取状态名称
	 */
	public void getAllStateNames() {
		List<KcardState> snames = countService.getAllCardStateNames();// 获取所有状态类型
		List<Map> list = new ArrayList<Map>();
		for (KcardState cardState : snames) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("TEXT", cardState.getState_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}
	
	/**
	 * 显示主页面
	 */
	public String viewCount(){
		return "viewCount";
	}
	
	/**
	 * 导出勾选的信息
	 */
	public void reportBound() {
		// 文件名获取
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String selectDatas = request.getParameter("select");

		List list = GsonUtil.getListFromJson(selectDatas, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("员工工号");
		colTitleList.add("员工姓名");
		colTitleList.add("卡号");
		colTitleList.add("状态名称");
		colTitleList.add("绑定时间");
		colTitleList.add("解绑时间");
		colTitleList.add("创建人员代码");
		colTitleList.add("创建时间");
		colTitleList.add("描述");

		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("card_num");
		colList.add("state_name");
		colList.add("bound_date");
		colList.add("unbound_date");
		colList.add("create_user");
		colList.add("create_time");
		colList.add("description");

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
			String fileName="补卡统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("补卡统计表",colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 已下发卡权限统计 
     */
    
    public void getCardAuthorityByName(){
    	HttpServletRequest request=getHttpServletRequest();
    	Integer page = Integer.parseInt(request.getParameter("page"));
    	Integer rows = Integer.parseInt(request.getParameter("rows"));
    	Integer skip = (page - 1) * rows;
		String data= getHttpServletRequest().getParameter("data");
		Map map=GsonUtil.toBean(data,HashMap.class);
		map.put("skip", skip+"");
		map.put("rows", rows+"");
		Grid<Map> grid=countService.getPeopleAuthorityInfoByStaffNumForCount(map);
		printHttpServletResponse(new Gson().toJson(grid));
    }
    
    /**
     * 权限统计页面
     */
    public String cardAuthorityView(){
    	return "cardAuthorityView";
    }
    
    /**
     * 已下发卡权限统计（导出勾选信息）
     */
	public void reportCardAuthority() {
		// 文件名获取
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String selectDatas = request.getParameter("select");

		List list = GsonUtil.getListFromJson(selectDatas, ArrayList.class);
		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("卡号");
		colTitleList.add("设备名称");
		colTitleList.add("门名称");
		colTitleList.add("有效时间起");
		colTitleList.add("有效时间止");
		colTitleList.add("时段组");
		colTitleList.add("操作员");
		colTitleList.add("创建时间");

		colList.add("staff_no");
		colList.add("staff_name");
		colList.add("card_num");
		colList.add("device_name");
		colList.add("door_name");
		colList.add("valid_start_time");
		colList.add("valid_end_time");
		colList.add("time_group_name");
		colList.add("create_user");
		colList.add("create_time");

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
			String fileName="已下发卡权限统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("已下发卡权限统计表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 导出已下发卡权限统计信息（查询条件下）
     */
	public void exportCardAuthorityByName() {

		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String data= getHttpServletRequest().getParameter("data");
		Map map=GsonUtil.toBean(data,HashMap.class);
		List<Map> listForExcel=countService.getPeopleAuthorityInfoByStaffNumForExcel(map);
		String jsonList = GsonUtil.toJson(listForExcel);
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);

		// 设置列表头和列数据键
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();
		colTitleList.add("员工工号");
		colTitleList.add("员工名称");
		colTitleList.add("部门名称");
		colTitleList.add("卡号");
		colTitleList.add("设备本地IP地址");
		colTitleList.add("设备名称");
		colTitleList.add("门名称");
		colTitleList.add("时段组");
		colTitleList.add("有效期开始时间");
		colTitleList.add("有效期结束时间");
		colTitleList.add("操作员");
		colTitleList.add("创建时间");

		colList.add("staffNo");
		colList.add("staffName");
		colList.add("bmMc");
		colList.add("cardNum");
		colList.add("device_ip");
		colList.add("deviceName");
		colList.add("doorName");
		colList.add("groupName");
		colList.add("validStartTime");
		colList.add("validEndTime");
		colList.add("createUser");
		colList.add("createTime");

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
			String fileName="已下发卡权限统计表.xls";
			response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecialCount.exportExcel("已下发卡权限统计表", colTitles, cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 /**报警统计页面显示 */
	  public String countWariningInfoView(){
	    	return "countWariningInfoView";
	    }
	  
	  /**操作日志页面显示 */
	  public String showlogManager(){
		  return "showlogManager";
	  }
	
	  /**组织信息页面显示 */
	  public String showDepartmentInfo(){
		  return "showDepartmentInfo";
	  }
	  
	  /**卡片信息页面显示 */
	  public String showCountCardInfo(){
		  return "showCountCardInfo";
	  }
	  
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29 
	 * @功能描述: 报警事件统计-Action
	 */
	public void getCountWariningInfo(){
		HttpServletRequest request = getHttpServletRequest();
		Grid<CountWarining> grid = new Grid<CountWarining>();
		String beanObject = request.getParameter("data");
		Map map = GsonUtil.toBean(beanObject, HashMap.class);
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer rows = Integer.parseInt(request.getParameter("rows"));
		map.put("page", (page - 1) * rows);
		map.put("rows", rows);
		grid = countService.getCountWariningInfo(map);
		printHttpServletResponse(GsonUtil.toJson(grid));
	}
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-9-29
	 * @功能描述: 导出报警事件信息-Action
	 */
		public void exportCountWariningInfo() {
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("exportData");
			Map map = GsonUtil.toBean(beanObject, HashMap.class);
			List<CountWarining> countWarining=countService.exportCountWariningInfo(map);
			String jsonList = GsonUtil.toJson(countWarining);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						//colTitleList.add("卡号");
						colTitleList.add("报警时间");
						colTitleList.add("设备MAC地址");
						colTitleList.add("设备IP地址");
						colTitleList.add("设备名称");
						colTitleList.add("门名称");
						colTitleList.add("事件名称");
						
						//colList.add("card_num");
						colList.add("time");
						colList.add("device_mac");
						colList.add("local_ip_address");
						colList.add("device_name");
						colList.add("door_name");
						colList.add("od_type");
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
							String fileName="报警统计表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("报警统计表", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
		/**
		 * @创建人　: chudan.guo
		 * @创建时间: 2016-10-25 
		 * @功能描述: 组织信息统计-Action
		 */
		public void getCountDepartmentInfo(){
			HttpServletRequest request = getHttpServletRequest();
			Grid<DepartmentInfo> grid = new Grid<DepartmentInfo>();
			String beanObject = request.getParameter("data");
			Map map = GsonUtil.toBean(beanObject, HashMap.class);
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			map.put("page", (page - 1) * rows);
			map.put("rows", rows);
			grid = countService.getCountDepartmentInfo(map);
			printHttpServletResponse(GsonUtil.toJson(grid));
		}
		/**
		 * @创建人　: chudan.guo
		 * @创建时间: 2016-10-25
		 * @功能描述: 导出组织信息-Action
		 */
			public void exportCountDepartmentInfo() {
				HttpServletResponse response = getHttpServletResponse();
				HttpServletRequest request = getHttpServletRequest();
				String beanObject = request.getParameter("exportData");
				Map map = GsonUtil.toBean(beanObject, HashMap.class);
				List<DepartmentInfo> departmentInfo=countService.exportCountDepartmentInfo(map);
				String jsonList = GsonUtil.toJson(departmentInfo);
				List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
				// 设置列表头和列数据键
							List<String> colList = new ArrayList<String>();
							List<String> colTitleList = new ArrayList<String>();
							colTitleList.add("部门编号");
							colTitleList.add("部门名称");
							colTitleList.add("描述");
							
							colList.add("bm_no");
							colList.add("bm_mc");
							colList.add("bz");
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
								String fileName="部门信息表.xls";
								response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
								Workbook workbook = ExcelUtilSpecialCount.exportExcel("部门信息表", colTitles, cols, list);
								workbook.write(out);
								out.flush();
							} catch (Exception e) {
								e.printStackTrace();
							}
			}
			/**
			 * @创建人　: chudan.guo
			 * @创建时间: 2016-10-25 
			 * @功能描述: 卡片信息统计-Action
			 */
			public void getCountCardInfo(){
				HttpServletRequest request = getHttpServletRequest();
				Grid<CardInfo> grid = new Grid<CardInfo>();
				String beanObject = request.getParameter("data");
				Map map = GsonUtil.toBean(beanObject, HashMap.class);
				Integer page = Integer.parseInt(request.getParameter("page"));
				Integer rows = Integer.parseInt(request.getParameter("rows"));
				map.put("page", (page - 1) * rows);
				map.put("rows", rows);
				grid = countService.getCountCardInfo(map);
				printHttpServletResponse(GsonUtil.toJson(grid));
			}
			/**
			 * @创建人　: chudan.guo
			 * @创建时间: 2016-10-25
			 * @功能描述: 导出卡片信息-Action
			 */
		public void exportCountCardInfo() {
			HttpServletResponse response = getHttpServletResponse();
			HttpServletRequest request = getHttpServletRequest();
			String beanObject = request.getParameter("exportData");
			Map map = GsonUtil.toBean(beanObject, HashMap.class);
			List<CardInfo> cardInfo=countService.exportCountCardInfo(map);
			String jsonList = GsonUtil.toJson(cardInfo);
			List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
			// 设置列表头和列数据键
						List<String> colList = new ArrayList<String>();
						List<String> colTitleList = new ArrayList<String>();
						colTitleList.add("员工工号");
						colTitleList.add("员工名称");
						colTitleList.add("卡号");
						colTitleList.add("卡类型");
						colTitleList.add("卡片状态");
						colTitleList.add("卡有效期");
						colTitleList.add("创建时间");
						
						colList.add("staff_no");
						colList.add("staff_name");
						colList.add("card_num");
						colList.add("type_name");
						colList.add("state_name");
						colList.add("card_validity");
						colList.add("create_time");
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
							String fileName="卡片信息表.xls";
							response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
							Workbook workbook = ExcelUtilSpecialCount.exportExcel("卡片信息表", colTitles, cols, list);
							workbook.write(out);
							out.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
	
	
}
