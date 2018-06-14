package com.kuangchi.sdd.baseConsole.list.whitelist.action;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.list.whitelist.model.WhiteList;
import com.kuangchi.sdd.baseConsole.list.whitelist.service.WhiteListService;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;
import com.kuangchi.sdd.baseConsole.holiday.util.ExcelUtilSpecial;

@Controller("whiteListAction")
public class WhiteListAction extends BaseActionSupport{
	
	 
	   private WhiteList model;
	
	   @Resource(name = "employeeService")
	   EmployeeService employeeService;
	   
	   @Resource(name = "whiteListServiceImpl")
	   WhiteListService whiteListService;
	   
	   
	   @Override
		public Object getModel() {
			return model;
		}
	   
	   
	   /**
	    * @创建人　: 邓积辉
	    * @创建时间: 2016-4-7 下午8:20:23
	    * @功能描述: 根据员工代码查询员工信息
	    * @参数描述:
	    */
	  public void getStaffNameByParam(){
		    HttpServletRequest request = getHttpServletRequest();
		    String staff_num = request.getParameter("data");
		    
		    List<String> ls = whiteListService.getStaffNameByParam(staff_num);
		    for(String yhMc :ls){
			printHttpServletResponse(GsonUtil.toJson(yhMc));
		   }
	  }
	  /**
	   * @创建人　: 邓积辉
	   * @创建时间: 2016-4-8 下午4:41:46
	   * @功能描述: 根据条件搜索员工信息（不包含黑名单人员编号）
	   * @参数描述:
	   */
	   public void searchWhiteListPage() {
	        HttpServletRequest request = getHttpServletRequest();
	        String beanObject = request.getParameter("data");
	        DepartmentPage departmentPage = GsonUtil.toBean(beanObject, DepartmentPage.class);    //将数据转化为javabean
	        departmentPage.setPage(Integer.parseInt(request.getParameter("page")));
	        departmentPage.setRows(Integer.parseInt(request.getParameter("rows")));
	        Grid<WhiteList> whiteListGrid = whiteListService.searchWhiteListPage(departmentPage);
	        printHttpServletResponse(GsonUtil.toJson(whiteListGrid));
	    }
	  
	   
	    /**
	     * @创建人　: 邓积辉
	     * @创建时间: 2016-4-10 下午2:35:44
	     * @功能描述: 导出查询条件下的白名单信息
	     * @参数描述:
	     */
	    public void downloadWhiteListByParam() {   
	    	        HttpServletResponse response = getHttpServletResponse();
	    	        HttpServletRequest request = getHttpServletRequest();
	    	        String beanObject = request.getParameter("data");
	    	        DepartmentPage departmentPage = GsonUtil.toBean(beanObject, DepartmentPage.class); 
	    	        List<WhiteList> whiteList=whiteListService.searchWhiteList(departmentPage);
	    	        String jsonList= GsonUtil.toJson(whiteList);
	    	        //list中的map为 ：    列键----列值的方式
	    	        List list =GsonUtil.getListFromJson(jsonList, ArrayList.class);
	    	        OutputStream out = null;
	    	        
	    	        Field[] fields=Employee.class.getDeclaredFields();
	    	        List<String> colList=new ArrayList<String>();
	    	        List<String> colTitleList =new ArrayList<String>();
	    	        
	    	        for (int i = 0; i < fields.length; i++) {
	    	        	String fieldName=fields[i].getName();
						if("yhNo".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("员工工号");
						}else if("yhMc".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("员工名称");
						}else if("nl".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("年龄");
						}else if("xbMc".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("性别");
						}else if("ygzz".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("员工地址");
						}else if("zjmc".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("证件类型");
						}else if("sfzhm".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("证件号码");
						}else if("sjhm".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("手机号码");
						}else if("gddh".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("固定电话");
						}else if("dzyj".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("电子邮箱");
						}else if("bmNo".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("部门编号");
						}else if("bmMc".equals(fieldName)){
							colList.add(fieldName);
							colTitleList.add("部门名称");
						}else if("sjldyhNo".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("上级领导工号");
						}else if("sjldyhMc".equals(fieldName)){
							colList.add(fieldName);
							colTitleList.add("上级领导名称");
						}else if("rzsj".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("入职时间");
						}else if("gwmc".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("岗位名称");
						}else if("bz".equals(fieldName)) {
							colList.add(fieldName);
							colTitleList.add("备注");
						}
					}   
	    	        //列数据键
	    	        String[] cols=new String[colList.size()];
	    	        //列表题
	    	        String[] colTitles=new String[colList.size()];    	        
	    	       for (int i = 0; i < colList.size(); i++) {
					cols[i]=colList.get(i);
					colTitles[i]=colTitleList.get(i);
				}
	    	        try {
	    	            out = response.getOutputStream();
	    	            response.setContentType("application/x-msexcel");
	    	            String fileName="白名单信息表.xls";
	    	            response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
	    	            Workbook workbook = ExcelUtilSpecial.exportExcel("白名单表",colTitles, cols, list);
	    	            workbook.write(out);
	    	            out.flush();
	    	        } catch (Exception e) {
	    	            e.printStackTrace();
	    	        }
	    	    }

	  
	}


