package com.kuangchi.sdd.base.filter;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.kuangchi.sdd.util.commonUtil.GsonUtil;

public class CoverInvalidCharWrapper extends HttpServletRequestWrapper {

	public CoverInvalidCharWrapper(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	 public String getParameter(String name){
		Object o= super.getParameter(name);
		if (null!=o) {
			String str=(String) o;
			str= str.replaceAll("<", "&lt;");	
			str= str.replaceAll(">", "&gt;");	
			return str;
		}
		return null;
	 }
	 
	 

	 
	 
}
