package com.kuangchi.sdd.util.file;

import java.io.FileInputStream;

import sun.net.ftp.FtpClient;

public class FTPUtil {
	public static FTP ftp=null;
	
	public static FTP getFtp(String absolutePropertiesPath){
		java.util.Properties properties=new java.util.Properties();
		FileInputStream fis=null;
		
		try {
			if (null==ftp) {
				fis= new FileInputStream(new java.io.File(absolutePropertiesPath));//属性文件流 
				properties.load(fis);	
				ftp=new FTP();
				ftp.setHost(properties.getProperty("host"));
				ftp.setUserName(properties.getProperty("username"));
				ftp.setPassword(properties.getProperty("password"));
				ftp.setPort(Integer.parseInt(properties.getProperty("port")));
			}      
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ftp;
				
	}
}
