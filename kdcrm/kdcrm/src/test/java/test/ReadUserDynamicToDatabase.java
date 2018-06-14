package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadUserDynamicToDatabase {

	public static void main(String[] args) {
		
		Connection con = null;
		Statement statementCompany =  null;
		
		/*String url = "jdbc:mysql://120.77.175.131:3306/data-center";
		String username = "root";
		String password = "dbonlyone";*/
		
		String url = "jdbc:mysql://172.116.100.20:3306/data-center";
		String username = "root";
		String password = "root";
		
		/*String url = "jdbc:mysql://120.77.158.180:3306/data-center" ;    
		String username = "root" ;   
		String password = "bdsj@123456-180" ; */
		
		
		try {

			try {

				Class.forName("com.mysql.jdbc.Driver");

			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}

			
			con = DriverManager.getConnection(url, username, password);

		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		try {
			
			FileInputStream fileInputStream = new FileInputStream("F:\\xz文档\\华邦云数据导入\\客户动态信息数据.txt");
			
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			int i = 0;
			String tempString = null;
			String DataStr = null;
			
			while ((tempString = bufferedReader.readLine()) != null) {
				
				String groupId = "";
				String groupName = "";
				String createBy = "";
				String createDate = "";
				String contentValue = "";
				
				String[] insertDate = tempString.split("    :");
				
				
				for(int n = 0;n<insertDate.length;n++){
					
					if(insertDate[n].indexOf("groupId") >-1){
						
						String[] groupIdStr = insertDate[n].split(":");
						
						if(groupIdStr != null){
							
							groupId = groupIdStr[1].trim();
						}
					}else if (insertDate[n].indexOf("groupName") >-1) {
						
						String[] groupNameStr = insertDate[n].split(":");
						
						if(groupNameStr != null){
							
							groupName = groupNameStr[1].trim();
						}
					}else if (insertDate[n].indexOf("createBy") >-1) {
						
						String[] createByStr = insertDate[n].split(":");
						
						if(createByStr != null){
							
							createBy = createByStr[1].trim();
						}
					}else if (insertDate[n].indexOf("createDate") >-1) {
						
						String[] createDateStr = insertDate[n].split(": ");
						
						if(createDateStr != null){
							
							createDate = createDateStr[1].trim();
						}
					}else if (insertDate[n].indexOf("contentValue") >-1) {
						
						String[] contentValueStr = insertDate[n].split(":");
						
						if(contentValueStr != null){
							
							contentValue = contentValueStr[1].trim();
						}
					}
				}
				
				
				try {
					
					Statement  st = con.createStatement();
					
					String sql = "insert into dc_user_dynamic(groupId,groupName,createBy,createDate,contentValue,status) values "
							+ "('"+groupId+"','"+groupName+"','"+createBy+"','"+createDate+"','"+contentValue+"',0)";
					
					st.executeUpdate(sql);
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				
				
				/*
				
				i++;
				
				DataStr += tempString;
				
				if(i%5==0){
					
					String[] groupStr = DataStr.split("groupId");
					
					for(int j = 0;j<groupStr.length;j++){
						
						String groupId = "";
						String groupName = "";
						String createBy = "";
						String createDate = "";
						String contentValue = "";
						
						String insertValueStr = "";
						
						if(j !=0){
							
							insertValueStr = groupId+groupStr[j];
						}
						
						
						
						String[] insertDate = insertValueStr.split("    :");
						
						
						con.setAutoCommit(false);
						
						for(int n = 0;n<insertDate.length;n++){
							
							if(insertDate[n].indexOf("groupId") >0){
								
								String[] groupIdStr = insertDate[n].split(":");
								
								if(groupIdStr != null){
									
									groupId = groupIdStr[1].trim();
								}
							}else if (insertDate[n].indexOf("groupName") >0) {
								
								String[] groupNameStr = insertDate[n].split(":");
								
								if(groupNameStr != null){
									
									groupName = groupNameStr[1].trim();
								}
							}else if (insertDate[n].indexOf("createBy") >0) {
								
								String[] createByStr = insertDate[n].split(":");
								
								if(createByStr != null){
									
									createBy = createByStr[1].trim();
								}
							}else if (insertDate[n].indexOf("createDate") >0) {
								
								String[] createDateStr = insertDate[n].split(":");
								
								if(createDateStr != null){
									
									createDate = createDateStr[1].trim();
								}
							}else if (insertDate[n].indexOf("contentValue") >0) {
								
								String[] contentValueStr = insertDate[n].split(":");
								
								if(contentValueStr != null){
									
									contentValue = contentValueStr[1].trim();
								}
							}
						}
						
						Statement  st = con.createStatement();
						
						String sql = "insert into dc_user_dynamic(groupId,groupName,createBy,createDate,contentValue,status) values "
								+ "('"+groupId+"','"+groupName+"','"+createBy+"','"+createDate+"','"+contentValue+"',0)";
						
						int num = st.executeUpdate(sql);
						
					}
					
					con.commit();
					
					DataStr =  null;
				}
				
			*/
				}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
