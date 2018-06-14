package com.xkd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class PropertiesUtil {
	
	//#应用服务器地址
	public static String APPID = "";
	public static String REDIRECT_URI = "";
	public static String RESPONSE_TYPE = "";
	public static String SCOPE = "";
	public static String STATE = "";
	public static String APPSECRET = "";
	public static String ASSET_URL = "";
	public static String FILE_UPLOAD_PATH = "";
	public static String FILE_HTTP_PATH = "";
	
	//支付商户号
	public static String API_KEY = "";
	public static String MCH_ID ="";
	public static String NOTIFYURL ="";
	//邮箱设置
	public static String MAIL_USER_CODE = "";
	public static String MAIL_USER_PWD = "";
	public static String MAIL_SMTP_HOST = "";
	public static String LOCAL_PATH = "";
	public static String LOCAL_FILE_PATH = "";
	
	public static String DB_URL = "";
	public static String DB_USER = "";
	public static String DB_PWD = "";
	
	//进入统登页面
	public static String USER_LOGIN_HREF = "";
	
	
	//爬虫异常信息通知邮箱
	public static String CRAWL_RECEIVER_MAIL="";
	
	//SOLR 全文索引URL
	public static String SOLR_INDEX_URL="";
	
	//kafka消息队列IP
	public static String KAFKA_BOOTSTRAP_SERVERS="";
	
	//是否写kafka消息
	public static String KAFKA_WRITE="";
	
	//百度地图开发者账号key
	public static String BAIDUMAP_AK="";

	//小程序
	public static String APPID_XCX = "";
	public static String APPSECRET_XCX = "";

	//支付回调域名
	public static String PAY_DOMAIN_NAME = "";
	
	static{
		APPID_XCX = PropertiesUtil.getProperty("config.properties","APPID_XCX").trim();
		APPSECRET_XCX = PropertiesUtil.getProperty("config.properties","APPSECRET_XCX").trim();

		APPID = PropertiesUtil.getProperty("config.properties","APPID").trim();
		REDIRECT_URI = PropertiesUtil.getProperty("config.properties","REDIRECT_URI").trim();
		RESPONSE_TYPE = PropertiesUtil.getProperty("config.properties","RESPONSE_TYPE").trim();
		SCOPE = PropertiesUtil.getProperty("config.properties","SCOPE").trim();
		STATE = PropertiesUtil.getProperty("config.properties","STATE").trim();
		APPSECRET = PropertiesUtil.getProperty("config.properties","APPSECRET").trim();
		ASSET_URL = PropertiesUtil.getProperty("config.properties","ASSET_URL").trim();
		ASSET_URL = PropertiesUtil.getProperty("config.properties","ASSET_URL").trim();
		FILE_UPLOAD_PATH = PropertiesUtil.getProperty("config.properties","FILE_UPLOAD_PATH").trim();
		FILE_HTTP_PATH = PropertiesUtil.getProperty("config.properties","FILE_HTTP_PATH").trim();
		//邮箱设置
		MAIL_USER_CODE = PropertiesUtil.getProperty("config.properties","MAIL_USER_CODE").trim();
		MAIL_USER_PWD = PropertiesUtil.getProperty("config.properties","MAIL_USER_PWD").trim();
		MAIL_SMTP_HOST = PropertiesUtil.getProperty("config.properties","MAIL_SMTP_HOST").trim();
		
		LOCAL_PATH = PropertiesUtil.getProperty("config.properties","LOCAL_PATH").trim();
		LOCAL_FILE_PATH = PropertiesUtil.getProperty("config.properties","LOCAL_FILE_PATH").trim();

		USER_LOGIN_HREF = PropertiesUtil.getProperty("config.properties","USER_LOGIN_HREF").trim();
		
		DB_URL = PropertiesUtil.getProperty("database.properties","url").trim();
		DB_USER = PropertiesUtil.getProperty("database.properties","username").trim();
		DB_PWD = PropertiesUtil.getProperty("database.properties","password").trim();
		CRAWL_RECEIVER_MAIL=PropertiesUtil.getProperty("config.properties","CRAWL_RECEIVER_MAIL").trim();
		SOLR_INDEX_URL=PropertiesUtil.getProperty("config.properties","SOLR_INDEX_URL").trim();
		KAFKA_BOOTSTRAP_SERVERS=PropertiesUtil.getProperty("config.properties", "kafka.bootstrap.servers").trim();
		KAFKA_WRITE=PropertiesUtil.getProperty("config.properties", "application.kafka.write").trim();
		BAIDUMAP_AK=PropertiesUtil.getProperty("config.properties", "baidumap.ak").trim();
		
		API_KEY = PropertiesUtil.getProperty("config.properties","API_KEY").trim();
		MCH_ID = PropertiesUtil.getProperty("config.properties","MCH_ID").trim();
		
		NOTIFYURL = PropertiesUtil.getProperty("config.properties","NOTIFYURL").trim();
		PAY_DOMAIN_NAME = PropertiesUtil.getProperty("config.properties","PAY_DOMAIN_NAME").trim();
	}
	
	
	public static String getProperty(String propertiesFilePath, String key) {
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFilePath); 
			try {
				Properties properties = new Properties();
				properties.load(is);
				String value = properties.getProperty(key);
				if(value==null){
					return "";
				}else{
					return properties.getProperty(key);
				}
			}
			finally {
				is.close();
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}


	public static Map<String,String>  loadProperties(String propertiesFilePath){
		Map<String,String> map=new HashMap<>();
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesFilePath);
			try {
				Properties properties = new Properties();
				properties.load(new InputStreamReader(is, "UTF-8"));
				Enumeration  enumeration=properties.propertyNames();
				while (enumeration.hasMoreElements()){
					String key=enumeration.nextElement().toString();
					map.put(key,(String)properties.get(key));
				}
			}
			finally {
				is.close();
			}
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return map;
	}


	public static void main(String[] args) {
			Map<String,String> map=loadProperties("pv.properties");
		System.out.println(map);
	}

}
