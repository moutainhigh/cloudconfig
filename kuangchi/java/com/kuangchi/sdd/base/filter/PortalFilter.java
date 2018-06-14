package com.kuangchi.sdd.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuangchi.sdd.base.constant.GlobalConstant;

public class PortalFilter  implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String requestUrl = req.getRequestURL().toString();
		Object staff=req.getSession().getAttribute(GlobalConstant.LOGIN_STAFF);
		if((requestUrl.contains(".jsp")&&requestUrl.contains("portal"))||requestUrl.contains("startProcess.do")){
			if(requestUrl.contains("pwd")){
				chain.doFilter(request, response);
			}else if(null==staff&&!requestUrl.contains("portal/index.jsp")){
				res.sendRedirect(req.getContextPath() + "/portal/index.jsp");
			}else{
				chain.doFilter(request, response);
			}
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
