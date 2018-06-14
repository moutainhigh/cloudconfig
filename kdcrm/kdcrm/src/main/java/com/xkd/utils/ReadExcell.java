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
public class ReadExcell {

	
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
        	 
        	input = new FileInputStream(new File("F:\\xz文档\\数据仓库\\中小渠道客户-2017-06-23.xlsx"));  //读取的文件路径   
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
	        
	        for( int rows=1;rows<=sheet.getLastRowNum();rows++){//有多少行  
	        	
	            XSSFRow row = sheet.getRow(rows);//取得某一行   对象                     
//	            String[] s =new String[50];//初始化数组长度  
	            
	            //从第二行开始第四列为企业名称，通过企查查获得企业的详细信息
	            //所有者
	            String suoyouz = row.getCell(0).getStringCellValue();
	            //客户类型
	            String kehuleix = row.getCell(1).getStringCellValue();
	            //客户行业
	            String kehuhangy = row.getCell(2).getStringCellValue();
	            //客户产品
	            String kehuchanp = row.getCell(3).getStringCellValue();
	            //企业名称
	            String companyName = row.getCell(4).getStringCellValue();
	            //注册时间
	            String zhucheshij = "";
	            try {
	            	
	            	zhucheshij = row.getCell(5).getStringCellValue();
	            	
				} catch (Exception e2) {
					
					zhucheshij = row.getCell(5).getNumericCellValue()+"";
					
					if(StringUtils.isNotBlank(zhucheshij) && zhucheshij.indexOf(".")>1){
						zhucheshij = zhucheshij.substring(0, zhucheshij.indexOf("."));
					}
					
				}
	            
	           
	            //法人代表
	            String farendaib = row.getCell(6).getStringCellValue();
	            //参会月份
	            String chanhuiyuef = row.getCell(7).getStringCellValue();
	            //参会状态
	            String canhuizhuangt = row.getCell(8).getStringCellValue();
	            //授课老师
	            String shoukelaos = row.getCell(9).getStringCellValue();
	            //报名日期
	            String baomingriq = row.getCell(10).getStringCellValue();
	            //渠道来源
	            String qudaolaiy = row.getCell(11).getStringCellValue();
	            //完款情况
	            String wankuanqingk = row.getCell(12).getStringCellValue();
	            //成交金额
	            String chengjiaojine = row.getCell(13).getStringCellValue();
	            //邮箱
	            String youxiang = row.getCell(14).getStringCellValue();
	            //电话
	            String dianhua = row.getCell(15).getStringCellValue();
	            //联系地址
	            String lianxidiz = row.getCell(16).getStringCellValue();
	            //获得用户地址信息
	            String userAddress = row.getCell(17).getStringCellValue();
	            //企业网址    qcc
	            String qiyewangz = row.getCell(18).getStringCellValue();
	            //官方微信
	            String guanfangweix = row.getCell(19).getStringCellValue();
	            //年营业额
	            String nianyingyee = row.getCell(20).getStringCellValue();
	            //年利润
	            String nianlr = row.getCell(21).getStringCellValue();
	            //项目名称
	            String xiangmumc = row.getCell(22).getStringCellValue();
	            //项目概况
	            String xiangmugaik = row.getCell(23).getStringCellValue();
	            //客户需求
	            String kehuxuq = row.getCell(24).getStringCellValue();
	            //所属投资总监
	            String suoshutouzzj = row.getCell(25).getStringCellValue();
	            //客户等级
	            String kehudengji = row.getCell(26).getStringCellValue();
	            //项目等级
	            String xiangmudengj = row.getCell(27).getStringCellValue();
	            //生日信息
	            String birth = row.getCell(28).getStringCellValue();
	            //项目跟进情况
	            String xiangmugenjqk = row.getCell(29).getStringCellValue();
	            //快递地址
	            String kuaididiz = row.getCell(30).getStringCellValue();
	            //可对接资源
	            String keduijiezy = row.getCell(31).getStringCellValue();
	            //结算金额
	            String jiesuanjine = row.getCell(32).getStringCellValue();
	            //结算日期
	            String jiesuanriq = row.getCell(33).getStringCellValue();
	            //备注
	            String beiz = row.getCell(34).getStringCellValue();
	            //客户标签
	            String kehubiaoq = row.getCell(35).getStringCellValue();
	            //回访人
	            String huifangr = row.getCell(36).getStringCellValue();
	            //回访情况
	            String huifangqingk = row.getCell(37).getStringCellValue();
	            //回访详情
	            String huifangxiangq = row.getCell(38).getStringCellValue();
	            //回访时间
	            String huifangshij = row.getCell(39).getStringCellValue();
	            //推荐人
	            String tuijianren = row.getCell(40).getStringCellValue();
	            
	            
	            Statement statementCompany = null;
	            ResultSet rsCompany = null;
	            
	            try {
					
	            	con   =  DriverManager.getConnection(url, username,password) ;
	            	statementCompany = con.createStatement();
	        		/*
	        		 * 如果该企业名称已经存在那就不插入数据
	        		 */
	        		rsCompany = statementCompany.executeQuery("select id from dc_company where company_name='"+companyName+"'");
	    			if(rsCompany.next()){
	    				
	    				String companyId = rsCompany.getString("id");
	    				if(StringUtils.isNotBlank(companyId)){
	    					continue;
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
	            
	            if(StringUtils.isBlank(companyName)){
	            	continue;
	            }
	            
	            String companyInfo = CompanyInfoApi.queryCompanyInfo(ReadExcell.COMANY_NAME, companyName,1,10);
	            
	            if(companyInfo == null){
	            	
	            	System.err.println(companyName+"----------该公司名称有误，通过企查查没有查到该公司信息");
	            	
            		//返回企业信息正确
            		String establish_time = "";
            		String operating_period = "";
            		String approve_date = "";
            		String updated_date = "";
            		String termStart = "";
            		try {
						
            			establish_time = zhucheshij;
            			operating_period = "";
            			approve_date = "";
            			
					} catch (Exception e) {
						
						e.printStackTrace();
					}
            		
            		
            		String id =  UUID.randomUUID().toString();
            		String social_credit = "";
            		String registration_code = "";
            		String organization_code = "";
            		String manage_type = "";
            		String representative = farendaib;
            		String registered_money = "";
            		String registration_authority = "";
            		String companyAddress = "";
            		String name = "";
            		String scope = "";
            		String province = "";
            		String econKind = "";
            		
            		
            		
            		StringBuffer sbCompanyAdress = new StringBuffer();
            		//地址表插入数据
            		
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
            		
            		//type 1 表示公司 2表示用户
            		sbCompanyAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
            				+ "values('"+(id==null?"":id)+"','','"+(provice==null?"":provice)+"','"+(city==null?"":city)+"','"+(county==null?"":county)+"','"+(companyAddress==null?"":companyAddress)+"',1)");
            		
            		StringBuffer sbUserAdress = new StringBuffer();
            		
            		//拆分地址
            		Map<String, String> mapuserAddress = SplitAddress.getCountryMap(userAddress);
					// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
            		String userProvice = null;
            		String userCity = null;
            		String userArea = null;
            		String userCounty = null;
            		String userCountry = null;
            		
					if(mapuserAddress != null && !mapuserAddress.isEmpty()){
						
						userProvice = mapuserAddress.get("provinces");
						userCity = mapuserAddress.get("city");
						userArea = mapuserAddress.get("area");
						userCounty = mapuserAddress.get("county");
						userCountry = mapuserAddress.get("country");
						
					}
            		
            		//用户ID也就是客户表的主键ID
            		//地址表插入数据
            		String userId = UUID.randomUUID().toString();
            		sbUserAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
            				+ "values('"+userId+"','','"+(userProvice==null?"":userProvice)+"','"+(userCity==null?"":userCity)+"',"
            						+ "'"+(userCounty==null?"":userCounty)+"','"+(userAddress==null?"":userAddress)+"',2)");
            		
            		//用户信息表插入数据
            		StringBuffer sbUserInfo = new StringBuffer();
            		sbUserInfo.append("insert into dc_user_info(id,companyid,birth,idcard,sex,mobile,email,qq,wechat,uname,nickname,hobbies,strongpoint,"
            				
            				+ "qualification,graduation,major,profession,station,uflag,status) values('"+userId+"','"+(id==null?"":id)+"','"+birth+"','',"
            						+ "'','"+dianhua+"','"+youxiang+"','','','"+farendaib+"',"
            						+ "'','','','','','','','',1,0)");
            		
            		
            		//企业商业运作项目（产品）表插入数据
            		StringBuffer sbcompany_operate_project = new StringBuffer();
            		sbcompany_operate_project.append("insert into dc_company_project(projectname,companyid,"
            				
            				+ "plevel,pdesc,product_point_describe,price,unit,usersize,consume_level,user_product,"
            				
            				+ "user_age_level,place,goodlabel,weakness,key_driven_factors,userNeed,following,status) values('"+xiangmumc+"','"+(id==null?"":id)+"',"
            						
            				+ "'"+xiangmudengj+"','"+xiangmugaik+"','','0','0','0人','','"+kehuchanp+"','0','','','','','"+kehuxuq+"','"+xiangmugenjqk+"',0)");
            		
            		//资源表
            		StringBuffer resource = new StringBuffer();
            		
            		resource.append("insert into dc_label(uid,cid,labels,ttype) values('"+userId+"','"+id+"','"+keduijiezy+"',1)");
            		
            		
            		//问卷结果表
//            		StringBuffer sbQuestion_result = new StringBuffer();
//            		sbQuestion_result.append("insert into dc_question_result(questionid,exam_time,score,meetiid,userid) values()");
            		
            		boolean rollbackFlag = false;
            		Statement statement =null;
            		ResultSet rs = null;
            		
            		try {
            			
            			con   =  DriverManager.getConnection(url, username,password) ;
            			con.setAutoCommit(false);
            			
            			
	            		statement = con.createStatement();
	            		
//	            		StringBuffer sbAsk_question = new StringBuffer();
//	            		int huifangrId = 0;
//	            		if(StringUtils.isNotBlank(huifangr)){
//	            			
//	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+huifangr+"'");
//	            			while(rs.next()){
//	            				huifangrId = rs.getInt("id");
//	            			}
//	            		}
	            		
	            		//提问（电话）记录表
//        				sbAsk_question.append("insert into dc_ask_question(meetingid,companyid,userid,consult_personid,industry_classify,question_keywords,"
//	            				
//            				+ "question_detail,answer_detail,answer_score,ask_time,answer_time,station,director) values(0,'"+(id==null?"":id)+"','"+userId+"',"+huifangrId+",0,'','','"+huifangxiangq+"',"
//            						
//            				+ "'','"+huifangshij+"','"+huifangshij+"','',"+huifangrId+")");
            			
        				int suoshutouzzjId = 0;
        				if(StringUtils.isNotBlank(suoshutouzzj)){
	            			
	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+suoshutouzzj+"'");
	            			while(rs.next()){
	            				suoshutouzzjId = rs.getInt("id");
	            			}
	            		}
        				
        				int teacher = 0;
        				if(StringUtils.isNotBlank(shoukelaos)){
	            			
	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+shoukelaos+"'");
	            			while(rs.next()){
	            				teacher = rs.getInt("id");
	            			}
	            		}
        				
        				//缴费信息表插入数据
                		StringBuffer sbPayment = new StringBuffer();
                		sbPayment.append("insert into dc_payment(userid,companyid,teacherid,userlevel,usertype,paymentmoney,paymentdate,channel,referee,"
                				
                				+ "user_need,adviser,director,money_situation,deal_money,end_money,end_time,remark) values('"+userId+"','"+(id==null?"":id)+"',"
                						
                				+ ""+teacher+",'"+kehudengji+"','"+kehuleix+"','','','"+qudaolaiy+"','"+tuijianren+"','"+kehuxuq+"',0,'"+suoshutouzzjId+"',"
                						
                				+ "'"+wankuanqingk+"','"+chengjiaojine+"','"+jiesuanjine+"','"+jiesuanriq+"','"+beiz+"')");
        				
                		
                		//中小企业客户参会表（报名记录表)
                		StringBuffer sbuser_attend_meeting = new StringBuffer();
                		sbuser_attend_meeting.append("insert into dc_meeting_user(userid,companyid,attendmeeting_time,enroll_time,meetingid,"
                				
                				
                				+ "training_situation,training_result_level,training_impact_assessment,status,ustatus,updateTime) values('"+userId+"','"+(id==null?"":id)+"','"+chanhuiyuef+"',"
                						
                				+ "'"+baomingriq+"',0,'',0,'','"+canhuizhuangt+"',0,NOW())");
                		
                		
                		int suoyouzId = 0;
        				if(StringUtils.isNotBlank(shoukelaos)){
	            			
	            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+suoyouz+"'");
	            			while(rs.next()){
	            				suoyouzId = rs.getInt("id");
	            			}
	            		}
                		
                		
                		StringBuffer sbCompany = new StringBuffer();
                		//企业基本表插入数据
                		sbCompany.append("insert into dc_company (id,social_credit,registration_code,organization_code,manage_type,"
                				+ "representative,registered_money,establish_time,operating_period,registration_authority,annual_sales_volume,annual_profit,"
                				+ "approve_date,wechat,company_type,companysize,english_name,before_name,company_name,manageScope,phone,email,website,company_desc,ttype,"
                				+ "province,updated_date,econKind,termStart,companyAdviser,db_change_time) values ('"+(id==null?"":id)+"','"+(social_credit==null?"":social_credit)+"','"+(registration_code==null?"":registration_code)+"', '"+(organization_code==null?"":organization_code)+"',"
                				+ "'"+(manage_type==null?"":manage_type)+"','"+(representative==null?"":representative)+"','"+(registered_money==null?"":registered_money)+"','"+(establish_time==null?"":establish_time)+"',"
                				+ "'"+(operating_period==null?"":operating_period)+"','"+(registration_authority==null?"":registration_authority)+"','"+((nianyingyee==null || "".equals(nianyingyee))?0:nianyingyee)+"','"+((nianlr==null || "".equals(nianlr))?0:nianlr)+"','"+(approve_date==null?"":approve_date)+"','"+guanfangweix+"',"
                						
                				+ "'"+kehuhangy+"','0','','','"+companyName+"','"+scope+"','','','"+qiyewangz+"','',2,'"+province+"','"+updated_date+"','"+econKind+"','"+termStart+"','"+suoyouzId+"',NOW())");
                		
                		
            			int num = statement.executeUpdate(sbCompany.toString());
            			if( num != 1 ){
            				System.err.println("插入企业信息失败");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			int num1 = statement.executeUpdate(sbCompanyAdress.toString());
            			if( num1 != 1 ){
            				System.err.println("插入企业地址信息失败");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			int num2 = statement.executeUpdate(sbUserAdress.toString());
            			if( num2 != 1 ){
            				System.err.println("插入用户地址信息失败");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			int num3 = statement.executeUpdate(sbUserInfo.toString());
            			if( num3 != 1 ){
            				System.err.println("插入用户信息失败");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			
            			int num4 = statement.executeUpdate(sbPayment.toString());
            			if( num4 != 1 ){
            				System.err.println("插入用户信息失败");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			int num5 = statement.executeUpdate(sbcompany_operate_project.toString());
            			if( num5 != 1 ){
            				System.err.println("企业商业运作项目（产品）插入失败 ");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			
            			int num6 = statement.executeUpdate(sbuser_attend_meeting.toString());
            			if( num6 != 1 ){
            				System.err.println("中小企业客户参会表（报名记录表）插入失败 ");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			
//            			int num7 = statement.executeUpdate(sbAsk_question.toString());
//            			if( num7 != 1 ){
//            				System.err.println("提问（电话）记录表插入失败 ");
//            				rollbackFlag = true;
//            				throw new RuntimeException("-----插入失败-----");
//            			}
            			
            			int num8 = statement.executeUpdate(resource.toString());
            			if( num8 != 1 ){
            				System.err.println("资源标签表插入失败 ");
            				rollbackFlag = true;
            				throw new RuntimeException("-----插入失败-----");
            			}
            			
            			con.commit();
						
            			System.err.println("------OK-------所有插入执行成功------OK--------");
            			
					} catch (Exception e) {
						
						e.printStackTrace();
						
					}finally{
						
						try {
							
							if(rollbackFlag == true){
								con.rollback();
							}
							
							if(con != null && !con.isClosed()){
								con.close();
							}
							
							if(statement != null && !statement.isClosed()){
								
								statement.close();
							}
							
							if(rs != null && !rs.isClosed()){
								
								rs.close();
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
	            	
	            		System.err.println("----------------"+companyName+"=====>"+(json.get("Result").getAsJsonObject().get("Name").isJsonNull()?"":json.get("Result").getAsJsonObject().get("Name").getAsString())+"----------------");
	            		
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
	            		String id = UUID.randomUUID().toString();
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
	            		
						//地址表插入数据
	            		//type 1 表示公司 2表示用户
	            		sbCompanyAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
	            				+ "values('"+(id==null?"":id)+"','','"+(provice==null?"":provice)+"','"+(city==null?"":city)+"','"+(county==null?"":county)+"','"+(companyAddress==null?"":companyAddress)+"',1)");
	            		
	            		
	            		StringBuffer sbUserAdress = new StringBuffer();
	            		
	            		//拆分地址
	            		Map<String, String> mapuserAddress = SplitAddress.getCountryMap(userAddress);
						// {area=c区, county=d县, provinces=a省, country=a省b市c区d县, city=b市}
	            		String userProvice = null;
	            		String userCity = null;
	            		String userArea = null;
	            		String userCounty = null;
	            		String userCountry = null;
	            		
						if(mapuserAddress != null && !mapuserAddress.isEmpty()){
							
							userProvice = mapuserAddress.get("provinces");
							userCity = mapuserAddress.get("city");
							userArea = mapuserAddress.get("area");
							userCounty = mapuserAddress.get("county");
							userCountry = mapuserAddress.get("country");
							
						}
	            		
	            		//用户ID也就是客户表的主键ID
	            		//地址表插入数据
	            		String userId = UUID.randomUUID().toString();
	            		sbUserAdress.append("insert into dc_user_address(userid,country,province,city,county,address,utype) "
	            				+ "values('"+userId+"','','"+(userProvice==null?"":userProvice)+"','"+(userCity==null?"":userCity)+"',"
	            						+ "'"+(userCountry==null?"":userCountry)+"','"+(userAddress==null?"":userAddress)+"',2)");
	            		
	            		//用户信息表插入数据
	            		StringBuffer sbUserInfo = new StringBuffer();
	            		sbUserInfo.append("insert into dc_user_info(id,companyid,birth,idcard,sex,mobile,email,qq,wechat,uname,nickname,hobbies,strongpoint,"
	            				
	            				+ "qualification,graduation,major,profession,station,uflag,status) values('"+userId+"','"+(id==null?"":id)+"','"+birth+"','','','"+dianhua+"','"+youxiang+"','','','"+farendaib+"',"
	            						+ "'','','','','','','','',1,0)");
	            		
	            		//企业商业运作项目（产品）表插入数据
	            		StringBuffer sbcompany_operate_project = new StringBuffer();
	            		sbcompany_operate_project.append("insert into dc_company_project(projectname,companyid,"
	            				
	            				+ "plevel,pdesc,product_point_describe,price,unit,usersize,consume_level,user_product,"
	            				
	            				+ "user_age_level,place,goodlabel,weakness,key_driven_factors,userNeed,following,status) values('"+xiangmumc+"','"+(id==null?"":id)+"',"
	            						
	            				+ "'"+xiangmudengj+"','"+xiangmugaik+"','','0','0','0人','','"+kehuchanp+"','0','','','','','"+kehuxuq+"','"+xiangmugenjqk+"',0)");
	            		
	            		
	            		//中小企业客户参会表（报名记录表
	            		StringBuffer sbuser_attend_meeting = new StringBuffer();
	            		sbuser_attend_meeting.append("insert into dc_meeting_user(userid,companyid,attendmeeting_time,enroll_time,meetingid,"
	            				
	            				
	            				+ "training_situation,training_result_level,training_impact_assessment,status,ustatus,updateTime) values('"+userId+"','"+(id==null?"":id)+"','"+chanhuiyuef+"',"
	            						
	            				+ "'"+baomingriq+"',0,'',0,'','"+canhuizhuangt+"',0,NOW())");
	            		
	            		//资源表
	            		StringBuffer resource = new StringBuffer();
	            		
	            		resource.append("insert into dc_label(uid,cid,labels,ttype) values('"+userId+"','"+id+"','"+keduijiezy+"',1)");
	            		
	            		//顾问信息表
//	            		String adviserId = UUID.randomUUID().toString();
//	            		StringBuffer sbAdviser = new StringBuffer();
//	            		sbAdviser.append("insert into dc_advisers(id,adviser_name,ttype) values('"+adviserId+"','"+huifangr+"',1)");
	            		
	            		
	            		//问卷结果表
//	            		StringBuffer sbQuestion_result = new StringBuffer();
//	            		sbQuestion_result.append("insert into dc_question_result(questionid,exam_time,score,meetiid,userid) values()");
	            		
	            		boolean rollbackFlag = false;
	            		Statement statement = null;
	            		ResultSet rs = null;
	            		
	            		try {
	            			
	            			con   =  DriverManager.getConnection(url, username,password) ;
	            			con.setAutoCommit(false);
	            			
	            			statement = con.createStatement();
		            		
//	            			StringBuffer sbAsk_question = new StringBuffer();
//		            		int huifangrId = 0;
//		            		if(StringUtils.isNotBlank(huifangr)){
//		            			
//		            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+huifangr+"'");
//		            			while(rs.next()){
//		            				huifangrId = rs.getInt("id");
//		            			}
//		            		}
//		            		
//		            		//提问（电话）记录表
//	        				sbAsk_question.append("insert into dc_ask_question(meetingid,userid,consult_personid,industry_classify,question_keywords,"
//		            				
//	            				+ "question_detail,answer_detail,answer_score,ask_time,answer_time,station,director) values(0,'"+userId+"',"+huifangrId+",0,'','','"+huifangxiangq+"',"
//	            						
//	            				+ "'','"+huifangshij+"','"+huifangshij+"','',"+huifangrId+")");
	            			
	            			
	        				int suoyouzId = 0;
	        				if(StringUtils.isNotBlank(shoukelaos)){
		            			
		            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+suoyouz+"'");
		            			while(rs.next()){
		            				suoyouzId = rs.getInt("id");
		            			}
		            		}
	        				
	        				StringBuffer sbCompany = new StringBuffer();
		            		//企业基本表插入数据
		            		sbCompany.append("insert into dc_company (id,social_credit,registration_code,organization_code,manage_type,"
		            				+ "representative,registered_money,establish_time,operating_period,registration_authority,annual_sales_volume,annual_profit,"
		            				+ "approve_date,wechat,company_type,companysize,english_name,before_name,company_name,manageScope,phone,email,website,company_desc,ttype,"
		            				+ "province,updated_date,econKind,termStart,companyAdviser,db_change_time) values ('"+(id==null?"":id)+"','"+(social_credit==null?"":social_credit)+"','"+(registration_code==null?"":registration_code)+"', '"+(organization_code==null?"":organization_code)+"',"
		            				+ "'"+(manage_type==null?"":manage_type)+"','"+(representative==null?"":representative)+"','"+(registered_money==null?"":registered_money)+"','"+(establish_time==null?"":establish_time)+"',"
		            				+ "'"+(operating_period==null?"":operating_period)+"','"+(registration_authority==null?"":registration_authority)+"','"+((nianyingyee==null || "".equals(nianyingyee))?0:nianyingyee)+"','"+((nianlr==null || "".equals(nianlr))?0:nianlr)+"','"+(approve_date==null?"":approve_date)+"','"+guanfangweix+"',"
		            						
		            				+ "'"+kehuhangy+"','0','','','"+name+"','"+scope+"','','','"+qiyewangz+"','',1,'"+province+"','"+updated_date+"','"+econKind+"','"+termStart+"','"+suoyouzId+"',NOW())");
		            		
	            			
	            			int num = statement.executeUpdate(sbCompany.toString());
	            			if( num != 1 ){
	            				System.err.println("插入企业信息失败");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			int teacher = 0;
	        				if(StringUtils.isNotBlank(shoukelaos)){
		            			
		            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+shoukelaos+"'");
		            			while(rs.next()){
		            				teacher = rs.getInt("id");
		            			}
		            		}
	        				
	        				int suoshutouzzjId = 0;
	        				if(StringUtils.isNotBlank(shoukelaos)){
		            			
		            			rs = statement.executeQuery("select id from dc_advisers where adviser_name='"+suoshutouzzj+"'");
		            			while(rs.next()){
		            				suoshutouzzjId = rs.getInt("id");
		            			}
		            		}
		            		
		            		//缴费信息表插入数据
		            		StringBuffer sbPayment = new StringBuffer();
		            		sbPayment.append("insert into dc_payment(userid,companyid,teacherid,userlevel,usertype,paymentmoney,paymentdate,channel,referee,"
		            				
		            				+ "user_need,adviser,director,money_situation,deal_money,end_money,end_time,remark) values('"+userId+"','"+(id==null?"":id)+"',"
		            						
		            				+ "'"+teacher+"','"+kehudengji+"','"+kehuleix+"','','','"+qudaolaiy+"','"+tuijianren+"','"+kehuxuq+"',0,'"+suoshutouzzjId+"',"
		            						
		            				+ "'"+wankuanqingk+"','"+chengjiaojine+"','"+jiesuanjine+"','"+jiesuanriq+"','"+beiz+"')");
	            			
		            		
	            			int num1 = statement.executeUpdate(sbCompanyAdress.toString());
	            			if( num1 != 1 ){
	            				System.err.println("插入企业地址信息失败");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			int num2 = statement.executeUpdate(sbUserAdress.toString());
	            			if( num2 != 1 ){
	            				System.err.println("插入用户地址信息失败");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			int num3 = statement.executeUpdate(sbUserInfo.toString());
	            			if( num3 != 1 ){
	            				System.err.println("插入用户信息失败");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			
	            			int num4 = statement.executeUpdate(sbPayment.toString());
	            			if( num4 != 1 ){
	            				System.err.println("插入用户信息失败");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			int num5 = statement.executeUpdate(sbcompany_operate_project.toString());
	            			if( num5 != 1 ){
	            				System.err.println("企业商业运作项目（产品）插入失败 ");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			
	            			int num6 = statement.executeUpdate(sbuser_attend_meeting.toString());
	            			if( num6 != 1 ){
	            				System.err.println("中小企业客户参会表（报名记录表）插入失败 ");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			
//	            			int num7 = statement.executeUpdate(sbAsk_question.toString());
//	            			if( num7 != 1 ){
//	            				System.err.println("提问（电话）记录表插入失败 ");
//	            				rollbackFlag = true;
//	            				throw new RuntimeException("-----插入失败-----");
//	            			}
	            		
	            			int num8 = statement.executeUpdate(resource.toString());
	            			if( num8 != 1 ){
	            				System.err.println("资源标签表插入失败 ");
	            				rollbackFlag = true;
	            				throw new RuntimeException("-----插入失败-----");
	            			}
	            			
	            			con.commit();
							
	            			System.err.println("------OK-------所有插入执行成功------OK--------");
	            			
						} catch (Exception e) {
							
							e.printStackTrace();
							
						}finally{
							
							try {
								
								if(rollbackFlag == true){
									con.rollback();
								}
								
								if(con != null && !con.isClosed()){
									con.close();
								}
								
								if(statement != null && !statement.isClosed()){
									
									statement.close();
								}
								
								if(rs != null && !rs.isClosed()){
									
									rs.close();
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
        	
    		String fileName ="F:\\xz文档\\数据仓库\\中小渠道客户-2017-06-23导出xz.xlsx";
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
