package com.kuangchi.sdd.businessConsole.databaseBackup.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("databaseBackup")
public class DatabaseBackup extends BaseActionSupport{
		//上传文件两个重要参数
	   private File uploadSqlFile;
	   private String uploadSqlFileFileName;
	   
	   public String getUploadSqlFileFileName() {
		return uploadSqlFileFileName;
	  }
	  public void setUploadSqlFileFileName(String uploadSqlFileFileName) {
		this.uploadSqlFileFileName = uploadSqlFileFileName;
	  }
	  public File getUploadSqlFile() {
		return uploadSqlFile;
	   }
	   public void setUploadSqlFile(File uploadSqlFile) {
		this.uploadSqlFile = uploadSqlFile;
	   }
	   
	   
	/**
	 * 数据库备份 chudan.guo
	 */
	public void backup(){ 
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
		//只能获取tomcat的路径
//		String mysqldumpUrl=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"com"+File.separator+"kuangchi"+File.separator+"sdd"+File.separator+"util"+File.separator+"databaseUtil"+File.separator+"mysqldump.exe");
		String mysqldumpUrl=request.getSession().getServletContext().getRealPath("/"+"databaseUtil"+File.separator+"mysqldump.exe");
		String savePath=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"dataBaseBackup");
		String serverIP=request.getParameter("serverIP");
		String serverPort=request.getParameter("serverPort");
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String databaseName=request.getParameter("databaseName");
		String fileName=request.getParameter("fileName").replace(" ", "");
		boolean re=isValidIP(serverIP);
		if(re==false){
			result.setMsg("请输入正确的IP地址");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return ;
		}
		
