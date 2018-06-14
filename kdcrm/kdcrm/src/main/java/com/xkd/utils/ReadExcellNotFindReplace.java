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
import java.util.List;
import java.util.Map;

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
public class ReadExcellNotFindReplace {

	
	private static final String COMANY_NAME = "http://i.yjapi.com/ECISimple/GetDetailsByName"; //企业关键字全名精确查询
	private static final String COMANY_NAME_LIKE = "http://i.yjapi.com/ECISimple/Search"; //企业关键字模糊查询
	private static final String COMANY_KEY_LIKE = "http://i.yjapi.com/ECISimple/GetDetails"; //根据ID获取照面信息
	
	
	
    public static void main(String[] args) {
    	
    	FileInputStream input = null;
    	XSSFWorkbook xSSFWorkbook = null;
    	Connection con  = null;
    	
    	 /*String url = "jdbc:mysql://120.77.158.180:3306/data-center" ;    
		 String username = "root" ;   
		 String password = "bdsj@123456-180" ; */
    	 String url = "" ;    
		 String username = "root" ;   
		 String password = "" ; 
    	
         try {
        	 
    		try{
    			
				Class.forName("com.mysql.jdbc.Driver");
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}  
        	 
        	Map<Integer,List<String[]>> map = new HashMap<>();
        	List<String[]> list  = new ArrayList<>();
        	 
        	input = new FileInputStream(new File("F:\\xz文档\\数据仓库\\企查查没查到企业模糊精确替换.xlsx"));  //读取的文件路径   
			xSSFWorkbook = new XSSFWorkbook(new BufferedInputStream(input));
			
		    XSSFSheet sheet = xSSFWorkbook.getSheetAt(0);  //获取 某个表                
	        String sheetName = sheet.getSheetName();//获取表名，存入数组  
	        System.out.println("------>>>---正在读取Excel表数据，当前表："+sheetName);  
	          
//	        HashMap<String,String> map = new HashMap<String,String>();
	        
	        JsonParser parse =new JsonParser();  //创建json解析器
	        
	        /*
	         * 没录入企业
	         * 
	         */
	        
	        for( int rows=0;rows<=sheet.getLastRowNum();rows++){//有多少行  
//	        for( int rows=1;rows<=1;rows++){//有多少行  
	        	
	            XSSFRow row = sheet.getRow(rows);//取得某一行   对象                     
//	            String[] s =new String[50];//初始化数组长度  
	            
	            //从第二行开始第四列为企业名称，通过企查查获得企业的详细信息
	            //所有者
	            String companyName = row.getCell(0).getStringCellValue();
	            
	            
	            
	            Statement statementCompany = null;
	            ResultSet rsCompany = null;
	            
	            if(StringUtils.isBlank(companyName)){
	            	continue;
	            }
	            
	            String companyInfo = CompanyInfoApi.queryCompanyInfo(ReadExcellNotFindReplace.COMANY_NAME_LIKE, companyName,1,10);
	            
	            if(companyInfo == null){
	            	
	            	System.err.println(companyName+"----------该公司名称有误，通过企查查模糊查询没有查到该公司信息");
	            	
	            	String[] strings = new String[1];
	            	strings[0] = companyName;
	            	list.add(strings);
	            	
	            }else{
	            	
	            	JsonObject json = (JsonObject) parse.parse(companyInfo);  //创建jsonObject对象
	            	if(!"200".equals(json.get("Status").getAsString())){
	            		
	            		
	            	}else{
	            	
	            		if(companyInfo != null){
	            			
	            			Map<String, Object> results = (Map<String, Object>)JSON.parseObject(companyInfo,Object.class);
	            			
	            			if(results != null && results.size() > 0){
	            				
	            				List<Map<String, Object>> Result = (List<Map<String, Object>>)results.get("Result");
	            				
	            				
	            				if(Result != null && Result.size() > 0){
	            					
	            					Map<String, Object> companyMHNameMap = Result.get(0);
	            					String companyMHName = (String)companyMHNameMap.get("Name");
	            					
	            					System.err.println("--------模糊查询取第一条成功--------"+companyMHName+"----------------");
	            					
	            					if(StringUtils.isNotBlank(companyMHName)){
	            						
	            						 String companyJQInfo = CompanyInfoApi.queryCompanyInfo(ReadExcellNotFindReplace.COMANY_NAME, companyMHName,1,10);
	            						 
	            						 Map<String, Object> resultsJQ = (Map<String, Object>)JSON.parseObject(companyJQInfo,Object.class);
	            						 
	            						 
	            						 try {
	            								
	            				            	con   =  DriverManager.getConnection(url, username,password) ;
	            				            	statementCompany = con.createStatement();
	            				        		/*
	            				        		 * 如果该企业名称已经存在那就不插入数据
	            				        		 */
	            				        		rsCompany = statementCompany.executeQuery("select id from dc_company where company_name='"+companyName+"'");
	            				    			if(rsCompany.next()){
	            				    				
	            				    				String companyId = rsCompany.getString("id");
	            				    				
	            				    				/*
	            				    				 * 如果存在企业ID更新企业信息
	            				    				 */
	            				    				if(StringUtils.isNotBlank(companyId)){
	            				    					
	            				    					//返回企业信息正确
	            					            		String establish_time = "";
	            					            		String operating_period = "";
	            					            		String approve_date = "";
	            					            		String updated_date = "";
	            					            		String termStart = "";
	            					            		
	            					            		Map<String, Object> resultsJQMap = null;
	            					            		
	            					            		try {
	            					            			
	            					            			resultsJQMap = (Map<String, Object>) resultsJQ.get("Result");
	            					            			
	            					            			establish_time = resultsJQMap.get("StartDate")==null?"":resultsJQMap.get("StartDate").toString().substring(0, 10);//get("StartDate").getAsString().substring(0, 10)
	            					            			operating_period = resultsJQMap.get("TeamEnd")==null?"":resultsJQMap.get("TeamEnd").toString().substring(0, 10);
	            					            			approve_date = resultsJQMap.get("CheckDate")==null?"":resultsJQMap.get("CheckDate").toString().substring(0, 10);
	            					            			updated_date = resultsJQMap.get("UpdatedDate")==null?"":resultsJQMap.get("UpdatedDate").toString().substring(0, 10);
	            					            			termStart = resultsJQMap.get("TermStart")==null?"":resultsJQMap.get("TermStart").toString().substring(0, 10);
	            					            			
	            										} catch (Exception e) {
	            											
	            											e.printStackTrace();
	            										}
	            					            		
	            					            		/*
	            					            		 * 企查查没查出数据，受影响的表dc_user_address
	            					            		 */
//	            					            		String id = json.get("Result").getAsJsonObject().get("KeyNo").isJsonNull()?null:json.get("Result").getAsJsonObject().get("KeyNo").getAsString();
	            					            		String social_credit = resultsJQMap.get("CreditCode")==null?null:resultsJQMap.get("CreditCode").toString();
	            					            		String registration_code = resultsJQMap.get("No")==null?null:resultsJQMap.get("No").toString();
	            					            		String organization_code = resultsJQMap.get("No")==null?null:resultsJQMap.get("No").toString();
	            					            		String manage_type = resultsJQMap.get("Status")==null?null:resultsJQMap.get("Status").toString();
	            					            		String representative = resultsJQMap.get("OperName")==null?null:resultsJQMap.get("OperName").toString();
	            					            		String registered_money = resultsJQMap.get("RegistCapi")==null?null:resultsJQMap.get("RegistCapi").toString();
	            					            		String registration_authority = resultsJQMap.get("BelongOrg")==null?null:resultsJQMap.get("BelongOrg").toString();
	            					            		String companyAddress = resultsJQMap.get("Address")==null?null:resultsJQMap.get("Address").toString();
	            					            		String name = resultsJQMap.get("Name")==null?"":resultsJQMap.get("Name").toString();
	            					            		String scope = resultsJQMap.get("Scope")==null?"":resultsJQMap.get("Scope").toString();
	            					            		String province = resultsJQMap.get("Province")==null?"":resultsJQMap.get("Province").toString();
	            					            		String econKind = resultsJQMap.get("EconKind")==null?"":resultsJQMap.get("EconKind").toString();
	            				    					
	            					            		System.err.println("--------精确查询取第一条成功--------"+name+"----------------");
	            				            			
	            				            			/*
	            				            			 * Statement statementCompany = null;
	            											ResultSet rsCompany = null;
	            				            			 */
	            					            		
	            				        				StringBuffer sbCompany = new StringBuffer();
	            				        				StringBuffer sbCompanyUpdate = new StringBuffer();
	            					            		//企业基本表插入数据
	            				        				
	            				        				try {
	            				        					
	            				        					sbCompanyUpdate.append("update dc_company set company_name='"+name+"',social_credit = '"+(social_credit==null?"":social_credit)+"',registration_code= '"+(registration_code==null?"":registration_code)+"', "
	            				        							+ "organization_code =  '"+(organization_code==null?"":organization_code)+"',manage_type = '"+(manage_type==null?"":manage_type)+"',"
	            				        							+ "representative = '"+(representative==null?"":representative)+"',registered_money = '"+(registered_money==null?"":registered_money)+"',"
	            				        							+ "establish_time = '"+(establish_time==null?"":establish_time)+"',operating_period = '"+(operating_period==null?"":operating_period)+"',"
	            				        							+ "registration_authority ='"+(registration_authority==null?"":registration_authority)+"',approve_date = '"+(approve_date==null?"":approve_date)+"',"
	            				        							+ "manageScope = '"+scope+"',ttype = 1,province = '"+province+"',updated_date='"+updated_date+"',econKind='"+econKind+"',termStart='"+termStart+"',"
	            				        							+ "db_change_time=NOW() "
	            				        							+ "  where id = '"+companyId+"'");
	            				        					
	            				        					/*sbCompany.append("insert into dc_company (id,company_name,social_credit,registration_code,organization_code,manage_type,"
		            					            				+ "representative,registered_money,establish_time,operating_period,registration_authority,annual_sales_volume,annual_profit,"
		            					            				+ "approve_date,wechat,company_type,companysize,english_name,before_name,company_name,manageScope,phone,email,website,company_desc,ttype,"
		            					            				+ "province,updated_date,econKind,termStart,companyAdviser,db_change_time) values ('"+(id==null?"":id)+"','"+(social_credit==null?"":social_credit)+"','"+(registration_code==null?"":registration_code)+"', '"+(organization_code==null?"":organization_code)+"',"
		            					            				+ "'"+(manage_type==null?"":manage_type)+"','"+(representative==null?"":representative)+"','"+(registered_money==null?"":registered_money)+"','"+(establish_time==null?"":establish_time)+"',"
		            					            				+ "'"+(operating_period==null?"":operating_period)+"','"+(registration_authority==null?"":registration_authority)+"','"+((nianyingyee==null || "".equals(nianyingyee))?0:nianyingyee)+"','"+((nianlr==null || "".equals(nianlr))?0:nianlr)+"','"+(approve_date==null?"":approve_date)+"','"+guanfangweix+"',"
		            					            						
		            					            				+ "'"+kehuhangy+"','0','','','"+name+"','"+scope+"','','','"+qiyewangz+"','',1,'"+province+"','"+updated_date+"','"+econKind+"','"+termStart+"','',NOW())");
	            				        					*/
	            				        					
	            				        					con =  DriverManager.getConnection(url, username,password) ;
		            				            			con.setAutoCommit(false);
		            				            			statementCompany = con.createStatement();
	            				        					int num = statementCompany.executeUpdate(sbCompanyUpdate.toString());
	            				        					
		            				            			rsCompany = statementCompany.executeQuery("select id from dc_user_address where userid='"+companyId+"' and utype = 1");
		            				            			
		            				            			if(rsCompany.next()){
		    	            				    				
		    	            				    				String companyAddressId = rsCompany.getString("id");
		    	            				    				
		    	            				    				StringBuffer sbCompanyAdress = new StringBuffer();
			            					            		
			            					            		//拆分地址
			            					            		String provice = null;
			            					            		String city = null;
			            					            		String area = null;
			            					            		String county = null;
			            					            		String country = null;
			            					            		Map<String, String> mapCompanyAddresss = SplitAddress.getCountryMap(companyAddress);
			            										// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
			            										if(mapCompanyAddresss != null && !mapCompanyAddresss.isEmpty()){
			            											
			            											provice = mapCompanyAddresss.get("provinces");
			            											city = mapCompanyAddresss.get("city");
			            											area = mapCompanyAddresss.get("area");
			            											county = mapCompanyAddresss.get("county");
			            											country = mapCompanyAddresss.get("country");
			            										}
			            										
			            										sbCompanyAdress.append("update dc_user_address set userid='"+companyId+"',province='"+(provice==null?"":provice)+"',"
			            				            					+ "city='"+(city==null?"":city)+"',county='"+(county==null?"":county)+"',address='"+(companyAddress==null?"":companyAddress)+"', utype = 1"
			            				            					+ " where id = '"+companyAddressId+"'");
			            										
			            										statementCompany.executeUpdate(sbCompanyAdress.toString());
			            				            			
			            					            		
			            										//地址表插入数据
			            					            		//type 1 表示公司 2表示用户
			            					            		/*sbCompanyAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
			            					            				+ "values('"+(id==null?"":id)+"','','"+(provice==null?"":provice)+"','"+(city==null?"":city)+"','"+(county==null?"":county)+"','"+(companyAddress==null?"":companyAddress)+"',1)");
			            					            		*/
		            				            			}
		            				            			
		            				            			
		            				            			con.commit();
															
														} catch (Exception e) {
															
															e.printStackTrace();
															
															con.rollback();
															
														}finally {
															
															try {
																
				            									con.close();
				            									statementCompany.close();
				            									rsCompany.close();
				            									
				            								} catch (SQLException e) {
				            									
				            									e.printStackTrace();
				            								}
														}
	            					            		
	            				    					
	            				    				}
	            				    			}
	            				            	
	            							} catch (Exception e) {
	            								
	            								e.printStackTrace();
	            								
	            							}finally {
	            								
	            								try {
	            									con.close();
	            									statementCompany.close();
	            									rsCompany.close();
	            									
	            								} catch (SQLException e) {
	            									
	            									e.printStackTrace();
	            								}
	            							}
	            						
	            					}
	            					
	            					System.out.println(companyName);
	            					
	            				}
	            				
	            			}
	            		}
	            		
	            		
	            	}
	            }
	            
	        }
	        
	        /*
	         * 将不在企查查的企业输出到excell中
	         */
	        if(list.size()>0){
	        	
	        	 map.put(new Integer(0),list);
	        	 writeXlsx(map);
	        	 
	         }
	        
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			
			try {
				
				input.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
    }
    
    //写入Xlsx
    public static void writeXlsx(Map<Integer,List<String[]>> map) {
        
    	try {
        	
    		String fileName ="F:\\xz文档\\数据仓库\\模糊查询没查到企业导出xz.xlsx";
            XSSFWorkbook wb = new XSSFWorkbook();
            for(int sheetnum=0;sheetnum<map.size();sheetnum++){
                XSSFSheet sheet = wb.createSheet(""+sheetnum);
                List<String[]> list = map.get(sheetnum);
                XSSFRow row = sheet.createRow(0);
                XSSFCell cell = row.createCell(0);
                cell.setCellValue("企业名称");
                XSSFCellStyle style = wb.createCellStyle();  
                Font fontHeader=wb.createFont();
	           	//字体号码
	           	fontHeader.setFontHeightInPoints((short)12);
	           	fontHeader.setBold(true);
	           	//字体名称
	           	fontHeader.setFontName("宋体");
	           	style.setFont(fontHeader);
                cell.setCellStyle(style);
                for(int i=0;i<list.size();i++){
                	//第一行是表头，所以要写到第二行
                    row = sheet.createRow(i+1);
                    String[] str = list.get(i);
                    for(int j=0;j<str.length;j++){
                        cell = row.createCell(j);
                        cell.setCellValue(str[j]);
                    }
                }
            }
            FileOutputStream outputStream = new FileOutputStream(fileName);
            wb.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
        	
            e.printStackTrace();
        } catch (IOException e) {
        	
            e.printStackTrace();
        }
    }
}
