package test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.mysql.jdbc.ResultSetMetaData;

public class ImportMeetData {
	
	public final static String url = "jdbc:mysql://172.116.100.20:3306/data-center";
	public final static String username = "root";
	public final static String password = "root";

	public static void main(String[] args) throws Exception
	   {
		
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		
		List<String> tables = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			statement = con.createStatement();
			rs = statement.executeQuery("select company_name from dc_company");
			
//			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
//			int count=rsmd.getColumnCount();
//			for(int i=1;i<=count;i++){
//				System.out.print(rsmd.getColumnName(i)+"       ");
//			}
			
		StringBuilder sb = new StringBuilder();
		
		Map<String, String> map = new HashMap<String, String>();
		
		File xlsFile = new File("E:\\bak\\2017年6月资本之道参会统计表 （处理）.xlsx");
	      // 获得工作簿
	      Workbook workbook = WorkbookFactory.create(xlsFile);
	      // 获得工作表个数
         Sheet sheet = workbook.getSheetAt(0);
         // 获得行数
         int rows = sheet.getLastRowNum() + 1;
         // 获得列数，先获得一行，在得到改行列数
         
         // 读取数据
         for (int row = 1; row < rows; row++)
         {
            Row r = sheet.getRow(row);
            String cid = UUID.randomUUID().toString();

            	String comName = r.getCell(5)+"";
            	
            	boolean isExt = false;
            	while(rs.next()){
            		
            		if(comName.equals(rs.getString(1))){
            			isExt = true;
            			
            			break;
            		}
    				
    			}
            	
            	if(isExt){
            		System.out.println("重复急r.getCell(13)  "+ r.getCell(13));
    				String sqlProject = "replace into  dc_company_project (companyid,following) "
    						+ "values( '"+cid+"','"+r.getCell(13)+"');";
    				sb.append(sqlProject);
            		System.out.println(sqlProject);
            		continue;
            	}
    		
            	System.err.println("继续。。。。");
	            String uid = UUID.randomUUID().toString();
            	if(!map.containsKey(comName)){
            		String sqlCompany = "replace into dc_company(id,company_name,parent_industryid) "
            				+ "VALUES('"+cid+"','"+r.getCell(5)+"','"+r.getCell(6)+"');";
//            		String sqlCompany = "replace into dc_company(id,company_name,parent_industryid,son_industryid) "
//            				+ "VALUES('"+cid+"','"+r.getCell(5)+"','"+r.getCell(6)+"','"+r.getCell(7)+"');";
    				sb.append(sqlCompany);
            		System.out.println(sqlCompany);
            		map.put(comName, cid);
            	}else{
            		System.out.println("comName  "+ comName);
            		cid = map.get(comName);
            	}
            	
            	String sqlAddr = "replace into dc_user_address(userid,province,city,utype) "
            			+ "values('"+cid+"','"+r.getCell(3)+"','"+r.getCell(4)+"','1');";
				sb.append(sqlAddr);
            	System.out.println(sqlAddr);
            	
            	DecimalFormat df = new DecimalFormat("0");  
            	String mobile = df.format(r.getCell(8).getNumericCellValue());  
            	String sqlUser = "replace into dc_user_info (id,uname,companyid,mobile,station) "
            			+ "values('"+uid+"','"+r.getCell(7)+"','"+cid+"','"+mobile+"','"+r.getCell(9)+"');";
				sb.append(sqlUser);
            	System.out.println(sqlUser);
            	
            	String enrollDate = r.getCell(1).toString().replace(".", "-");
            	String sqlPayment = "replace into  dc_payment (companyid,userid,adviser,end_money,money_situation,channel,enroll_date,usertype) "
            			+ "select '"+cid+"','"+uid+"',id,'"+r.getCell(10)+"','"+r.getCell(11)+"','"+r.getCell(12)+"','"+enrollDate+"','"+r.getCell(2)+"' from dc_advisers where adviser_name = '"+r.getCell(0)+"';";
            	System.out.println(sqlPayment);
				sb.append(sqlPayment);
            	
            	
            System.out.println();
         }
         
         System.out.println();
         System.out.println();
         System.out.println(sb.toString());
         
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				statement.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	      
	   }
	 
}
