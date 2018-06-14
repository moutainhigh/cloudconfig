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
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 操作Excel表格的功能类
 */
public class ReadExcellAgain {

	
	private static final String COMANY_NAME = "http://i.yjapi.com/ECISimple/GetDetailsByName"; //企业关键字全名精确查询
	private static final String COMANY_NAME_LIKE = "http://i.yjapi.com/ECISimple/Search"; //企业关键字模糊查询
	private static final String COMANY_KEY_LIKE = "http://i.yjapi.com/ECISimple/GetDetails"; //根据ID获取照面信息
	
	
	
    public static void main(String[] args) {
    	
    	FileInputStream input = null;
    	XSSFWorkbook xSSFWorkbook = null;
    	Connection con  = null;
    	
         try {
        	 
        	 try{  
        		 
	        		try{
	        			
						Class.forName("com.mysql.jdbc.Driver");
						
					} catch (ClassNotFoundException e) {
						
						e.printStackTrace();
					}  
	         		 	
	       		 	 String url = "jdbc:mysql://120.77.175.131:3306/data-center" ;    
	       		     String username = "root" ;   
	       		     String password = "dbonlyone" ;   
					 con   =  DriverManager.getConnection(url, username,password) ;
					  
				} catch (SQLException e) {
					
					e.printStackTrace();
				}  
        		                
        		 	
        	 
        	Map<Integer,List<String[]>> map = new HashMap<>();
        	List<String[]> list  = new ArrayList<>();
        	 
        	input = new FileInputStream(new File("F:\\xz文档\\数据仓库\\2017年5月博友汇客户汇总表.xlsx"));  //读取的文件路径   
			xSSFWorkbook = new XSSFWorkbook(new BufferedInputStream(input));
			
		    XSSFSheet sheet = xSSFWorkbook.getSheetAt(0);  //获取 某个表                
	        String sheetName = sheet.getSheetName();//获取表名，存入数组  
	        System.out.println("------>>>---正在读取Excel表数据，当前表："+sheetName);  
	          
//	        HashMap<String,String> map = new HashMap<String,String>();
	        
	        JsonParser parse =new JsonParser();  //创建json解析器
	        
	        String id =  UUID.randomUUID().toString();
    		String userId = UUID.randomUUID().toString();
	        for( int rows=2;rows<=sheet.getLastRowNum();rows++){//有多少行  
	            XSSFRow row = sheet.getRow(rows);//取得某一行   对象                     
//	            String[] s =new String[50];//初始化数组长度  
	            
	            //从第二行开始第四列为企业名称，通过企查查获得企业的详细信息
	            
	            //序号
//	            String xuhao = row.getCell(0).getNumericCellValue()+"";
	            //标志，如果为1表示一条记录，如果为0则和上面那个一同一类
	            String Xflag = row.getCell(1).getNumericCellValue()+"";
	            
	            //顾问
	            String adviser = row.getCell(2).getStringCellValue();
	            //省份
	            String companyProvince = row.getCell(3).getStringCellValue();
	            //城市
	            String city = row.getCell(4).getStringCellValue();
	            //企业名字
	            String companyName = row.getCell(5).getStringCellValue();
	            //企业联系人名称
	            String userName = row.getCell(6).getStringCellValue();
	            String mobile = "";
	            //联系人电话
	            try {
	            	mobile = row.getCell(7).getStringCellValue();
//	            	if("0.0".equals(mobile)){
//	            		mobile = "";
//	            	}
	            	
				} catch (Exception e) {
					System.out.print(e);
					mobile = "";
				}
	            
	            //项目描述
	            String projectDesc = row.getCell(8).getStringCellValue();
	            //行业
	            String industry = row.getCell(9).getStringCellValue();
	            
	            
	            
	            
	            if(StringUtils.isBlank(companyName)){
	            	continue;
	            }
	            
	            //如果
	            if("1.0".equals(Xflag)){
	            	
	            	id =  UUID.randomUUID().toString();
            		userId = UUID.randomUUID().toString();
	            	
	            }else{
	            	userId = UUID.randomUUID().toString();
	            }
	            
	            
	            
	            String companyInfo = CompanyInfoApi.queryCompanyInfo(ReadExcellAgain.COMANY_NAME, companyName,1,10);
	            
	            if(companyInfo == null){
	            	
	            	System.err.println(companyName+"----------该公司名称有误，通过企查查没有查到该公司信息");
	            	
            		//返回企业信息正确
            		String establish_time = "";
            		String operating_period = "";
            		String approve_date = "";
            		String updated_date = "";
            		String termStart = "";
            		try {
						
            			establish_time = "";
            			operating_period = "";
            			approve_date = "";
            			
					} catch (Exception e) {
						
						e.printStackTrace();
					}
            		
            		
            		String social_credit = "";
            		String registration_code = "";
            		String organization_code = "";
            		String manage_type = "";
            		String representative = "";
            		String registered_money = "";
            		String registration_authority = "";
            		String companyAddress = "";
            		String name = "";
            		String scope = "";
            		String province = "";
            		String econKind = "";
            		
            		StringBuffer sbCompany = new StringBuffer();
            		//企业基本表插入数据
            		sbCompany.append("insert into dc_company (id,social_credit,registration_code,organization_code,manage_type,"
            				+ "representative,registered_money,establish_time,operating_period,registration_authority,annual_sales_volume,annual_profit,"
            				+ "approve_date,wechat,company_type,companysize,english_name,before_name,company_name,company_desc,phone,email,website,content,ttype,"
            				+ "province,updated_date,econKind,termStart) values ('"+(id==null?"":id)+"','"+(social_credit==null?"":social_credit)+"','"+(registration_code==null?"":registration_code)+"', '"+(organization_code==null?"":organization_code)+"',"
            				+ "'"+(manage_type==null?"":manage_type)+"','"+(representative==null?"":representative)+"','"+(registered_money==null?"":registered_money)+"','"+(establish_time==null?"":establish_time)+"',"
            				+ "'"+(operating_period==null?"":operating_period)+"','"+(registration_authority==null?"":registration_authority)+"','','','"+(approve_date==null?"":approve_date)+"','',"
            						
            				+ "'"+industry+"','0','','','"+companyName+"','"+scope+"','','','','',2,'"+province+"','"+updated_date+"','"+econKind+"','"+termStart+"')");
            		
            		StringBuffer sbCompanyAdress = new StringBuffer();
            		//地址表插入数据
            		
            		//拆分地址
//            		String provice = null;
//            		String city = null;
//            		String area = null;
//            		String county = null;
//            		String country = null;
//            		Map<String, String> mapCompanyAddresss = SplitAddress.getCountryMap(companyAddress);
//					// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
//					if(map != null){
//						
//						provice = mapCompanyAddresss.get("provinces");
//						city = mapCompanyAddresss.get("city");
//						area = mapCompanyAddresss.get("area");
//						county = mapCompanyAddresss.get("county");
//						country = mapCompanyAddresss.get("country");
//					}
            		
            		//type 1 表示公司 2表示用户
            		sbCompanyAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
            				+ "values('"+(id==null?"":id)+"','','"+companyProvince+"','"+(city==null?"":city)+"','','',1)");
            		
            		StringBuffer sbUserAdress = new StringBuffer();
            		
            		//拆分地址
//            		Map<String, String> mapuserAddress = SplitAddress.getCountryMap(userAddress);
//					// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
//            		String userProvice = null;
//            		String userCity = null;
//            		String userArea = null;
//            		String userCounty = null;
//            		String userCountry = null;
//            		
//					if(map != null){
//						
//						userProvice = mapuserAddress.get("provinces");
//						userCity = mapuserAddress.get("city");
//						userArea = mapuserAddress.get("area");
//						userCounty = mapuserAddress.get("county");
//						userCountry = mapuserAddress.get("country");
//						
//					}
            		
            		//用户ID也就是客户表的主键ID
            		//地址表插入数据
            		
            		sbUserAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
            				+ "values('"+userId+"','','','',"
            						+ "'','',2)");
            		
            		//用户信息表插入数据
            		StringBuffer sbUserInfo = new StringBuffer();
            		sbUserInfo.append("insert into dc_user_info(id,companyid,birth,idcard,sex,phone,email,qq,wechat,uname,nickname,hobbies,strongpoint,"
            				
            				+ "qualification,graduation,major,profession,station,uflag,status) values('"+userId+"','"+(id==null?"":id)+"','','',"
            						+ "'','"+mobile+"','','','','"+userName+"',"
            						+ "'','','','','','','','',1,0)");
            		
            		
            		//企业商业运作项目（产品）表插入数据
            		StringBuffer sbcompany_operate_project = new StringBuffer();
            		sbcompany_operate_project.append("insert into dc_company_project(projectname,companyid,parent_industryid,son_industryid,ttype,"
            				
	            				+ "plevel,pdesc,product_point_describe,price,unit,usersize,consume_level,user_product,"
	            				
	            				+ "user_age_level,user_group,place,goodlabel,weakness,key_driven_factors) values('','"+(id==null?"":id)+"','0',"
	            						
	            				+ "0,'','','"+projectDesc+"','','0','0','0人','','','0','','','','','')");
            		
            		
            		//问卷结果表
//            		StringBuffer sbQuestion_result = new StringBuffer();
//            		sbQuestion_result.append("insert into dc_question_result(questionid,exam_time,score,meetiid,userid) values()");
            		
            		boolean rollbackFlag = false;
            		
            		try {
            			
            			con.setAutoCommit(false);
            			
//            			StringBuffer sbAsk_question = new StringBuffer();
	            		Statement statement = con.createStatement();
	            		ResultSet rs = null;
	            		int adviserId = 0;
	            		if(StringUtils.isNotBlank(adviser)){
	            			
	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+adviser+"'");
	            			while(rs.next()){
	            				adviserId = rs.getInt("id");
	            			}
	            		}
	            		
	            		//提问（电话）记录表
//        				sbAsk_question.append("insert into dc_ask_question(meetingid,companyid,userid,consult_personid,industry_classify,question_keywords,"
//	            				
//            				+ "question_detail,answer_detail,answer_score,ask_time,answer_time,station,director) values(0,'"+(id==null?"":id)+"','"+userId+"',"+huifangrId+",0,'','','"+huifangxiangq+"',"
//            						
//            				+ "'','"+huifangshij+"','"+huifangshij+"','',"+huifangrId+")");
            			
//        				int suoshutouzzjId = 0;
//        				if(StringUtils.isNotBlank(suoshutouzzj)){
//	            			
//	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+suoshutouzzj+"'");
//	            			while(rs.next()){
//	            				suoshutouzzjId = rs.getInt("id");
//	            			}
//	            		}
//        				
//        				int teacher = 0;
//        				if(StringUtils.isNotBlank(shoukelaos)){
//	            			
//	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+shoukelaos+"'");
//	            			while(rs.next()){
//	            				teacher = rs.getInt("id");
//	            			}
//	            		}
        				
        				//缴费信息表插入数据
                		StringBuffer sbPayment = new StringBuffer();
                		sbPayment.append("insert into dc_payment(userid,companyid,teacherid,userlevel,usertype,paymentmoney,paymentdate,channel,referee,"
                				
	                				+ "user_need,adviser,director,money_situation,deal_money,end_money,end_time,remark) values('"+userId+"','"+(id==null?"":id)+"',"
	                						
	                				+ "0,'','','','','','','',"+adviserId+",0,"
	                						
	                				+ "'','','','','')");
        				
            			
                		
                		
//                		//中小企业客户参会表（报名记录表)
//                		StringBuffer sbuser_attend_meeting = new StringBuffer();
//                		sbuser_attend_meeting.append("insert into dc_meeting_user(userid,companyid,attendmeeting_time,enroll_time,meetingid,"
//                				
//                				
//                				+ "training_situation,training_result_level,training_impact_assessment,status) values('"+userId+"','"+(id==null?"":id)+"','',"
//                						
//                				+ "'',0,'',0,'','')");
                		
                		if("1.0".equals(Xflag)){
        	            	
                			int num = statement.executeUpdate(sbCompany.toString());
                			if( num != 1 ){
                				System.err.println("插入企业信息失败");
                				rollbackFlag = true;
                				continue;
                			}
        	            	
        	            }
                		
                		
                		if("1.0".equals(Xflag)){
        	            	
                			int num1 = statement.executeUpdate(sbCompanyAdress.toString());
                			if( num1 != 1 ){
                				System.err.println("插入企业地址信息失败");
                				rollbackFlag = true;
                				continue;
                			}
        	            	
        	            }
            			
            			
            			
            			
//            			int num2 = statement.executeUpdate(sbUserAdress.toString());
//            			if( num2 != 1 ){
//            				System.err.println("插入用户地址信息失败");
//            				rollbackFlag = true;
//            				continue;
//            			}
            			
            			int num3 = statement.executeUpdate(sbUserInfo.toString());
            			if( num3 != 1 ){
            				System.err.println("插入用户信息失败");
            				rollbackFlag = true;
            				continue;
            			}
            			
            			
//            			int num4 = statement.executeUpdate(sbPayment.toString());
//            			if( num4 != 1 ){
//            				System.err.println("插入用户信息失败");
//            				rollbackFlag = true;
//            				continue;
//            			}
            			
            			if("1.0".equals(Xflag)){
        	            	
            				int num5 = statement.executeUpdate(sbcompany_operate_project.toString());
                			if( num5 != 1 ){
                				System.err.println("企业商业运作项目（产品）插入失败 ");
                				rollbackFlag = true;
                				continue;
                			}
        	            	
        	            }
            			
            			
            			
//            			int num6 = statement.executeUpdate(sbuser_attend_meeting.toString());
//            			if( num6 != 1 ){
//            				System.err.println("中小企业客户参会表（报名记录表）插入失败 ");
//            				rollbackFlag = true;
//            				continue;
//            			}
            			
            			
//            			int num7 = statement.executeUpdate(sbAsk_question.toString());
//            			if( num7 != 1 ){
//            				System.err.println("提问（电话）记录表插入失败 ");
//            				rollbackFlag = true;
//            				continue;
//            			}
            			
            			con.commit();
						
            			System.err.println("------OK-------所有插入执行成功------OK--------");
            			
					} catch (SQLException e) {
						
						try {
							
							con.rollback();
						} catch (SQLException e1) {
							
							e1.printStackTrace();
						}
						
						e.printStackTrace();
					}finally{
						
						try {
							
							if(rollbackFlag = true){
								con.rollback();
							}
							
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
					}
            	
	            	
	            	String[] strings = new String[1];
	            	strings[0] = companyName;
	            	list.add(strings);
	            	
	            }else{
	            	
	            	JsonObject json = (JsonObject) parse.parse(companyInfo);  //创建jsonObject对象
	            	if(!"200".equals(json.get("Status").getAsString())){
	            		
	            		
	            		
	            		
	            	}else{
	            		
	            		
	            		 if("1.0".equals(Xflag)){
	     	            	
	     	            	id =  UUID.randomUUID().toString();
	                 		userId = UUID.randomUUID().toString();
	     	            	
	     	            }else{
	     	            	userId = UUID.randomUUID().toString();
	     	            }
	            		
	            	
	            		System.err.println("----------------"+companyName+"----------------");
	            		
	            		//返回企业信息正确
	            		String establish_time = "";
	            		String operating_period = "";
	            		String approve_date = "";
	            		String updated_date = "";
	            		String termStart = "";
	            		
	            		try {
							
	            			establish_time = json.get("Result").getAsJsonObject().get("StartDate")==null?"":json.get("Result").getAsJsonObject().get("StartDate").getAsString().substring(0, 10);//get("StartDate").getAsString().substring(0, 10)
	            			operating_period = json.get("Result").getAsJsonObject().get("TeamEnd")==null?"":json.get("Result").getAsJsonObject().get("TeamEnd").getAsString().substring(0, 10);
	            			approve_date = json.get("Result").getAsJsonObject().get("CheckDate")==null?"":json.get("Result").getAsJsonObject().get("CheckDate").getAsString().substring(0, 10);
	            			updated_date = json.get("Result").getAsJsonObject().get("UpdatedDate")==null?"":json.get("Result").getAsJsonObject().get("UpdatedDate").getAsString().substring(0, 10);
	            			termStart = json.get("Result").getAsJsonObject().get("TermStart")==null?"":json.get("Result").getAsJsonObject().get("TermStart").getAsString().substring(0, 10);
	            			
						} catch (Exception e) {
							
							e.printStackTrace();
						}
	            		
	            		/*
	            		 * 企查查没查出数据，受影响的表dc_user_address
	            		 */
//	            		String id = json.get("Result").getAsJsonObject().get("KeyNo").isJsonNull()?null:json.get("Result").getAsJsonObject().get("KeyNo").getAsString();
	            		
	            		String social_credit = json.get("Result").getAsJsonObject().get("CreditCode").isJsonNull()?null:json.get("Result").getAsJsonObject().get("CreditCode").getAsString();
	            		String registration_code = json.get("Result").getAsJsonObject().get("No").isJsonNull()?null:json.get("Result").getAsJsonObject().get("No").getAsString();
	            		String organization_code = json.get("Result").getAsJsonObject().get("No").isJsonNull()?null:json.get("Result").getAsJsonObject().get("No").getAsString();
	            		String manage_type = json.get("Result").getAsJsonObject().get("Status").isJsonNull()?null:json.get("Result").getAsJsonObject().get("Status").getAsString();
	            		String representative = json.get("Result").getAsJsonObject().get("OperName").isJsonNull()?null:json.get("Result").getAsJsonObject().get("OperName").getAsString();
	            		String registered_money = json.get("Result").getAsJsonObject().get("RegistCapi").isJsonNull()?null:json.get("Result").getAsJsonObject().get("RegistCapi").getAsString();
	            		String registration_authority = json.get("Result").getAsJsonObject().get("BelongOrg").isJsonNull()?null:json.get("Result").getAsJsonObject().get("BelongOrg").getAsString();
	            		String companyAddress = json.get("Result").getAsJsonObject().get("Address").isJsonNull()?null:json.get("Result").getAsJsonObject().get("Address").getAsString();
	            		String name = json.get("Result").getAsJsonObject().get("Name").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Name").getAsString();
	            		String scope = json.get("Result").getAsJsonObject().get("Scope").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Scope").getAsString();
	            		String province = json.get("Result").getAsJsonObject().get("Province").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Province").getAsString();
	            		String econKind = json.get("Result").getAsJsonObject().get("EconKind").isJsonNull()?"":json.get("Result").getAsJsonObject().get("EconKind").getAsString();
	            		
	            		
	            		StringBuffer sbCompany = new StringBuffer();
	            		//企业基本表插入数据
	            		sbCompany.append("insert into dc_company (id,social_credit,registration_code,organization_code,manage_type,"
	            				+ "representative,registered_money,establish_time,operating_period,registration_authority,annual_sales_volume,annual_profit,"
	            				+ "approve_date,wechat,company_type,companysize,english_name,before_name,company_name,company_desc,phone,email,website,content,ttype,"
	            				+ "province,updated_date,econKind,termStart) values ('"+(id==null?"":id)+"','"+(social_credit==null?"":social_credit)+"','"+(registration_code==null?"":registration_code)+"', '"+(organization_code==null?"":organization_code)+"',"
	            				+ "'"+(manage_type==null?"":manage_type)+"','"+(representative==null?"":representative)+"','"+(registered_money==null?"":registered_money)+"','"+(establish_time==null?"":establish_time)+"',"
	            				+ "'"+(operating_period==null?"":operating_period)+"','"+(registration_authority==null?"":registration_authority)+"','','','"+(approve_date==null?"":approve_date)+"','',"
	            						
	            				+ "'"+industry+"','0','','','"+name+"','"+scope+"','','','','',1,'"+province+"','"+updated_date+"','"+econKind+"','"+termStart+"')");
	            		
	            		StringBuffer sbCompanyAdress = new StringBuffer();
	            		//地址表插入数据
	            		//type 1 表示公司 2表示用户
	            		sbCompanyAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
	            				+ "values('"+(id==null?"":id)+"','','"+companyProvince+"','"+(city==null?"":city)+"','','"+companyAddress+"',1)");
	            		
	            		StringBuffer sbUserAdress = new StringBuffer();
//	            		//用户ID也就是客户表的主键ID
//	            		//地址表插入数据
	            		
//	            		sbUserAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) values('"+userId+"','','','','','"+(userAddress==null?"":userAddress)+"',2)");
	            		sbUserAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
						+ "values('"+userId+"','','','',"
						+ "'','',2)");
	            		//用户信息表插入数据
//	            		StringBuffer sbUserInfo = new StringBuffer();
//	            		sbUserInfo.append("insert into dc_user_info(id,birth,idcard,sex,phone,email,qq,wechat,uname,nickname,hobbies,strongpoint,"
//	            				
//	            				+ "qualification,graduation,major,profession,station) values('"+userId+"','"+birth+"','','','"+dianhua+"','"+youxiang+"','','','"+farendaib+"',"
//	            						+ "'','','','','','','','')");
	            		StringBuffer sbUserInfo = new StringBuffer();
	            		sbUserInfo.append("insert into dc_user_info(id,companyid,birth,idcard,sex,phone,email,qq,wechat,uname,nickname,hobbies,strongpoint,"
	            				
	            				+ "qualification,graduation,major,profession,station,uflag,status) values('"+userId+"','"+(id==null?"":id)+"','','',"
	            						+ "'','"+mobile+"','','','','"+userName+"',"
	            						+ "'','','','','','','','',1,0)");
	            		//缴费信息表插入数据
//	            		StringBuffer sbPayment = new StringBuffer();
//	            		sbPayment.append("insert into dc_payment(userid,companyid,userlevel,usertype,paymentmoney,paymentdate,channel,referee,"
//	            				
//	            				+ "user_need,adviser,director,money_situation,deal_money,end_money,end_time,remark) values('"+userId+"','"+(id==null?"":id)+"',"
//	            						
//	            				+ "'"+kehudengji+"','"+kehuleix+"','','','"+suoyouz+"','"+qudaolaiy+"','"+tuijianren+"','"+kehuxuq+"','','"+suoshutouzzj+"',"
//	            						
//	            				+ "'"+wankuanqingk+"','"+chengjiaojine+"','"+jiesuanjine+"','"+jiesuanriq+"','"+beiz+"')");
	            		
	            		//企业商业运作项目（产品）表插入数据
	            		StringBuffer sbcompany_operate_project = new StringBuffer();
	            		sbcompany_operate_project.append("insert into dc_company_project(projectname,companyid,parent_industryid,son_industryid,ttype,"
	            				
	            				+ "plevel,pdesc,product_point_describe,price,unit,usersize,consume_level,user_product,"
	            				
	            				+ "user_age_level,user_group,place,goodlabel,weakness,key_driven_factors) values('','"+(id==null?"":id)+"','0',"
	            						
	            				+ "0,'','','"+projectDesc+"','','0','0','0人','','','0','','','','','')");
	            		
	            		
	            		//中小企业客户参会表（报名记录表
//	            		StringBuffer sbuser_attend_meeting = new StringBuffer();
//	            		sbuser_attend_meeting.append("insert into dc_meeting_user(userid,companyid,attendmeeting_time,enroll_time,meetingid,"
//	            				
//	            				
//	            				+ "training_situation,training_result_level,training_impact_assessment,status) values('"+userId+"','"+(id==null?"":id)+"','"+chanhuiyuef+"',"
//	            						
//	            				+ "'"+baomingriq+"',0,'',0,'','"+canhuizhuangt+"')");
	            		
	            		
	            		//顾问信息表
//	            		String adviserId = UUID.randomUUID().toString();
//	            		StringBuffer sbAdviser = new StringBuffer();
//	            		sbAdviser.append("insert into dc_advisers(id,adviser_name,ttype) values('"+adviserId+"','"+huifangr+"',1)");
	            		
	            		
	            		//问卷结果表
//	            		StringBuffer sbQuestion_result = new StringBuffer();
//	            		sbQuestion_result.append("insert into dc_question_result(questionid,exam_time,score,meetiid,userid) values()");
	            		
	            		boolean rollbackFlag = false;
	            		
	            		try {
	            			
	            			
	            			con.setAutoCommit(false);
	            			
	            			StringBuffer sbAsk_question = new StringBuffer();
		            		Statement statement = con.createStatement();
		            		ResultSet rs = null;
		            		int adviserId = 0;
		            		if(StringUtils.isNotBlank(adviser)){
		            			
		            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+adviser+"'");
		            			while(rs.next()){
		            				adviserId = rs.getInt("id");
		            			}
		            		}
		            		
		            		StringBuffer sbPayment = new StringBuffer();
	                		sbPayment.append("insert into dc_payment(userid,companyid,teacherid,userlevel,usertype,paymentmoney,paymentdate,channel,referee,"
	                				
	                				+ "user_need,adviser,director,money_situation,deal_money,end_money,end_time,remark) values('"+userId+"','"+(id==null?"":id)+"',"
	                						
	                				+ "0,'','','','','','','',"+adviserId+",0,"
	                						
	                				+ "'','','','','')");
//		            		//提问（电话）记录表
//	        				sbAsk_question.append("insert into dc_ask_question(meetingid,userid,consult_personid,industry_classify,question_keywords,"
//		            				
//	            				+ "question_detail,answer_detail,answer_score,ask_time,answer_time,station,director) values(0,'"+userId+"',"+huifangrId+",0,'','','"+huifangxiangq+"',"
//	            						
//	            				+ "'','"+huifangshij+"','"+huifangshij+"','',"+huifangrId+")");
//	            			
	            			
	                		if("1.0".equals(Xflag)){
	        	            	
	                			int num = statement.executeUpdate(sbCompany.toString());
		            			if( num != 1 ){
		            				System.err.println("插入企业信息失败");
		            				rollbackFlag = true;
		            				continue;
		            			}
	        	            	
	        	            }
	                		
	            			
	                		if("1.0".equals(Xflag)){
	        	            	
	                			int num1 = statement.executeUpdate(sbCompanyAdress.toString());
		            			if( num1 != 1 ){
		            				System.err.println("插入企业地址信息失败");
		            				rollbackFlag = true;
		            				continue;
		            			}
	        	            	
	        	            }
	            			
	            			
	            			
//	            			int num2 = statement.executeUpdate(sbUserAdress.toString());
//	            			if( num2 != 1 ){
//	            				System.err.println("插入用户地址信息失败");
//	            				rollbackFlag = true;
//	            				continue;
//	            			}
	            			
	            			int num3 = statement.executeUpdate(sbUserInfo.toString());
	            			if( num3 != 1 ){
	            				System.err.println("插入用户信息失败");
	            				rollbackFlag = true;
	            				continue;
	            			}
	            			
	            			
//	            			int num4 = statement.executeUpdate(sbPayment.toString());
//	            			if( num4 != 1 ){
//	            				System.err.println("插入用户信息失败");
//	            				rollbackFlag = true;
//	            				continue;
//	            			}
	            			
	            			if("1.0".equals(Xflag)){
	        	            	
	            				int num5 = statement.executeUpdate(sbcompany_operate_project.toString());
		            			if( num5 != 1 ){
		            				System.err.println("企业商业运作项目（产品）插入失败 ");
		            				rollbackFlag = true;
		            				continue;
		            			}
	        	            	
	        	            }
	            			
//	            			int num6 = statement.executeUpdate(sbuser_attend_meeting.toString());
//	            			if( num6 != 1 ){
//	            				System.err.println("中小企业客户参会表（报名记录表）插入失败 ");
//	            				rollbackFlag = true;
//	            				continue;
//	            			}
	            			
	            			
//	            			int num7 = statement.executeUpdate(sbAsk_question.toString());
//	            			if( num7 != 1 ){
//	            				System.err.println("提问（电话）记录表插入失败 ");
//	            				rollbackFlag = true;
//	            				continue;
//	            			}
	            		
	            			con.commit();
							
	            			System.err.println("------OK-------所有插入执行成功------OK--------");
	            			
						} catch (SQLException e) {
							
							e.printStackTrace();
							
							try {
								
								con.rollback();
							} catch (SQLException e1) {
								
								e1.printStackTrace();
							}
							
							e.printStackTrace();
						}finally{
							
							try {
								
								if(rollbackFlag = true){
									con.rollback();
								}
								
							} catch (SQLException e) {
								
								e.printStackTrace();
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
        	
    		String fileName ="F:\\xz文档\\数据仓库\\中小十几个未查到企业.xlsx";
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
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }
}
