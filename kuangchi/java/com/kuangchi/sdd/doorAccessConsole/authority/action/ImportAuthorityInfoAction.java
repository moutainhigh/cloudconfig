package com.kuangchi.sdd.doorAccessConsole.authority.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;

import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.exception.ImportException;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByCardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByDoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByStaffModel;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.doorAccessConsole.authority.util.ExcelExportDoorTemplate;
import com.kuangchi.sdd.doorAccessConsole.authority.util.ExcelExportCardTemplate;
import com.kuangchi.sdd.doorAccessConsole.authority.util.ExcelExportStaffTemplate;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * 按人、卡、门导入权限
 *
 */
@Controller("importAuthorityInfoAction")
public class ImportAuthorityInfoAction extends BaseActionSupport  {

	private static final long serialVersionUID = 1L;

	@Resource(name = "peopleAuthorityService")
	PeopleAuthorityInfoService peopleAuthorityService;
	@Resource(name = "employeeService")
	EmployeeService employeeService;
	
	@Resource(name = "deviceService")
	DeviceService deviceService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	private File importAuthorityFile;	
	public File getImportAuthorityFile() {
		return importAuthorityFile;
	}
	public void setImportAuthorityFile(File importAuthorityFile) {
		this.importAuthorityFile = importAuthorityFile;
	}
	FileInputStream fis = null;
	
	//选择类型下载导入权限模板 weixuan.lu 2016/4/14
		public void selectDownloadUploadAuthority(){
			HttpServletRequest request = getHttpServletRequest();
			String sel = request.getParameter("authority");
				if("卡".equals(sel)){
					downloadImportAuthorityByCardTemplate();
				}else if("人".equals(sel)){
					downloadImportAuthorityByStaffTemplate();
				}else if("门".equals(sel)){
					downloadImportAuthorityByDoorTemplate();
				}
			} 
	