		 if (EmptyUtil.atLeastOneIsEmpty(serverIP, username,serverPort,mysqldumpUrl,databaseName,savePath,fileName)) {
		    	result.setMsg("必输的参数不能为空");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return ;
		}
		boolean backup=exportDatabaseTool(mysqldumpUrl,serverIP,serverPort,username,password,savePath,fileName,databaseName);
       if (backup) {
    	   result.setMsg("数据库备份成功");
    	   result.setSuccess(true);
       } else {
    	   result.setMsg("备份失败,请检查参数是否正确");
    	   result.setSuccess(false);
       }
       printHttpServletResponse(GsonUtil.toJson(result));
	}
	 	
	/**
	 * 数据库还原 chudan.guo
	 */
	public void restoreDatabase(){
		HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
//		String mysqlUrl=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"com"+File.separator+"kuangchi"+File.separator+"sdd"+File.separator+"util"+File.separator+"databaseUtil"+File.separator+"mysql.exe");
		String mysqlUrl=request.getSession().getServletContext().getRealPath("/"+"databaseUtil"+File.separator+"mysql.exe");
		String savePath=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"dataBaseBackup");
		String serverIP2=request.getParameter("serverIP2");
		String serverPort2=request.getParameter("serverPort2");
		String username2=request.getParameter("username2");
		String password2=request.getParameter("password2");
		String databaseName2=request.getParameter("databaseName2");
		String filename=request.getParameter("uploadFilename");
		String restoreFile=savePath+"/"+filename; 
		boolean re=isValidIP(serverIP2);
		if(re==false){
			result.setMsg("请输入正确的IP地址");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return ;
		}
		
		 if (EmptyUtil.atLeastOneIsEmpty(mysqlUrl, serverIP2,serverPort2,username2,databaseName2)) {
		    	result.setMsg("必输参数不能为空");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return ;
		}
		 if(filename==null||filename.equals("")){
			   result.setMsg("请先上传文件再还原！");
				result.setSuccess(false);
				printHttpServletResponse(GsonUtil.toJson(result));
				return ;
			}
		boolean restore=restoreDatabaseTool(mysqlUrl,serverIP2,serverPort2,username2,password2,databaseName2,restoreFile);
       if (restore) {
    	   result.setMsg("数据库还原成功");
    	   result.setSuccess(true);
       } else {
    	   result.setMsg("还原失败,请检查参数是否正确");
    	   result.setSuccess(false);
       }
       printHttpServletResponse(GsonUtil.toJson(result));
	}
	
	/**
    * 下载备份文件 chudan.guo
    */
   public void downloadBackup(){
	   HttpServletRequest request = getHttpServletRequest();
	   String filename=(String) request.getSession().getAttribute("downloadFilename");//从session中获取备份文件的名称
	   downloadBackupTool(filename);
   }
   
   /**
    * 上传文件  chudan.guo
    * @return
    */
   public String uploadSql(){
	   HttpServletRequest request = getHttpServletRequest();
	 //得到上传文件的保存目录，将上传的文件存放于dataBaseBackup目录下
		String savePath=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"dataBaseBackup");
	   File files = new File(savePath);
	   String uploadName=uploadSqlFileFileName.replace(" ", "");
	   boolean r=listfile(files,uploadName);
	 
	   if(r){
		   request.setAttribute("uploadFilename", uploadName);
		   request.setAttribute("message", "上传成功"); 
		   return SUCCESS;
	   }
	   //这里需要判断上传文件名的格式,正确的格式：photoncard_20160915.sql
	   int a=uploadName.indexOf("_");
	   if (a>0){
		   String uploadNamesub = uploadName.substring(uploadName.lastIndexOf("_")+1); // 20160915.sql
			 String[] time=uploadNamesub.split("\\.");
			 String time2=time[0];  //20160915
			 boolean rt=isValidDate(time2);
			 if(!rt){
				  Date date=new Date();
			       DateFormat format=new SimpleDateFormat("yyyyMMdd");
			       String day=format.format(date);
		    	   String[] str=uploadName.split("\\."); //uploadName=photoncard.sql
		    	   String name=str[0];
		    	   uploadName = name + "_"+day+".sql";
			 }
	   }else{
			  Date date=new Date();
		       DateFormat format=new SimpleDateFormat("yyyyMMdd");
		       String day=format.format(date);
	    	   String[] str=uploadName.split("\\."); //uploadName=photoncard.sql
	    	   String name=str[0];
	    	   uploadName = name + "_"+day+".sql";
		 }
	   File file = new File(savePath,uploadName);
	   OutputStream os =null;
	   InputStream is = null;
       try {
		os = new FileOutputStream(file);         //输出流 
        is = new FileInputStream(uploadSqlFile); //输入流 
        byte[] buf = new byte[1024];  
        int length = 0 ;  
        while(-1 != (length = is.read(buf) ) )  
        {  
            os.write(buf, 0, length) ;  
        }  
         request.setAttribute("uploadFilename", uploadName);
        request.setAttribute("message", "上传成功");  
		} catch (FileNotFoundException e) {
			request.setAttribute("message","上传失败！");
			return SUCCESS;
		}catch (IOException e) {
			request.setAttribute("message","上传失败！");
			return SUCCESS;
		}  finally{
			try {  
				if(is!=null){
					is.close();
				}
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return SUCCESS;
   }
   /**
    * 判断日期格式是否正确  chudan.guo
    * @param str
    * @return
    */
   public boolean isValidDate(String str) {
       boolean convertSuccess=true;
       //指定日期格式为yyyyMMdd,注意区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
        	format.parse(str);
      } catch (ParseException e) {
       // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess=false;
        } 
        return convertSuccess;
 }
	/**
	 * 判断IP地址是否正确  chudan.guo
	 * @param ipText
	 * @return
	 */
   public boolean isValidIP(String ipText){
	        if (ipText != null && !ipText.isEmpty()) {
	            // 定义正则表达式
	            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
	                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	            // 判断ip地址是否与正则表达式匹配
	            if (ipText.matches(regex)) {
	                return true;
	            } else {
	                return false;
	            }
	        }
	        return false;
    }  
	
	 /**
	    * MySQL数据库导出工具方法   chudan.guo
	    * 
	    * @param serverIP MySQL数据库所在服务器地址IP
	    * @param username 进入数据库所需要的用户名
	    * @param password 进入数据库所需要的密码
	    * @param mysqldumpUrl 表示mysqldump所在全路径
	    * @param databaseName 要导出的数据库名
	    * @param savePath 数据库导出文件保存路径
	    * @param fileName 数据库导出文件文件名
	    * @return 返回true表示导出成功，否则返回false。
	    */
 
   public  boolean exportDatabaseTool(String mysqldumpUrl,String serverIP,String serverPort,String username,String password,String savePath,String fileName,String databaseName) {      
	   HttpServletRequest request = getHttpServletRequest();
	   File saveFile = new File(savePath);
	       if (!saveFile.exists()) {// 如果目录不存在
	           saveFile.mkdirs();// 创建文件夹
	       }
	       if (!savePath.endsWith(File.separator)) {
	           savePath = savePath + File.separator;
	       }
	       Date date=new Date();
	       DateFormat format=new SimpleDateFormat("yyyyMMdd");
	       String time=format.format(date);
	       if (!fileName.endsWith(".sql")) {
	    	   fileName = fileName + "_"+time+".sql";
	       }else{  
	    	   String[] str=fileName.split("\\."); //fileName=110.sql
	    	   String name=str[0];
	    	  fileName = name + "_"+time+".sql";
	       }
	       // 获取操作系统的名称 如:"Windows" or "Linux"
	       String osName = System.getProperties().getProperty("os.name");
	       //String backupDatabase="mysqldump"+" -h"+serverIP+" -u"+username+" -p"+password +" --opt "+databaseName;
	       String backupDatabase;
	       if(password != null){
	    	   backupDatabase="mysqldump"+" -h"+serverIP+" -P"+serverPort+" -u"+username+" -p"+password +" --opt "+databaseName;
	       } else {
	    	   backupDatabase="mysqldump"+" -h"+serverIP+" -P"+serverPort+" -u"+username+" --opt "+databaseName;
	       }
	       String invoke=backupDatabase.toString();
	       String[] cmd_arr = new String[] { "/bin/sh", "-c", invoke }; // 根据操作系统生成shell脚本
	      
	       if (osName.contains("Windows")){
	    	   String mysqldump=mysqldumpUrl;
	    	   if (!(mysqldumpUrl.endsWith("mysqldump")||mysqldumpUrl.endsWith("mysqldump.exe")) ){
	   	   		 mysqldumpUrl = mysqldumpUrl + " mysqldump";
	   	        }
	    	   //backupDatabase=mysqldumpUrl+" -h"+serverIP+" -u"+username+" -p"+password +" --opt "+databaseName;
	    	   if(password != null){
		    	   backupDatabase=mysqldumpUrl+" -h"+serverIP+" -P"+serverPort+" -u"+username+" --opt "+databaseName;
		       } else {
		    	   backupDatabase=mysqldumpUrl+" -h"+serverIP+" -P"+serverPort+" -u"+username+" --opt "+databaseName;
		       }
	    	   invoke=backupDatabase.toString();
	           cmd_arr = new String[] { "cmd", "/c", invoke };
	       }
	       InputStream in =null;
	       InputStreamReader xx = null;
	       StringBuffer sb =null;
	       BufferedReader br = null;
	       FileOutputStream fout =null;
	       OutputStreamWriter writer =null;
		 try {      
	            Runtime rt = Runtime.getRuntime();      
	            Process process = rt.exec(cmd_arr);
	            // 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行      
	            in = process.getInputStream();// 控制台的输出信息作为输入流      
	            xx = new InputStreamReader(in, "utf8");// 设置输出流编码为utf8。这里必须是utf8，否则从流中读入的是乱码      
	            String inStr;      
	            sb = new StringBuffer("");      
	            String outStr;      
	            // 组合控制台输出信息字符串      
	            br = new BufferedReader(xx);      
	            while ((inStr = br.readLine()) != null) {      
	                sb.append(inStr + "\r\n");      
	            }     
	            outStr = sb.toString();     
	            // 要用来做导入用的sql目标文件：      
	             String name=savePath + fileName;
	            fout = new FileOutputStream(name);      
	            writer = new OutputStreamWriter(fout, "utf8");      
	            writer.write(outStr);      
	            // 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免      
	            writer.flush();      
	            int processNum= process.waitFor();
	            if (processNum == 0) { // 0 表示线程正常终止。
					request.getSession().setAttribute("downloadFilename", fileName);
	                return true;
	            }
	        } catch (Exception e) {      
	            e.printStackTrace();      
	        } finally{  // 关闭输入输出流    
	        	 try {
	        		 if(in!=null){ in.close(); }
	        		 if(xx!=null){  xx.close(); }
	        		 if(br!=null){ br.close(); }
	        		 if(writer!=null){ writer.close(); }
	        		 if(fout!=null){ fout.close(); }
					} catch (IOException e) {
					e.printStackTrace();
				} 
	        }  
		 return false;
	    }     
   
   /**
    * MySQL数据库还原工具方法  chudan.guo 
    * 
    * @param serverIP MySQL数据库所在服务器地址IP
    * @param username 进入数据库所需要的用户名
    * @param password 进入数据库所需要的密码
    * @param mysqlUrl 表示mysql运行文件所在全路径
    * @param databaseName 要还原的数据库名
    * @param restoreFile 用来还原的文件
    * @return 返回true表示导出成功，否则返回false。
    */
   public  boolean restoreDatabaseTool(String mysqlUrl, String serverUrl,String serverPort2, String username, String password, String databaseName, String restoreFile)  {
	    // 获取操作系统的名称 如:"Windows" or "Linux"
	    String osName = System.getProperties().getProperty("os.name");
	    //String restoreDatabase = "mysql"+" --default-character-set=utf8 "+" -h"+serverUrl+" -u" + username + " -p"+ password + " " + databaseName + " < " + restoreFile;
	   
	    String restoreDatabase;
	    if(password != null){
	    	restoreDatabase = "mysql"+" --default-character-set=utf8 "+" -h"+serverUrl+" -P"+serverPort2+" -u" + username + " -p"+ password + " " + databaseName + " < " + restoreFile;
	    } else {
	    	restoreDatabase = "mysql"+" --default-character-set=utf8 "+" -h"+serverUrl+" -P"+serverPort2+" -u" + username + " " + databaseName + " < " + restoreFile;
	    }
	    String invoke=restoreDatabase.toString();
	    String[] cmd_arr = new String[] { "/bin/sh", "-c", invoke };// 根据操作系统生成shell脚本
	    
	    if (osName.contains("Windows")){
	    	if (!(mysqlUrl.endsWith("mysql")||mysqlUrl.endsWith("mysql.exe"))) {
		   		mysqlUrl = mysqlUrl + "mysql";
		       }
	    	//restoreDatabase = mysqlUrl+" --default-character-set=utf8 "+" -h"+serverUrl+" -u" + username + " -p"+ password + " " + databaseName + " < " + restoreFile;
	    	 if(password != null){
	    		 restoreDatabase = mysqlUrl+" --default-character-set=utf8 "+" -h"+serverUrl+" -P"+serverPort2+" -u" + username + " -p"+ password + " " + databaseName + " < " + restoreFile;
	    	 } else {
	    		 restoreDatabase = mysqlUrl+" --default-character-set=utf8 "+" -h"+serverUrl+" -P"+serverPort2+" -u" + username + " " + databaseName + " < " + restoreFile;
	    		 
	    	 }
	    	invoke=restoreDatabase.toString();
	        cmd_arr = new String[] { "cmd", "/c", invoke };
	    }		
	   	try {
				Process process=Runtime.getRuntime().exec(cmd_arr);
				int i= process.waitFor();
				if(i==0){
					return true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
	   	  }
	   	   return false;
   }
   
   /**
    * 下载备份文件工具方法  chudan.guo
    * @param fileName
    */
   public void downloadBackupTool(String fileName){
	   try {
	   HttpServletRequest request = getHttpServletRequest();
	   HttpServletResponse response = getHttpServletResponse();
	   OutputStream out = null;
	   
       //通过文件路径获得File对象(文件夹路径+文件名称)  
		String path=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"dataBaseBackup");
       File file = new File(path + "/" + fileName); 
       // 以流的形式下载文件。
       InputStream fis = new BufferedInputStream(new FileInputStream(file));
       byte[] buffer = new byte[fis.available()];
       fis.read(buffer);
       fis.close();
	     //1.设置文件ContentType类型  
	   	response.setContentType("application/x-msdownload");
			String iso_filename = DownloadFile.toUtf8String(fileName);
		//2.设置文件头：fileName参数是设置下载文件名 
		response.setHeader("Content-Disposition","attachment;filename=" + iso_filename);
       response.addHeader("Content-Length", "" + file.length());
       OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
       toClient.write(buffer);
       toClient.flush();
       toClient.close();
	   } catch (IOException ex) {
	       ex.printStackTrace();
	   }
   }
   
   
   /**
    * 判断文件夹中是否存在已经存在文件,用于上传判断  chudan.guo
    * @param file
    * @param inputName 传入文件名进行查找对比
    * @return  true表示文件夹里存在同名文件，false则不存在
    */
   public boolean listfile(File file,String inputName){
	 //如果file代表的不是一个文件，而是一个目录
	 if(!file.isFile()){
	 //列出该目录下的所有文件和目录
	 File[] files = file.listFiles();
	 int i=0;
		 for( i=0;i<files.length;i++){
			 String mm=files[i].getName();
			 if(inputName.equals(mm)){
				 break;
			 }
		 }
		 if(i < files.length){	//如果for语句是因break终止，说明有符合要求的数据
			 return true;
		 }else{
			 return false;
		 }
	 }
	 return false;
   }
   
   /**
    * 删除服务器中保存的备份文件 chudan.guo
    */
   public void deleBackupFile(){
	   HttpServletRequest request = getHttpServletRequest();
		JsonResult result = new JsonResult();
	   String dateTime=request.getParameter("dateTime"); //2016-09-14
	   String inputTime=dateTime.replace("-", "");
	   String savePath=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"dataBaseBackup");
       File files = new File(savePath);
	   boolean rt=listfiles(files,inputTime);
	   if (rt) {
    	   result.setMsg("删除成功");
    	   result.setSuccess(true);
       } else {
    	   result.setMsg("没有存在该日期之前的备份文件");
    	   result.setSuccess(false);
       }
       printHttpServletResponse(GsonUtil.toJson(result));
   }
   
	 /**
	  * 删除服务器中存在的备份文件 chudan.guo
	  * @param file
	  * @param inputTime  格式：20160915
	  * @return
	  */
   public boolean listfiles(File file,String inputTime){
	   boolean result=false;
	   //如果file代表的不是一个文件，而是一个目录
		 if(!file.isFile()){
		 //列出该目录下的所有文件和目录
		 File[] files = file.listFiles();
		 int i=0;
			 for( i=0;i<files.length;i++){
				 String filename=files[i].getName();   //photoncard_20160915.sql
				 filename = filename.substring(filename.lastIndexOf("_")+1); // 20160915.sql
				 String[] time=filename.split("\\.");
				 String time2=time[0];  //20160915
				 DateFormat df = new SimpleDateFormat("yyyyMMdd");
				  //获取Calendar实例
				  Calendar currentTime = Calendar.getInstance();
				  Calendar compareTime = Calendar.getInstance();
			     //把字符串转成日期类型
				  try {
					currentTime.setTime(df.parse(inputTime)); //页面提交的时间
					compareTime.setTime(df.parse(time2));     //服务器中文件截取的时间
				} catch (ParseException e) {
					e.printStackTrace();
				}
				 //利用Calendar的方法比较大小, -1后者大，0相等，1前者大
				  if (compareTime.compareTo(currentTime) < 0) { 
				    	 result=true;
						 files[i].delete(); 
				  }}
		 }
		 return result;
	   }
   
		@Override
		public Object getModel() {
			return null;
		}
 
	
}
