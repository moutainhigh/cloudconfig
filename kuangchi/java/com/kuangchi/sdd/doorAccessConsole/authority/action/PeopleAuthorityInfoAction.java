package com.kuangchi.sdd.doorAccessConsole.authority.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.exception.MyException;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByCardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByDoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByStaffModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.SearchAuthorityBean;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.doorAccessConsole.authority.util.ExcelExportDelCardTemplate;
import com.kuangchi.sdd.doorAccessConsole.authority.util.ExcelExportDelDoorTemplate;
import com.kuangchi.sdd.doorAccessConsole.authority.util.ExcelExportDelStaffTemplate;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("peopleAuthorityInfoAction")
public class PeopleAuthorityInfoAction extends BaseActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService; 
	
	@Resource(name = "peopleAuthorityService")
	PeopleAuthorityInfoService peopleAuthorityService;
	
	@Resource(name = "deviceService")
	DeviceService deviceService;
	
	
	public String toDownloadAuthorityPage(){
	       return  "success";
	    }
	
	//@Resource(name = "peopleAuthorityService")
	
	
	//文件
    private File uploadAuthorityFile;
    public File getUploadAuthorityFile() {
		return uploadAuthorityFile;
	}
	public void setUploadAuthorityFile(File uploadAuthorityFile) {
		this.uploadAuthorityFile = uploadAuthorityFile;
	}
	//提交过来的file的名字
	private  String uploadAuthorityFileFileName;
		
		

	public String getUploadAuthorityFileFileName() {
			return uploadAuthorityFileFileName;
		}
		public void setUploadAuthorityFileFileName(String uploadAuthorityFileFileName) {
			this.uploadAuthorityFileFileName = uploadAuthorityFileFileName;
		}
	//选择下载删除权限模板的类型 
	public void selectDownloadAuthority(){
		HttpServletRequest request = getHttpServletRequest();
		String sel = request.getParameter("deleteAuthority");
			if("卡".equals(sel)){
				downloadDeleteAuthorityByCardTemplate();
			}else if("人".equals(sel)){
				downloadDeleteAuthorityByStaffTemplate();
			}else if("门".equals(sel)){
				downloadDeleteAuthorityByDoorTemplate();
			}
		} 
		
	
	/*按卡片下载删除权限导入模版  by jihui.deng*/
	public void downloadDeleteAuthorityByCardTemplate(){
		  HttpServletResponse response = getHttpServletResponse();
		   OutputStream out = null;
		   ExcelExportDelCardTemplate excelExport = new ExcelExportDelCardTemplate();
		   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		  // List<String> list = peopleAuthorityService.getAllTimeGroups();//获取全部排班类型
		   List<String> doorList = deviceService.getAllDoorNum();//获取全部门号
		   List<String> deviceList = deviceService.getAllDeviceNum();//获取全部设备编号
		   TitleRowCell t1t = new TitleRowCell("卡号",true);
		   
		   TitleRowCell t2t = new TitleRowCell("门名称",true);
		   for(String doorNum:doorList){
			   t2t.addSuggest(doorNum);
		   }
		   TitleRowCell t3t = new TitleRowCell("设备名称",true);
		   /*TitleRowCell t4t = new TitleRowCell("设备名称 *",true);*/
		   for(String deviceNum:deviceList){
			   t3t.addSuggest(deviceNum);
		   }
		   
		   titleRowCells.add(t1t);
		   titleRowCells.add(t2t);
		   titleRowCells.add(t3t);
		   //titleRowCells.add(t4t);
		   excelExport.createLongTitleRow(titleRowCells);
		   try {
				out = response.getOutputStream();
				response.setContentType("application/x-msexcel");
				String fileName="按卡删除权限模版.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				excelExport.getWorkbook().write(out);
		        out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/*按人员下载删除权限导入模版  by jihui.deng*/
	public void downloadDeleteAuthorityByStaffTemplate(){
		   HttpServletResponse response = getHttpServletResponse();
		   OutputStream out = null;
		   ExcelExportDelStaffTemplate excelExport = new ExcelExportDelStaffTemplate();
		   List<String> doorList = deviceService.getAllDoorNum();//获取全部门号
		   List<String> deviceList = deviceService.getAllDeviceNum();//获取全部设备编号
		   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		   TitleRowCell t1t = new TitleRowCell("员工工号",true);
		   TitleRowCell t2t = new TitleRowCell("员工名称",false);
		   TitleRowCell t3t = new TitleRowCell("门名称",true);
		   for(String doorNum:doorList){
			   t3t.addSuggest(doorNum);
		   }
		   TitleRowCell t4t = new TitleRowCell("设备名称",true);
		   
		   for(String deviceNum:deviceList){
			   t4t.addSuggest(deviceNum);
		   }
		   titleRowCells.add(t1t);
		   titleRowCells.add(t3t);
		   titleRowCells.add(t4t);
		   titleRowCells.add(t2t);
		   excelExport.createLongTitleRow(titleRowCells);
		   try {
				out = response.getOutputStream();
				response.setContentType("application/x-msexcel");
				String fileName="按员工删除权限模版.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				excelExport.getWorkbook().write(out);
		        out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/*按门下载删除权限模板*/
	public void downloadDeleteAuthorityByDoorTemplate(){
		  HttpServletResponse response = getHttpServletResponse();
		   OutputStream out = null;
		   ExcelExportDelDoorTemplate excelExport = new ExcelExportDelDoorTemplate();
		   List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		   List<String> doorList = deviceService.getAllDoorNum();//获取全部门号
		  // List<String> list = peopleAuthorityService.getAllTimeGroups();//获取全部排班类型
		   List<String> deviceList = deviceService.getAllDeviceNum();//获取全部设备编号
		   TitleRowCell t1t = new TitleRowCell("门名称",true);
		   for(String doorNum:doorList){
			   t1t.addSuggest(doorNum);
		   }
		   TitleRowCell t2t = new TitleRowCell("设备名称",true);
		   for(String deviceNum:deviceList){
			   t2t.addSuggest(deviceNum);
		   }
		   
		   titleRowCells.add(t1t);
		   titleRowCells.add(t2t);
		   excelExport.createLongTitleRow(titleRowCells);
		   try {
				out = response.getOutputStream();
				response.setContentType("application/x-msexcel");
				String fileName="按门删除权限模版.xls";
				response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
				excelExport.getWorkbook().write(out);
		        out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
    
	 //导出数据库中的数据
    public void downloadAuthority() {   
    			HttpServletRequest request = getHttpServletRequest();
    			String data = request.getParameter("data");
    			PeopleAuthorityInfoModel searchPeopleAuthority=GsonUtil.toBean(data, PeopleAuthorityInfoModel.class);
    			List<PeopleAuthorityInfoModel> peopleAuthoritylist=peopleAuthorityInfoService.searchAuthorityDownLoad(searchPeopleAuthority.getDoorNum(), 
    					searchPeopleAuthority.getDoorName(),searchPeopleAuthority.getStaffName(),searchPeopleAuthority.getStaffNum(),searchPeopleAuthority.getCardNum());
    			HttpServletResponse response = getHttpServletResponse();
    			List list = GsonUtil.getListFromJson(GsonUtil.toJson(peopleAuthoritylist), ArrayList.class);
    	        
    	        OutputStream out = null;
    	        Field[] fields=PeopleAuthorityInfoModel.class.getDeclaredFields();
    	        List<String> colList=new ArrayList<String>();
    	        List<String> colTitleList =new ArrayList<String>();
    	        
    	        for (int i = 0; i < fields.length; i++) {
    	        	String fieldName=fields[i].getName();
					if("cardNum".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("卡号");
					}else if("yhMc".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("门编号");
					}else if("nl".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("门名称");
					}else if("ygzz".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("员工名称");
					}else if("xbMc".equals(fieldName)) {
						colList.add(fieldName);
						colTitleList.add("员工工号");
					}else if("createTime".equals(fieldName)){
						colList.add(fieldName);
						colTitleList.add("创建时间");
					}else if("deviceNum".equals(fieldName)){
						colList.add(fieldName);
						colTitleList.add("设备编号");
					}else if("deviceName".equals(fieldName)){
						colList.add(fieldName);
						colTitleList.add("设备名称");
					}else if("description".equals(fieldName)) {
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
    	            String fileName="权限表.xls";
    	            response.setHeader("content-Disposition", "attachment;filename="+DownloadFile.toUtf8String(fileName));
    	            Workbook workbook = ExcelUtilSpecial.exportExcel("权限表",colTitles, cols, list);
    	            workbook.write(out);
    	            out.flush();
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }
	
    
	public void initAuthorityList1(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		SearchAuthorityBean searchBean=GsonUtil.toBean(data, SearchAuthorityBean.class);
		Integer page=Integer.parseInt(request.getParameter("page"));
		Integer rows=Integer.parseInt(request.getParameter("rows"));
		Integer skip=(page-1)*rows;
		Grid<PeopleAuthorityInfoModel> peopleAuthorityInfoModel=peopleAuthorityInfoService.searchAuthority
		(searchBean.getDoorNum(), searchBean.getDoorName(), searchBean.getStaffName(),searchBean.getStaffNum(),searchBean.getCardNum(), skip, rows);
		printHttpServletResponse(GsonUtil.toJson(peopleAuthorityInfoModel));
	}
	
	

	
	//批量导入删除权限 
		public String uploadDeleteAuthority(){
			HttpServletRequest request=getHttpServletRequest();
			List<AuthorityByCardModel> cardList=new  ArrayList<AuthorityByCardModel>();
			List<AuthorityByStaffModel> peopleList=new  ArrayList<AuthorityByStaffModel>();
			List<AuthorityByDoorModel> doorList=new  ArrayList<AuthorityByDoorModel>();

			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
	        String val=request.getParameter("deleteAuthority");
			try {
				boolean b = true;
				if(uploadAuthorityFile==null){
					request.setAttribute("message", "请选择要导入的文件");
				}else if("卡".equals(val)){
					cardList=authorityByCard(uploadAuthorityFile);
					try {
						b=peopleAuthorityInfoService.batchDelAuthorityByCard(cardList, loginUser.getYhMc());
						if(b){
							request.setAttribute("message", "删除权限成功！");
						}else if(!b){
							request.setAttribute("message", "删除权限失败！请检查导入数据是否正确"); 
						} 
					} catch (Exception e) {
						e.printStackTrace();
						request.setAttribute("message", e.getMessage()); 
					}
				}else if("人".equals(val)){
					peopleList=authorityByStaff(uploadAuthorityFile);
					try {
						
						b=peopleAuthorityInfoService.batchDelAuthorityByStaff(peopleList, loginUser.getYhMc()); 
						if(b){
							request.setAttribute("message", "删除权限成功！");
						}else if(!b){
							request.setAttribute("message", "删除权限失败！请检查导入数据是否正确"); 
						}
					} catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute("message", e.getMessage());
					}
					
					
				}else if("门".equals(val)){
					doorList=authorityByDoor(uploadAuthorityFile);
					try {
						b=peopleAuthorityInfoService.batchDelAuthorityByDoor(doorList, loginUser.getYhMc());
						if(b){
							request.setAttribute("message", "删除权限成功！");
						}else if(!b){
							request.setAttribute("message", "删除权限失败！请检查导入数据是否正确"); 
						} 
					} catch (Exception e) {
						e.printStackTrace();
                        request.setAttribute("message", e.getMessage());
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				
				request.setAttribute("message", e.getMessage());
			}
			request.setAttribute("deleteAuthority", val);
			return "success";
		}
		
			   
			//批量导入删除卡权限 
			public  List<AuthorityByCardModel> authorityByCard(File file) throws Exception{
				
				List<AuthorityByCardModel> list=new ArrayList<AuthorityByCardModel>();
				//创建一个文件输入流
				FileInputStream fis=null;
				
				try {
					 fis=new FileInputStream(file);
					 //新建一个workbook，并把文件输入流传入
					 Workbook wb=new HSSFWorkbook(fis);
					 Sheet sheet=wb.getSheetAt(0);//获取表单
					 Row row=sheet.getRow(0);
					 
					 int rowNum=sheet.getLastRowNum();//获取总行数
					 //int rowNum=sheet.getPhysicalNumberOfRows();
					// int colNum=row.getPhysicalNumberOfCells();//获取标题总列数
					 
					 String colCell=row.getCell(0).getStringCellValue();
		             if("按卡删除权限表".equals(colCell)){
					 //正文内容从第四行开始，第一行为表头的标题，第二行为备注，第三行为标题名
		            	 if(rowNum>4){
		            		 for(int i=5;i<=rowNum;i++){
								 row=sheet.getRow(i);//遍历每一行
								
									 AuthorityByCardModel abcm=new AuthorityByCardModel();
									 
									 String cardNum=null;//卡编号
									 String doorNum=null;//门编号
									 String doorNumNames=null;
									 String doorNums[]=null;
									 
									 String doorName=null;//门名称
									 String deviceNum = null; //设备编号
									 String deviceNumNames = null;
									 String deviceNums[] = null;
									 Cell cardNumCell=row.getCell(0);
									 if(null!=cardNumCell){ 
								        	//设置cell的类型，把纯数字作为String类型读进来
										 cardNumCell.setCellType(Cell.CELL_TYPE_STRING);
								         cardNum=cardNumCell.getStringCellValue();
								        	 if(null==cardNum||"".equals(cardNum)){
								        		 throw new MyException("上传失败，请检查第"+(i+1)+"行的“卡号”是否为空?");
								        	 }
								      }else{
											throw new MyException("上传失败，请检查第"+(i+1)+"行的“卡号”是否为空?");
											}
							         
									 Cell doorCell=row.getCell(1);
					                 if(null!=doorCell){
									 doorCell.setCellType(Cell.CELL_TYPE_STRING);
									 doorNumNames=doorCell.getStringCellValue().trim();
									     if(null==doorNumNames||"".equals(doorNumNames)){
							        		 throw new MyException("上传失败，请检查第"+(i+1)+"行的“门名称”是否为空?");
							        	 }
					                 }else{
										 throw new MyException("上传失败，请检查第"+(i+1)+"行的“门名称”是否为空?");
										}
					                 doorNums =  doorNumNames.split(" :");
					                 doorNum = doorNums[1];
					               /*  //新增获取表格的卡名称 by weixuan.lu
									 Cell doorNameCell=row.getCell(2);
									 if(null!=doorNameCell){
										 doorNameCell.setCellType(Cell.CELL_TYPE_STRING);
								         doorName=doorNameCell.getStringCellValue();
									 }*/
							         //新增获取表格的设备编号 by weixuan.lu 2016/4/25
									 Cell deviceNumCell=row.getCell(2);
									 if(null!=deviceNumCell){
										 deviceNumCell.setCellType(Cell.CELL_TYPE_STRING);
									     deviceNumNames=deviceNumCell.getStringCellValue();
										     if(null==deviceNumNames||"".equals(deviceNumNames)){
								        		 throw new MyException("上传失败，请检查第"+(i+1)+"行的“设备名称”是否为空?");
								        	 }
						                 }else{
											 throw new MyException("上传失败，请检查第"+(i+1)+"行的“设备名称”是否为空?");
											}
									 	  deviceNums = deviceNumNames.split(" :");
									 	  
									 	  deviceNum = deviceService.getDeviceNumByDeviceMac(deviceNums[1]);
								     abcm.setCardNum(cardNum);
								     abcm.setDoorNum(doorNum);
								     abcm.setDoorName(doorName);
								     abcm.setDeviceNum(deviceNum);
								     abcm.setGroupId(""+(i+1));//记录行数
					                 
					                 list.add(abcm);
		            		 }
					 }else{
						 throw new MyException("导入内容为空，请重新填写后再提交！");
						 }
		        }else{
					 throw new MyException("导入模版不符合按卡删除表格格式！");
					} 
					 
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}catch (MyException e) {
					 throw new Exception(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					 throw new Exception("删除失败！");
				}finally{
					if(null!=fis){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return list;
			}
			
			//批量导入删除员工权限 
		    public  List<AuthorityByStaffModel> authorityByStaff(File file) throws Exception{
				
				List<AuthorityByStaffModel> list=new ArrayList<AuthorityByStaffModel>();
				//创建一个文件输入流
				FileInputStream fis=null;
				
				try {
					 fis=new FileInputStream(file);
					 //新建一个workbook，并把文件输入流传入
					 Workbook wb=new HSSFWorkbook(fis);
					 Sheet sheet=wb.getSheetAt(0);//获取表单
					 Row row=sheet.getRow(0);
					 
					 int rowNum=sheet.getLastRowNum();//获取总行数
					// int colNum=row.getPhysicalNumberOfCells();//获取标题总列数
					
					 String colCell=row.getCell(0).getStringCellValue();
		             if("按员工删除权限表".equals(colCell)){
		            	 if(rowNum > 5){
		            //正文内容从第四行开始，第一行为表头的标题，第二行为备注，第三行为标题名
							 for(int i=6;i<=rowNum;i++){
								 row=sheet.getRow(i);//遍历每一行
								 AuthorityByStaffModel absm=new AuthorityByStaffModel();
								 
								 String staffNum=null;//员工编号
								 String doorNum=null;//门编号
								 String doorNumNames=null;
								 String doorNums[]=null;
								 String staffName=null;//员工姓名
								 String doorName=null;//门名称
								 String deviceNum = null;//设备编号
								 String deviceNumNames = null;
								 String deviceNums[] = null;
								 
								 Cell staffCell=row.getCell(0);
								 if(null!=staffCell){ 
							        	//设置cell的类型，把纯数字作为String类型读进来
									     
									 staffCell.setCellType(Cell.CELL_TYPE_STRING);
									 staffNum=staffCell.getStringCellValue();
									     if(null==staffNum||"".equals(staffNum)){
							        		 throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“员工工号”是否为空?");
							        	 }
							      }else{
										 throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“员工工号”是否为空?");
								  }
								 
								
								 Cell doorCell=row.getCell(1);
				                 if(null!=doorCell){
				                	 doorNumNames=doorCell.getStringCellValue().trim();
									 doorCell.setCellType(Cell.CELL_TYPE_STRING);
								    // doorNum=doorCell.getStringCellValue();
								     if(null==doorNumNames||"".equals(doorNumNames)){
						        		 throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“门名称”是否为空?");
						        	 }
				                 }else{
									 throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“门名称”是否为空?");
								 }
				                 
				                 doorNums =  doorNumNames.split(" :");
				                 doorNum = doorNums[1];
				                 doorName = doorNums[0];
				                 /*Cell doorNameCell=row.getCell(3);
				                 if(null!=doorNameCell){
					                 doorNameCell.setCellType(Cell.CELL_TYPE_STRING);
								     doorName=doorNameCell.getStringCellValue();
				                 }*/

							     Cell deviceNumCell=row.getCell(2);
				                 if(null!=deviceNumCell){
					                 deviceNumCell.setCellType(Cell.CELL_TYPE_STRING);
					                 deviceNumNames=deviceNumCell.getStringCellValue();
								     if(null==deviceNumNames||"".equals(deviceNumNames)){
						        		 throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空?");
						        	 }
				                 }else{
									 throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“设备名称”是否为空?");
									}
				                 
				                 Cell staffNameCell=row.getCell(3);
								 if(null!=staffNameCell){
									 staffNameCell.setCellType(Cell.CELL_TYPE_STRING);
								     staffName=staffNameCell.getStringCellValue();
								     
								     if (null != staffName || "".equals(staffName)) {
										if (staffName.length() > 50) {
											throw new MyException("删除权限失败，请检查第"+(i+1)+"行的“员工名称”是否超过50位?");
										}
										 if (!staffName.matches("^([\u4e00-\u9fa5]|[a-zA-Z]|[0-9\\s\\,，.])+$")) {
											 throw new MyException("导入失败，请检查第" + (i + 1)
														+ "行的“员工名称”是否含有除，.外的符号!");
											}
									}
								 }
								 
								
								 
								 
								 
								 
				                 deviceNums = deviceNumNames.split(" :");
			                     deviceNum = deviceService.getDeviceNumByDeviceMac(deviceNums[1]);
				                 
							     absm.setStaffNum(staffNum);
							     absm.setStaffName(staffName);
							     absm.setDoorNum(doorNum);
							     absm.setDoorName(doorName);
							     absm.setDeviceNum(deviceNum);
							     absm.setStaffNo(""+(i+1));//记录行数
				                 
				                 list.add(absm);
				 		 }
		         }else{
		        	 throw new MyException("导入内容为空，请重新填写后再提交！");
		         } 
		             } 
		             else{
						 throw new MyException("导入模版不符合按员工删除表格格式！");
						}
				} catch (IOException e) {
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}catch(MyException e){
					throw new Exception(e.getMessage());
				}catch (Exception e) {
					e.printStackTrace();
					 throw new Exception("删除失败！");
				}
				finally{
					if(null!=fis){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				return list;
			}

		    
		    //批量导入删除门权限 
		   public static List<AuthorityByDoorModel> authorityByDoor(File file) throws Exception{
			
			List<AuthorityByDoorModel> list=new ArrayList<AuthorityByDoorModel>();
			
			//创建一个文件输入流
			FileInputStream fis=null;
			
			try {
				 fis=new FileInputStream(file);
				 //新建一个workbook，并把文件输入流传入
				 Workbook wb=new HSSFWorkbook(fis);
				 Sheet sheet=wb.getSheetAt(0);//获取表单
				 Row row=sheet.getRow(0);
				 
				 int rowNum=sheet.getLastRowNum();//获取总行数
				// int colNum=row.getPhysicalNumberOfCells();//获取标题总列数
				
				 String colCell=row.getCell(0).getStringCellValue();
				 //AuthorityByDoorModel authority=new AuthorityByDoorModel();
		         if("按门删除权限表".equals(colCell)){
		        	 if(rowNum > 3){
		        		 
		        	 
		        //正文内容从第四行开始，第一行为表头的标题，第二行为备注，第三行为标题名
						 for(int i=4;i<=rowNum;i++){
							 AuthorityByDoorModel abdm = new AuthorityByDoorModel();
							 row=sheet.getRow(i);//遍历每一行
							 
							 String doorNum=null;//门编号
							 String doorNumNames=null;
							 String doorNums[]=null;
							 String doorName=null;//名称
							 String deviceName = null;//设备编号
							 String deviceMac = null;//设备编号
							 String deviceNumNames = null;
							 String deviceNums[] = null;
							 Cell doorCell=row.getCell(0);
				             if(null!=doorCell){
							 doorCell.setCellType(Cell.CELL_TYPE_STRING);
							 doorNumNames=doorCell.getStringCellValue().trim();
						   //  doorNum=doorCell.getStringCellValue();
							     if(null==doorNumNames||"".equals(doorNumNames)){
					        		 throw new MyException("上传失败，请检查第"+(i+1)+"行的“门名称”是否为空?");
					        	 }
				             }else{
								 throw new MyException("上传失败，请检查第"+(i+1)+"行的“门名称”是否为空?");
							 }
				             doorNums =  doorNumNames.split(" :");
			                 doorNum = doorNums[1];
			                 doorName = doorNums[0];
				             /*Cell doorNameCell=row.getCell(1);
				             if(null!=doorNameCell){
					             doorNameCell.setCellType(Cell.CELL_TYPE_STRING);
							     doorName=doorNameCell.getStringCellValue();
				             }
				             */
						     Cell deviceNumCell=row.getCell(1);
				             if(null!=deviceNumCell){
				             deviceNumCell.setCellType(Cell.CELL_TYPE_STRING);
				             deviceNumNames=deviceNumCell.getStringCellValue();
							     if(null==deviceNumNames||"".equals(deviceNumNames)){
					        		 throw new MyException("上传失败，请检查第"+(i+1)+"行的设备名称是否为空?");
					        	 }
				             }else{
								 throw new MyException("上传失败，请检查第"+(i+1)+"行的设备名称是否为空?");
								}
				              deviceNums = deviceNumNames.split(" :");
		                      deviceName = deviceNums[0];
		                      deviceMac = deviceNums[1];
				             
						     abdm.setDoorNum(doorNum);
						     abdm.setDoorName(doorName);
						     abdm.setDeviceName(deviceName);
						     abdm.setDeviceMac(deviceMac);
						     abdm.setStaffNum(""+(i+1));//记录行号
				             list.add(abdm);
						 }
		         }else{throw new MyException("导入内容为空，请重新填写后再提交！"); }
		         }
		         else{
					 throw new MyException("导人模版不符合按门删除表格格式！");
					}
			} catch (IOException e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}catch(MyException e){
				throw new Exception(e.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
				 throw new Exception("删除失败！");
			}
			finally{
				if(null!=fis){
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return list;
		}
		

	
	
	
	
	@Override
	public Object getModel() {
		return null;
	}
}