		/*按卡片下载导入模版  by jihui.deng*/
		public void downloadImportAuthorityByCardTemplate(){
			   HttpServletResponse response = getHttpServletResponse();
			   OutputStream out = null;
			   ExcelExportCardTemplate excelExport = new ExcelExportCardTemplate();
			   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
			   List<String> list = deviceService.getAllTimeGroups();//获取全部排班类型
			   List<String> doorList = deviceService.getAllDoorNum();//获取全部门号
			   List<String> deviceList = deviceService.getAllDeviceNum();//获取全部设备编号
			   TitleRowCell t1t = new TitleRowCell("卡号",true);
			   TitleRowCell t2t = new TitleRowCell("门名称",true);
			   for(String doorNum:doorList){
				   t2t.addSuggest(doorNum);
			   }
			   
			   TitleRowCell t3t = new TitleRowCell("设备名称",true);
			   for(String deviceNum:deviceList){
				   t3t.addSuggest(deviceNum);
			   }
			   TitleRowCell t4t = new TitleRowCell("有效开始时间",true);
			   TitleRowCell t5t = new TitleRowCell("有效结束时间",true);
			   TitleRowCell t6t = new TitleRowCell("时段组名称",false);
			   for(String time_group:list){
				   t6t.addSuggest(time_group);
			   }
			   
			   titleRowCells.add(t1t);
			   titleRowCells.add(t2t);
			   titleRowCells.add(t3t);
			   titleRowCells.add(t4t);
			   titleRowCells.add(t5t);
			   titleRowCells.add(t6t);
			   excelExport.createLongTitleRow(titleRowCells);
			   try {
					out = response.getOutputStream();
					response.setContentType("application/x-msexcel");
					String fileName="按卡添加权限模版 .xls";
					response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
					excelExport.getWorkbook().write(out);
			        out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	
		/*按员工下载导入模版  by jihui.deng*/
		public void downloadImportAuthorityByStaffTemplate(){
			   HttpServletResponse response = getHttpServletResponse();
			   OutputStream out = null;
			   ExcelExportStaffTemplate excelExport = new ExcelExportStaffTemplate();
			   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
			   List<String> list = deviceService.getAllTimeGroups();//获取全部排班类型
			   List<String> doorList = deviceService.getAllDoorNum();//获取全部门号
			   List<String> deviceList = deviceService.getAllDeviceNum();//获取全部设备编号
			   TitleRowCell t1t = new TitleRowCell("员工工号",true);
			   TitleRowCell t2t = new TitleRowCell("门名称",true);
			   for(String doorNum:doorList){
				   t2t.addSuggest(doorNum);
			   }
			   TitleRowCell t3t = new TitleRowCell("设备名称",true);
			   
			   for(String deviceNum:deviceList){
				   t3t.addSuggest(deviceNum);
			   }
			   TitleRowCell t4t = new TitleRowCell("有效开始时间",true);
			   TitleRowCell t5t = new TitleRowCell("有效结束时间",true);
			   TitleRowCell t6t = new TitleRowCell("时段组名称",false);
			   for(String time_group:list){
				   t6t.addSuggest(time_group);
			   }
			   titleRowCells.add(t1t);
			   titleRowCells.add(t2t);
			   titleRowCells.add(t3t);
			   titleRowCells.add(t4t);
			   titleRowCells.add(t5t);
			   titleRowCells.add(t6t);
			   excelExport.createLongTitleRow(titleRowCells);
			   try {
					out = response.getOutputStream();
					response.setContentType("application/x-msexcel");
					String fileName="按员工添加权限模版 .xls";
					response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
					excelExport.getWorkbook().write(out);
			        out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		
		
		/*按门下载导入模版  by jihui.deng*/
		public void downloadImportAuthorityByDoorTemplate(){
			   HttpServletResponse response = getHttpServletResponse();
			   OutputStream out = null;
			   ExcelExportDoorTemplate excelExport = new ExcelExportDoorTemplate();
			   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
			   List<String> list = deviceService.getAllTimeGroups();//获取全部排班类型
			   List<String> doorList = deviceService.getAllDoorNum();//获取全部门号
			   List<String> deviceList = deviceService.getAllDeviceNum();//获取全部设备编号
			   TitleRowCell t1t = new TitleRowCell("门名称",true);
			   for(String doorNum:doorList){
				   t1t.addSuggest(doorNum);
			   }
			   TitleRowCell t2t = new TitleRowCell("设备名称",true);
			   for(String deviceNum:deviceList){
				   t2t.addSuggest(deviceNum);
			   }
			   TitleRowCell t3t = new TitleRowCell("有效开始时间",true);
			   TitleRowCell t4t = new TitleRowCell("有效结束时间",true);
			   TitleRowCell t5t = new TitleRowCell("时段组名称",false);
			   for(String time_group:list){
				   t5t.addSuggest(time_group);
			   }
			   titleRowCells.add(t1t);
			   titleRowCells.add(t2t);
			   titleRowCells.add(t3t);
			   titleRowCells.add(t4t);
			   titleRowCells.add(t5t);
			   excelExport.createLongTitleRow(titleRowCells);
			   try {
					out = response.getOutputStream();
					response.setContentType("application/x-msexcel");
					String fileName="按门添加权限模版 .xls";
					response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
					excelExport.getWorkbook().write(out);
			        out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		

		/**
		 * 选择方式批量导入
		 * @author minting.he
		 */
		public String selectAuthority() throws ImportException{
			HttpServletRequest request = getHttpServletRequest();
			String sel = request.getParameter("authority");
			if(EmptyUtil.atLeastOneIsEmpty(sel)){
				throw new ImportException("请选择导入文件的类型!");	
			}else{
				FileInputStream fis = null;
				try {
					if(importAuthorityFile == null){
						throw new ImportException("请选择需要上传的文件!");	
					}else {
						fis = new FileInputStream(importAuthorityFile);
						HSSFWorkbook workbook = new HSSFWorkbook(fis);
						HSSFSheet sheet = workbook.getSheetAt(0);
						HSSFRow row=sheet.getRow(0);
						int totalRow = sheet.getLastRowNum();
						Cell titleCell= row.getCell(0);
						if("按卡添加权限表".equals(titleCell.getStringCellValue().trim()) && "card".equals(sel)){
							if(totalRow > 6 || totalRow == 6){
								importAuthorityByCard(request, sheet);
							}else{
								throw new ImportException("导入权限失败，导入内容为空!");
							}
						}else if("按员工添加权限表".equals(titleCell.getStringCellValue().trim()) && "staff".equals(sel)){
							if(totalRow > 6 || totalRow ==6){
								importAuthorityByStaff(request, sheet);
							}else{
								throw new ImportException("导入权限失败，导入内容为空!");
							}
						}else if("按门添加权限表".equals(titleCell.getStringCellValue().trim()) && "door".equals(sel)){
							if(totalRow > 5 || totalRow ==5){
								importAuthorityByDoor(request, sheet);
							}else{
								throw new ImportException("导入权限失败，导入内容为空!");
							}
						}else {
							throw new ImportException("导入权限失败，请检查模板或选择项是否正确!");
						}
					} 
				} catch (ImportException e2){
					request.setAttribute("message", e2.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("message", "导入权限失败！");
				} finally {
					close(request, fis);
					importAuthorityFile = null;
				}
			}
			request.setAttribute("authority", sel);
			return "success";
		}
		
		/**
		 * 按卡批量导入权限
		 * @author minting.he
		 */	
		public void importAuthorityByCard(HttpServletRequest request, HSSFSheet sheet){
			try {
				int totalRow = sheet.getLastRowNum();
				List<AuthorityByCardModel> list = new ArrayList<AuthorityByCardModel>();
				for (int i=6; i<=totalRow; i++) {
	        		HSSFRow row=sheet.getRow(i);
	        		AuthorityByCardModel authority=new AuthorityByCardModel();
	           		String cardNum = null;// 卡号， 必填        
	        		String doorNum = null;// 门号，必填
					String doorNumNames=null;
					String doorNums[]=null;
	        		String deviceNum = null;//设备编号
	        		String deviceNumNames = null;
					String deviceNums[] = null;
	        		String validStartTime = null;//有效开始时间
	        		String validEndTime = null;//有效结束时间
	        		String timeGroupName = null;//时段组名称
	        		
	        		//卡号
	        		Cell cardNumCell= row.getCell(0);
	        		if (null != cardNumCell) {
	        			cardNumCell.setCellType(Cell.CELL_TYPE_STRING);
	        			cardNum = cardNumCell.getStringCellValue().trim();
						if (null == cardNum||"".equals(cardNum)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“卡号”是否为空！");
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“卡号”是否为空！");
					}
	        		
	        		//门号
	        		Cell doorNumCell= row.getCell(1);
	        		if (null != doorNumCell) {
	        			 doorNumCell.setCellType(Cell.CELL_TYPE_STRING);
	        			 doorNumNames=doorNumCell.getStringCellValue().trim();
	        			//doorNum = doorNumCell.getStringCellValue().trim();
	        			if (null == doorNumNames||"".equals(doorNumNames)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“门名称”是否为空！");
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“门名称”是否为空！");
					}
	        		 doorNums =  doorNumNames.split(" :");
	                 doorNum = doorNums[1];
	        		
	        		//设备名称
	        		Cell deviceNumCell= row.getCell(2);
	        		if (null != deviceNumCell) {
	        			deviceNumCell.setCellType(Cell.CELL_TYPE_STRING);
	        			deviceNumNames = deviceNumCell.getStringCellValue().trim();
	        			if (null == deviceNumNames||"".equals(deviceNumNames)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空！");
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空！");
					}
	        		 deviceNums = deviceNumNames.split(" :");
	        		 
                     deviceNum = deviceService.getDeviceNumByDeviceMac(deviceNums[1]);
	        		
                     //有效开始时间
	        		Cell validStartTimeCell= row.getCell(3);  
	        		if (null != validStartTimeCell) {
	        			/*SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	           			Date dt = HSSFDateUtil.getJavaDate(validStartTimeCell.getNumericCellValue());//获取成DATE类型     
	           			validStartTime = dateformat.format(dt);*/
	        			validStartTime = validStartTimeCell.getStringCellValue().trim();
	        			if (null == validStartTime||"".equals(validStartTime)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否为空！");
						}else{
							if(!validStartTime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
	        					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”格式是否正确！");
	        				}
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否为空！");
					}
	        		
	        		//有效结束时间
	        		Cell validEndTimeCell= row.getCell(4);  
	        		if (null != validEndTimeCell) {
	        			/*SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	           			Date dt = HSSFDateUtil.getJavaDate(validEndTimeCell.getNumericCellValue());//获取成DATE类型     
	           			validEndTime = dateformat.format(dt);*/
	        			validEndTime = validEndTimeCell.getStringCellValue().trim();
	        			if (null == validEndTime||"".equals(validEndTime)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”是否为空！");
						}else {
							if(!validEndTime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
	        					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”格式是否正确！");
							}
    						if(validStartTime.compareTo(validEndTime)>0){
        						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否大于“有效结束时间”！");
    						}
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”是否为空！");
					}
	        		
	        		//用户时段组
	        		Cell timeGroupNameCell= row.getCell(5);  
	        		if (null != timeGroupNameCell) {
	        			timeGroupNameCell.setCellType(Cell.CELL_TYPE_STRING);
	        			timeGroupName = timeGroupNameCell.getStringCellValue().trim();
					}
	        		authority.setCardNum(cardNum);
	        		authority.setDoorNum(doorNum);
	        		authority.setDeviceNum(deviceNum);
	        		authority.setValidStartTime(validStartTime);
	        		authority.setValidEndTime(validEndTime);
	        		authority.setTimeGroupName(timeGroupName);
	        		authority.setTaskState("00");
	        		list.add(authority);	
	        		
				}  
				User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
				peopleAuthorityService.batchAddAuthorityByCard(list, loginUser.getYhMc());    			
	        	request.setAttribute("message", "导入权限成功!");
	        	
			} catch (ImportException e2) {
				request.setAttribute("message", e2.getMessage());
			} catch (Exception e) {
				request.setAttribute("message", "导入权限失败，请检查导入数据是否正确！");
			}
		}

		/**
		 * 按员工批量导入
		 * @author minting.he
		 */
		public void importAuthorityByStaff(HttpServletRequest request, HSSFSheet sheet){
			try {
				int totalRow = sheet.getLastRowNum();
				List<AuthorityByStaffModel> list = new ArrayList<AuthorityByStaffModel>();
				for (int i=6; i<=totalRow; i++) {
	        		HSSFRow row=sheet.getRow(i);
	        		AuthorityByStaffModel authority=new AuthorityByStaffModel();
	           		String staffNo = null;// 员工编号， 必填        
	        		String doorNum=null;// 门号，必填    
	        		String doorNumNames=null;
					String doorNums[]=null;
	        		String deviceNum = null;
	        		String deviceNumNames = null;
					String deviceNums[] = null;
	        		String validStartTime = null;//有效开始时间
	        		String validEndTime = null;//有效结束时间
	        		String timeGroupName = null;//时段组名称
	        		
	        		//拿到员工编号和门号的数据
	        		Cell staffNoCell= row.getCell(0);
	        		if (null != staffNoCell) {
	        			staffNoCell.setCellType(Cell.CELL_TYPE_STRING);
	        			staffNo = staffNoCell.getStringCellValue().trim();
						if (null == staffNo||"".equals(staffNo)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“员工工号”是否为空！");
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“员工工号”是否为空！");
					}   
	        		
	        		Cell doorNumCell= row.getCell(1);
	        		if (null != doorNumCell) {
	        			 doorNumCell.setCellType(Cell.CELL_TYPE_STRING);
	        			 doorNumNames=doorNumCell.getStringCellValue().trim();
	        			//doorNum = doorNumCell.getStringCellValue().trim();
	        			if (null == doorNumNames||"".equals(doorNumNames)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“门名称”是否为空！");
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“门名称”是否为空！");
					}
	        		 doorNums =  doorNumNames.split(" :");
	                 doorNum = doorNums[1];
	                 
	        		Cell deviceNumCell= row.getCell(2);
	        		if (null != deviceNumCell) {
	        			deviceNumCell.setCellType(Cell.CELL_TYPE_STRING);
	        			deviceNumNames = deviceNumCell.getStringCellValue().trim();
	        			if (null == deviceNumNames||"".equals(deviceNumNames)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空！");
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空！");
					}
	        		 deviceNums = deviceNumNames.split(" :");
	        		 
                     deviceNum = deviceService.getDeviceNumByDeviceMac(deviceNums[1]);
	        		
	        		
	        		Cell validStartTimeCell= row.getCell(3);  
	        		if (null != validStartTimeCell) {
	        		/*	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	           			Date dt = HSSFDateUtil.getJavaDate(validStartTimeCell.getNumericCellValue());//获取成DATE类型     
	           			validStartTime = dateformat.format(dt);*/
	        			validStartTime = validStartTimeCell.getStringCellValue().trim();
	        			if (null == validStartTime||"".equals(validStartTime)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否为空！");
						}else{
							if(!validStartTime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
	        					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”格式是否正确！");
	        				}
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否为空！");
					}
	        		Cell validEndTimeCell= row.getCell(4);  
	        		if (null != validEndTimeCell) {
	        			/*SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
	           			Date dt = HSSFDateUtil.getJavaDate(validEndTimeCell.getNumericCellValue());//获取成DATE类型     
	           			validEndTime = dateformat.format(dt);*/
	        			validEndTime = validEndTimeCell.getStringCellValue().trim();
	        			if (null == validEndTime||"".equals(validEndTime)) {
							throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”是否为空！");
						}else {
							if(!validEndTime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
	        					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”格式是否正确！");
							}
    						if(validStartTime.compareTo(validEndTime)>0){
        						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否大于“有效结束时间”！");
    						}
						}
					}else{
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”是否为空！");
					}
	        		Cell timeGroupNameCell= row.getCell(5);
	        		if (null != timeGroupNameCell) {
	        			timeGroupNameCell.setCellType(Cell.CELL_TYPE_STRING);
	        			timeGroupName = timeGroupNameCell.getStringCellValue().trim();
					}   
	        		authority.setStaffNo(staffNo);
	        		String staffNum = employeeService.selectStaffNum(staffNo);
	        		authority.setStaffNum(staffNum);
	        		authority.setDoorNum(doorNum);
	        		authority.setDeviceNum(deviceNum);
	        		authority.setValidStartTime(validStartTime+":00");
	        		authority.setValidEndTime(validEndTime+":00");
	        		authority.setTimeGroupName(timeGroupName);
	        		authority.setTaskState("00");
	        		list.add(authority);		
				}  	
				User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
				peopleAuthorityService.batchAddAuthorityByStaff(list, loginUser.getYhMc());   			
	        	request.setAttribute("message", "导入权限成功!");
			} catch (ImportException e2) {
				request.setAttribute("message", e2.getMessage());
			} catch (Exception e) {
				request.setAttribute("message", "导入权限失败，请检查导入数据是否正确！");
			}
		}
		
		/**
		 * 按门批量导入
		 * @author minting.he
		 */
		public void importAuthorityByDoor(HttpServletRequest request, HSSFSheet sheet){
		try {
			int totalRow = sheet.getLastRowNum();
			List<AuthorityByDoorModel> list = new ArrayList<AuthorityByDoorModel>();			
			for (int i=5; i<=totalRow; i++) {
        		HSSFRow row=sheet.getRow(i);
        		AuthorityByDoorModel authority=new AuthorityByDoorModel();
        		String doorNum=null;// 门号，必填    
        		String doorNumNames=null;
				String doorNums[]=null;
        		String deviceNum = null;// 设备编号， 必填
        		String deviceNumNames = null;
				 String deviceNums[] = null;
        		String validStartTime = null;//有效开始时间
        		String validEndTime = null;//有效结束时间
        		String timeGroupName = null;//时段组名称
        		
        		Cell doorNumCell= row.getCell(0);
        		if (null != doorNumCell) {
        			doorNumCell.setCellType(Cell.CELL_TYPE_STRING);
        			 doorNumNames=doorNumCell.getStringCellValue().trim();
        			//doorNum = doorNumCell.getStringCellValue().trim();
        			if (null == doorNumNames||"".equals(doorNumNames)) {
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“门名称”是否为空！");
					}
				}else{
					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“门名称”是否为空！");
				}
        		 doorNums =  doorNumNames.split(" :");
                 doorNum = doorNums[1];
                 
        		Cell deviceNumCell= row.getCell(1);
        		if (null != deviceNumCell) {
        			deviceNumCell.setCellType(Cell.CELL_TYPE_STRING);
        			deviceNumNames = deviceNumCell.getStringCellValue().trim();
        			if (null == deviceNumNames||"".equals(deviceNumNames)) {
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空！");
					}
				}else{
					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空！");
				}
        		 deviceNums = deviceNumNames.split(" :");
        		 
                 deviceNum = deviceService.getDeviceNumByDeviceMac(deviceNums[1]);
        		
        		Cell validStartTimeCell= row.getCell(2);  
        		if (null != validStartTimeCell) {
        			/*SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
           			Date dt = HSSFDateUtil.getJavaDate(validStartTimeCell.getNumericCellValue());//获取成DATE类型     
           			validStartTime = dateformat.format(dt);*/
        			validStartTime = validStartTimeCell.getStringCellValue().trim();
        			if (null == validStartTime||"".equals(validStartTime)) {
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否为空！");
					}else{
						if(!validStartTime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
        					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”格式是否正确！");
        				}
					}
				}else{
					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否为空！");
				}
        		Cell validEndTimeCell= row.getCell(3);  
        		if (null != validEndTimeCell) {
        			/*SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
           			Date dt = HSSFDateUtil.getJavaDate(validEndTimeCell.getNumericCellValue());//获取成DATE类型     
           			validEndTime = dateformat.format(dt);*/
        			validEndTime = validEndTimeCell.getStringCellValue().trim();
        			if (null == validEndTime||"".equals(validEndTime)) {
						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”是否为空！");
					}else {
						if(!validEndTime.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))([ \t\n\f\r])(([0-1]{1}[0-9]{1})|([2]{1}[0-3]{1}))([:])(([0-5]{1}[0-9]{1}))$")){
        					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”格式是否正确！");
						}
						if(validStartTime.compareTo(validEndTime)>0){
    						throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效开始时间”是否大于“有效结束时间”！");
						}
					}
				}else{
					throw new ImportException("导入权限失败，请检查第"+(i+1)+"行的“有效结束时间”是否为空！");
				}
        		Cell timeGroupNameCell = row.getCell(4);
        		if (null != timeGroupNameCell) {
        			timeGroupNameCell.setCellType(Cell.CELL_TYPE_STRING);
        			timeGroupName = timeGroupNameCell.getStringCellValue().trim();
				} 
        		authority.setDoorNum(doorNum);
        		authority.setDeviceNum(deviceNum);
        		authority.setValidStartTime(validStartTime+":00");
        		authority.setValidEndTime(validEndTime+":00");
        		authority.setTimeGroupName(timeGroupName);
        		authority.setTaskState("00");
        		list.add(authority);		
			}  	
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			peopleAuthorityService.batchAddAuthorityByDoor(list, loginUser.getYhMc()); 			
        	request.setAttribute("message", "导入权限成功!"); 
		} catch (ImportException e2) {
			request.setAttribute("message", e2.getMessage());
		} catch (Exception e) {
			request.setAttribute("message", "导入权限失败，请检查导入数据是否正确！");
		}
	}
	
	/**
	 * 关闭文件输入流
	 * @author minting.he
	 */
	public void close(HttpServletRequest request, FileInputStream fis){
		if (fis != null) {
			try {
				fis.close();
			} catch (IOException e) {
				request.setAttribute("message", "系统异常！");
			}
		}
	}
	
}
