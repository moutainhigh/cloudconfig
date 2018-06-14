package com.xkd.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 操作Excel表格的功能类
 */
public class ReadExcellIndustry {

	
	private static final String COMANY_NAME = "http://i.yjapi.com/ECISimple/GetDetailsByName"; //企业关键字全名精确查询
	private static final String COMANY_NAME_LIKE = "http://i.yjapi.com/ECISimple/Search"; //企业关键字模糊查询
	private static final String COMANY_KEY_LIKE = "http://i.yjapi.com/ECISimple/GetDetails"; //根据ID获取照面信息
	
	
	
    public static void main(String[] args) {
    	
    	FileInputStream input = null;
    	XSSFWorkbook xSSFWorkbook = null;
    	Connection con  = null;
    	Statement statement = null;
        ResultSet rs = null;
         
         
    	 String url = "jdbc:mysql://172.116.100.20:3306/data-center-new" ;    
		 String username = "root" ;   
		 String password = "root" ; 
    	
         try {
        	 
    		try{
    			
    			//97行
				Class.forName("com.mysql.jdbc.Driver");
				con   =  DriverManager.getConnection(url, username,password) ;
				con.setAutoCommit(false);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}  
        	 
        	input = new FileInputStream(new File("F:\\xz文档\\分类整理.xlsx"));  //读取的文件路径   
			xSSFWorkbook = new XSSFWorkbook(new BufferedInputStream(input));
			
		    XSSFSheet sheet = xSSFWorkbook.getSheetAt(0);  //获取 某个表                
	        String sheetName = sheet.getSheetName();//获取表名，存入数组  
	        System.out.println("------>>>---正在读取Excel表数据，当前表："+sheetName);  
	          
	        int i = 0;
	        for( int rows=0;rows<=sheet.getLastRowNum();rows++){//有多少行  
	        	
	            XSSFRow row = sheet.getRow(rows);//取得某一行   对象                     
	            String parantIndustry = row.getCell(0).getStringCellValue();
	            String industry = row.getCell(1).getStringCellValue();

	        	String industryId = UUID.randomUUID().toString();
	        	
	        	String sqlExists = "select * from dc_dictionary where ttype = 'industry' and value = '"+parantIndustry+"'";
	        	
	        	try {
	        		
	        		statement = con.prepareStatement(sqlExists);
	        		rs  = statement.executeQuery(sqlExists);
	        		
	        		if (rs.next()) {
						
	        			String id = rs.getString("id");
	        			
	        			String sql = "insert into dc_dictionary(id,ttype,ttypeName,parentId,value,useCount,level) "
	    	        			+ "values('"+industryId+"','industry','行业','"+id+"','"+industry+"',0,"+i+++")";
	        			
	        			statement = con.prepareStatement(sql);
		        		statement.executeUpdate(sql);
	        			
					}else{
						
						String parentIndustryId = UUID.randomUUID().toString();
						
						String parentSql = "insert into dc_dictionary(id,ttype,ttypeName,parentId,value,useCount,level) "
			        			+ "values('"+parentIndustryId+"','industry','行业','0','"+parantIndustry+"',0,"+i+++")";
						
						String sql = "insert into dc_dictionary(id,ttype,ttypeName,parentId,value,useCount,level) "
	    	        			+ "values('"+industryId+"','industry','行业','"+parentIndustryId+"','"+industry+"',0,"+i+++")";
						
						statement = con.prepareStatement(parentSql);
		        		statement.executeUpdate(parentSql);
		        		
		        		statement = con.prepareStatement(sql);
		        		statement.executeUpdate(sql);
						
					}
	        		
				} catch (Exception e) {
					
					e.printStackTrace();
					
					throw new RuntimeException("插入异常！");
				}
	        }
	        
	        con.commit();
	        
		} catch (Exception e) {
			
			e.printStackTrace();
			
			try {
				
				con.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			
		}finally{
			
			try {
				
				input.close();
				xSSFWorkbook.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
    }
}
