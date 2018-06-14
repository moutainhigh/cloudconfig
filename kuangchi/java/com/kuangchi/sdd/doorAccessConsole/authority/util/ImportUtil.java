package com.kuangchi.sdd.doorAccessConsole.authority.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kuangchi.sdd.doorAccessConsole.authority.exception.MyException;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByCardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByStaffModel;

public class ImportUtil {
   
	//批量导入卡权限
	public static List<AuthorityByCardModel> athorityByCard(File file) throws Exception{
		
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
			 int colNum=row.getPhysicalNumberOfCells();//获取标题总列数
			 
			 String colCell=row.getCell(0).getStringCellValue();
             if("按卡删除权限".equals(colCell)){
			 //正文内容从第四行开始，第一行为表头的标题，第二行为备注，第三行为标题名
			 for(int i=3;i<=rowNum;i++){
				 row=sheet.getRow(i);//遍历每一行
				 
				 AuthorityByCardModel abcm=new AuthorityByCardModel();
				 
				 String cardNum=null;//卡号
				 String doorNum=null;//门编号
				
				 
				 Cell cardCell=row.getCell(0);
				 if(null!=cardCell){ 
			        	//设置cell的类型，把纯数字作为String类型读进来
					 cardCell.setCellType(Cell.CELL_TYPE_STRING);
			         cardNum=cardCell.getStringCellValue();
			        	 if(null==cardNum||"".equals(cardNum)){
			        		 throw new MyException("上传失败，请检查是否有卡号为空?");
			        	 }
			        	 }else{
							 throw new MyException("上传失败，请检查是否有卡号为空?");
							}
				 
				 Cell doorCell=row.getCell(2);
                 if(null!=doorCell){
				 doorCell.setCellType(Cell.CELL_TYPE_STRING);
			     doorNum=doorCell.getStringCellValue();
				     if(null==doorNum||"".equals(doorNum)){
		        		 throw new MyException("上传失败，请检查是否有门编号为空?");
		        	 }
                 }else{
					 throw new MyException("上传失败，请检查是否有门编号为空?");
					}
               
			     abcm.setCardNum(cardNum);
			     abcm.setDoorNum(doorNum);
                 
                 list.add(abcm);
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
			 throw new Exception("删除失败");
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
	
	//批量导入员工权限
    public static List<AuthorityByStaffModel> athorityByStaff(File file) throws Exception{
		
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
			 int colNum=row.getPhysicalNumberOfCells();//获取标题总列数
			
			 String colCell=row.getCell(0).getStringCellValue();
             if("按人删除权限".equals(colCell)){
            //正文内容从第四行开始，第一行为表头的标题，第二行为备注，第三行为标题名
			 for(int i=3;i<=rowNum;i++){
				 row=sheet.getRow(i);//遍历每一行
				 AuthorityByStaffModel absm=new AuthorityByStaffModel();
				 
				 String staffNum=null;//员工编号
				 String doorNum=null;//门编号
				
				 
				 Cell staffCell=row.getCell(0);
				 if(null!=staffCell){ 
			        	//设置cell的类型，把纯数字作为String类型读进来
					     
					 staffCell.setCellType(Cell.CELL_TYPE_STRING);
					 staffNum=staffCell.getStringCellValue();
					     if(null==staffNum||"".equals(staffNum)){
			        		 throw new MyException("上传失败，请检查是否有员工编号为空?");
			        	 }
			      }else{
						 throw new MyException("上传失败，请检查是否有员工编号为空?");
						}
				 
				 Cell doorCell=row.getCell(2);
                 if(null!=doorCell){
				 doorCell.setCellType(Cell.CELL_TYPE_STRING);
			     doorNum=doorCell.getStringCellValue();
				     if(null==doorNum||"".equals(doorNum)){
		        		 throw new MyException("上传失败，请检查是否有门编号为空?");
		        	 }
                 }else{
					 throw new MyException("上传失败，请检查是否有门编号为空?");
					}
               
			     absm.setStaffNum(staffNum);
			     absm.setDoorNum(doorNum);
                 
                 list.add(absm);
 			 }
         }else{
			 throw new MyException("导入模版不符合按人删除表格格式！");
			} 	 
			 
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}catch(MyException e){
			throw new Exception(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			 throw new Exception("删除失败");
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

    
    //批量导入门权限
   public static List<String> athorityByDoor(File file) throws Exception{
	
	List<String> list=new ArrayList<String>();
	//创建一个文件输入流
	FileInputStream fis=null;
	
	try {
		 fis=new FileInputStream(file);
		 //新建一个workbook，并把文件输入流传入
		 Workbook wb=new HSSFWorkbook(fis);
		 Sheet sheet=wb.getSheetAt(0);//获取表单
		 Row row=sheet.getRow(0);
		 
		 int rowNum=sheet.getLastRowNum();//获取总行数
		 int colNum=row.getPhysicalNumberOfCells();//获取标题总列数
		
		 String colCell=row.getCell(0).getStringCellValue();
         if("按门删除权限".equals(colCell)){
        //正文内容从第四行开始，第一行为表头的标题，第二行为备注，第三行为标题名
		 for(int i=3;i<=rowNum;i++){
			 row=sheet.getRow(i);//遍历每一行
			 
			 String doorNum=null;//门编号
			 
			 Cell doorCell=row.getCell(0);
             if(null!=doorCell){
			 doorCell.setCellType(Cell.CELL_TYPE_STRING);
		     doorNum=doorCell.getStringCellValue();
			     if(null==doorNum||"".equals(doorNum)){
	        		 throw new MyException("上传失败，请检查是否有门编号为空?");
	        	 }
             }else{
				 throw new MyException("上传失败，请检查是否有门编号为空?");
				}
           
             list.add(doorNum);
			 }
         }else{
			 throw new MyException("导人模版不符合按门删除表格格式！");
			} 
		 
	} catch (IOException e) {
		e.printStackTrace();
		throw new Exception(e.getMessage());
	}catch(MyException e){
		throw new Exception(e.getMessage());
	}catch (Exception e) {
		e.printStackTrace();
		 throw new Exception("删除失败");
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
}
