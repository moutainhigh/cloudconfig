package com.kuangchi.sdd.base.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.infinispan.util.FastCopyHashMap.EntrySet;

import com.kuangchi.sdd.util.commonUtil.GsonUtil;

public class CoverInvalidCharFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	@Override
	public void destroy() {
	}

	
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse res=(HttpServletResponse) response;
		StringBuffer url=req.getRequestURL();
		//当URL不等于等于“”时过滤
		if (!url.toString().contains("activiti/deploy")) {
			chain.doFilter(new CoverInvalidCharWrapper(req), res);
		}else{
			chain.doFilter(req, res);
		}
	}
}
