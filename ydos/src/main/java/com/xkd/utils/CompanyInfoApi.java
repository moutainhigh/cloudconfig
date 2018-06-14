package com.xkd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.xkd.model.UserInfo;

/**
 * 公司信息查询api
 */
public class CompanyInfoApi {

	/*
	 * http://i.yjapi.com/ECIV4/GetDetailsByName?key=AppKey&keyword= 

北京小桔科技有限公司
	 */
	public static final String COMANY_NAME = "http://i.yjapi.com/ECISimple/GetDetailsByName"; //企业关键字全名精确查询
	public static final String COMANY_NAME_LIKE = "http://i.yjapi.com/ECIV4/Search"; //企业关键字模糊查询
	public static final String COMANY_KEY_LIKE = "http://i.yjapi.com/ECISimple/GetDetails"; //根据ID获取照面信息

	/**
	 * 查询企业信息(关键字)
	 * @param ttype （COMANY_NAME，COMANY_NAME_LIKE，COMANY_KEY_LIKE）
	 * @param search 关键字
	 * @return
	 */
	public static String queryCompanyInfo(String ttype, String search,int pageIndex,int pageSize) {

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(ttype);

		// 向StringBuffer追加手机号码
		sb.append("?key="+"b6961bfe69774633a8b8d88bb751776d");
		
		if("http://i.yjapi.com/ECIV4/Search".equals(ttype)){
			
			sb.append("&keyword="+search+"&pageSize="+pageSize+"&pageIndex="+pageIndex);
			
		}else if ("http://i.yjapi.com/ECISimple/GetDetailsByName".equals(ttype)) {
			
			sb.append("&keyword="+search);
		}else {
			
			sb.append("&keyword="+search);
		}
		
		

		// 向StringBuffer追加消息内容转URL标准码
		try {
			// 创建url对象
			URL url = new URL(sb.toString());
	
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("GET");
	
			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = CompanyInfoApi.convertStreamToString(is);
//			System.out.println(returnStr);
			if(returnStr.indexOf("Status\":\"200") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				return returnStr;
			}else{
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * 根据ID获取照面信息
	 * @param keyno 关键字
	 * @return
	 */
	public static String queryCompanyKeyNo(String keyno) {
		
		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer(COMANY_KEY_LIKE);
		
		// 向StringBuffer追加手机号码
		sb.append("?key="+"b6961bfe69774633a8b8d88bb751776d");
		sb.append("&keyno="+keyno);
		
		// 向StringBuffer追加消息内容转URL标准码
		try {
			// 创建url对象
			URL url = new URL(sb.toString());
			
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("GET");
			
			// 发送
			InputStream is =url.openStream();
			//转换返回值
			String returnStr = CompanyInfoApi.convertStreamToString(is);
			System.out.println(returnStr);
			if(returnStr.indexOf("Status\":\"200") > 0){
				// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功   具体见说明文档
				return returnStr;
			}else{
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e);
		}
		return null;
	}

	public static void main(String[] args) {
		
//		System.out.println(queryCompanyKeyNo("45fb8d18b61e02899c79a9be71d69f71"));﻿
//		System.out.println(queryCompanyInfo(CompanyInfoApi.COMANY_NAME_LIKE, "浙江王家风范海绵制品有限公司"));
//		String companysInfo = queryCompanyInfo(CompanyInfoApi.COMANY_NAME_LIKE, "小米",2,20);
//		System.out.println(companysInfo);
		
//		System.out.println(queryCompanyInfo(CompanyInfoApi.COMANY_NAME, "北京棋坤阳光农业科技发展有限公司"));
		System.out.println(queryCompanyInfo(CompanyInfoApi.COMANY_NAME, "北京棋坤阳光农业科技发展有限公司",1,10));
//		System.out.println(queryCompanyInfo("http://i.yjapi.com/ECIV4/GetDetailsByName", "北京小桔科技有限公司",1,10));
//		String companysInfo = queryCompanyInfo(CompanyInfoApi.COMANY_NAME, "北京棋坤阳光农业科技发展有限公司");
		
	}
	
	/**
	 * 转换返回值类型为UTF-8格式.
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {    
        StringBuilder sb1 = new StringBuilder();    
        byte[] bytes = new byte[4096];  
        int size = 0;  
        
        try {    
        	while ((size = is.read(bytes)) > 0) {  
                String str = new String(bytes, 0, size, "UTF-8");  
                sb1.append(str);  
            }  
        } catch (IOException e) {    
            System.out.println(e);    
        } finally {    
            try {    
                is.close();    
            } catch (IOException e) {    
               System.out.println(e);    
            }    
        }    
        return sb1.toString();    
    }
}